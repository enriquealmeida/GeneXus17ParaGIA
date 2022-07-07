package com.genexus.android.core.controls.video;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.genexus.android.R;
import com.genexus.android.core.activities.ActivityLauncher;
import com.genexus.android.core.base.controls.IGxControlNotifyEvents;
import com.genexus.android.core.base.metadata.enums.Orientation;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controls.IGxEdit;
import com.genexus.android.core.controls.media.SelectMediaDialog;

@SuppressLint("ClickableViewAccessibility")
class GxRegularVideoView extends FrameLayout implements IGxControlNotifyEvents, IVideoView {

	private final IGxEdit mOwner;
	private final LayoutItemDefinition mDefinition;
	private Uri mVideoUri;
	private boolean mShowPlaybackControls;
	private boolean mIsPrepared;
	private float mPlaybackRate;
	private boolean mAutoPlay;
	private final boolean mPlayFullScreen;
	private String mThumbnailAtt;

	private CustomMediaController mMediaController;
	private SelectMediaDialog mSelectMediaDialog;
	private MediaPlayer mMediaPlayer = null;
	private VideoView mVideoView = null;
	private ProgressBar mBufferingIndicator = null;
	private ImageButton mPlayButton = null;
	private ImageButton mEditButton = null;
	private boolean mIsBuffering = false;
	private int mCurrentPosition;

	private int mViewHeight;
	private int mVideoHeight;

	private enum State {INIT, PAUSED, RESUMED}
	private State mState = State.INIT;

	private static boolean mExecutedFullScreen;

	private final ViewTreeObserver.OnGlobalLayoutListener mVideoObserver = new ViewTreeObserver.OnGlobalLayoutListener() {
		@Override
		public void onGlobalLayout() {
			mVideoHeight = (int) mVideoView.getY() + mVideoView.getHeight();
			mViewHeight = (int) getY() + getHeight();
			updateButtons();
		}
	};

	public GxRegularVideoView(Context context, LayoutItemDefinition definition, Uri videoUri, int currentPosition,
							  boolean showPlaybackControls, int playbackRate, boolean autoPlay, boolean playFullScreen,
							  String thumbnailAtt, SelectMediaDialog dialog, IGxEdit owner) {
		super(context);
		mOwner = owner;
		mDefinition = definition;
		mVideoUri = videoUri;
		mCurrentPosition = currentPosition;
		mShowPlaybackControls = showPlaybackControls;
		mPlaybackRate = playbackRate;
		mAutoPlay = autoPlay;
		mPlayFullScreen = playFullScreen;
		mThumbnailAtt = thumbnailAtt;
		mExecutedFullScreen = false;
		mSelectMediaDialog = dialog;
		mMediaController = null;
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		setupLayout();
		setupCallbacks();
		prepareVideo();
	}

