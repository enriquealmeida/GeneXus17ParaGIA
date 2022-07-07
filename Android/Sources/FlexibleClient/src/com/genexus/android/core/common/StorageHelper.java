package com.genexus.android.core.common;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import androidx.annotation.NonNull;

import com.genexus.android.core.base.services.Services;

public class StorageHelper
{
	/**
	 * Returns the external files directory (normally /mnt/sdcard/Android/data/<package>/files)
	 * if the external storage is mounted and the allowExternal parameter is true,
	 * or the internal files directory otherwise.
	 */
	public static File getStorageDirectory(boolean allowExternal)
	{
		File directory;
		Context context = Services.Application.getAppContext();

		if (allowExternal && Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			// Use getExternalFilesDir().
			// In particular in Android 4.2, this will return different folders per user.
			directory = Services.Application.getAppContext().getExternalFilesDir(null);
		}
		else
		{
			// No external storage. Use private one.
			directory = context.getFilesDir();
		}

		// if null use getFilesDir, avoid crash for not getting external directory.
		if (directory == null)
			directory = context.getFilesDir();

        if (!directory.exists())
        	directory.mkdirs();

		return directory;
	}

	/**
	 * Returns the path of a directory with the specified name under the application's storage directory
	 * (external files directory if the external storage is mounted or the internal files directory otherwise).
	 */
	public static File getStorageDirectory(String name)
	{
		return getStorageDirectory(name, true);
	}

	/**
	 * Returns the path of a directory with the specified name under the application's storage directory.
	 */
	public static File getStorageDirectory(String name, boolean allowExternal)
	{
		File base = getStorageDirectory(allowExternal);
		File sub = new File(base, name);

		if (!sub.exists())
			sub.mkdirs();

		return sub;
	}

	public static File getCacheStorageDirectory(boolean allowExternal)
	{
		File directory;
		Context context = Services.Application.getAppContext();
		if (allowExternal)
		{
			directory = context.getExternalCacheDir();
			if (directory==null)
			{
				directory = context.getCacheDir();
			}
		}
		else
			directory = context.getCacheDir();

		return directory;

	}

	/**
	 *  Creates an empty file with a unique name and the given file extension in the corresponding
	 *  directory (according to the given file extension) inside the app's external storage
	 *  directory.
	 *
	 * @throws IOException if the external storage is unavailable or unable to access the external
	 * storage directory for some reason.
	 */
	public static @NonNull File createNewFileInAppExtStorage(Context context, String subdirectory, String extension) throws IOException {
		return createNewFileInAppExtStorage(context, subdirectory, null, extension);
	}

	/**
	 *  Creates an empty file with the given file name and extension in the corresponding
	 *  directory (according to the given file extension) inside the app's external storage
	 *  directory.
	 *
	 * @throws IOException if the external storage is unavailable or unable to access the external
	 * storage directory for some reason.
	 */
	public static @NonNull File createNewFileInAppExtStorage(Context context, String subdirectory, String name, String extension) throws IOException {
		File directory = context.getExternalFilesDir(subdirectory);
		if (directory == null || !directory.exists()) {
			throw new IOException(String.format("Could not access the external files directory path for '%s'.",
					subdirectory));
		}

		String fileName = name == null || name.isEmpty() ?
				new SimpleDateFormat("yyyy-MM-dd--HH-mm-ss-SSS", Locale.US).format(new Date()) : name;

		return File.createTempFile(fileName, extension, directory);
	}

	@SuppressLint("SdCardPath")
	// TODO: We should stop using this method to determine whether a file uri is local.
	public static boolean isLocalFile(String uri)
	{
		// Approximate.
		return (uri != null && (uri.startsWith("file://") || uri.startsWith("/mnt/") || uri.startsWith("/sdcard/")  || uri.startsWith("/data/") || uri.startsWith("/storage/")));
	}

	public static String getApplicationDataPath()
	{
		File appDirectory = StorageHelper.getStorageDirectory(false);
		return appDirectory.getAbsolutePath();
	}

	public static String getTemporaryFilesPath()
	{
		File tempDirectory = StorageHelper.getCacheStorageDirectory(false);
		return tempDirectory.getAbsolutePath();
	}

	public static String getExternalFilesPath()
	{
		File appExtDirectory = StorageHelper.getStorageDirectory(true);
		return appExtDirectory.getAbsolutePath();
	}

	public static String getMiniAppsDirectoryPath() {
		return getApplicationDataPath() + File.separator + "miniapps" + File.separator;
	}
}
