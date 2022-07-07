package com.genexus.android.core.controls.media;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;

import com.genexus.android.core.base.controls.IGxControlPreserveState;
import com.genexus.android.core.base.metadata.enums.ActionTypes;
import com.genexus.android.core.base.metadata.enums.Alignment;
import com.genexus.android.core.base.metadata.enums.ControlTypes;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controls.GxImageViewData;
import com.genexus.android.core.controls.GxTextBlockTextView;
import com.genexus.android.core.resources.MediaTypes;
import com.genexus.android.core.resources.StandardImages;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.utils.Objects;
import com.genexus.android.core.utils.ThemeUtils;

import java.util.Map;

import static com.genexus.android.core.base.metadata.theme.ApplicationClassExtensionKt.PLACEHOLDER_IMAGE;

/**
 * Default control for attributes/variables of type Image, Video or Audio in Edit mode. It
 * previews and imports media files (images, videos & audio) from different sources (Web URLs,
 * local file system, cloud storage providers) or enables to create the media file in the moment
 * (e.g. by using a Camera app).
 * <p>
 * If the control is part of an Online object, the reference this control holds could be of one
 * the following types:
 * <ol>
 * <li>A web URL</li>
 * <ul>
 * <li>Relative to the app's endpoint (e.g. /Resources/img1.png).</li>
 * <li>Absolute to an external site. (e.g. http://i.imgur.com/lEK6VVu.png).</li>
 * </ul>
 * <li>A file on the filesystem of the device.</li>
 * </ol>
 * In case of being part of an Offline object, the control may only hold a reference of the
 * second type.
 * </p>
 */
