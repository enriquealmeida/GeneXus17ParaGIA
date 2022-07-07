package com.genexus.android.media;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.artech.base.services.IPropertiesObject;
import com.genexus.android.media.actions.MediaAction;
import com.genexus.android.core.base.metadata.enums.ControlTypes;
import com.genexus.android.core.base.providers.IApplicationServer;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.ResultDetail;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.IProgressListener;
import com.genexus.android.core.common.ImageResizingHelper;
import com.genexus.android.core.common.StorageHelper;
import com.genexus.android.core.utils.FileUtils2;

public class MediaUtils {
	public static final String FULL_FILE_SCHEME = ContentResolver.SCHEME_FILE + "://";

	/**
	 * Useful to check onActivityResult for MediaPicker request codes.
	 */
	public static boolean isPickMediaRequest(int requestCode) {
		return requestCode == MediaAction.PICK_IMAGE.getRequestCode()
				|| requestCode == MediaAction.PICK_VIDEO.getRequestCode()
				|| requestCode == MediaAction.PICK_AUDIO.getRequestCode()
				|| requestCode == MediaAction.PICK_IMAGES.getRequestCode();
	}

	/**
	 * Useful to check onActivityResult for CameraHelper request codes.
	 */
	public static boolean isTakeMediaRequest(int requestCode) {
		return requestCode == MediaAction.TAKE_PICTURE.getRequestCode()
				|| requestCode == MediaAction.CAPTURE_VIDEO.getRequestCode()
				|| requestCode == MediaAction.CAPTURE_AUDIO.getRequestCode();
	}
	
