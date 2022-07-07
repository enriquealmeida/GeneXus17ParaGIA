package com.genexus.android.core.usercontrols;

import java.text.NumberFormat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import androidx.annotation.NonNull;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.genexus.android.core.activities.ActivityHelper;
import com.genexus.android.core.base.controls.IGxControlRuntime;
import com.genexus.android.core.base.controls.IGxEditThemeable;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.layout.ControlInfo;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.loader.MetadataLoader;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.IGxEdit;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.utils.Cast;
import com.genexus.android.core.utils.DrawableUtils;
import com.genexus.android.core.utils.ThemeUtils;

public class SeekBarControl extends LinearLayout implements IGxEdit, IGxEditThemeable, IGxControlRuntime
{
	public static final String NAME = "SDSlider";
	private static final String PROPERTY_MIN_VALUE = "MinValue";
	private static final String PROPERTY_MAX_VALUE = "MaxValue";
	private static final String PROPERTY_MIN_VALUE_IMAGE = "MinValueImage";
	private static final String PROPERTY_MAX_VALUE_IMAGE = "MaxValueImage";
	private static final String PROPERTY_STEP = "Step";

	private LayoutItemDefinition mDefinition;
	private Coordinator mCoordinator;

	private TextView mTextCurrent;
	private TextView mTextMin;
	private TextView mTextMax;
	private SeekBar mSeekBar;
	private ImageView mMinImage;
	private ImageView mMaxImage;
	private String mLastValue;

	private int mMaxValue;
	private double mSeekBarMinValue;
	private double mSeekBarMaxValue;
	private double mSeekBarStep;
	private String mSeekBarMinValueImage;
	private String mSeekBarMaxValueImage;
	private boolean mSeekBarDisplayValue;

