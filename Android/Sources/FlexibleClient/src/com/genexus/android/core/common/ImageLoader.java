package com.genexus.android.core.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.MimeTypeMap;

import com.genexus.android.core.activities.IGxBaseActivity;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.common.IViewDisplayGifSupport;
import com.genexus.android.core.controls.common.IViewDisplayImage;
import com.genexus.android.core.resources.MediaTypes;
import com.genexus.android.core.resources.StandardImages;
import com.genexus.android.core.utils.Cast;
import com.genexus.android.core.utils.FileUtils2;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import androidx.annotation.NonNull;
import androidx.collection.LruCache;
import androidx.exifinterface.media.ExifInterface;

@SuppressWarnings("ThreadPriorityCheck")
public class ImageLoader
{
	private static final String LOG_TAG = "ImageLoader";

	private static final LruCache<String, Bitmap> CACHE = initCache();
	private static final ConcurrentHashMap<String, SoftReference<Drawable>> CACHE_DRAWABLE = new ConcurrentHashMap<>();
	private static DiskCache sDiskCache = new DiskCache(Services.Application.getAppContext());

	private final PhotosQueue photosQueue = new PhotosQueue();
	private final Context mAppContext;

	private ArrayList<PhotosLoader> mLoaderThreads;
	private static final int NUMBER_OF_THREADS = 2;

	public ImageLoader(Context context)
	{
		mAppContext = context;
		// Make the background thread low priority. This way it will not affect the UI performance
		mLoaderThreads = new ArrayList<>();
		for (int i = 0; i < NUMBER_OF_THREADS; i++)
		{
			PhotosLoader loader = new PhotosLoader();
			loader.setPriority(Thread.NORM_PRIORITY - 1);
			mLoaderThreads.add(loader);
		}
	}

	private static LruCache<String, Bitmap> initCache()
	{
	    final int maxMemory = (int)(Runtime.getRuntime().maxMemory() / 1024);
	    final int cacheSize = maxMemory / 8;

		return new LruCache<String, Bitmap>(cacheSize)
		{
			@Override
			protected int sizeOf(String key, Bitmap value)
			{
				if (value != null)
					return (value.getRowBytes() * value.getHeight()) / 1024;
				else
					return 0;
			}
		};
	}

	public void displayImage(String url, IViewDisplayImage imageView, boolean showLoading,
							 boolean fullResolution, boolean hasAutoMirror)
	{
		Bitmap cachedBitmap = null;
		// if fullResolution not use memory cache, could return resized bitmap
		if (!fullResolution)
		{
			cachedBitmap = getFromMemoryCache(url);
		}
		if (cachedBitmap != null)
		{
			StandardImages.stopLoading(imageView);
			imageView.setImageBitmap(cachedBitmap);
		}
		else
		{
			// Loading is shown here.
			// Not necessary if the image is cached, since it will be displayed immediately.
			if (showLoading)
				StandardImages.startLoading(imageView);

			queuePhoto(url, imageView, fullResolution, hasAutoMirror);
		}
	}

	private void queuePhoto(String url, IViewDisplayImage imageView, boolean fullResolution,
							boolean hasAutoMirror)
	{
		// This ImageView may be used for other images before. So there may be some old tasks in the queue. We need to discard them.
		PhotoToLoad p = new PhotoToLoad(url, imageView, fullResolution, hasAutoMirror);
		synchronized (photosQueue.photosToLoad)
		{
			photosQueue.clean(imageView);
			photosQueue.photosToLoad.add(p);
			photosQueue.photosToLoad.notifyAll();
		}

		// start thread if it's not started yet
		for (PhotosLoader loader : mLoaderThreads)
		{
			if (loader.getState() == Thread.State.NEW)
				loader.start();
		}
	}