	/**
	 * Creates a local file in the DCIM directory storing the media content and scans it so it pops up in the Gallery.
	 * 
	 * @param uri the media's uri. Supports content:// or file:// schemes.
	 * @param fileName name for the file that will be created.
	 * @return the file path to the created media file.
	 */
	public static String addToGallery(Context context, Uri uri, String fileName) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
			return addToFolderPreQ(context, uri, fileName, Environment.DIRECTORY_DCIM, false);
		} else {
			ContentResolver resolver = context.getContentResolver();
			String mimeType = resolver.getType(uri);
			Uri contentUri = null;

			//MIME Type may be null if scheme is "file"
			if (mimeType == null) mimeType = getMimeType(uri.toString());

			if (mimeType != null) {
				if (mimeType.startsWith("video"))
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				else if (mimeType.startsWith("image"))
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				else if (mimeType.startsWith("audio"))
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
			}

			if (contentUri != null) {
				ContentValues contentValues = new ContentValues();
				contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
				contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
				contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM);

				Uri outputUri = resolver.insert(contentUri, contentValues);

				try {
					OutputStream outputStream = resolver.openOutputStream(outputUri);
					InputStream inputStream;
					if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
						inputStream = context.getContentResolver().openInputStream(uri);
					} else { // Assume it's a local file (with or without the file:// scheme).
						inputStream = new FileInputStream(uri.getPath());
					}
					IOUtils.copy(inputStream, outputStream);
					inputStream.close();
					outputStream.close();
					return uri.getPath();
				} catch (IOException ex) {
					Services.Log.error(String.format("Adding to gallery '%s'", outputUri.toString()), ex);
				}
			}

			return null;
		}
	}

	@SuppressWarnings("deprecation")
	public static String addToFolderPreQ(Context context, Uri uri, String fileName, String directory, boolean renameFile) {
		File externalDir = Environment.getExternalStoragePublicDirectory(directory);
		externalDir.mkdirs();
		File outputFile = new File(externalDir, fileName);

		// do no override, rename to create a new file.
		if (renameFile)
		{
			String destFilePath = outputFile.getPath();
			String destBaseName = FilenameUtils.getBaseName(destFilePath);
			String destExtension = FilenameUtils.getExtension(destFilePath);

			int index = 1;
			while (outputFile.exists())
			{
				// create a new file, Name(i).ext
				String newFileName = destBaseName + " (" + index + ")" + Strings.DOT + destExtension;
				outputFile = new File(externalDir, newFileName);
				index++;
			}
		}

		try {
			if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
				InputStream inputStream = context.getContentResolver().openInputStream(uri);
				FileUtils.copyInputStreamToFile(inputStream, outputFile);
			} else { // Assume it's a local file (with or without the file:// scheme).
				File inputFile = new File(uri.getPath());
				FileUtils.copyFile(inputFile, outputFile);
			}
		} catch (IOException e) {
			return null;
		}

		MediaScannerConnection.scanFile(context, new String[]{outputFile.getAbsolutePath()}, null, null);

		return outputFile.getAbsolutePath();
	}

	public static Uri translateGenexusImageUri(Uri imageUri)
	{
		if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(imageUri.getScheme()))
			return imageUri;

		String imagePath = imageUri.toString();
		if (imagePath.startsWith("Resources/")) {
			imagePath = FileUtils2.SCHEME_GX_RESOURCE + "://" + imagePath;
		} else if (StorageHelper.isLocalFile(imagePath)) {
			if (!imagePath.startsWith(FULL_FILE_SCHEME)) {
				imagePath = FULL_FILE_SCHEME + imagePath;
			}
		} else {
			imagePath = Services.Application.get().UriMaker.getImageUrl(imagePath);
		}
		return Uri.parse(imagePath);
	}

	public static Object translateGenexusBlobPathToUri(Object blobPath)
	{
		if (blobPath instanceof String)
		{
			String blobPathString = (String)blobPath;
			// check if its local file. Then add scheme
			if (Services.Strings.hasValue(blobPathString) && blobPathString.startsWith("/"))
			{
				File file = new File(blobPathString);
				if (file.exists())
					return FULL_FILE_SCHEME + blobPath;
			}
		}
		return blobPath;

	}

	public static String getMimeType(String url) {
		String extension = MimeTypeMap.getFileExtensionFromUrl(url);
		return extension == null ? null : MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
	}

	/**
	 *  Map Genexus' video quality parameter values to Android's parameter values.
	 */
	public static int mapVideoQualityValue(String videoQuality) {
		int videoQualityExtra;
		switch (videoQuality) {
			case "3": // Low quality
				videoQualityExtra = 0;
				break;
			default:
			case "5": // Medium quality
				videoQualityExtra = 1;
				break;
			case "7": // High quality
				videoQualityExtra = 2;
				break;
		}
		return videoQualityExtra;
	}

	/**
	 * Given a content://, file:// or gx.resource:// URI, it saves the data to the server and sets
	 * the token returned by the server to the corresponding control.
	 * If the control is of type {@link ControlTypes#PHOTO_EDITOR}, it also re-sizes the image before
	 * uploading it, in accordance to the policy established.
	 *
	 * @return whether or not the data was successfully saved.
	 */
	public static @NonNull ResultDetail<Void> uploadMedia(@NonNull Context context, @NonNull Uri dataUri, @NonNull String controlName, String controlType,
														  int maxUploadSizeMode, @NonNull IApplicationServer appServer, @NonNull IPropertiesObject properties,
														  @NonNull String objectName, @Nullable IProgressListener listener)
	{
		ResultDetail<Void> result;
		File tmpFile = null;
		try
		{
			File tempDir = new File(context.getCacheDir(), "temp");
			tmpFile = FileUtils2.copyDataToFile(context, dataUri, tempDir);

			if (ControlTypes.PHOTO_EDITOR.equalsIgnoreCase(controlType))
			{
				File resizedImageFile = ImageResizingHelper.resizeImageForUpload(tmpFile, maxUploadSizeMode);
				FileUtils.deleteQuietly(tmpFile);
				tmpFile = resizedImageFile;
			}

			ResultDetail<String> uploadResult = appServer.uploadBinary(tmpFile, objectName, listener);
			if (uploadResult.getResult())
			{
				String binaryToken = uploadResult.getData();
				if (binaryToken != null)
				{
					properties.setProperty(controlName, binaryToken);
					result = ResultDetail.ok(null);
				}
				else
					result = ResultDetail.error("Media upload was successful but no token was returned", null);
			}
			else
				result = ResultDetail.error(uploadResult.getMessage(), null);
		}
		catch (IOException e)
		{
			Services.Log.error("Error while attempting to save control value.", e);
			result = ResultDetail.error(e.getMessage(), null);
		}
		finally
		{
			FileUtils.deleteQuietly(tmpFile);
		}

		return result;
	}
}
