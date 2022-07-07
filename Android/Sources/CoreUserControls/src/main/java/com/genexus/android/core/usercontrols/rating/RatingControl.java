package com.genexus.android.core.usercontrols.rating;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.genexus.android.core.base.metadata.layout.ControlInfo;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controls.IGxEdit;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.utils.ThemeUtils;
import com.genexus.android.core.usercontrols.R;

@SuppressLint("ViewConstructor")
public class RatingControl extends LinearLayout implements IGxEdit {
	public static final String NAME = "Rating";

	private Coordinator mCoordinator;
	private float mStep;

	private SimpleRatingBar mRatingBar;

	public RatingControl(Context context, Coordinator coordinator, LayoutItemDefinition def) {
		super(context);
		mCoordinator = coordinator;
		initialize(context);
		setLayoutDefinition(def);
	}

	private void initialize(Context context) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (inflater != null)
			inflater.inflate(R.layout.ratingcontrol, this, true);
		mRatingBar = findViewById(R.id.ratingbar);
		mRatingBar.setOnClickListener((v) -> {
			if (mCoordinator != null)
				mCoordinator.onValueChanged(this, true);
		});
	}

	@Override
	public String getGxValue() {
		return Integer.toString(Math.round(mRatingBar.getRating() * mStep));
	}

	@Override
	public void setGxValue(String value) {
		if (Services.Strings.hasValue(value)) {
			mRatingBar.setRating(Float.parseFloat(value) / mStep);
		}
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
	public void setValueFromIntent(Intent data) { }

	@Override
	public void setEnabled(boolean enabled) {
		mRatingBar.setIndicator(!enabled);
	}

	@Override
	public IGxEdit getViewControl() {
		mRatingBar.setIndicator(true);
		return this;
	}

	@Override
	public IGxEdit getEditControl() {
		return this;
	}

	private float getStepValue(String stepString) {
		if (stepString.length() > 0) {
			try {
				return Float.parseFloat(stepString);
			}
			catch (NumberFormatException ex) {
				Services.Log.error("formatStep", ex);
			}
		}
		return 1;
	}

	private int getMaxValue(String maxString) {
		if (maxString.length() > 0) {
			try {
				return Integer.parseInt(maxString);
			}
			catch (NumberFormatException ex) {
				Services.Log.error("value", ex);
			}
		}
		return 5;
	}

	private void setLayoutDefinition(LayoutItemDefinition layoutItemDefinition) {
		ControlInfo info = layoutItemDefinition.getControlInfo();
		if (info == null)
			return;

		mStep = getStepValue(info.optStringProperty("@RatingStep"));
		int maxValue = getMaxValue(info.optStringProperty("@RatingMaxValue"));

		int totalStar = (int) (maxValue / mStep);

		mRatingBar.setStepSize(1);
		mRatingBar.setNumberOfStars(totalStar);

		ThemeClassDefinition themeClass = layoutItemDefinition.getThemeClass();
		if (themeClass != null) {
			Integer fillColor = ThemeUtils.getColorId(themeClass.optStringProperty("RatingSelectedColor"));
			if (fillColor != null) {
				mRatingBar.setFillColor(fillColor);
				mRatingBar.setPressedFillColor(fillColor);
			}

			Integer emptyColor = ThemeUtils.getColorId(themeClass.optStringProperty("RatingUnselectedColor"));
			if (emptyColor != null) {
				mRatingBar.setStarBackgroundColor(emptyColor);
				mRatingBar.setPressedStarBackgroundColor(emptyColor);
			}

			Integer borderColor = ThemeUtils.getColorId(themeClass.getBorderColor());
			if (borderColor != null) {
				mRatingBar.setBorderColor(borderColor);
				mRatingBar.setPressedBorderColor(borderColor);
			}
			else if (fillColor != null) {
				mRatingBar.setBorderColor(fillColor); // so it easier to have filled and unfilled starts with the same border
				mRatingBar.setPressedBorderColor(fillColor);
			}

			if (themeClass.getBorderWidth() == 0) {
				mRatingBar.setDrawBorderEnabled(false);
			} else {
				mRatingBar.setDrawBorderEnabled(true);
				mRatingBar.setStarBorderWidth(themeClass.getBorderWidth());
			}
		}
	}

	@Override
	public boolean isEditable() {
		return !mRatingBar.isIndicator();
	}
}