	public File getInputFile(String imageUrl) {
		if (StorageHelper.isLocalFile(imageUrl)) {
			// Read local file from filesystem.
			if (imageUrl.startsWith(ContentResolver.SCHEME_FILE))
				imageUrl = Uri.parse(imageUrl).getPath();

			assert imageUrl != null;
			return new File(imageUrl);
		}

		// Check cache
		File cacheFile = getCachedImageFile(imageUrl);
		if (cacheFile.exists())
			return cacheFile;

		try
		{
			// Download from source (remote, content provider) to local cache file.
			Uri imageUri = Uri.parse(imageUrl);
			InputStream is;
			if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(imageUri.getScheme()))
				is = mAppContext.getContentResolver().openInputStream(imageUri);
			else
			{
				Services.Log.debug(" getInputFile " + imageUrl);
				prepareCookieManager();
				is = new URL(imageUrl).openStream();
			}

			if (is != null)
			{
				try (OutputStream os = new FileOutputStream(cacheFile))
				{
					IOUtils.copy(is, os);
					return cacheFile;
				}
				finally {
					is.close();
				}
			}
			else
			{
				Services.Log.warning(LOG_TAG, "Could not open input stream from " + imageUrl);
				return null;
			}
		}
		catch (InterruptedIOException ex)
		{
			Services.Log.warning(LOG_TAG, "InterruptedIOException during getInputFile(), loading " + imageUrl + " canceled ");
			// should remove cacheFile because it could be incomplete
			FileUtils.deleteQuietly(cacheFile);
			return null;
		}
		catch (IOException ex) {
			Services.Log.error(LOG_TAG, "Exception during getInputFile(), loading " + imageUrl, ex);
			return null;
		}
	}

	public Bitmap getBitmap(String imageUrl, boolean getFullImage)
	{
		File file = getInputFile(imageUrl);
		if (file == null)
			return null;
		Bitmap b = decodeFile(file, 0, getFullImage, true);
		if (b != null)
			putInMemoryCache(imageUrl, b);
		return b;
	}

	private static void prepareCookieManager()
	{
		// copy session cookies.
		CookieManager cookieManager = new CookieManager();
		copyCookies(cookieManager);
		CookieHandler.setDefault(cookieManager);
	}

	private static void copyCookies(CookieManager cookieManager) {
		// add cookies from httpclient
		List<HttpCookie> cookies = Services.HttpService.getCookieManager().getCookieStore().getCookies();
		CookieStore store = cookieManager.getCookieStore();
		for (HttpCookie cookie : cookies) {
			HttpCookie newCookie = new HttpCookie(cookie.getName(), cookie.getValue());
			newCookie.setPath(cookie.getPath());
			newCookie.setDomain(cookie.getDomain());
			newCookie.setVersion(cookie.getVersion());
			try {
				store.add(new URI(cookie.getDomain()), newCookie);
			} catch (URISyntaxException ex) {
				Services.Log.error(ex);
			}
		}
	}

	public static class LocalImageFile
	{
		private final File mFile;
		private final boolean mDeleteOnCleanup;

		private LocalImageFile(File file, boolean isTempFile)
		{
			mFile = file;
			mDeleteOnCleanup = isTempFile;
		}

		private static final LocalImageFile NONE = new LocalImageFile(null, false);

		public File getFile()
		{
			return mFile;
		}

		public void cleanup()
		{
			// If a temporary file, remove it. Otherwise do nothing.
			if (mDeleteOnCleanup && mFile != null)
				FileUtils.deleteQuietly(mFile);
		}

		public boolean exists()
		{
			return mFile != null && mFile.exists();
		}
	}

	public static @NonNull LocalImageFile getImageFile(Context context, String imagePath)
	{
		// 1) Already a file => return it directly.
		final String SCHEME_FILE = "file://";
		if (Strings.starsWithIgnoreCase(imagePath, SCHEME_FILE))
			return new LocalImageFile(new File(imagePath.substring(SCHEME_FILE.length())), false);

		if (StorageHelper.isLocalFile(imagePath))
			return new LocalImageFile(new File(imagePath), false);

		// 2) An URI that can be extracted to a file (content://, resource://)
		Uri imageUri = Uri.parse(imagePath);
		if (FileUtils2.canCopyDataToFile(imageUri))
		{
			try
			{
				File tempFile = FileUtils2.copyDataToFile(context, imageUri, context.getCacheDir());
				return new LocalImageFile(tempFile, true);
			}
			catch (IOException e)
			{
				// The URI was incorrect, e.g. a resource that doesn't exist.
				Services.Log.warning("Error getting file from imagePath", e);
				return LocalImageFile.NONE;
			}
		}

		// 3) Neither of those => An image on the server.
		// Either get from cache, if already downloaded, or download it now.
	 	String imageFullPath = Services.Application.get().UriMaker.getImageUrl(imagePath);
	    File file = getCachedImageFile(imageFullPath);

	    if (!file.exists())
	    {
	    	try
	    	{
				file = sDiskCache.getFileFor(imageFullPath);
				Services.Log.debug(" getImageFile " + imageFullPath );
				prepareCookieManager();
				InputStream is = new URL(imageFullPath).openStream();
				OutputStream os = new FileOutputStream(file);
				IOUtils.copy(is, os);
				os.close();
				is.close();
	    	}
			catch (IOException e)
			{
				Services.Log.error(LOG_TAG, "Exception during getImage()", e);
				return LocalImageFile.NONE;
			}
	    }
		return new LocalImageFile(file, false);
	}

	public static @NonNull File getCachedImageFile(String imageRemoteUri)
	{
		return sDiskCache.getFileFor(imageRemoteUri);
	}

	private static Bitmap decodeFile(File file, int scaleFactor, boolean getFullImage, boolean applyExifRotation)
	{
		Bitmap bitmap = internalDecodeFile(file, scaleFactor, getFullImage);
		if (bitmap != null && applyExifRotation)
			bitmap = internalApplyExifRotation(file, bitmap);

		return bitmap;
	}

	/**
	 * Decodes a bitmap from the File, possibly resizing it to avoid excessive memory consumption.
	 * Does not apply a rotation according to the file's EXIF orientation.
	 */
	private static Bitmap internalDecodeFile(File f, int scaleFactor, boolean getFullImage)
	{
		if (f == null || !f.exists())
			return null;

		try
		{
			if (getFullImage)
			{
				scaleFactor = 1;
				BitmapFactory.Options o2 = getScaleOptions(scaleFactor);
				try (FileInputStream fis = new FileInputStream(f)) {
					return BitmapFactory.decodeStream(fis, null, o2);
				}
			}

			//decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			try (FileInputStream fis = new FileInputStream(f)) {
				BitmapFactory.decodeStream(fis, null, o);
			}

			if (scaleFactor == 0)
				scaleFactor = getScaleForBitmap(o);

			BitmapFactory.Options o2 = getScaleOptions(scaleFactor);
			FileInputStream fis = new FileInputStream(f);
			Bitmap scaledBitmap = BitmapFactory.decodeStream(fis, null, o2);
			fis.close();
			if (scaledBitmap == null) {
				Services.Log.debug("Failed when attempting to decode image: " + f.getAbsolutePath());
				return null;
			}

			int dstWidth = o.outWidth / scaleFactor;
			int dstHeight = o.outHeight / scaleFactor;
			if (dstWidth < o2.outWidth || dstHeight < o2.outHeight)
			{
				Services.Log.debug("Using createScaledBitmap to resize to scale " + scaleFactor);
				Bitmap rescaledBitmap = Bitmap.createScaledBitmap(scaledBitmap, dstWidth, dstHeight, true);
				scaledBitmap.recycle();
				scaledBitmap = rescaledBitmap;
			}

			return scaledBitmap;

		}
		catch (IOException e)
		{
			Services.Log.error(LOG_TAG, String.format("IOException decoding file '%s'.", f.getName()), e);
		}
		catch (OutOfMemoryError e)
		{
			clearMemoryCache();
			Services.Log.error(LOG_TAG, String.format("Out of memory decoding file '%s'.", f.getName()), e);
		}

		return null;
	}

	private static Bitmap internalApplyExifRotation(File srcFile, Bitmap bitmap)
	{
		if (srcFile == null || !srcFile.exists() || bitmap == null)
			throw new IllegalArgumentException("Invalid parameters for internalApplyExifRotation().");

		// Avoid trying to read exif metadata from files that are known not to contain it.
		String fileExtension = FilenameUtils.getExtension(srcFile.getAbsolutePath());
		String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);

		if (mimeType == null || !mimeType.equals("image/jpeg"))
			return bitmap;

		try
		{
			ExifInterface fileExif = new ExifInterface(srcFile.getAbsolutePath());
			int exifOrientation = fileExif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			if (exifOrientation != ExifInterface.ORIENTATION_NORMAL)
			{
				// We only consider the most common cases.
				Matrix matrix = new Matrix();
				if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90)
					matrix.setRotate(90);
				else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180)
					matrix.setRotate(180);
				else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270)
					matrix.setRotate(270);

				if (!matrix.isIdentity())
				{
					Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

					// Discard the old bitmap, and set up to return the rotated one instead.
					bitmap.recycle();
					bitmap = rotatedBitmap;
				}
			}
		}
		catch (OutOfMemoryError | IOException e)
		{
			// Probably an OutOfMemoryError, or possibly IOException while reading the EXIF metadata.
			Services.Log.warning(LOG_TAG, "Exception trying to apply EXIF orientation; returning original bitmap", e);
		}

		return bitmap;
	}

	@SuppressWarnings("deprecation")
	private static BitmapFactory.Options getScaleOptions(int scale)
	{
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize= scale;
		o2.inPurgeable = true;
		o2.inInputShareable = true;
		o2.inTempStorage = new byte[16*1024];
		return o2;
	}

	private static int getScaleForBitmap(BitmapFactory.Options o)
	{
		// Default required size for display is same as screen's smallest width in pixels
		// (reasonable for image displayed with "Fit").
		// multiply for 1.5 to avoid scale /2 images to near of screen size
		int requiredSize = Services.Device.dipsToPixels((int)(Services.Device.getScreenSmallestWidth() * 1.5));

		int width = o.outWidth;
		int height = o.outHeight;
		int widthTmp = o.outWidth;
		int heightTmp = o.outHeight;
		int scale = 1;

		// fit to square of REQUIRED_SIZE by REQUIRED_SIZE (does not return a power of 2).
		while (widthTmp > requiredSize || heightTmp > requiredSize)
		{
			scale++;
			widthTmp = width/scale;
			heightTmp = height/scale;
		}

		return scale;
	}

	//Task for the queue
	private static class PhotoToLoad
	{
		public String url;
		IViewDisplayImage imageView;
		boolean isFullRes;
		boolean hasAutoMirror;

		PhotoToLoad(String u, IViewDisplayImage i, boolean fullResolution, boolean autoMirror)
		{
			url = u;
			imageView = i;
			isFullRes = fullResolution;
			hasAutoMirror = autoMirror;
		}
	}

	public void stopThread()
	{
		for (PhotosLoader loader : mLoaderThreads)
			loader.interrupt();
	}

	//stores list of photos to download
	private static class PhotosQueue
	{
		private final ArrayList<PhotoToLoad> photosToLoad= new ArrayList<>();

		//removes all instances of this ImageView
		public void clean(IViewDisplayImage image)
		{
			int removed = 0;
			for (int j = photosToLoad.size() - 1 ;j >= 0; j--)
			{
				if (photosToLoad.get(j) != null && photosToLoad.get(j).imageView == image)
				{
					photosToLoad.remove(j);
					removed++;
				}
			}

			if (removed != 0)
				Services.Log.debug(LOG_TAG, String.format("Cancel() cleared %s pending operations.", removed));
		}
	}

	private class PhotosLoader extends Thread
	{
		@Override
		public void run()
		{
			try
			{
				while(true)
				{
					// Thread waits until there are any images to load in the queue
					PhotoToLoad photoToLoad;
					synchronized (photosQueue.photosToLoad)
					{
						while (photosQueue.photosToLoad.size() == 0)
							photosQueue.photosToLoad.wait();

						photoToLoad = photosQueue.photosToLoad.get(0);
						photosQueue.photosToLoad.remove(0);
					}

					// Check if this ImageView has been reused since the request was queued.
					if (!isLoadValidForImageView(photoToLoad))
						continue;

					final File file = getInputFile(photoToLoad.url);

					// Check if this ImageView has been reused while the image was downloading from the server.
					if (!isLoadValidForImageView(photoToLoad))
						continue;

					// Display the image, if have one.
					if (file == null)
						displayPlaceholder(photoToLoad.imageView);
					else {
						String mimeType = null;
						if (photoToLoad.url.endsWith(".tmp")) {
							// Image from GX Attribute (it can be svg)
							try (InputStream is = new FileInputStream(file)) {
								mimeType = URLConnection.guessContentTypeFromStream(new BufferedInputStream(is));
							} catch (IOException e) {
								Services.Log.error(e);
							}
						}
						if (photoToLoad.url.endsWith(".gif") && photoToLoad.imageView instanceof IViewDisplayGifSupport) {
							postImageLoad(() -> ((IViewDisplayGifSupport) photoToLoad.imageView).setGifImageFile(file));
						} else if (photoToLoad.url.endsWith(".svg") || "application/xml".equals(mimeType)) {
							try (InputStream stream = new FileInputStream(file)) {
								SVG svg = SVG.getFromInputStream(stream);
								final Drawable drawable = new PictureDrawable(svg.renderToPicture());
								postImageLoad(() -> {
									photoToLoad.imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
									photoToLoad.imageView.setImageDrawable(drawable);
								});
							} catch (SVGParseException | IOException e) {
								Services.Log.error(e);
							}
						} else {
							Bitmap bmp = decodeFile(file, 0, photoToLoad.isFullRes, true);
							if (bmp == null)
								displayPlaceholder(photoToLoad.imageView);
							else {
								putInMemoryCache(photoToLoad.url, bmp);
								postImageLoad(new BitmapDisplayer(bmp, photoToLoad));
							}
						}
					}
					if (Thread.interrupted())
						break;
				}
			}
			catch (InterruptedException e)
			{
				//allow thread to exit
			}
		}

		private void displayPlaceholder(IViewDisplayImage imageView) {
			postImageLoad(new PlaceholderDisplayer(mAppContext, imageView));
		}

		private void postImageLoad(Runnable displayer)
		{
			Services.Device.runOnUiThread(displayer);
		}
	}

	private boolean isLoadValidForImageView(PhotoToLoad photoToLoad)
	{
		// Check that a request for image load is still valid (basically, that the ImageView hasn't been
		// reused for other data).
		String imageViewTag = photoToLoad.imageView.getImageTag();
		return (imageViewTag != null && imageViewTag.equalsIgnoreCase(photoToLoad.url));
	}

	//Used to display bitmap in the UI thread
	private class BitmapDisplayer implements Runnable
	{
		private final Bitmap mBitmap;
		private final PhotoToLoad mPhotoToLoad;

		BitmapDisplayer(Bitmap b, PhotoToLoad photoToLoad)
		{
			mBitmap = b;
			mPhotoToLoad = photoToLoad;
		}

		@Override
		public void run()
		{
			// Last check, in UI thread.
			// Previous checks were done in background and situation may have changed
			// while this code was waiting to run.
			if (!isLoadValidForImageView(mPhotoToLoad))
				return;

			IViewDisplayImage imageView = mPhotoToLoad.imageView;
			StandardImages.stopLoading(imageView);

			if (mBitmap != null)
				imageView.setImageBitmap(mBitmap);
			else {
				Drawable drawable = Services.Resources.getContentDrawableFor(imageView.getContext(), MediaTypes.IMAGE_STUB);
				StandardImages.showPlaceholderImage(imageView, drawable, false);
			}
		}
	}

	private static class PlaceholderDisplayer implements Runnable {

		private final Context mContext;
		private final IViewDisplayImage mImageView;

		PlaceholderDisplayer(Context context, IViewDisplayImage imageView) {
			mContext = context;
			mImageView = imageView;
		}

		@Override
		public void run() {
			Drawable drawable = Services.Resources.getContentDrawableFor(mContext, MediaTypes.IMAGE_STUB);
			StandardImages.showPlaceholderImage(mImageView, drawable, true);
		}
	}

	public static void clearCache()
	{
		// Clear memory cache
		clearMemoryCache();

		// Clear disk cache
		sDiskCache.clear();
	}

	public static void trimCacheSize()
	{
		sDiskCache.trimSize();
	}

	// createFromResourceStream with opts has been deprecated in API 28 but there does not seem to
	// be a clear alternative if we require to decode a high density picture
	@SuppressWarnings("deprecation")
	public static Drawable getDrawable(Context context, String url, String source2, boolean loadAsHdpi)
	{
		try
		{
			Drawable drawable = getCachedDrawable(context, url);
			if (drawable != null)
				return drawable;

			Services.Log.debug(" getDrawable " + url );
			prepareCookieManager();
			InputStream is = new URL(url).openStream();
			try
			{
				// To ensure get the correct target density
				// http://developer.android.com/reference/android/graphics/drawable/BitmapDrawable.html#BitmapDrawable(java.io.InputStream)
				// load image as were hdpi, set the correct density to the bitmap
				// http://stackoverflow.com/questions/8837810/android-load-drawable-programatically-and-resize-it
				// Could fail in some htc phones

				if (loadAsHdpi)
				{
					// Set options to resize the image
					Options opts = new BitmapFactory.Options();
					opts.inDensity = DisplayMetrics.DENSITY_HIGH;
					drawable = Drawable.createFromResourceStream(context.getResources(), null, is, source2);
				}
				else
				{
					// Load image with the same density as the device (in case we want to use "no scale").
					drawable = Drawable.createFromStream(is, source2);
					if (drawable instanceof BitmapDrawable)
						((BitmapDrawable) drawable).setTargetDensity(context.getResources().getDisplayMetrics());
				}
			}
			finally
			{
				IOUtils.closeQuietly(is);
			}
			CACHE_DRAWABLE.put(url, new SoftReference<>(drawable));
			return drawable;

		}
		catch (IOException ex)
		{
			Services.Log.error(LOG_TAG, "Exception during getDrawable()", ex);
			return null;
		}
		catch (OutOfMemoryError e)
		{
			clearMemoryCache();
			Services.Log.error(LOG_TAG, String.format("Out of memory reading '%s'.", url), e);
			return null;
		}
	}

	public static Drawable getCachedDrawable(Context context, String imageIdentifier)
	{
		final SoftReference<Drawable> ref = CACHE_DRAWABLE.get(imageIdentifier);
		if (ref != null)
		{
			Drawable draw = ref.get();
			if (draw == null)
				CACHE_DRAWABLE.remove(imageIdentifier);

			// HACK needed for Android 11 when the bitmap is in more than one place on the screen
			if (draw instanceof BitmapDrawable)
				draw = new BitmapDrawable(context.getResources(), ((BitmapDrawable) draw).getBitmap());

			return draw;
		}

		return null;
	}

	private static void putInMemoryCache(String url, Bitmap bitmap)
	{
		synchronized (CACHE)
		{
			CACHE.put(url, bitmap);
		}
	}

	private static Bitmap getFromMemoryCache(String url)
	{
		synchronized (CACHE)
		{
			return CACHE.get(url);
		}
	}

	public static void clearImageFromAllCaches(String imageUrl) {
		CACHE.remove(imageUrl);
		CACHE_DRAWABLE.remove(imageUrl);

		File cachedImageFile = getCachedImageFile(imageUrl);
		if (cachedImageFile.exists()) {
			boolean wasFileDeleted = cachedImageFile.delete();
			if (!wasFileDeleted) {
				Services.Log.error(ImageLoader.class.getName(),
						"Could not delete cache file: " + cachedImageFile.getAbsolutePath());
			}
		}
	}

	public static void clearMemoryCache()
	{
		CACHE.evictAll();
		CACHE_DRAWABLE.clear();
	}

	public static ImageLoader fromContext(Context context)
	{
		IGxBaseActivity baseActivity = Cast.as(IGxBaseActivity.class, context);
		if (baseActivity != null)
			return baseActivity.getImageLoader();
		else
			throw new IllegalArgumentException(String.format("Context '%s' does not have an ImageLoader.", context));
	}

	private static class DiskCache
	{
		private final Context mAppContext;
		private File mCacheDir;
		private static final boolean USE_EXTERNAL_STORAGE = false; // Internal by default.
		private static final long HIGH_WATERMARK_BYTES = 50 * 1024 * 1024; // 50 MB
		private static final long LOW_WATERMARK_BYTES = HIGH_WATERMARK_BYTES / 2;

		public DiskCache(Context context) {
			mAppContext = context;
		}

		private synchronized @NonNull File getCacheDir()
		{
			if (mCacheDir == null)
			{
				File parentDir = (USE_EXTERNAL_STORAGE ? mAppContext.getExternalCacheDir() : mAppContext.getCacheDir());

				File cacheDir = new File(parentDir, "ImageLoader");
				if (!cacheDir.exists())
				{
					if (!cacheDir.mkdirs())
						Services.Log.warning(LOG_TAG, "Could not create ImageLoader cache dir! All operations will fail.");
				}

				mCacheDir = cacheDir;
			}

			return mCacheDir;
		}

		@NonNull File getFileFor(String remoteUrl)
		{
			String filename = makeFilename(remoteUrl);
			return new File(getCacheDir(), filename);
		}

		private static final String FILE_RESERVED_CHARS = "|\\?*<\":>+[]/' ";
		private static final int FILE_NAME_MAXIMUM_LENGTH = 127;

		private static String makeFilename(String remoteUrl)
		{
			if (remoteUrl != null)
			{
				for (int i = 0; i < FILE_RESERVED_CHARS.length(); i++)
					remoteUrl = remoteUrl.replace(FILE_RESERVED_CHARS.charAt(i), '_');

				// When trimming, keep the ending part (supposed to be more representative,
				// at least when creating names from URLs).
				if (remoteUrl.length() > FILE_NAME_MAXIMUM_LENGTH)
					remoteUrl = remoteUrl.substring(remoteUrl.length() - FILE_NAME_MAXIMUM_LENGTH);
			}

			return remoteUrl;
		}

		void clear()
		{
			File[] files = getCacheDir().listFiles();
			if (files != null)
			{
				for (File f : files)
					FileUtils.deleteQuietly(f);
			}
		}

		private static class FileInfo
		{
			File file;
			long size;
			long lastModified;
		}

		void trimSize()
		{
			if (Services.Device.isMainThread())
				throw new IllegalStateException("This method must be called from a background thread.");

			long cacheSize = FileUtils.sizeOfDirectory(getCacheDir());
			if (cacheSize > HIGH_WATERMARK_BYTES)
			{
				// If we have surpassed the maximum allowed space for the cache (HIGH_WATERMARK),
				// delete files until we dip below half that (LOW_WATERMARK).
				File[] files = getCacheDir().listFiles();
				if (files != null)
				{
					ArrayList<FileInfo> cacheFiles = new ArrayList<>();
					for (File file : files)
					{
						FileInfo fileInfo = new FileInfo();
						fileInfo.file = file;
						fileInfo.lastModified = file.lastModified();
						fileInfo.size = file.length();
						cacheFiles.add(fileInfo);
					}

					// Sort by ascending modified date.
					Collections.sort(cacheFiles, new Comparator<FileInfo>()
					{
						@Override
						public int compare(FileInfo f1, FileInfo f2)
						{
							return Long.compare(f1.lastModified, f2.lastModified);
						}
					});

					while (cacheFiles.size() > 0 && cacheSize > LOW_WATERMARK_BYTES)
					{
						FileInfo victim = cacheFiles.get(0);
						FileUtils.deleteQuietly(victim.file);
						cacheSize -= victim.size;
						cacheFiles.remove(0);
					}
				}
			}
		}
	}
}