public class GxMediaEditControl extends GxImageViewData implements IGxControlPreserveState,
																   View.OnClickListener,
																   IGxMediaEdit {
	private static final String STATE_OUTPUT_MEDIA_FILE = "OutputMediaFile";
	private final LayoutItemDefinition mDefinition;
	private final Coordinator mCoordinator;

	private Uri mMediaUri;
	private SelectMediaDialog mSelectMediaDialog;

	//invite message member
	private final GxTextBlockTextView mInviteMessageText;
	private boolean mHasCustomPlaceHolder = false;
	private boolean mHasShowInviteMessage = false;


	public GxMediaEditControl(Context context, Coordinator coordinator, LayoutItemDefinition layoutItem) {
		super(context, layoutItem);
		mDefinition = layoutItem;
		mCoordinator = coordinator;
		mSelectMediaDialog = new SelectMediaDialog(getContext(), mCoordinator, this);
		setOnClickListener(this);

		// invite message text
		mInviteMessageText = new GxTextBlockTextView(context);
		mInviteMessageText.setVisibility(GONE);

		addView(mInviteMessageText, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER));
		mInviteMessageText.setGravity( Gravity.CENTER);

		if (Services.Strings.hasValue(mDefinition.getInviteMessage()))
		{
			mInviteMessageText.setText(mDefinition.getInviteMessage());
		}
		inviteMessageCheck();
	}

	@Override
	public void setGxValue(String value) {
		Uri mediaUri = (value != null && !TextUtils.isEmpty(value)) ? Uri.parse(value) : null;
		setControlValue(mediaUri, false);
	}

	@Override
	public String getGxValue() {
		return (mMediaUri != null) ? mMediaUri.toString() : null;
	}

	private void setControlValue(Uri newMediaUri, boolean fireValueChangedEvent) {
		Uri oldMediaUri = mMediaUri;
		mMediaUri = newMediaUri;

		if (!Objects.equals(newMediaUri, oldMediaUri)) {
			mCoordinator.onValueChanged(this, fireValueChangedEvent);
		}

		if (newMediaUri == null) {
			loadPlaceholder(getControlType());
		} else {
			loadMediaPreview(getControlType());
		}
		inviteMessageCheck();
	}

	private void loadPlaceholder(String controlType) {
		String placeholderType;

		switch (controlType) {
			case ControlTypes.PHOTO_EDITOR:
				placeholderType = MediaTypes.IMAGE_STUB;
				break;
			case ControlTypes.AUDIO_VIEW:
				placeholderType = MediaTypes.AUDIO_STUB;
				break;
			case ControlTypes.VIDEO_VIEW:
				placeholderType = MediaTypes.VIDEO_STUB;
				break;
			default:
				throw new IllegalArgumentException("Invalid media control does not have a valid Control Type");
		}

		Drawable drawable = Services.Resources.getContentDrawableFor(getContext(), placeholderType);
		StandardImages.showPlaceholderImage(this, drawable, true);
	}

	private void loadMediaPreview(String controlType) {
		if (ControlTypes.PHOTO_EDITOR.equals(controlType)) {
			Services.Images.showImage(getContext(), this, mMediaUri.toString(), true, false);
		} else if (ControlTypes.VIDEO_VIEW.equals(controlType) || ControlTypes.AUDIO_VIEW.equals(controlType)) {
			String actionType = ControlTypes.VIDEO_VIEW.equals(controlType) ?
					ActionTypes.VIEW_VIDEO : ActionTypes.VIEW_AUDIO;
			Drawable drawable = Services.Resources.getContentDrawableFor(getContext(), actionType);
			setImageDrawable(drawable);
		}
	}

	@Override
	public void onClick(View v) {
		mSelectMediaDialog.show();
	}

	@Override
	protected void onDetachedFromWindow() {
		mSelectMediaDialog.dismiss();
		super.onDetachedFromWindow();
	}

	@Override
	public String getControlId() {
		return mDefinition.getName();
	}

	@Override
	public void saveState(Map<String, Object> state) {
		state.put(STATE_OUTPUT_MEDIA_FILE, mMediaUri);
	}

	@Override
	public void restoreState(Map<String, Object> state) {
		mMediaUri = (Uri) state.get(STATE_OUTPUT_MEDIA_FILE);
		if (mMediaUri != null) {
			// If a media file was selected, make sure the gx value has this value.
			setControlValue(mMediaUri, false);
		}
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	@Override
	public String getControlType() {
		return mDefinition.getControlType();
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		inviteMessageCheck();
	}

	@Override
	public void applyEditClass(@NonNull ThemeClassDefinition themeClass)
	{
		super.applyEditClass(themeClass);

		mHasCustomPlaceHolder = false;
		ThemeClassDefinition imageClass = StandardImages.getImageClass(this,  PLACEHOLDER_IMAGE);
		if (imageClass != null)
		{
			String imageName = imageClass.getImage(PLACEHOLDER_IMAGE);
			if (Services.Strings.hasValue(imageName))
			{
				mHasCustomPlaceHolder = true;
			}
		}

		// set invite message text color
		ThemeUtils.setFontTextColorWithInviteColor(mInviteMessageText, themeClass);

		inviteMessageCheck();

	}

	protected void inviteMessageCheck()
	{
		boolean showInviteMessage = false;
		// showinvitemessage, if isEditable, isEnable, invitemessa<>caption, this control no has value, it parent either
		if (isEditable() && !mDefinition.getInviteMessage().equalsIgnoreCase(mDefinition.getCaption())
				/*&& !mAlreadyLoaded */
				&& isEnabled()
				&& (mMediaUri==null || !Services.Strings.hasValue( mMediaUri.toString()))
				&& !Services.Strings.hasValue(super.getGxValue())
				)
		{
			showInviteMessage = true;
		}
		if (showInviteMessage)
		{
			mHasShowInviteMessage = true;
			mInviteMessageText.setVisibility(VISIBLE);
			if (!mHasCustomPlaceHolder)
			{
				setImageViewVisibility(INVISIBLE);
			}
			else
			{
				// set invite message horizontal align, if not autogrow.
				if (!mDefinition.hasAutoGrow())
				{
					// apply gravity if different than default.
					if (mDefinition.getCellGravity() != Alignment.NONE)
						mInviteMessageText.setGravity(mDefinition.getCellGravity());
				}
				setImageViewVisibility(VISIBLE);
			}
		}
		else
		{
			mInviteMessageText.setVisibility(GONE);
			if (mHasShowInviteMessage && !mHasCustomPlaceHolder)
				setImageViewVisibility(VISIBLE);
		}
	}

	@Override
	public void setLoadingForCopying(boolean value) {
		setLoading(value);
	}

	@Override
	public void onMediaChanged(Uri mediaUri, boolean successful, boolean fireValueChangedEvent) {
		if (successful)
			setControlValue(mediaUri, fireValueChangedEvent);
		else
			Services.Log.error(getClass().getName(), "Failed to copy data to a local file.");

		setLoading(false);
		inviteMessageCheck();
	}
}
