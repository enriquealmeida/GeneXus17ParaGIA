package com.genexus.android.core.controls.video;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.genexus.android.core.base.controls.IGxControlPreserveState;
import com.genexus.android.core.base.controls.IGxControlRuntime;
import com.genexus.android.core.base.metadata.enums.ControlTypes;
import com.genexus.android.core.base.metadata.enums.DisplayModes;
import com.genexus.android.core.base.metadata.enums.LayoutModes;
import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.metadata.layout.ControlInfo;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.loader.MetadataLoader;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.StorageHelper;
import com.genexus.android.core.controls.GxImageViewStatic;
import com.genexus.android.core.controls.IGxEdit;
import com.genexus.android.core.controls.media.IGxMediaEdit;
import com.genexus.android.core.controls.media.SelectMediaDialog;
import com.genexus.android.core.resources.MediaTypes;
import com.genexus.android.core.resources.StandardImages;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.utils.Cast;

import java.util.List;
import java.util.Map;

public class GxVideoView extends FrameLayout implements IGxEdit, IGxControlPreserveState, IGxControlRuntime, IGxMediaEdit {

	public static final String NAME = "VideoControl";

	private IVideoView mVideoView;
	private final ViewGroup.LayoutParams mLayoutParams;
	private final LayoutItemDefinition mDefinition;
	private final Coordinator mCoordinator;
	private final SelectMediaDialog mSelectMediaDialog;

	private static final String METHOD_PLAY = "Play";
	private static final String METHOD_PAUSE = "Pause";
	private static final String METHOD_STOP = "Stop";
	private static final String METHOD_SEEK = "Seek";

	private static final String PROPERTY_SHOW_PLAYBACK_CONTROLS = "ShowPlaybackControls";
	private static final String PROPERTY_PLAYBACK_RATE = "PlaybackRate";
	private static final String PROPERTY_AUTO_PLAY = "Autoplay";
	private static final String PROPERTY_PLAY_FULL_SCREEN = "PlayInFullScreen";
	private static final String PROPERTY_PLACEHOLDER_IMAGE = "PlaceholderImage";
	private static final String PROPERTY_THUMBNAIL_ATT = "ThumbnailAtt";
	private static final String PROPERTY_THUMBNAIL_SPECIFIER = "ThumbnailFieldSpecifier";
	private static final String STATE_CURRENT_POSITION = "CurrentPosition";

	private Uri mVideoUri;
	private int mCurrentPosition = 0;
	private boolean mShowPlaybackControls = true;
	private int mPlaybackRate = 100;
	private boolean mAutoPlay = false;
	private boolean mPlayFullScreen = false;
	private String mPlaceholderImage = Strings.EMPTY;
	private String mThumbnailAtt = Strings.EMPTY;
	private String mThumbnailSpecifier = Strings.EMPTY;
	private String mThumbnail = Strings.EMPTY;

