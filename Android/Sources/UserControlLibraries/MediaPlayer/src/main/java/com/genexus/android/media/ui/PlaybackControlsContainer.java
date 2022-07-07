package com.genexus.android.media.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;

import com.genexus.android.core.actions.ICustomMenuManager;
import com.genexus.android.ActivityResources;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Function;
import com.genexus.android.core.resources.BuiltInResources;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.usercontrols.IGxUserControl;
import com.genexus.android.uamp.MusicService;
import com.genexus.android.uamp.R;
import com.genexus.android.uamp.ui.PlaybackControlsFragment;
import com.genexus.android.uamp.utils.LogHelper;

/**
 * Playback controls holder.
 */
@SuppressLint("ViewConstructor")
public class PlaybackControlsContainer extends FrameLayout implements IGxUserControl, ICustomMenuManager
{
	private static final String TAG = LogHelper.makeLogTag(PlaybackControlsContainer.class);

	private final AppCompatActivity mActivity;
	private MediaBrowserCompat mMediaBrowser;
	private PlaybackControlsFragment mControlsFragment;
	private ActivityCastResource mCastResource;

	private boolean mUseCard;
	private boolean mVisibleWhenStopped;

	public PlaybackControlsContainer(Context context, Coordinator coordinator, LayoutItemDefinition definition)
	{
		super(context);

		// TODO: These two should be properties of the UC. For now they are hardcoded
		mUseCard = false;
		mVisibleWhenStopped = true;

		//noinspection ConstantConditions
		if (mUseCard)
		{
			CardView cardView = new CardView(context);
			cardView.setId(R.id.fragment_playback_controls);
			cardView.setCardElevation(Services.Device.dipsToPixels(8));
			cardView.setCardBackgroundColor(BuiltInResources.getResource(context, R.color.cardview_dark_background, R.color.cardview_light_background, R.color.cardview_light_background));
			addView(cardView, generateDefaultLayoutParams());
		}
		else
			setId(R.id.fragment_playback_controls);

		mActivity = (AppCompatActivity)context;

		// Replace leftover fragment, if any, before adding new one.
		Fragment oldFragment = mActivity.getSupportFragmentManager().findFragmentById(R.id.fragment_playback_controls);
		if (oldFragment != null)
			mActivity.getSupportFragmentManager().beginTransaction().remove(oldFragment).commit();

		mControlsFragment = new PlaybackControlsFragment();
		mActivity.getSupportFragmentManager().beginTransaction().add(R.id.fragment_playback_controls, mControlsFragment).commit();

		// Connect a media browser just to get the media session token. There are other ways
		// this can be done, for example by sharing the session token directly.
		mMediaBrowser = new MediaBrowserCompat(mActivity, new ComponentName(mActivity, MusicService.class), mConnectionCallback, null);

		// Initialize the Cast resource.
		mCastResource = ActivityResources.getResource(mActivity, ActivityCastResource.class, new Function<Activity, ActivityCastResource>()
		{
			@Override
			public ActivityCastResource run(Activity activity)
			{
				return new ActivityCastResource(mActivity);
			}
		});
	}

	@Override
	protected void onAttachedToWindow()
	{
		super.onAttachedToWindow();
		hidePlaybackControls();
		mMediaBrowser.connect();
	}

	@SuppressWarnings("FieldCanBeLocal")
	private final MediaBrowserCompat.ConnectionCallback mConnectionCallback =
			new MediaBrowserCompat.ConnectionCallback() {
				@Override
				public void onConnected() {
					LogHelper.d(TAG, "onConnected");
					connectToSession(mMediaBrowser.getSessionToken());
				}
			};

	@SuppressWarnings("deprecation")
	@Override
	protected void onDetachedFromWindow()
	{
		if (MediaControllerCompat.getMediaController(mActivity) != null)
			MediaControllerCompat.getMediaController(mActivity).unregisterCallback(mMediaControllerCallback);

		mMediaBrowser.disconnect();

		super.onDetachedFromWindow();
	}

	protected void showPlaybackControls()
	{
		setVisibility(View.VISIBLE);
	}

	protected void hidePlaybackControls()
	{
		setVisibility(View.GONE);
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
				return false;
			case PlaybackStateCompat.STATE_STOPPED:
				return mVisibleWhenStopped;
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

			// onMediaControllerConnected();

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

	@Override
	public void onCustomCreateOptionsMenu(Menu menu)
	{
		if (mCastResource != null)
			mCastResource.onCreateOptionsMenu(menu);
	}
}
