package com.genexus.android.media.audio;

import android.app.Activity;

import com.genexus.android.core.activities.ActivityHelper;
import com.genexus.android.WithPermission;

/**
 * Audio Recorder wrapper for Offline External Object.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class AudioRecorder
{
	public static boolean start()
	{
		Activity activity = ActivityHelper.getCurrentActivity();

		WithPermission<Boolean> method = new WithPermission.Builder<Boolean>(activity)
				.require(AudioRecorderImpl.PERMISSIONS)
				.setBlockThread(true)
				.attachToActivityController()
				.onSuccess(() -> AudioRecorderImpl.getInstance().start())
				.onFailure(() -> false)
				.build();

		return method.run();
	}

	public static boolean isRecording()
	{
		return AudioRecorderImpl.getInstance().isRecording();
	}

	public static String stop()
	{
		return AudioRecorderImpl.getInstance().stop();
	}
}
