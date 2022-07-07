package com.genexus.android.media.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import androidx.appcompat.app.AppCompatActivity;

import com.genexus.android.ActivityResourceBase;
import com.genexus.android.uamp.MusicService;
import com.genexus.android.uamp.R;
import com.genexus.android.uamp.ui.PlaybackControlsFragment;
import com.genexus.android.uamp.utils.LogHelper;

/**
 * Base activity for activities that need to show a playback control fragment when media is playing.
 */
public class ActivityMediaBrowserResource extends ActivityResourceBase
{
	private static final String TAG = LogHelper.makeLogTag(ActivityMediaBrowserResource.class);

	private AppCompatActivity mActivity;
	private MediaBrowserCompat mMediaBrowser;
	private PlaybackControlsFragment mControlsFragment;

	@Override
	public void onCreate(Activity activity, Bundle savedInstanceState) {
		mActivity = (AppCompatActivity)activity;

		// Connect a media browser just to get the media session token. There are other ways
		// this can be done, for example by sharing the session token directly.
		mMediaBrowser = new MediaBrowserCompat(mActivity,
			new ComponentName(mActivity, MusicService.class), mConnectionCallback, null);
	}

	@Override
	public void onStart(Activity activity) {

		mControlsFragment = (PlaybackControlsFragment) mActivity.getSupportFragmentManager()
				.findFragmentById(R.id.fragment_playback_controls);
		if (mControlsFragment == null) {
			throw new IllegalStateException("Mising fragment with id 'controls'. Cannot continue.");
		}

		hidePlaybackControls();

		mMediaBrowser.connect();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStop(Activity activity) {
		if (MediaControllerCompat.getMediaController(mActivity) != null) {
			MediaControllerCompat.getMediaController(mActivity).unregisterCallback(mMediaControllerCallback);
		}
		mMediaBrowser.disconnect();
	}

	protected void onMediaControllerConnected() {
		// empty implementation, can be overridden by clients.
	}

	protected void showPlaybackControls() {
		LogHelper.d(TAG, "showPlaybackControls");

		mActivity.getSupportFragmentManager().beginTransaction()
			/*
			.setCustomAnimations(
				R.animator.slide_in_from_bottom, R.animator.slide_out_to_bottom,
				R.animator.slide_in_from_bottom, R.animator.slide_out_to_bottom)
			*/
				.show(mControlsFragment)
				.commit();
	}

	protected void hidePlaybackControls() {
		LogHelper.d(TAG, "hidePlaybackControls");
		mActivity.getSupportFragmentManager().beginTransaction()
				.hide(mControlsFragment)
				.commit();
	}

	/**
	 * Check if the MediaSessionCompat is active and in a "playback-able" state
	 * (not NONE and not STOPPED).
	 *
	 * @return true if the MediaSessionCompat's state requires playback controls to be visible.
	 */
	@SuppressWarnings("deprecation")
	protected boolean shouldShowControls() {
		MediaControllerCompat mediaController = MediaControllerCompat.getMediaController(mActivity);
		if (mediaController == null ||
				mediaController.getMetadata() == null ||
				mediaController.getPlaybackState() == null) {
			return false;
		}
		switch (mediaController.getPlaybackState().getState()) {
			case PlaybackStateCompat.STATE_ERROR:
			case PlaybackStateCompat.STATE_NONE:
			case PlaybackStateCompat.STATE_STOPPED:
				return false;
			default:
				return true;
		}
	}

	@SuppressWarnings("deprecation")
	private void connectToSession(MediaSessionCompat.Token token) {

			MediaControllerCompat mediaController = new MediaControllerCompat(mActivity, token);
			MediaControllerCompat.setMediaController(mActivity, mediaController);
			mediaController.registerCallback(mMediaControllerCallback);

			if (shouldShowControls()) {
				showPlaybackControls();
			} else {
				LogHelper.d(TAG, "connectionCallback.onConnected: " +
						"hiding controls because metadata is null");
				hidePlaybackControls();
			}

			if (mControlsFragment != null) {
				mControlsFragment.onConnected();
			}

			onMediaControllerConnected();

	}

	// Callback that ensures that we are showing the controls
	private final MediaControllerCompat.Callback mMediaControllerCallback =
			new MediaControllerCompat.Callback() {
				@Override
				public void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
					if (shouldShowControls()) {
						showPlaybackControls();
					} else {
						LogHelper.d(TAG, "mediaControllerCallback.onPlaybackStateChanged: " +
								"hiding controls because state is ", state.getState());
						hidePlaybackControls();
					}
				}

				@Override
				public void onMetadataChanged(MediaMetadataCompat metadata) {
					if (shouldShowControls()) {
						showPlaybackControls();
					} else {
						LogHelper.d(TAG, "mediaControllerCallback.onMetadataChanged: " +
								"hiding controls because metadata is null");
						hidePlaybackControls();
					}
				}
			};


	private final MediaBrowserCompat.ConnectionCallback mConnectionCallback =
			new MediaBrowserCompat.ConnectionCallback() {
				@Override
				public void onConnected() {
					LogHelper.d(TAG, "onConnected");
					connectToSession(mMediaBrowser.getSessionToken());
				}
			};
}