	public SeekBarControl(Context context, Coordinator coordinator, LayoutItemDefinition def) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    if (inflater != null)
	       inflater.inflate(R.layout.seekbarcontrol, this, true);
	    mCoordinator = coordinator;
	    setLayoutDefinition(def);
	    init();
	}

	public SeekBarControl(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    if(inflater != null)
	       inflater.inflate(R.layout.seekbarcontrol, this, true);
	    init();
	}

	@Override
	public String getGxValue() {
		return getCurrentValue();
	}

	@Override
	public void setGxValue(String value) {
		setCurrentValue(value);
		mLastValue = getCurrentValue();
	}

	@Override
	public String getGxTag() {
		return getTag().toString();
	}

	@Override
	public void setGxTag(String tag) {
		setTag(tag);
	}

	private void init()
	{
		mSeekBar = findViewById(R.id.seekBar);
		mTextCurrent = findViewById(R.id.textCurrent);
		mTextMin = findViewById(R.id.textMin);
		mTextMax = findViewById(R.id.textMax);
		mMinImage = findViewById(R.id.minImage);
		mMaxImage = findViewById(R.id.maxImage);

		// Do not show texts as specified
		mTextMin.setVisibility(GONE);
		mTextMax.setVisibility(GONE);
		if (mSeekBarDisplayValue) {
			mTextCurrent.setText(getCurrentValue());
			mTextCurrent.setX(getCurrentTextX(mSeekBar));
		} else {
			mTextCurrent.setVisibility(GONE);
		}

		mSeekBar.setMax(mMaxValue);
		mSeekBar.incrementProgressBy(1);
		mTextMin.setText(String.valueOf(mSeekBarMinValue));
		mTextMax.setText(String.valueOf(mSeekBarMaxValue));

		if (Strings.hasValue(mSeekBarMinValueImage)) {
			Drawable minImage = Services.Images.getStaticImage(getContext(), MetadataLoader.getObjectName(mSeekBarMinValueImage));
			mMinImage.setVisibility(VISIBLE);
			mMinImage.setImageDrawable(minImage);
		}
		if (Strings.hasValue(mSeekBarMaxValueImage)) {
			Drawable minImage = Services.Images.getStaticImage(getContext(), MetadataLoader.getObjectName(mSeekBarMaxValueImage));
			mMaxImage.setVisibility(VISIBLE);
			mMaxImage.setImageDrawable(minImage);
		}

		mLastValue = "";

		final View control = this;
		mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				if (mTextCurrent.isShown()) {
					mTextCurrent.setText(getCurrentValue());
					mTextCurrent.setX(getCurrentTextX(seekBar));
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) { }

			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{
				if (mCoordinator!=null)
					mCoordinator.runControlEvent(control, "ValueChanged");

				if (!getCurrentValue().equals(mLastValue) && mCoordinator != null) {
					mCoordinator.onValueChanged(SeekBarControl.this, true);
				}

				mLastValue = getCurrentValue();
			}
		});
	}

	@Override
	public void setValueFromIntent(Intent data) { }

	@Override
	public void setEnabled(boolean enabled)
	{
		mSeekBar.setEnabled(enabled);
		mTextMin.setEnabled(enabled);
		mTextMax.setEnabled(enabled);
		mTextCurrent.setEnabled(enabled);
	}

	@Override
	public IGxEdit getViewControl()
	{
		setEnabled(false);
		return this;
	}

	@Override
	public IGxEdit getEditControl()
	{
		return this;
	}

	private void setCurrentValue(String value)
	{
		int numberOfDecimals = mDefinition.getDataItem().getDecimals();
		double val = Double.parseDouble(value);

		int progress;
		if (mSeekBarStep == 0)
			progress = (int) ((val - mSeekBarMinValue) * Math.pow(10, numberOfDecimals));
		else
			progress = (int) ((val - mSeekBarMinValue) / mSeekBarStep);

		mSeekBar.setProgress(progress);
	}

	private String getCurrentValue()
	{
		int numberOfDecimals = mDefinition.getDataItem().getDecimals();
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(numberOfDecimals);
		if (mSeekBarStep == 0) {
			return nf.format((mSeekBarMinValue + (mSeekBar.getProgress() / Math.pow(10, numberOfDecimals))));
		}
		else {
			return nf.format((mSeekBarMinValue + (mSeekBar.getProgress() * mSeekBarStep)));
		}
	}

	private void calculateSettings(LayoutItemDefinition layoutItemDefinition) {
		int numberOfDecimals = layoutItemDefinition.getDataItem().getDecimals();
		if (mSeekBarStep == 0) {
				int maxValue = (int) (mSeekBarMaxValue *  Math.pow(10, numberOfDecimals));
				int minValue = (int) (mSeekBarMinValue * Math.pow(10, numberOfDecimals));
				mMaxValue = maxValue - minValue;
		} else
			if (mSeekBarStep > 0)
				mMaxValue = (int) ((mSeekBarMaxValue - mSeekBarMinValue) / mSeekBarStep);
	}

	private void updateSeekbar()
	{
		calculateSettings(mDefinition);
		mSeekBar.setMax(mMaxValue);
	}

	private void setLayoutDefinition(LayoutItemDefinition layoutItemDefinition)
	{
		mDefinition = layoutItemDefinition;
		ControlInfo info = layoutItemDefinition.getControlInfo();
		if (info == null)
			return;

		mSeekBarMinValue = Services.Strings.tryParseDouble(info.optStringProperty("@SDSliderMinValue"), 0);
		mSeekBarMaxValue = Services.Strings.tryParseDouble(info.optStringProperty("@SDSliderMaxValue"), 10);
		mSeekBarStep = Services.Strings.tryParseDouble(info.optStringProperty("@SDSliderStep"), 0);
		if (mSeekBarMinValue > mSeekBarMaxValue)
			resetValues();

		calculateSettings(layoutItemDefinition);

		mSeekBarMaxValueImage = info.optStringProperty("@SDSliderMinValueImageIndicator");
		mSeekBarMaxValueImage = info.optStringProperty("@SDSliderMaxValueImageIndicator");

		mSeekBarDisplayValue = info.optBooleanProperty("@SDSliderDisplayValue");
	}

	private void resetValues()
	{
		mSeekBarMinValue = 0;
		mSeekBarMaxValue = 10;
		mSeekBarStep = 1;
	}

	private int getCurrentTextX(SeekBar seekBar) {
		Point point = ActivityHelper.getAppUsableScreenSizeInPixels(mCoordinator.getUIContext().getActivity().getWindowManager());
		float density = mCoordinator.getUIContext().getActivity().getResources().getDisplayMetrics().density / 10;
		int progress = seekBar.getProgress();
		int thumbOffset = seekBar.getThumbOffset();
		int max = seekBar.getMax();
		int percentage = (int) ((100 - (progress * 100 / max)) * density);
		int val = (progress * (seekBar.getWidth() - 2 * thumbOffset)) / max;
		return (int) seekBar.getX() + val + thumbOffset / 2 - point.x / 2 + percentage;
	}

	@Override
	public void setPropertyValue(String name, Value value)
	{
		if (value == null)
			return;

		boolean isChanged = false;
		String currentValue = getGxValue();

		if (PROPERTY_MIN_VALUE.equalsIgnoreCase(name)) {
			mSeekBarMinValue = value.coerceToDouble(0);
			isChanged = true;
		} else if (PROPERTY_MAX_VALUE.equalsIgnoreCase(name)) {
			mSeekBarMaxValue = value.coerceToDouble(10);
			isChanged = true;
		} else if (PROPERTY_STEP.equalsIgnoreCase(name)) {
			mSeekBarStep = value.coerceToDouble(0);
			isChanged = true;
		} else if (PROPERTY_MIN_VALUE_IMAGE.equalsIgnoreCase(name)) {
			mSeekBarMinValueImage = value.coerceToString();
			if (Strings.hasValue(mSeekBarMinValueImage)) {
				Drawable minImage = Services.Images.getStaticImage(getContext(), MetadataLoader.getObjectName(mSeekBarMinValueImage));
				mMinImage.setVisibility(VISIBLE);
				mMinImage.setImageDrawable(minImage);
			} else {
				mMinImage.setVisibility(GONE);
			}
		} else if (PROPERTY_MAX_VALUE_IMAGE.equalsIgnoreCase(name)) {
			mSeekBarMaxValueImage = value.coerceToString();
			if (Strings.hasValue(mSeekBarMaxValueImage)) {
				Drawable minImage = Services.Images.getStaticImage(getContext(), MetadataLoader.getObjectName(mSeekBarMaxValueImage));
				mMaxImage.setVisibility(VISIBLE);
				mMaxImage.setImageDrawable(minImage);
			} else {
				mMaxImage.setVisibility(GONE);
			}
		}

		if (isChanged) {
			// Update calculations and visual position in seekbar.
			updateSeekbar();
			setCurrentValue(currentValue);
		}
	}

	@Override
	public Value getPropertyValue(String name)
	{
		if (PROPERTY_MIN_VALUE.equalsIgnoreCase(name)) {
			return Value.newDouble(mSeekBarMinValue);
		} else if (PROPERTY_MAX_VALUE.equalsIgnoreCase(name)) {
			return Value.newDouble(mSeekBarMaxValue);
		} else if (PROPERTY_STEP.equalsIgnoreCase(name)) {
			return Value.newDouble(mSeekBarStep);
		} else if (PROPERTY_MIN_VALUE_IMAGE.equalsIgnoreCase(name)) {
			return Value.newString(mSeekBarMinValueImage);
		} else if (PROPERTY_MAX_VALUE_IMAGE.equalsIgnoreCase(name)) {
			return Value.newString(mSeekBarMaxValueImage);
		}
		return null;
	}

	@Override
	public void applyEditClass(@NonNull ThemeClassDefinition themeClass)
	{
		Integer progressColor = ThemeUtils.getColorId(themeClass.optStringProperty("SDSliderSelectedBarColor"));
		Integer backgroundColor = ThemeUtils.getColorId(themeClass.optStringProperty("SDSliderUnselectedBarColor"));
		Integer thumbColor = ThemeUtils.getColorId(themeClass.optStringProperty("SDSliderThumbColor"));

		// Enable reverting to original color, which is colorControlActivated according to support TintManager.
		if (progressColor == null)
			progressColor = ThemeUtils.getAndroidThemeColorId(getContext(), R.attr.colorControlActivated);
		if (thumbColor == null)
			thumbColor = ThemeUtils.getAndroidThemeColorId(getContext(), R.attr.colorControlActivated);
		if (backgroundColor == null)
			backgroundColor = ThemeUtils.getAndroidThemeColorId(getContext(), R.attr.colorControlNormal);

		LayerDrawable layers = Cast.as(LayerDrawable.class, mSeekBar.getProgressDrawable());
		if (progressColor != null && layers != null) {
			Drawable progressDrawable = layers.findDrawableByLayerId(android.R.id.progress);
			if (progressDrawable != null) {
				progressDrawable = DrawableUtils.applyTint(progressDrawable, progressColor);
				layers.setDrawableByLayerId(android.R.id.progress, progressDrawable);
			}
		}

		if (backgroundColor != null && layers != null) {
			Drawable backgroundDrawable = layers.findDrawableByLayerId(android.R.id.background);
			if (backgroundDrawable != null) {
				backgroundDrawable = DrawableUtils.applyTint(backgroundDrawable, backgroundColor);
				layers.setDrawableByLayerId(android.R.id.background, backgroundDrawable);
			}
		}

		if (thumbColor != null) {
			Drawable thumbDrawable = mSeekBar.getThumb();
			if (thumbDrawable != null) {
				thumbDrawable = DrawableUtils.applyTint(thumbDrawable, thumbColor);
				mSeekBar.setThumb(thumbDrawable);
			}
		}

		invalidate();
	}

	@Override
	public boolean isEditable()
	{
		return isEnabled(); // Editable when enabled.
	}
}
