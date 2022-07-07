package com.genexus.android.core.usercontrols.sparkline;

import android.graphics.Color;

import com.genexus.android.core.base.utils.Strings;

public class SparkLineOptions {
	private String mLabelText = Strings.EMPTY;
	private int mLabelColor = Color.DKGRAY;
	private boolean mShowCurrentValue = true;
	private int mCurrentValueColor = Color.BLUE;
	private int mPenColor = Color.BLACK;
	private float mPenWidth = 1.0f;

	//! The text to be displayed beside the graph data.
	public String getLabelText() {
		return mLabelText;
	}
	public void setLabelText(String labelText) {
		mLabelText = labelText;
	}
	//! The colour of the label text (default: dark gray).
	public int getLabelColor() {
		return mLabelColor;
	}
	public void setLabelColor(int labelColor) {
		mLabelColor = labelColor;
	}

	//! Flag to enable display of the numerical current (last) value (default: YES).
	public boolean isShowCurrentValue() {
		return mShowCurrentValue;
	}
	public void setShowCurrentValue(boolean showCurrentValue) {
		mShowCurrentValue = showCurrentValue;
	}

	//! The Color used to display the numeric current value and the marker anchor.
	public int getCurrentValueColor() {
		return mCurrentValueColor;
	}
	public void setCurrentValueColor(int currentValueColor) {
		mCurrentValueColor = currentValueColor;
	}

	//! The Color used for the sparkline colour itself
	public int getPenColor() {
		return mPenColor;
	}
	public void setPenColor(int penColor) {
		mPenColor = penColor;
	}
	//! The float value used for the sparkline pen width
	public float getPenWidth() {
		return mPenWidth;
	}
	public void setPenWidth(float penWidth) {
		mPenWidth = penWidth;
	}
}
