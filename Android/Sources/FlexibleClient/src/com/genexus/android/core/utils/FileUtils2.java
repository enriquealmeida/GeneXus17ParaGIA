package com.genexus.android.core.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.OpenableColumns;
import android.webkit.MimeTypeMap;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.StorageHelper;

import androidx.annotation.NonNull;
import androidx.exifinterface.media.ExifInterface;

public class FileUtils2 {
    public static final String SCHEME_GX_RESOURCE = "gx.resource";
    public static final String SCHEME_HTTP = "http";
    public static final String SCHEME_HTTPS = "https";
    public static final String[] COPYABLE_DATA_URIS = {ContentResolver.SCHEME_CONTENT,
            ContentResolver.SCHEME_FILE, SCHEME_GX_RESOURCE, SCHEME_HTTP, SCHEME_HTTPS};

    private static final HashMap<String, String> OVERRIDE_MIME_TYPES;
	static
	{
		OVERRIDE_MIME_TYPES = new HashMap<>();
		OVERRIDE_MIME_TYPES.put("m4a", "audio/mp4");
	}

    /**
     * Obtains the MIME type of a {@link File} object.
     *
     * @param file to obtain its mime type.
     * @return the mime type associated to the extension of the {@code file} passed.
     */
    public static String getMimeType(@NonNull File file)
	{
        String fileUrl = Uri.fromFile(file).toString();
        String fileExt = MimeTypeMap.getFileExtensionFromUrl(fileUrl);

		if (Strings.hasValue(fileExt))
		{
			String overrideMimeType = OVERRIDE_MIME_TYPES.get(fileExt.toLowerCase(Locale.US));
			if (overrideMimeType != null)
				return overrideMimeType;
		}

		// Use the default mime type list from libcore.
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExt);
    }

    public static boolean canCopyDataToFile(@NonNull Uri dataUri) {
        return Arrays.asList(COPYABLE_DATA_URIS).contains(dataUri.getScheme());
    }

    /**
     * Stores in a local {@link File} the data referenced by an URI. <b>Only</b> the following schemes
     * are supported: content, file, gx.resources, http and https.
     *
     * @param context necessary to access the content.
     * @param dataUri URI that references the content.
     * @param destDirectory directory in which the file containing the obtained data should be stored.
     *
     * @return the file where the data was stored.
     *
     * @throws IOException if an error occurs attempting to obtain the content from {@code dataUri}.
     * @throws FileNotFoundException if the file referenced by the {@code file} URI provided in
     *         {@code dataUri} does not exist.
     */
	public static @NonNull File copyDataToFile(@NonNull Context context, @NonNull Uri dataUri, @NonNull File destDirectory) throws IOException {
        File destFile;
        String scheme = dataUri.getScheme();

        if (scheme == null) {
			throw new IllegalArgumentException("Uri scheme is null");
		}

        switch (scheme) {
            case ContentResolver.SCHEME_CONTENT: {
                InputStream dataStream = context.getContentResolver().openInputStream(dataUri);
                if (dataStream == null) {
                    throw new IOException("Could not open an input stream for the URI specified: " + dataUri);
                }
                String fileName = getFileNameWithExtension(context, dataUri);
                destFile = new File(destDirectory, fileName);
                FileUtils.copyInputStreamToFile(dataStream, destFile);
                break;
			}

            case ContentResolver.SCHEME_FILE: {
            	String filePath = dataUri.getPath();
            	if (filePath == null) {
            		throw new IllegalArgumentException(
            			String.format("The URI provided (%s) has a valid scheme (file://) but contains an invalid filepath",
							dataUri)
					);
				}
				File sourceFile = new File(filePath);
				if (!sourceFile.exists()) {
					throw new FileNotFoundException("File not found: " + sourceFile.getPath());
				}
				destFile = new File(destDirectory, sourceFile.getName());
				FileUtils.copyFile(sourceFile, destFile);
				break;
			}

            case SCHEME_GX_RESOURCE: {
				String uriWithNoScheme = dataUri.toString().substring((SCHEME_GX_RESOURCE + "://").length());
				int resId = Services.Resources.getDataImageResourceId(uriWithNoScheme);
				if (resId == 0) {
					throw new IOException(String.format("Embedded resource with name '%s' not found in the app's resources.", dataUri.getLastPathSegment()) + dataUri);
				}
				String resFileName = dataUri.getLastPathSegment();
				if (resFileName == null) {
					throw new IllegalArgumentException(
						String.format("The URI provided (%s) has a valid scheme (gx.resource://) but contains an invalid last path segment",
							dataUri)
					);
				}
				InputStream resStream = context.getResources().openRawResource(resId);
				destFile = new File(destDirectory, resFileName);
				FileUtils.copyInputStreamToFile(resStream, destFile);
				break;
			}

            case SCHEME_HTTP:
            case SCHEME_HTTPS: {
                String plainUrl = dataUri.toString();
				InputStream dataStream = new URL(plainUrl).openStream();
                String fileName = FilenameUtils.getBaseName(plainUrl);
                String fileExtension = FilenameUtils.getExtension(plainUrl);
                destFile = new File(destDirectory, fileName + "." + fileExtension);
				if (dataStream != null) {
					FileUtils.copyInputStreamToFile(dataStream, destFile);
				}
				break;
			}

            default:
                throw new IllegalArgumentException(
                        String.format("URI with scheme '%s'. Only the following schemes are supported: %s.",
                                scheme,
                                Arrays.toString(COPYABLE_DATA_URIS)
                        )
                );
        }

		copyExifInfoIfPossible(context, dataUri, destFile);

		return destFile;
    }

	private static void copyExifInfoIfPossible(@NonNull Context context, @NonNull Uri dataUri, File destFile) {
		String mimeType = context.getContentResolver().getType(dataUri);

		if (mimeType != null && mimeType.equals("image/jpeg")) {
			String inputOrientation;

            try (InputStream inputStream = context.getContentResolver().openInputStream(dataUri))
			{
				if (inputStream == null)
				{
					Services.Log.debug(String.format("Error while attempting to open URI(%s)", dataUri));
					return;
				}

				ExifInterface inputExifInterface = new ExifInterface(inputStream);
				inputOrientation = inputExifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);

				ExifInterface outputExifInterface = new ExifInterface(destFile.getAbsolutePath());
				outputExifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, inputOrientation);
				outputExifInterface.saveAttributes();

			} catch (FileNotFoundException e) {
				Services.Log.debug(String.format("Error while attenting to open URI URI(%s) ", dataUri));
			}
            catch (IOException e) {
            	Services.Log.debug(String.format("Error while copying Exif information from URI(%s) to FILE (%s)", dataUri, destFile));
			}
        }
	}

	public static String saveBitmapToFile(Context context, Bitmap bitmap, String extension) {
		return saveBitmapToFile(context, bitmap, null, null, extension);
	}

	public static String saveBitmapToFile(Context context, Bitmap bitmap, String fileName, String extension) {
		return saveBitmapToFile(context, bitmap, null, fileName, extension);
	}

	public static String saveBitmapToFile(Context context, Bitmap bitmap, String subdirectory, String fileName, String extension) {
		Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.PNG;
		if (!Strings.hasValue(extension) || extension.equalsIgnoreCase(".png"))
			extension = ".png";
		else {
			extension = ".jpeg";
			compressFormat = Bitmap.CompressFormat.JPEG;
		}

		try {
			File file = StorageHelper.createNewFileInAppExtStorage(context, subdirectory, fileName, extension);
			FileOutputStream outputStream = new FileOutputStream(file);
			bitmap.compress(compressFormat, 100, outputStream);
			return file.getAbsolutePath();
		} catch (IOException e) {
			Services.Log.error(e);
		}
		return Strings.EMPTY;
	}

	public static boolean isQueryable(@NonNull Uri uri)
	{
		if (!ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(uri.getScheme()))
			throw new IllegalArgumentException("This method only accepts content:// URIs.");

		// Contact photos are not "openable"
		if (uri.getAuthority().equals(ContactsContract.AUTHORITY))
			return false;

		// Document provider blobs are known to be openable.
		// For all others that we detect, add exceptions.
		return true;
	}

    public static String getFileNameWithExtension(@NonNull Context context, @NonNull Uri uri)
	{
        if (!ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(uri.getScheme()))
            throw new IllegalArgumentException("This method only accepts content:// URIs.");

		String fileName = "";
		if (isQueryable(uri))
		{
			try (Cursor cursor = context.getContentResolver().query(uri, new String[]{OpenableColumns.DISPLAY_NAME}, null, null, null))
			{
				if (cursor != null && cursor.moveToFirst())
					fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
			}
			Services.Log.debug("getFileNameWithExtension , isQueryable  uri: " + uri + " fileName: " + fileName);
		}

		if (!Strings.hasValue(fileName))
		{
			String mimeType = context.getContentResolver().getType(uri);
			Services.Log.debug("getFileNameWithExtension , else uri: " + uri + " mimeType: " + mimeType);
			if (Strings.hasValue(mimeType))
			{
				String name = UUID.randomUUID().toString();
				String extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
				if (Strings.hasValue(extension))
					fileName = name + "." + extension;
			}
			Services.Log.debug("getFileNameWithExtension , else fileName: " + fileName);
		}

        return fileName;
    }

    public static String getFileName(String fileUri) {
		String name = Strings.EMPTY;

		if (fileUri != null) {
			int slashIndex = fileUri.lastIndexOf("/");
			if (slashIndex != -1) {
				String nameAndExtension = fileUri.substring(slashIndex + 1);
				String[] split = Services.Strings.split(nameAndExtension, ".");
				if (split.length == 2) {
					try {
						name = URLDecoder.decode(split[0], "UTF-8");
					} catch (UnsupportedEncodingException ignored) {
						name = split[0];
					}
				}
			}
		}

		return name;
	}

	public static String getFileExtension(String fileUri) {
		String type = Strings.EMPTY;

		if (fileUri != null) {
			int dotIndex = fileUri.lastIndexOf(".");
			if (dotIndex != -1)
				type = fileUri.substring(dotIndex + 1);
		}

		return type;
	}

	public static void copyDataToFileAsync(@NonNull Context context,
                                           @NonNull Uri dataUri,
										   @NonNull File destDirectory,
										   @NonNull CopyDataToFileListener listener) {
		TaskRunner.execute(new CopyDataToFileTask(context, dataUri, destDirectory, listener));
	}

	public static boolean isHttp(String url) {
		return Strings.hasValue(url) && (url.startsWith(SCHEME_HTTP) || url.startsWith(SCHEME_HTTPS));
	}

    private static class CopyDataToFileTask extends TaskRunner.BaseTask<File> {
        private final Context mContext;
        private final Uri mDataUri;
        private final File mDestDirectory;
        private final CopyDataToFileListener mListener;

        public CopyDataToFileTask(Context context, Uri dataUri, File destDirectory,
								  CopyDataToFileListener listener) {
            mContext = context;
            mDataUri = dataUri;
            mDestDirectory = destDirectory;
            mListener = listener;
        }

        @Override
        public File doInBackground() {
            try {
                return copyDataToFile(mContext, mDataUri, mDestDirectory);
            } catch (IOException e) {
				Services.Log.error("FileUtils2", "Copy data to file failed: " + e.getMessage());
                return null;
            }
        }

        @Override
        public void onPostExecute(File destFile) {
            boolean successful = destFile.exists();
            mListener.onCopyDataFinished(successful, destFile);
        }
    }

	/**
	 * Same as {@link org.apache.commons.io.FileUtils#directoryContains(File, File)} but doesn't
	 * throw an exception, and accepts receiving null in the first argument. Returns false in
	 * both of those cases, otherwise returns the same result.
	 */
	public static boolean directoryContains(File directory, File child)
	{
		if (directory == null)
			return false;

		try
		{
			return FileUtils.directoryContains(directory, child);
		}
		catch (IOException e)
		{
			Services.Log.debug(String.format("Exception in directoryContains (directory = '%s'; child = '%s')", directory, child), e);
			return false;
		}
	}

    public interface CopyDataToFileListener {
        void onCopyDataFinished(boolean successful, @NonNull File destFile);
    }
}