	public GxVideoView(Context context, Coordinator coordinator, LayoutItemDefinition definition) {
		super(context);
		mDefinition = definition;
		mCoordinator = coordinator;
		mSelectMediaDialog = new SelectMediaDialog(context, mCoordinator, this);
		mLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, mDefinition.getCellGravity());
		initializeProperties(definition.getControlInfo());
		setOnClickListener(v -> { if (isEditable() && (mVideoUri == null || mVideoUri.toString().isEmpty())) mSelectMediaDialog.show(); });
	}

	public void initializeProperties(ControlInfo controlInfo) {
		mShowPlaybackControls = !controlInfo.optStringProperty(getPropertyId(PROPERTY_SHOW_PLAYBACK_CONTROLS)).equalsIgnoreCase("Never");
		mPlaybackRate = controlInfo.optIntProperty(getPropertyId(PROPERTY_PLAYBACK_RATE));
		mAutoPlay = controlInfo.optBooleanProperty(getPropertyId(PROPERTY_AUTO_PLAY));
		mPlayFullScreen = controlInfo.optStringProperty(getPropertyId(PROPERTY_PLAY_FULL_SCREEN)).equalsIgnoreCase("Yes");
		mThumbnailAtt = controlInfo.optStringProperty(getPropertyId(PROPERTY_THUMBNAIL_ATT));
		mThumbnailSpecifier = controlInfo.optStringProperty(getPropertyId(PROPERTY_THUMBNAIL_SPECIFIER));
		mThumbnail = buildDataExpression(mThumbnailAtt, mThumbnailSpecifier);

		String placeholder = controlInfo.optStringProperty(getPropertyId(PROPERTY_PLACEHOLDER_IMAGE));
		mPlaceholderImage = MetadataLoader.getObjectName(placeholder);
	}

	private String buildDataExpression(String dataItem, String fieldSelector) {
		if (!Services.Strings.hasValue(dataItem) || dataItem.equalsIgnoreCase("(none)"))
			return null;

		if (!Services.Strings.hasValue(fieldSelector))
			return dataItem;

		return dataItem + Strings.DOT + fieldSelector;
	}

	private String getPropertyId(String property) {
		return "@" + NAME + property;
	}

	@Override
	public Expression.Value callMethod(String method, List<Expression.Value> parameters) {
		if (mVideoView == null) {
			Services.Log.error("VideoView is not set up yet");
			return null;
		}

		switch (method) {
			case METHOD_PLAY:
				mVideoView.startVideo();
				return null;
			case METHOD_PAUSE:
				mVideoView.pauseVideo();
				return null;
			case METHOD_STOP:
				mVideoView.stopVideo();
				return null;
			case METHOD_SEEK:
				if (parameters.size() == 1) {
					int position = parameters.get(0).coerceToInt();
					if (position < 0) position = 0;
					mVideoView.seekTo(position);
					return null;
				}
		}

		throw new IllegalArgumentException(String.format("Unknown method %s/%s", method, parameters.size()));
	}

	@Override
	public void setPropertyValue(String name, Expression.Value value) {
		if (mVideoView == null) {
			Services.Log.error("VideoView is not set up yet");
			return;
		}

		switch (name) {
			case PROPERTY_SHOW_PLAYBACK_CONTROLS:
			case PROPERTY_PLAY_FULL_SCREEN:
			case PROPERTY_PLACEHOLDER_IMAGE:
			case PROPERTY_THUMBNAIL_ATT:
				break; //These four properties above are design-time only
			case PROPERTY_PLAYBACK_RATE:
				mPlaybackRate = value.coerceToInt();
				mVideoView.setPlaybackRate(mPlaybackRate);
				break;
			case PROPERTY_AUTO_PLAY:
				mAutoPlay = value.coerceToBoolean();
				mVideoView.setAutoPlay(mAutoPlay);
				break;
			default:
				throw new IllegalArgumentException(String.format("Unknown run-time property %s", name));
		}
	}

	@Override
	public Expression.Value getPropertyValue(String name) {
		switch (name) {
			case PROPERTY_SHOW_PLAYBACK_CONTROLS:
				String showPlaybackControls = mShowPlaybackControls ? "interaction" : "never";
				return Expression.Value.newString(showPlaybackControls);
			case PROPERTY_PLAYBACK_RATE:
				return Expression.Value.newInteger(mPlaybackRate);
			case PROPERTY_AUTO_PLAY:
				return Expression.Value.newBoolean(mAutoPlay);
			case PROPERTY_PLAY_FULL_SCREEN:
				String autoPlay = mPlayFullScreen ? "yes" : "no";
				return Expression.Value.newString(autoPlay);
			case PROPERTY_PLACEHOLDER_IMAGE:
				return Expression.Value.newString(mPlaceholderImage);
			case PROPERTY_THUMBNAIL_ATT:
				return Expression.Value.newString(mThumbnailAtt);
		}

		throw new IllegalArgumentException(String.format("Unknown property %s", name));
	}

	@Override
	public String getGxTag() {
		return getTag().toString();
	}

	@Override
	public void setGxTag(String tag) {
		setTag(tag);
	}

	@Override
	public String getGxValue() {
		return mVideoUri == null ? null : mVideoUri.toString();
	}

	private void insertEmptyVideo() {
		if (mVideoView != null) {
			mVideoView.destroy();
			mVideoView = null;
		}

		// Make sure to remove all views in the layout.
		removeAllViews();

		// Set default image.
		GxImageViewStatic imageView = new GxImageViewStatic(getContext(), null, null);

		Drawable drawable = null;
		if (!mPlaceholderImage.isEmpty())
			drawable = Services.Resources.getImageDrawable(getContext(), mPlaceholderImage);

		if (drawable == null)
			drawable = Services.Resources.getContentDrawableFor(getContext(), MediaTypes.VIDEO_STUB);

		StandardImages.showPlaceholderImage(imageView, drawable, true);
		addView(imageView, mLayoutParams);
	}

	private void insertYouTubeVideo() {
		// Get the videoId from the YouTube URL.
		VideoUtils.YouTubeVideoInfo videoInfo = VideoUtils.getYouTubeVideo(mVideoUri.toString());
		if (videoInfo == null) {
			insertEmptyVideo();
			Services.Log.error(NAME, "Invalid YouTube video URL.");
			return;
		}

		// If there's already a GxYouTubeVideoView loaded, update it. Otherwise, instantiate a new one.
		if (mVideoView instanceof GxYouTubeVideoView) {
			mVideoView.setVideoUri(mVideoUri);
		} else {
			// If there's a different videoView instantiated destroy it.
			if (mVideoView != null)
				mVideoView.destroy();

			// Make sure to remove all views in the layout.
			removeAllViews();

			// Add the new GxYouTubeVideoView.
			mVideoView = new GxYouTubeVideoView(getContext(), mVideoUri, mAutoPlay);
			addView(Cast.as(GxYouTubeVideoView.class, mVideoView), mLayoutParams);
		}
	}

	private void insertRegularVideo() {
		// If it's not a local video file and it's not an absolute URL, add base path of the application server.
		if (!StorageHelper.isLocalFile(mVideoUri.toString()) && !mVideoUri.toString().contains("://")) {
			mVideoUri = Uri.parse(Services.Application.get().UriMaker.getImageUrl(mVideoUri.toString()));
		}

		// If there's already a GxRegularVideoView loaded, update it. Otherwise, instantiate a new one.
		if (mVideoView instanceof GxRegularVideoView) {
			mVideoView.setVideoUri(mVideoUri);
		} else {
			// If there's a different videoView instantiated destroy it.
			if (mVideoView != null)
				mVideoView.destroy();

			// Make sure to remove all views on the layout.
			removeAllViews();

			// Add the new GxRegularVideoView.
			mVideoView = new GxRegularVideoView(getContext(), mDefinition, mVideoUri, mCurrentPosition,
				mShowPlaybackControls, mPlaybackRate, mAutoPlay, mPlayFullScreen, mThumbnailAtt, mSelectMediaDialog, this);

			addView(Cast.as(GxRegularVideoView.class, mVideoView), mLayoutParams);
		}
	}

	@Override
	public void setGxValue(String value) {
		String oldValue = mVideoUri == null ? Strings.EMPTY : mVideoUri.toString();
		if ((!Strings.hasValue(value) && !Strings.hasValue(oldValue)) || oldValue.equalsIgnoreCase(value))
			return;

		mVideoUri = (value == null) ? null : Uri.parse(value);
		mCoordinator.onValueChanged(this, true, value);
		if (TextUtils.isEmpty(value)) {
			insertEmptyVideo();
		} else if (VideoUtils.isYouTubeUrl(value)) {
			insertYouTubeVideo();
		} else {
			insertRegularVideo();
		}
	}

	@Override
	public void setValueFromIntent(Intent data) {
	}

	public void retryYoutubeInitialization() {
		if (mVideoView instanceof GxYouTubeVideoView)
			((GxYouTubeVideoView) mVideoView).retryInitialization();
	}

	private int getCurrentPosition() {
		if (mVideoView != null)
			mCurrentPosition = mVideoView.getCurrentPosition();

		return mCurrentPosition;
	}

	private void setCurrentPosition(int currentPosition) {
		if (mVideoView != null) {
			mCurrentPosition = currentPosition;
			mVideoView.seekTo(mCurrentPosition);
		}
	}

	@Override
	public IGxEdit getViewControl() {
		return this;
	}

	@Override
	public IGxEdit getEditControl() {
		return this;
	}

	@Override
	public boolean isEditable() {
		boolean isReadOnly = mDefinition.getReadOnly(LayoutModes.EDIT, DisplayModes.EDIT);
		return isEnabled() && !isReadOnly;
	}

	@Override
	public String getControlId() {
		return mDefinition.getName();
	}

	@Override
	public void saveState(Map<String, Object> state) {
		state.put(STATE_CURRENT_POSITION, getCurrentPosition());
	}

	@Override
	public void restoreState(Map<String, Object> state) {
		Object positionObj = state.get(STATE_CURRENT_POSITION);
		int currentPosition = positionObj == null ? 0 : (Integer) positionObj;
		setCurrentPosition(currentPosition);
	}

	@Override
	public void onMediaChanged(Uri mediaUri, boolean successful, boolean fireValueChangedEvent) {
		if (successful)
			setGxValue(mediaUri == null ? null : mediaUri.toString());
		else
			Services.Log.error(getClass().getName(), "Failed to copy data to a local file.");
	}

	@Override
	public String getControlType() {
		return ControlTypes.VIDEO_VIEW;
	}

	@Override
	public void setLoadingForCopying(boolean value) {
		//No loading progress here as GxRegularVideoView will show its BufferingIndicator when a new Uri is set
	}
}
