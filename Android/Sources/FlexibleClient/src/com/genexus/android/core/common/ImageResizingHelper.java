package com.genexus.android.core.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.exifinterface.media.ExifInterface;
import androidx.annotation.NonNull;

import android.os.Build;
import android.util.Pair;
import android.webkit.MimeTypeMap;

import com.genexus.android.core.base.metadata.enums.ImageUploadModes;
import com.genexus.android.core.base.metadata.settings.UploadSizeDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.utils.FileUtils2;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.squareup.picasso.Picasso;

/**
 * Helper class to resize image for uploading to the server.
 */
public class ImageResizingHelper {

	/**
	 * Given an image {@link File}, it creates a new {@link File} containing a re-sized
	 * image that complies to the size policy given. In case the original image already complied
	 * with the policy, the content is simply copied as-is.
	 *
	 * @return a new {@link File} containing the complying image.
	 * @throws IOException
	 */
	public static @NonNull File resizeImageForUpload(@NonNull File srcFile, int maxUploadSizeMode) throws IOException {
		String destBaseName = FilenameUtils.getBaseName(srcFile.getPath()) + "_resized";
		String srcExtension = FilenameUtils.getExtension(srcFile.getPath());
		File destPath = srcFile.getParentFile();

		UploadSizeDefinition uploadSizeDef = Services.Application.getDefinition().getPatternSettings().getUploadSize(maxUploadSizeMode);

		File destFile;
		if (uploadSizeDef.UploadMode == ImageUploadModes.ACTUALSIZE) {
			destFile = new File(destPath, destBaseName + "." + srcExtension);
			FileUtils.copyFile(srcFile, destFile);
		} else if (uploadSizeDef.SizeMode == UploadSizeDefinition.SIZEINPX) {
			BitmapFactory.Options options = getImageDimensions(srcFile);
			boolean wideImage = options.outWidth > options.outHeight;
			int targetWidth = wideImage ? uploadSizeDef.SizeLimit : 0;
			int targetHeight = wideImage ? 0 : uploadSizeDef.SizeLimit;
			destFile = scaleDownToDesiredDimensions(srcFile, destPath, destBaseName, targetWidth, targetHeight);
		} else {
			destFile = scaleDownToDesiredFileSize(srcFile, destPath, destBaseName, uploadSizeDef.SizeLimit);
		}

		return destFile;
	}

	/**
	 * <p>Scales down the image to the desired dimensions preserving the image's ratio. If the image dimensions
	 * are already lower than those desired, the image is just copied to a new {@link File}.</p>
	 * <p>Moreover, if the image is scaled down, the encoding will be preserved in case of PNG and JPEG. Otherwise,
	 * JPEG will be used as the default encoding format.</p>
	 *
	 * @param srcFile      the image {@link File} that we want to scale down.
	 * @param destPath     the path of the directory in which the scaled down image should be created.
	 * @param destBaseName the base name (name without the extension) of the file in which scaled down image should be created.
	 * @param targetWidth  the target width for the scaled down image.
	 * @param targetHeight the target height for the scaled down image.
	 * @throws IOException in case of an unexpected error
	 * @return the file in which scaled down image {@link File} was saved.
	 */
	private static File scaleDownToDesiredDimensions(@NonNull File srcFile, @NonNull File destPath, @NonNull String destBaseName,
													 int targetWidth, int targetHeight) throws IOException {
		BitmapFactory.Options options = getImageDimensions(srcFile);
		boolean shouldResizeImage = (targetWidth > 0 && targetWidth < options.outWidth)
				|| (targetHeight > 0 && targetHeight < options.outHeight);
		String srcExtension = FilenameUtils.getExtension(srcFile.getPath());

		if (shouldResizeImage) {
			Bitmap scaledBitmap = Picasso.get()
					.load(srcFile)
					.resize(targetWidth, targetHeight)
					.get();

			if (scaledBitmap == null) {
				throw new IOException(String.format("Could not scale down the image file: %s", srcFile.getAbsolutePath()));
			}

			String mimeType = FileUtils2.getMimeType(srcFile);

			/*
			// Currently this is not necessary because the EXIF metadata is not copied after
			if (mimeType != null && mimeType.equals("image/jpeg")) // only jpeg has EXIF metadata
			{
				// Picasso reads the picture's orientation from the EXIF metadata and rotates it. Thus,
				// the resulting bitmap is in the correct position and the Orientation TAG should
				// always be set to NORMAL so it isn't rotated again.
				try
				{
					ExifInterface exif = new ExifInterface(srcFile.getAbsolutePath());
					exif.resetOrientation();
					exif.saveAttributes();
				}
				catch (IOException ignored) { }
			}
			*/

			Pair<Bitmap.CompressFormat, String> p = getCompressFormatAndExtension(mimeType);
			File destFile = new File(destPath, destBaseName + "." + p.second);

			try (FileOutputStream outputStream = new FileOutputStream(destFile)) {
				boolean compressionSuccessful = scaledBitmap.compress(p.first, 100, outputStream);
				scaledBitmap.recycle();

				if (!compressionSuccessful) {
					throw new IOException("Compression failed.");
				}
			}
			return destFile;
		} else {
			File destFile = new File(destPath, destBaseName + "." + srcExtension);
			FileUtils.copyFile(srcFile, destFile);
			return destFile;
		}
	}

	private static Pair<Bitmap.CompressFormat, String> getCompressFormatAndExtension(String mimeType)
	{
		if (mimeType == null)
			return new Pair<>(Bitmap.CompressFormat.JPEG, "jpeg"); // all unknown file types are saved as jpeg in destination.

		switch (mimeType) {
			case "image/png":
				return new Pair<>(Bitmap.CompressFormat.PNG, "png");
			case "image/webp":
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
					return new Pair<>(Bitmap.CompressFormat.WEBP_LOSSLESS, "webp");
				else
					return new Pair<>(getCompressFormatWebpDeprecated(), "webp");
			case "image/jpeg":
			default: // all other file types (like i.e. gif) are saved as jpeg in destination.
				return new Pair<>(Bitmap.CompressFormat.JPEG, "jpeg");
		}
	}

