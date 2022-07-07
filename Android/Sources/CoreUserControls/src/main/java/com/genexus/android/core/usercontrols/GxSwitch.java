package com.genexus.android.core.usercontrols;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.res.ColorStateList;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;

import com.genexus.android.core.base.controls.IGxEditThemeable;
import com.genexus.android.core.base.metadata.enums.Alignment;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.controls.IGxEdit;
import com.genexus.android.core.controls.IManualAlignment;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.utils.Cast;
import com.genexus.android.core.utils.ThemeUtils;

@SuppressLint("ViewConstructor")
public class GxSwitch extends SwitchCompat implements IGxEdit, OnCheckedChangeListener, IManualAlignment, IGxEditThemeable {
	public static final String NAME = "SDSwitch";
	private String mCheckedValue = "";
	private String mUncheckedValue = "";
	private Coordinator mCoordinator = null;
	private boolean mFireControlValueChanged;
	private LayoutItemDefinition mItemDefinition;

	public GxSwitch(Context context, Coordinator coordinator, LayoutItemDefinition item) {
		super(context);

		mCheckedValue = item.getControlInfo().optStringProperty("@SDSwitchCheckedValue");
		mUncheckedValue = item.getControlInfo().optStringProperty("@SDSwitchUncheckedValue");
		mCoordinator = coordinator;
		mFireControlValueChanged = true;
		mItemDefinition = item;

		setOnCheckedChangeListener(this);
	}

	@Override
	public String getGxValue() {
		return isChecked() ? mCheckedValue : mUncheckedValue;
	}

	@Override
	public void setGxValue(String value) {
		boolean currentState = isChecked();
		boolean newState = value != null && value.equalsIgnoreCase(mCheckedValue);

		if (newState != currentState) {
			mFireControlValueChanged = false;
			setChecked(newState);
			mFireControlValueChanged = true;
		}
	}

	@Override
	public String getGxTag() {
		return (String) this.getTag();
	}

	@Override
	public void setGxTag(String data) {
		this.setTag(data);
	}

	@Override
	public void setValueFromIntent(Intent data) {
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		super.setFocusable(enabled);
	}

	@Override
	public IGxEdit getViewControl() {
		setEnabled(false);
		return this;
	}

	@Override
	public IGxEdit getEditControl() {
		return this;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (mCoordinator != null)
			mCoordinator.onValueChanged(this, mFireControlValueChanged);
	}

	@Override
	public boolean isEditable() {
		return isEnabled(); // Editable when enabled.
	}

	@Override
	public void applyAlignment() {
		// SwitchCompat always draw the control aligned to the right, to simulate alignment we use the rightMargin
		int margin = -1;
		switch (mItemDefinition.getCellGravity() & Alignment.HORIZONTAL_MASK) {
			case Alignment.LEFT:
				margin = getWidth() - getCompoundPaddingRight(); // getCompoundPaddingRight() is used because inspecting the code it returns mSwitchWidth
				break;
			case Alignment.CENTER_HORIZONTAL:
				margin = (getWidth() - getCompoundPaddingRight()) / 2;
				break;
		}

		if (margin >= 0) {
			LinearLayout.LayoutParams params = Cast.as(LinearLayout.LayoutParams.class, getLayoutParams());
			if (params != null && margin != params.rightMargin) {
				params.rightMargin = margin;
				setLayoutParams(params);
			}
		}
	}



	@Override
	public void applyEditClass(@NonNull ThemeClassDefinition themeClass) {
		// apply theme class to Switch control
		if (themeClass != null)
		{
			// if default class do not change style
			if (GxSwitchThemeClassExtensionKt.isDefaultSwitchClass(themeClass))
				return;

			// colors from Theme
			Integer colorOnId = ThemeUtils.getColorId(GxSwitchThemeClassExtensionKt.getSwitchOnColor(themeClass));
			Integer colorOffId = ThemeUtils.getColorId(GxSwitchThemeClassExtensionKt.getSwitchOffColor(themeClass));
			Integer colorOnTextId = ThemeUtils.getColorId(GxSwitchThemeClassExtensionKt.getSwitchOnTextColor(themeClass));
			Integer colorOffTextId = ThemeUtils.getColorId(GxSwitchThemeClassExtensionKt.getSwitchOffTextColor(themeClass));

			// states to modify
			int[][] states = new int[][] {
				new int[] {android.R.attr.state_checked},
				new int[] {-android.R.attr.state_checked},
			};

			if (colorOnTextId!=null && colorOffTextId!=null) {
				int[] thumbColors = new int[]{
					colorOnTextId,
					colorOffTextId,
				};
				DrawableCompat.setTintList(DrawableCompat.wrap(this.getThumbDrawable()), new ColorStateList(states, thumbColors));
			}
			if (colorOnId!=null && colorOffId!=null) {
				int[] trackColors = new int[]{
					colorOnId,
					colorOffId,
				};
				DrawableCompat.setTintList(DrawableCompat.wrap(this.getTrackDrawable()), new ColorStateList(states, trackColors));
			}
		}
	}
}
