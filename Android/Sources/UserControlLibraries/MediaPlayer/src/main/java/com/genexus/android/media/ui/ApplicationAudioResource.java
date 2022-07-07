package com.genexus.android.media.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.media.MediaDescriptionCompat;

import com.genexus.android.ActivityResourceBase;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.uamp.ui.FullScreenPlayerActivity;

/**
 * Application Audio Resource.
 */
public class ApplicationAudioResource extends ActivityResourceBase
{
	private static final String EXTRA_START_FULLSCREEN_MEDIA = "com.example.android.uamp.EXTRA_START_FULLSCREEN_MEDIA";

	/**
	 * Optionally used with {@link #EXTRA_START_FULLSCREEN_MEDIA} to carry a MediaDescription to
	 * the {@link FullScreenPlayerActivity}, speeding up the screen rendering
	 * while the {@link android.support.v4.media.session.MediaControllerCompat} is connecting.
	 */
	public static final String EXTRA_CURRENT_MEDIA_DESCRIPTION = "com.example.android.uamp.CURRENT_MEDIA_DESCRIPTION";

	private static ApplicationAudioResource sInstance;

	public static ApplicationAudioResource getInstance()
	{
		if (sInstance == null)
			sInstance = new ApplicationAudioResource();

		return sInstance;
	}

	private Intent mMediaPlayerActivityIntent;

	public void setCurrentMediaPlayerActivity(Activity activity)
	{
		Intent intent = new Intent(activity.getIntent());
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		mMediaPlayerActivityIntent = intent;
	}

	public Intent getContentIntent(MediaDescriptionCompat description)
	{
		Intent intent;
		if (mMediaPlayerActivityIntent != null)
		{
			intent = new Intent(mMediaPlayerActivityIntent);
			intent.putExtra(EXTRA_START_FULLSCREEN_MEDIA, true);
		}
		else
			intent = new Intent(Services.Application.getAppContext(), FullScreenPlayerActivity.class);

		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		if (description != null)
			intent.putExtra(EXTRA_CURRENT_MEDIA_DESCRIPTION, description);

		return intent;
	}

	@Override
	public void onCreate(Activity activity, Bundle savedInstanceState)
	{
		// Only check if a full screen player is needed on the first time:
		if (savedInstanceState == null)
			startFullScreenActivityIfNeeded(activity, activity.getIntent());
	}

	@Override
	public void onNewIntent(Activity activity, Intent intent)
	{
		startFullScreenActivityIfNeeded(activity, intent);
	}

	private void startFullScreenActivityIfNeeded(Activity fromActivity, Intent intent)
	{
		if (intent != null && intent.getBooleanExtra(EXTRA_START_FULLSCREEN_MEDIA, false))
		{
			Parcelable currentMediaDesc = intent.getParcelableExtra(EXTRA_CURRENT_MEDIA_DESCRIPTION);

			Intent fullScreenIntent = new Intent(fromActivity, FullScreenPlayerActivity.class)
					.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP)
					.putExtra(EXTRA_CURRENT_MEDIA_DESCRIPTION, currentMediaDesc);

			fromActivity.startActivity(fullScreenIntent);
		}
	}
}