	private void setupLayout() {
		mVideoView = new VideoView(getContext());
		mBufferingIndicator = new ProgressBar(getContext());
		mPlayButton = new ImageButton(getContext());
		mEditButton = new ImageButton(getContext());

		if (!mShowPlaybackControls)
			setButtonsVisibility(GONE);

		mVideoView.setBackgroundColor(Color.TRANSPARENT);
		mBufferingIndicator.setBackgroundColor(Color.TRANSPARENT);
		mPlayButton.setBackgroundColor(Color.TRANSPARENT);
		mEditButton.setBackgroundColor(Color.TRANSPARENT);

		mBufferingIndicator.setIndeterminate(true);
		mPlayButton.setImageResource(R.drawable.gx_domain_action_play_dark);
		mEditButton.setImageResource(R.drawable.gx_action_edit_video);

		FrameLayout.LayoutParams playLP = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
		playLP.leftMargin -= isEditable() ? 75 : 0;

		addView(mVideoView, 0, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
		addView(mBufferingIndicator, 1, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
		addView(mPlayButton, 2, playLP);

		if (isEditable()) {
			FrameLayout.LayoutParams editLP = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
			editLP.rightMargin -= 75;
			addView(mEditButton, 3, editLP);
		}
	}

	private void updateButtons() {
		int heightOffset = (mViewHeight - mVideoHeight) / 2;

		if (mMediaController != null)
			mMediaController.setPadding(0, 0, 0, heightOffset * 2);

		FrameLayout.LayoutParams playLP = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
		playLP.leftMargin -= isEditable() ? 75 : 0;
		playLP.topMargin -= heightOffset;
		mPlayButton.setLayoutParams(playLP);

		if (isEditable()) {
			FrameLayout.LayoutParams editLP = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
			editLP.rightMargin -= 75;
			editLP.topMargin -= heightOffset;
			mEditButton.setLayoutParams(editLP);
		}
	}

	private void registerViewTreeObserver() {
		ViewTreeObserver videoObserver = mVideoView.getViewTreeObserver();
		videoObserver.addOnGlobalLayoutListener(mVideoObserver);
	}

	private void setupCallbacks() {
		this.setOnClickListener(mOnVideoViewClickListener);
		mVideoView.setOnPreparedListener(mOnPreparedListener);
		mVideoView.setOnErrorListener(mOnErrorListener);
		mPlayButton.setOnClickListener(mOnPlayClickListener);
		mPlayButton.setOnTouchListener(mOnPlayTouchListener);
		mEditButton.setOnClickListener(mOnEditClickListener);
	}

	private void setupMediaController() {
		if (mShowPlaybackControls) {
			mMediaController = new CustomMediaController(mVideoView.getContext(),
				mOnFullscreenClickListener, mOnFullscreenTouchListener, 30);
			mMediaController.setForegroundGravity(mDefinition.getCellGravity());
			mVideoView.setMediaController(mMediaController);
		}
	}

	private void prepareVideo() {
		setButtonsVisibility(GONE);
		setBufferingIndicator(true);
		resetVideo();
		registerViewTreeObserver();
		mVideoView.setVideoURI(mVideoUri);
	}

	@Override
	public void startVideo() {
		if (mMediaPlayer != null && mIsPrepared) {
			setButtonsVisibility(GONE);
			setBufferingIndicator(false);
			mMediaPlayer.start();
		} else {
			setAutoPlay(true);
		}
	}

	@Override
	public void pauseVideo() {
		if (mMediaPlayer != null && mIsPrepared) {
			try {
				if (mMediaPlayer.isPlaying())
					mMediaPlayer.pause();

				setBufferingIndicator(false);
			} catch (IllegalStateException ex) {
				Services.Log.warning("Cannot pause video because MediaPlayer is not prepared.");
			}
		}
	}

	@Override
	public void stopVideo() {
		seekTo(0);
		pauseVideo();
	}

	@Override
	public void seekTo(int position) {
		if (mMediaPlayer != null && mIsPrepared) {
			try {
				int positionMillis = VideoUtils.toMillis(position);
				if (mMediaPlayer.getDuration() < positionMillis || positionMillis < 0) {
					positionMillis = 0;
					if (mMediaPlayer.isPlaying())
						mMediaPlayer.pause();
				}

				setCurrentPosition(positionMillis);
				mMediaPlayer.seekTo(positionMillis);
			} catch (IllegalStateException ex) {
				setCurrentPosition(0);
				Services.Log.warning(String.format("Cannot seekTo %s because MediaPlayer is not prepared yet", position));
			}
		}
	}

	private void releaseVideo() {
		if (mMediaPlayer != null) {
			try {
				mMediaPlayer.release();
				mIsPrepared = false;
			} catch (IllegalStateException ex) {
				Services.Log.debug("MediaPlayer was not released because it was already freed");
			}
		}
	}

	private void resetVideo() {
		if (mMediaPlayer != null) {
			try {
				mMediaPlayer.reset();
			} catch (IllegalStateException ex) {
				Services.Log.debug("MediaPlayer was not reset because it was already freed");
			}
		}
	}

	protected void playInFullScreen() {
		ActivityLauncher.callViewVideoFullscreen(getContext(), mVideoUri.toString(), mShowPlaybackControls,
			mAutoPlay, Orientation.LANDSCAPE, mMediaPlayer.getCurrentPosition(), mPlaybackRate);
	}

	private void setBufferingIndicator(boolean isBuffering) {
		if (mIsBuffering != isBuffering) {
			mIsBuffering = isBuffering;
			mBufferingIndicator.setVisibility(isBuffering ? View.VISIBLE : View.GONE);
		}
	}

	@Override
	public void setVideoUri(Uri videoUri) {
		if (!mVideoUri.equals(videoUri)) {
			mVideoUri = videoUri;
			prepareVideo();
		}
	}

	@Override
	public int getCurrentPosition() {
		if (mMediaPlayer == null)
			return 0;

		try {
			return VideoUtils.toSeconds(mMediaPlayer.getCurrentPosition());
		} catch (IllegalStateException ex) {
			return 0;
		}
	}

	private void setCurrentPosition(int currentPosition) {
		mCurrentPosition = currentPosition;
	}

	@Override
	public void setPlaybackRate(int playbackRate) {
		if (mPlaybackRate != playbackRate) {
			updatePlaybackRate(playbackRate);
		}
	}

	private void updatePlaybackRate(float playbackRate) {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M
			&& playbackRate != 0 && mMediaPlayer != null) {

			float playbackSpeed = playbackRate / 100f;
			PlaybackParams playbackParams = new PlaybackParams();
			playbackParams.setSpeed(playbackSpeed);
			boolean wasPlaying = mMediaPlayer.isPlaying();
			mMediaPlayer.setPlaybackParams(playbackParams);
			if (!wasPlaying)
				mMediaPlayer.pause();

			mPlaybackRate = playbackRate;
		}
	}

	@Override
	public void setAutoPlay(boolean autoPlay) {
		mAutoPlay = autoPlay;
	}

	@Override
	public void destroy() {
		//Nothing to do in a RegularVideoView.
	}

	@Override
	public void notifyEvent(EventType type) {
		if (mVideoView != null) {
			if (type == EventType.ACTIVITY_PAUSED && mState != State.PAUSED) {
				try {
					if (mMediaPlayer != null && mMediaPlayer.getCurrentPosition() > 0) {
						setCurrentPosition(mMediaPlayer.getCurrentPosition());
						pauseVideo();
					}

					mViewHeight = 0;
					mVideoHeight = 0;
				} catch (IllegalStateException ex) {
					setCurrentPosition(0);
					releaseVideo();
				}
				mState = State.PAUSED;
			} else if (type == EventType.ACTIVITY_RESUMED && mState != State.RESUMED) {
				prepareVideo();
				mState = State.RESUMED;
			} else if (type == EventType.GRID_PAGE_CHANGED) {
				if (mMediaController != null && mVideoView.isShown()) {
					pauseVideo();
					mMediaController.hide();
					/* DisplayMetrics dm = mVideoView.getContext().getResources().getDisplayMetrics();
					Rect videoRect = new Rect();
					mVideoView.getGlobalVisibleRect(videoRect);
					Rect screenRect = new Rect(0, 0, dm.widthPixels, dm.heightPixels);
					if (videoRect.intersect(screenRect))
						mMediaController.show();
					else
						mMediaController.hide(); */
				}
			}
		}
	}

	private final MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {

		@Override
		public void onPrepared(MediaPlayer mp) {
			mIsPrepared = true;
			mMediaPlayer = mp;
			mMediaPlayer.setOnInfoListener(mOnInfoListener);

			setupMediaController();
			seekTo(1);

			if (mMediaPlayer.getCurrentPosition() != mCurrentPosition) {
				seekTo(mCurrentPosition);
			}

			pauseVideo();
			setBufferingIndicator(false);

			if (mPlayFullScreen && !mExecutedFullScreen) {
				playInFullScreen();
				mExecutedFullScreen = true;
			} else {
				if (mAutoPlay)
					startVideo();
				else
					setButtonsVisibility(VISIBLE);
			}
		}
	};

	private final MediaPlayer.OnInfoListener mOnInfoListener = (mp, what, extra) -> {
		if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
			setBufferingIndicator(true);
		} else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
			setBufferingIndicator(false);
		}

