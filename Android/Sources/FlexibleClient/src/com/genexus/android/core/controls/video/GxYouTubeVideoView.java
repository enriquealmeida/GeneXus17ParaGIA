package com.genexus.android.core.controls.video;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.net.Uri;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.genexus.android.R;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.fragments.LayoutFragmentActivity;
import com.genexus.android.core.utils.Cast;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX;

class GxYouTubeVideoView extends FrameLayout implements YouTubePlayer.OnInitializedListener, IVideoView {
	private static final String TAG = "GxYouTubeVideoView";
	private static final int RECOVERY_DIALOG_REQUEST = 1;

	private String mDeveloperKey;
	private VideoUtils.YouTubeVideoInfo mVideoInfo;
	private LayoutFragmentActivity mActivity = null;
	private YouTubePlayerSupportFragmentX mPlayerFragment = null;
	private YouTubePlayer mPlayer = null;
	private Integer mPendingSeek = null;

	private boolean mAutoPlay;
	private int mPlaybackRate = 100;

	public GxYouTubeVideoView(Context context, Uri videoUri, boolean autoPlay) {
		super(context);

		mVideoInfo = VideoUtils.getYouTubeVideo(videoUri.toString());
		mAutoPlay = autoPlay;

		// Get the Developer key specified in the control property.
		try {
			mDeveloperKey = Services.Strings.getResource(R.string.GoogleServicesApiKey);
		} catch (Resources.NotFoundException e) {
			// Android YouTube API Key not found in the resources.
			Services.Exceptions.handle(e);
			return;
		}

		if (mDeveloperKey.equalsIgnoreCase(Strings.EMPTY)) {
			Services.Log.error(TAG, "YouTube API Developer Key is empty.");
			return;
		}

		mActivity = Cast.as(LayoutFragmentActivity.class, context);
		if (mActivity == null) {
			throw new IllegalArgumentException(TAG + ": Invalid context");
		}

		inflate(context, R.layout.youtube_view, this);
		if (findViewById(R.id.youtube_fragment_container) == null) {
			Services.Log.error(TAG, "Failed to find the YouTube fragment container.");
			return;
		}

		mPlayerFragment = (YouTubePlayerSupportFragmentX) mActivity.getSupportFragmentManager().findFragmentById(R.id.youtube_fragment_container);

		if (mPlayerFragment != null) {
			mActivity.getSupportFragmentManager().beginTransaction().remove(mPlayerFragment).commit();
		}

		mPlayerFragment = YouTubePlayerSupportFragmentX.newInstance();
		mActivity.getSupportFragmentManager().beginTransaction().add(R.id.youtube_fragment_container, mPlayerFragment).commit();

		mPlayerFragment.initialize(mDeveloperKey, this);
	}

	private YouTubePlayer.OnFullscreenListener mOnFullScreenListener = entersFullscreen ->
		mActivity.setAllowUnrestrictedOrientationChange(entersFullscreen);

	@Override
	public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
		Services.Log.debug(TAG, "Player Initialized.");
		mPlayer = player;
		mPlayer.setOnFullscreenListener(mOnFullScreenListener);
		mPlayer.setPlayerStateChangeListener(playerStateChangeListener);

		if (mVideoInfo != null && !wasRestored) {
			Services.Log.debug(TAG, "Video Cued.");
			cueVideo(mVideoInfo);
		}
	}

	@Override
	public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
		Services.Log.error(TAG, "YoutubePlayer failed to initialize.");
		if (errorReason.isUserRecoverableError()) {
			errorReason.getErrorDialog(mActivity, RECOVERY_DIALOG_REQUEST).show();
		} else {
			String errorMessage = Services.Strings.getResource(R.string.GXM_YoutubeError, errorReason.toString());
			Toast.makeText(mActivity.getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
		}
	}

	YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
		@Override
		public void onLoading() {}

		@Override
		public void onLoaded(String s) {
			if (mAutoPlay)
				startVideo();
		}

		@Override
		public void onAdStarted() {}

		@Override
		public void onVideoStarted() {}

		@Override
		public void onVideoEnded() {}

		@Override
		public void onError(YouTubePlayer.ErrorReason errorReason) {}
	};

	public void retryInitialization() {
		// Retry initialization if user performed a recovery action
		mPlayerFragment.initialize(mDeveloperKey, this);
	}

	private void setVideo(@NonNull VideoUtils.YouTubeVideoInfo video) {
		if (mVideoInfo == null || !video.id.equals(mVideoInfo.id)) {
			// Re-load the player
			mVideoInfo = video;
			cueVideo(video);
		}
	}

	private void cueVideo(VideoUtils.YouTubeVideoInfo video) {
		if (video.startTimeSeconds != null)
			mPlayer.cueVideo(video.id, VideoUtils.toMillis(video.startTimeSeconds));
		else if (mPendingSeek != null)
		{
			Services.Log.debug(TAG, "Set Video PendingSeek start: " + mPendingSeek);
			mPlayer.cueVideo(video.id, VideoUtils.toMillis(mPendingSeek));
			mPendingSeek = null;
		}
		else
			mPlayer.cueVideo(video.id);
	}

	@Override
	public void destroy() {
		Fragment fragment = Cast.as(Fragment.class, mPlayerFragment);
		if (fragment != null)
			mActivity.getSupportFragmentManager().beginTransaction().remove(fragment).commit();
	}

	@Override
	public void startVideo() {
		mPlayer.play();
	}

	@Override
	public void pauseVideo() {
		mPlayer.pause();
	}

	@Override
	public void stopVideo() {
		seekTo(0);
		pauseVideo();
	}

	@Override
	public void seekTo(int position) {
		if (mPlayer!=null)
			mPlayer.seekToMillis(VideoUtils.toMillis(position));
		else {
			Services.Log.warning(String.format("Cannot seekTo %s because Player is not prepared yet", position));
			mPendingSeek = position;
		}
	}

	@Override
	public void setVideoUri(Uri uri) {
		VideoUtils.YouTubeVideoInfo videoInfo = VideoUtils.getYouTubeVideo(uri.toString());
		if (videoInfo != null)
			setVideo(videoInfo);
	}

	@Override
	public int getCurrentPosition() {
		return VideoUtils.toSeconds(mPlayer.getCurrentTimeMillis());
	}

	@Override
	public void setAutoPlay(boolean autoPlay) {
		mAutoPlay = autoPlay;
	}

	@Override
	public void setPlaybackRate(int playbackRate) {
		if (playbackRate != 0)
			mPlaybackRate = playbackRate / 100;
	}
}
