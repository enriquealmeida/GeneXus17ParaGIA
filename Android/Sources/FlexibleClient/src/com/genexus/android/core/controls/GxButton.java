package com.genexus.android.core.controls;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.genexus.android.R;
import com.genexus.android.analytics.IGxAnalyticsControl;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.enums.Alignment;
import com.genexus.android.core.base.metadata.layout.LayoutActionDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.UIActionHelper;
import com.genexus.android.core.controls.common.FocusHelper;
import com.genexus.android.core.controls.common.IAdjustContentSizeText;
import com.genexus.android.core.controls.common.TextViewUtils;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.utils.Cast;
import com.genexus.android.core.utils.ThemeUtils;

import org.jetbrains.annotations.NotNull;

public class GxButton extends LinearLayout implements IGxActionControl, IGxThemeable, IGxLocalizable, IAdjustContentSizeText, IGxAnalyticsControl
{
	private Coordinator mCoordinator;
	private LayoutActionDefinition mLayoutAction;

	private Entity mEntity; // Optional, only for "in grid" buttons.

	// Child control(s).
	private View mControl;
	private Button mButton;
	private ThemedViewHelper mThemedHelper;
	private ThemedViewHelper mThemedHelperControl;

	public GxButton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mButton = new AppCompatButton(context);
		mThemedHelper = new ThemedViewHelper(this, null);
		mThemedHelperControl = new ThemedViewHelper(mButton, null);
		mThemedHelper.setScope(true, false, false);
		mThemedHelperControl.setScope(false, true, true);
	}

	public GxButton(Context context, Coordinator coordinator, LayoutActionDefinition layoutAction)
	{
		super(context);
		mCoordinator = coordinator;

		// Get action definition from layout parent and set button properties from there.
		mLayoutAction = layoutAction;

		mControl = createControl(context, mLayoutAction);
		mControl.setOnClickListener(mOnClickListener);
		FocusHelper.removeFocusabilityIfNecessary(mControl, layoutAction);

		mThemedHelper = new ThemedViewHelper(this, null);
		mThemedHelperControl = new ThemedViewHelper(mControl, layoutAction);
		mThemedHelper.setScope(true, false, false);
		mThemedHelperControl.setScope(false, true, true);

		// Respect size if specified.
		int width = (mLayoutAction.getWidth() != 0 ? mLayoutAction.getWidth() : LinearLayout.LayoutParams.MATCH_PARENT);
		int height = (mLayoutAction.getHeight() != 0 ? mLayoutAction.getHeight() : LinearLayout.LayoutParams.MATCH_PARENT);

		mControl.setLayoutParams(new LinearLayout.LayoutParams(width, height));
		setGravity(mLayoutAction.getCellGravity());

		addView(mControl);
	}

	private static View createControl(Context context, LayoutActionDefinition layoutAction)
	{
		String caption = layoutAction.getCaption();

		// If image and no caption, use image. If there is caption, use a button (with or without image).
		if (Services.Strings.hasValue(layoutAction.getImage()) && !Services.Strings.hasValue(caption))
		{
			Drawable actionImage = UIActionHelper.getActionImage(context, layoutAction);
			if (actionImage != null)
			{
				ImageView imageView = new ImageView(context);
				imageView.setImageDrawable(actionImage);
				return imageView;
			}
			else
			{
				// If the image is not there, and there is no caption either, make up a caption from
				// the name. Otherwise a blank will be displayed, although the action is still clickable!
				caption = layoutAction.getEventName();
			}
		}

		if (layoutAction.getImagePosition() == Alignment.TOP ||
			layoutAction.getImagePosition() == Alignment.BOTTOM ||
			layoutAction.getImagePosition() == Alignment.START ||
			layoutAction.getImagePosition() == Alignment.END) {
			Drawable actionImage = UIActionHelper.getActionImage(context, layoutAction);
			if (actionImage != null)
				return createImageAndTextButton(context, actionImage, caption, layoutAction);
		}

		Button button = new AppCompatButton(context);
		TextViewUtils.setText(button, caption, layoutAction);
		UIActionHelper.setActionButtonImage(context, layoutAction, layoutAction.getImagePosition(), button);

		return button;
	}

	private static View createImageAndTextButton(Context context, Drawable actionImage, String caption, LayoutActionDefinition layoutAction)
	{
		ImageView imageView = new ImageView(context);
		LayoutParams imageViewParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		imageViewParams.gravity = Gravity.CENTER;
		imageView.setLayoutParams(imageViewParams);
		imageView.setImageDrawable(actionImage);

		TextView textView = new TextView(context);
		LayoutParams textViewParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		textViewParams.gravity = Gravity.CENTER;
		textView.setLayoutParams(textViewParams);
		textView.setAllCaps(true);
		textView.setText(caption);
		
		Space space1 = new Space(context);
		space1.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
		Space space2 = new Space(context);
		space2.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));

		LinearLayout linearLayout = new LinearLayout(context);
		if (layoutAction.getImagePosition() == Alignment.START || layoutAction.getImagePosition() == Alignment.END)
			linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		else
			linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.addView(space1);
		if (layoutAction.getImagePosition() == Alignment.START || layoutAction.getImagePosition() == Alignment.TOP)
			linearLayout.addView(imageView);
		linearLayout.addView(textView);
		if (layoutAction.getImagePosition() == Alignment.END || layoutAction.getImagePosition() == Alignment.BOTTOM)
			linearLayout.addView(imageView);
		linearLayout.addView(space2);

		// Default colors
		Integer normalColor = ThemeUtils.getAndroidThemeColorId(context, R.attr.colorButtonNormal);
		Integer activatedColor = ThemeUtils.getAndroidThemeColorId(context, R.attr.colorControlActivated);
		if (normalColor != null && activatedColor != null) {
			ColorStateList rippleColorStateList = new ColorStateList(new int[][]{new int[]{}}, new int[]{activatedColor});
			Drawable normalBackground = new ColorDrawable(normalColor);
			Drawable rippleMask = new ColorDrawable(Color.WHITE); // Color is irrelevant, must only be a solid drawable that fills a rectangle.
			RippleDrawable res = new RippleDrawable(rippleColorStateList, normalBackground, rippleMask);
			linearLayout.setBackground(res);
		}
		Integer textColor = ThemeUtils.getAndroidThemeColorId(context, android.R.attr.textColorPrimary);
		if (textColor != null)
			textView.setTextColor(textColor);

		return linearLayout;
	}

	private final OnClickListener mOnClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			// On child control click, fire composite click.
			// Apparently button touches are not reported to coordinator, do so.
			boolean handled = (mCoordinator != null && mCoordinator.runControlEvent(GxButton.this, GxTouchEvents.TAP));

			if (!handled)
			{
				// Calling performClick() causes a repeat click sound. Use callOnClick() instead.
				callOnClick();
			}
		}
	};

	@Override
	public ActionDefinition getAction()
	{
		return mLayoutAction.getEvent();
	}

	@Override
	public Entity getEntity()
	{
		return mEntity;
	}

	@Override
	public void setEntity(Entity entity)
	{
		mEntity = entity;
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		TextView textView = getTextView();
		if (textView != null)
			textView.setEnabled(enabled);
	}

	public void setAttributes(int caption, int width, int height)
	{
		mButton.setText(caption);
		mButton.setOnClickListener(mOnClickListener);
		mButton.setLayoutParams(new LinearLayout.LayoutParams(width, height));

		mControl = mButton;
		addView(mButton);
	}

	public String getCaption()
	{
		if (mControl != null && mControl instanceof Button)
			return ((Button)mControl).getText().toString();
		else
			return Strings.EMPTY;
	}

	public void setCaption(String caption)
	{
		if (mControl != null)
		{
			// Set caption property
			if (mControl instanceof Button)
				TextViewUtils.setText((Button) mControl, caption, mLayoutAction);
		}
	}

	@Override
	public void setThemeClass(ThemeClassDefinition themeClass) {
		mThemedHelper.setThemeClass(themeClass);
		mThemedHelperControl.setThemeClass(themeClass);
		applyControlClass(themeClass);
	}

	@Override
	public ThemeClassDefinition getThemeClass() {
		return mThemedHelper.getThemeClass();
	}

	@Override
	public void applyClass(ThemeClassDefinition themeClass) {
		if (mControl != null) {
			mThemedHelper.applyClass(themeClass);
			mThemedHelperControl.applyClass(themeClass);
			applyControlClass(themeClass);
		}
	}

	private @Nullable TextView getTextView() {
		if (mControl instanceof Button) {
			return (Button)mControl;
		} else if (mControl instanceof ViewGroup) {
			for (int n = 0; n < ((ViewGroup)mControl).getChildCount(); n++) {
				TextView textView = Cast.as(TextView.class, ((ViewGroup)mControl).getChildAt(n));
				if (textView != null)
					return textView;
			}
		}
		return null;
	}

	private void applyControlClass(ThemeClassDefinition themeClass) {
		TextView textView = getTextView();
		if (textView != null) {
			// Set font properties
			ThemeUtils.setFontProperties(textView, themeClass, true);

			// if all caps is false
			if (!themeClass.getFontAllCaps())
				textView.setAllCaps(false);
		}
	}

	@Override
	public void setLayoutParams(ViewGroup.LayoutParams params) {
		super.setLayoutParams(params);
		mThemedHelper.setLayoutParams(params);
		mThemedHelperControl.setLayoutParams(params);
	}

	@Override
	public void onTranslationChanged() {
		setCaption(mLayoutAction.getCaption());
	}

	public View getInnerControl()
	{
		return mControl;
	}

	@Override
	public void adjustContentSize() {
		mControl.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
	}

	@NotNull
	@Override
	public String getLabel() {
		return getCaption();
	}

	@Override
	public long getValue() {
		return 0;
	}
}