		return false;
	};

	private final MediaPlayer.OnErrorListener mOnErrorListener = new MediaPlayer.OnErrorListener() {

		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			releaseVideo();
			return VideoUtils.openVideoIntent(getContext(), mVideoUri);
		}
	};

	private final View.OnClickListener mOnPlayClickListener = v -> startVideo();

	private final View.OnTouchListener mOnPlayTouchListener = (v, event) -> {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			((ImageButton) v).setColorFilter(Color.argb(191, 0, 0, 0));
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			((ImageButton) v).setColorFilter(Color.TRANSPARENT);
		}
		return false;
	};

	private final OnClickListener mOnEditClickListener = view -> {
		if (isEditable() && mSelectMediaDialog != null) mSelectMediaDialog.show();
	};

	private final View.OnClickListener mOnFullscreenClickListener = v -> playInFullScreen();

	private final View.OnTouchListener mOnFullscreenTouchListener = (v, event) -> {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			((ImageButton) v).setColorFilter(Color.argb(191, 255, 255, 255));
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			((ImageButton) v).setColorFilter(Color.TRANSPARENT);
		}
		return false;
	};

	private final OnClickListener mOnVideoViewClickListener = view -> {
		if (mIsPrepared && mMediaPlayer != null && !mMediaPlayer.isPlaying())
			toggleButtonVisibility();
	};

	private boolean isEditable() {
		if (mOwner == null)
			return false;

		return mOwner.isEditable();
	}

	private void toggleButtonVisibility() {
		if (mPlayButton.getVisibility() == VISIBLE)
			setButtonsVisibility(GONE);
		else
			setButtonsVisibility(VISIBLE);
	}

	private void setButtonsVisibility(int value) {
		mPlayButton.setVisibility(value);
		if (isEditable()) mEditButton.setVisibility(value);
	}
}
