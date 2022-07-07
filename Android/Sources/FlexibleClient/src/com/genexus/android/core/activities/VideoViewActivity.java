package com.genexus.android.core.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Insets;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowMetrics;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.VideoView;

import com.genexus.android.R;
import com.genexus.android.core.base.metadata.enums.Orientation;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controls.LoadingIndicatorView;
import com.genexus.android.core.controls.video.CustomMediaController;
import com.genexus.android.core.controls.video.VideoUtils;

import java.util.List;

/**
 * Relevant samples that illustrate the interaction with SystemUI flags.
 * 1. https://android.googlesource.com/platform/development/+/master/samples/ApiDemos/src/com/example/android/apis/view/VideoPlayerActivity.java
 * 2. https://android.googlesource.com/platform/developers/samples/android/+/master/ui/window/AdvancedImmersiveMode/AdvancedImmersiveModeSample/src/main/java/com/example/android/advancedimmersivemode/AdvancedImmersiveModeFragment.java
 */

@SuppressLint("ClickableViewAccessibility")
public class VideoViewActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener,
	MediaPlayer.OnErrorListener,
	MediaPlayer.OnCompletionListener {
	public static final String INTENT_EXTRA_LINK = "Link";
	public static final String INTENT_EXTRA_IS_AUDIO = "IsAudio";
	public static final String INTENT_EXTRA_SHOW_BUTTONS = "ShowButtons";
	public static final String INTENT_EXTRA_ORIENTATION = "Orientation";
	public static final String INTENT_EXTRA_CURRENT_POSITION = "CurrentPosition";
	public static final String INTENT_EXTRA_AUTO_PLAY = "AutoPlay";
	public static final String INTENT_EXTRA_PLAYBACK_RATE = "PlaybackRate";
	private static final int AUTO_HIDE_TIMEOUT = 2000;    // time in milliseconds

	private LoadingIndicatorView mBufferingIndicator = null;
	private VideoView mVideoView = null;
	private View mDecorView = null;
	private CustomMediaController mMediaController = null;
	private Uri mVideoUri = null;
	private boolean mIsAudio = false;
	private boolean mShowButtons = false;
	private boolean mAutoPlay = true;
	private float mPlaybackRate = 100f;
	private String mOrientation = null;
	private int mCurrentPosition = 0;
	private int mLastSystemUiVis = 0;
	private boolean mWindowInsetsVis = true;
	private Handler mHandler = null;

	private Runnable mHideNavigation = () -> setNavigationVisibility(false);

	@Override
	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		final Object data = getLastCustomNonConfigurationInstance();
		if (data != null) {
			mCurrentPosition = Integer.parseInt(data.toString());
		}

		Intent intent = getIntent();
		if (intent.hasExtra(INTENT_EXTRA_LINK))
			mVideoUri = Uri.parse(intent.getStringExtra(INTENT_EXTRA_LINK));
		if (intent.hasExtra(INTENT_EXTRA_IS_AUDIO))
			mIsAudio = intent.getBooleanExtra(INTENT_EXTRA_IS_AUDIO, false);
		if (intent.hasExtra(INTENT_EXTRA_SHOW_BUTTONS))
			mShowButtons = intent.getBooleanExtra(INTENT_EXTRA_SHOW_BUTTONS, false);
		if (intent.hasExtra(INTENT_EXTRA_ORIENTATION))
			mOrientation = intent.getStringExtra(INTENT_EXTRA_ORIENTATION);
		if (intent.hasExtra(INTENT_EXTRA_CURRENT_POSITION))
			mCurrentPosition = intent.getIntExtra(INTENT_EXTRA_CURRENT_POSITION, 0);
		if (intent.hasExtra(INTENT_EXTRA_AUTO_PLAY))
			mAutoPlay = intent.getBooleanExtra(INTENT_EXTRA_AUTO_PLAY, true);
		if (intent.hasExtra(INTENT_EXTRA_PLAYBACK_RATE))
			mPlaybackRate = intent.getFloatExtra(INTENT_EXTRA_PLAYBACK_RATE, 100f);

		if (VideoUtils.isYouTubeUrl(mVideoUri.toString())) {
			viewInYouTubeApp(mVideoUri);
			finish();
			return;
		}

		if (mIsAudio) {
			// Prevent rotation, because that would restart the audio currently playing.
			// This is a stopgap measure until we have a bound service for playing audio.
			ActivityHelper.setOrientation(this, Services.Device.getScreenOrientation());
		}

		if (mOrientation != null) {
			ActivityHelper.setOrientation(this, mOrientation.equals(Orientation.LANDSCAPE.toString()) ? Orientation.LANDSCAPE : Orientation.PORTRAIT);
		}

		mHandler = new Handler(Looper.getMainLooper());

		setupLayout();
		setupCallbacks();

		if (mShowButtons)
			setupMediaController();
	}

	private void setupLayout() {
		setContentView(R.layout.videoviewlayout);
		mVideoView = findViewById(R.id.VideoView);
		mBufferingIndicator = findViewById(R.id.bufferingIndicator);
		mDecorView = getWindow().getDecorView();

		// set support toolbar, dont add action bar to video view activity, not needed.
		//Toolbar toolbar = (Toolbar)this.findViewById(R.id.toolbar);
		//this.setSupportActionBar(toolbar);
	}

	@SuppressLint("ClickableViewAccessibility")
	private void setupCallbacks() {
		mVideoView.setOnPreparedListener(this);
		mVideoView.setOnErrorListener(this);
		mVideoView.setOnCompletionListener(this);

		mDecorView.setOnTouchListener((v, event) -> {
			mHandler.removeCallbacks(mHideNavigation);
			mHandler.post(mHideNavigation);
			return true;
		});

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			mDecorView.setOnApplyWindowInsetsListener((view, windowInsets) -> {
				boolean visible = windowInsets.isVisible(WindowInsets.Type.navigationBars());
				if (!mWindowInsetsVis && visible) {
					setNavigationVisibility(true);
				}
				mWindowInsetsVis = visible;
				return WindowInsets.CONSUMED;
			});
		} else {
			setupCallbacksDeprecated();
		}
	}

	@SuppressWarnings("deprecation")
	private void setupCallbacksDeprecated() {
		mDecorView.setOnSystemUiVisibilityChangeListener(visibility -> {
			int diff = mLastSystemUiVis ^ visibility;
			mLastSystemUiVis = visibility;
			if ((diff & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) != 0 && (visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0) {
				setNavigationVisibility(true);
			}
		});
	}

	private void setNavigationVisibility(boolean visible) {
		setMediaControllerVisibility(visible);

		if (visible) {
			mHandler.removeCallbacks(mHideNavigation);
			mHandler.postDelayed(mHideNavigation, AUTO_HIDE_TIMEOUT);
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			getWindow().setDecorFitsSystemWindows(false);
			if (visible) {
				getWindow().getInsetsController().show(WindowInsets.Type.statusBars());
			} else {
				getWindow().getInsetsController().hide(WindowInsets.Type.statusBars());
				getWindow().getInsetsController().hide(WindowInsets.Type.navigationBars());
			}
		} else {
			setNavigationVisibilityDeprecated(visible);
		}
	}

	@SuppressWarnings("deprecation")
	private void setNavigationVisibilityDeprecated(boolean visible) {
		int newVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
			| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
			| View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

		if (!visible) {
			newVisibility |= View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
		}

		mDecorView.setSystemUiVisibility(newVisibility);
	}

	private void setupMediaController() {
		mMediaController = new CustomMediaController(mVideoView.getContext(),
			mOnFullscreenClickListener, mOnFullscreenTouchListener, 140);

		mMediaController.setAnchorView(mVideoView);
		mVideoView.setMediaController(mMediaController);

		mMediaController.setOnClickListener(v -> {
			mHandler.removeCallbacks(mHideNavigation);
			mHandler.postDelayed(mHideNavigation, AUTO_HIDE_TIMEOUT);
		});
	}

	private void setMediaControllerVisibility(boolean visible) {
		if (mMediaController != null) {
			if (visible) {
				mMediaController.show(0);
				adjustControllerMargin();
			} else {
				mMediaController.hide();
			}
		}
	}

	private void adjustControllerMargin() {
		ViewGroup.LayoutParams params = mMediaController.getLayoutParams();
		if (params instanceof FrameLayout.LayoutParams) {
			FrameLayout.LayoutParams frameParams = (FrameLayout.LayoutParams) params;
			int marginBottom = getNavigationBarHeight();
			frameParams.setMargins(0, 0, 0, marginBottom);
		}
	}

	private int getNavigationBarHeight() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			return getNavigationBarHeightApi30();
		} else {
			return getNavigationBarHeightDeprecated();
		}
	}

	@RequiresApi(Build.VERSION_CODES.R)
	private int getNavigationBarHeightApi30() {
		final WindowMetrics metrics = getWindowManager().getCurrentWindowMetrics();
		final WindowInsets windowInsets = metrics.getWindowInsets();
		Insets insets = windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars());
		return insets.bottom - insets.top;
	}

	@SuppressWarnings("deprecation")
	private int getNavigationBarHeightDeprecated() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int usableHeight = metrics.heightPixels;
		getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
		int realHeight = metrics.heightPixels;
		if (realHeight > usableHeight)
			return realHeight - usableHeight;
		else
			return 0;
	}

	private void startVideo() {
		mVideoView.start();
	}

	private void stopVideo() {
		mVideoView.seekTo(0);
		mVideoView.pause();
		mBufferingIndicator.setVisibility(View.GONE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mHandler.removeCallbacks(mHideNavigation);
		mHandler.post(mHideNavigation);
		mBufferingIndicator.setVisibility(View.VISIBLE);
		mVideoView.resume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mCurrentPosition = mVideoView.getCurrentPosition();
		mBufferingIndicator.setVisibility(View.GONE);
		mVideoView.suspend();
	}

	@Override
	protected void onStart() {
		super.onStart();
		mVideoView.setVideoURI(mVideoUri);
		if (mCurrentPosition > 0) {
			mVideoView.seekTo(mCurrentPosition);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mVideoView != null)
			stopVideo();
	}

	@Override
	@SuppressWarnings("deprecation")
	public Object onRetainCustomNonConfigurationInstance() {
		return (mCurrentPosition > 0) ? mCurrentPosition : null;
	}

	private void viewInYouTubeApp(Uri url) {
		String videoId = (url.getQueryParameter("v") != null) ? url.getQueryParameter("v") : url.getLastPathSegment();
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
		List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		if (list.size() > 0) {
			startActivity(intent);
		}
	}

	// MediaPlayer callbacks
	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		stopVideo();
		return VideoUtils.openVideoIntent(this, mVideoUri);
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M
			&& mPlaybackRate > 0 && mPlaybackRate != 100) {

			float playbackSpeed = mPlaybackRate / 100f;
			PlaybackParams playbackParams = new PlaybackParams();
			playbackParams.setSpeed(playbackSpeed);
			boolean wasPlaying = mp.isPlaying();
			mp.setPlaybackParams(playbackParams);
			if (!wasPlaying)
				mp.pause();
		}

		mBufferingIndicator.setVisibility(View.GONE);

		if (mAutoPlay)
			startVideo();
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		if (mIsAudio) {
			finish();
		}
	}

	private View.OnClickListener mOnFullscreenClickListener = v -> finish();

	private View.OnTouchListener mOnFullscreenTouchListener = (v, event) -> {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			((ImageButton) v).setColorFilter(Color.argb(191, 255, 255, 255));
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			((ImageButton) v).setColorFilter(Color.TRANSPARENT);
		}
		return false;
	};
}
