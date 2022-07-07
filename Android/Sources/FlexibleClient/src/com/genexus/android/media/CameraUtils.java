package com.genexus.android.media;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

public class CameraUtils {
	
	public static boolean supportsExtraOutput()
	{
		// Samsung *Galaxy Models* contain a Camera app that does not implement EXTRA_OUTPUT properly.
		// Either doesn't support it or have a different behavior than the specified (e.g. Copies the
		// media file to both the destination path in the uri and the default gallery path).
		//
		// Update: Samsung fixed it in Android 7.0
		//
		// Tested with Samsung Remote Test Lab
		// Model       - Android - Device Model - Works without EXTRA_OUTPUT
		// Galaxy SIII - 4.3     - GT-I9300-KR1 - Yes
		// Galaxy S4   - 5.0.1   - GT-I9500-PL3 - No (it doesn't work with EXTRA_OUTPUT either)
		// Galaxy S5   - 5.0     - SM-G900F-KR1 - Yes
		// Galaxy S5   - 6.0.1   - SM-G900F-PL2 - Yes
		// Galaxy S6   - 6.0.1   - SM-G920V-PL2 - Yes
		// Galaxy S6   - 7.0     - SM-G920S-KR2 - No (but works with EXTRA_OUTPUT)
		// Galaxy S7   - 6.0.1   - SM-G930F-IN2 - Yes
		// Galaxy S7   - 7.0     - SM-G930F-PL2 - No (but works with EXTRA_OUTPUT)
		boolean isSamsungGalaxy = Build.MANUFACTURER.equalsIgnoreCase("samsung") &&
				(Build.MODEL.startsWith("GT-") || Build.MODEL.startsWith("SM-")) &&
				Build.VERSION.SDK_INT < Build.VERSION_CODES.N;

		// Tested with LG G2 Mini (LG-D625) - Works without EXTRA_OUTPUT. With EXTRA_OUTPUT it saves
		// the picture to both the specified file and to the gallery.
		boolean isLGG2 = Build.MODEL.startsWith("LG-D625")  &&
			Build.VERSION.SDK_INT < Build.VERSION_CODES.N;

		return !isSamsungGalaxy && !isLGG2;
	}

	public static void copyUriToFile(Context context, Uri inputUri, File outputFile)
	{
		try
		{
			InputStream inputStream = context.getContentResolver().openInputStream(inputUri);
			if (inputStream != null)
			{
				OutputStream outputStream = new FileOutputStream(outputFile);
				IOUtils.copy(inputStream, outputStream);
			}
		}
		catch (IOException e) { }
	}
}