	@SuppressWarnings("deprecation")
	private static Bitmap.CompressFormat getCompressFormatWebpDeprecated() {
		return Bitmap.CompressFormat.WEBP;
	}

	/**
	 * Given a source image {@link File} and a target for the image {@link File} size we want to achieve,
	 * this method attempts to scale down the image according to a coefficient obtained magically from the
	 * {@code ImageUploadModes.getScaleRatioFromCoefficient} method. It then attempts a second try in case the resulting
	 * image {@link File} size is still not close enough (within 10%) to the {@code targetSize}.
	 *
	 * @param srcFile    the image {@link File} that we want to scale down.
	 * @param destPath     the path of the directory in which the scaled down image should be created.
	 * @param destBaseName the base name (name without the extension) of the file in which scaled down image should be created.
	 * @param targetSize the target image {@link File} size (expressed in KBytes).
	 * @throws IOException in case of an unexpected error
	 * @return the file in which scaled down image {@link File} was saved.
	 */
	private static File scaleDownToDesiredFileSize(@NonNull File srcFile, @NonNull File destPath, @NonNull String destBaseName,
												   double targetSize) throws IOException {
		double srcFileSize = srcFile.length() / 1024;
		int scale = ImageUploadModes.getScaleRatioFromCoefficient(srcFileSize / targetSize);

		String srcExtension = FilenameUtils.getExtension(srcFile.getPath());
		if (scale <= 1) {
			File destFile = new File(destPath, destBaseName + "." + srcExtension);
			FileUtils.copyFile(srcFile, destFile);
			return destFile;
		}

		File firstAttemptFile = File.createTempFile("first_attempt", "_tmp." + srcExtension, destPath); // Use srcExtension for temporary file so we get a unique name for that extension that will probably be the final extension
		File secondAttemptFile = File.createTempFile("second_attempt", "_tmp." + srcExtension, destPath);
		File firstAttemptDestFile = null;
		File secondAttemptDestFile = null;

		try {
			// First attempt.
			BitmapFactory.Options options = getImageDimensions(srcFile);
			firstAttemptDestFile = scaleDownToDesiredDimensions(srcFile, destPath, FilenameUtils.getBaseName(firstAttemptFile.getPath()), options.outWidth / scale, options.outHeight / scale);
			double scaledImageSize = firstAttemptDestFile.length() / 1024;

			if (scaledImageSize > targetSize * 1.1) {
				scale = scale + 1;
			} else if (scaledImageSize < targetSize * 0.9) {
				scale = scale - 1;
			} else {
				scale = -1;
			}

			File chosenFile;

			// Second attempt (if the image size is still not close to the target size).
			if (scale > 0) {
				secondAttemptDestFile = scaleDownToDesiredDimensions(srcFile, destPath, FilenameUtils.getBaseName(secondAttemptFile.getPath()), options.outWidth / scale, options.outHeight / scale);
				double scaledImage2Size = secondAttemptDestFile.length() / 1024;
				// Use this second scaled image if the distance to the target size is closer and its size is under 110% of the target size.
				// Otherwise, just use the first scaled image.
				chosenFile = Math.abs(scaledImage2Size - targetSize) < Math.abs(scaledImageSize - targetSize)
						&& scaledImage2Size < targetSize * 1.1
						? secondAttemptDestFile
						: firstAttemptDestFile;
			} else {
				chosenFile = firstAttemptDestFile;
			}

			String destExtension = FilenameUtils.getExtension(chosenFile.getPath());
			File destFile = new File(destPath, destBaseName + "." + destExtension);
			FileUtils.copyFile(chosenFile, destFile);
			return destFile;
		} finally {
			FileUtils.deleteQuietly(firstAttemptFile);
			FileUtils.deleteQuietly(secondAttemptFile);
			FileUtils.deleteQuietly(firstAttemptDestFile);
			FileUtils.deleteQuietly(secondAttemptDestFile);
		}
	}

	private static @NonNull BitmapFactory.Options getImageDimensions(@NonNull File srcFile) throws IOException {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		try (FileInputStream inputStream = new FileInputStream(srcFile)) {
			BitmapFactory.decodeStream(inputStream, null, options);
		}

		if (options.outWidth < 0 || options.outHeight < 0)
		{
			String fileExtension = FilenameUtils.getExtension(srcFile.getAbsolutePath());
			String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);

			if (mimeType != null) {
				if (mimeType.equals("image/jpeg")) {
					// Attempt to obtain the image dimensions from the EXIF information if it could not be obtained decoding the image file.
					ExifInterface exifInterface = new ExifInterface(srcFile.getAbsolutePath());
					options.outWidth = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0);
					options.outHeight = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0);
				} else if (mimeType.equals("image/svg+xml")) {
					try (InputStream stream = new FileInputStream(srcFile)) {
						SVG svg = SVG.getFromInputStream(stream);
						options.outWidth = (int) Math.ceil(svg.getDocumentViewBox().width());
						options.outHeight = (int) Math.ceil(svg.getDocumentViewBox().height());
					} catch (SVGParseException | IOException e) {
						Services.Log.error(e);
					}
				}
			}
		}

		if (options.outWidth <= 0 || options.outHeight <= 0) {
			throw new IOException(String.format("Unable to obtain image dimensions for the file: %s", srcFile.getAbsolutePath()));
		}

		return options;
	}
}
