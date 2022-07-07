package com.genexus.android.controls.wheels;

import com.genexus.android.core.base.metadata.layout.ControlInfo;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

import java.util.ArrayList;
import java.util.List;

public class GxWheelNumericControl implements IGxWheelControl {

	private String mInitialMaxValue;
	private String mInitialMinValue;
	private String mInitialStepValue;
	
	private float mMaxValue;
	private float mMinValue;
	private float mStepValue = 0;
	private String mDisplayInitialValue = Strings.EMPTY;
	private String[] mItemsValue = null;
	private String[] mItemsValueStep = null;

	public GxWheelNumericControl(ControlInfo info) {
		mInitialMaxValue = info.optStringProperty("@SDWheelMaxValue");
		mMaxValue = Float.parseFloat(mInitialMaxValue);
		mInitialMinValue = info.optStringProperty("@SDWheelMinValue");
		mMinValue = Float.parseFloat(mInitialMinValue);
		mInitialStepValue = info.optStringProperty("@SDWheelStep");
		mStepValue = Float.parseFloat(mInitialStepValue);

		if (mInitialStepValue.contains(Strings.DOT)) {
			//the number is a float
			String [] strStepValueSplit = Services.Strings.split(mInitialStepValue, Strings.DOT);
			String strInitialValue = info.optStringProperty("@SDWheelInitialValue");
			if (strInitialValue.contains(Strings.DOT))
			{
				String [] strInitialValueSplit = Services.Strings.split(strInitialValue, Strings.DOT);
				String strDisplayInitialValue = GxWheelHelper.addZerosDecimal(strInitialValueSplit[1], String.valueOf(strStepValueSplit[1]), false);
				mDisplayInitialValue =  strInitialValueSplit[0].concat(Strings.DOT).concat(strDisplayInitialValue);
			} else {
				String strDisplayInitialValue = GxWheelHelper.addZerosDecimal(Strings.ZERO, String.valueOf(strStepValueSplit[1]), false);
				mDisplayInitialValue =  strInitialValue.concat(Strings.DOT).concat(strDisplayInitialValue);
			}

			mInitialMaxValue = GxWheelHelper.getCorrectWheelValue(mInitialMaxValue, mInitialStepValue);
			mInitialMinValue = GxWheelHelper.getCorrectWheelValue(mInitialMinValue, mInitialStepValue);

		} else {
			mDisplayInitialValue = info.optStringProperty("@SDWheelInitialValue");
		}

		obtainValues();
	}
	
	private void obtainValues() {
		//Check if the number is a float
		if (numberIsFloat()) {
			//The number is a float
			obtainValuesFloats();
		} else {
			obtainValuesNumeric(mStepValue);
		}
	}
	
	//check if number is a float
	private boolean numberIsFloat() {
		return (mStepValue > 0 && mStepValue < 1);
	}
	
	private void obtainValuesFloats() {
		obtainValuesNumeric(1);
		
		int lengthStep = (int) (1 / mStepValue);
		mItemsValueStep = new String[lengthStep];
		int currentValueStep = 0;
		String [] strStepValue = Services.Strings.split(mInitialStepValue,Strings.DOT);
		int stepValue = Integer.parseInt(strStepValue[1]);

		for (int i = 0; i < lengthStep; i++) {
			mItemsValueStep[i] = GxWheelHelper.addZerosDecimal(String.valueOf(currentValueStep), strStepValue[1], true);
			currentValueStep += stepValue;
		}
	}

	private void obtainValuesNumeric(float stepValue) {
		int length = (int) (((GxWheelHelper.getNumericByDouble(mMaxValue) - GxWheelHelper.getNumericByDouble(mMinValue)) / stepValue) + 1);
		//int length = (int) (((mMaxValue - mMinValue) / stepValue) + 1);
		mItemsValue = new String[length];
		int currentValue = (int) mMinValue;
		for (int i = 0; i < length; i++) {
			mItemsValue[i]= String.valueOf(currentValue);
			currentValue += Math.round(stepValue);
		}
	}

	@Override
	public Boolean isReady() {
		return true;
	}

	@Override
	public void setOnReady(IGxWheelControlReady ready) {
	}

	@Override
	public String getDisplayInitialValue() {
		return mDisplayInitialValue;
	}
	
	public boolean isOnlyWheelControlNumeric() {
		return (!mDisplayInitialValue.contains(Strings.DOT) && !mInitialStepValue.contains(Strings.DOT));
	}
	
	@Override
	public String getGxDisplayValue(String value) {
		if (Services.Strings.hasValue(value)) {
			value = checkNumberOutOfRange(value);
			if (value.contains(Strings.DOT)) {
				if (numberIsFloat()) {
					value = GxWheelHelper.getCorrectWheelValue(value, mInitialStepValue);
					String [] strValue = Services.Strings.split(value, Strings.DOT);
					String valueNumeric = mItemsValue[GxWheelHelper.getPositionValue(strValue[0], mItemsValue)];
					String valueDecimal = mItemsValueStep[GxWheelHelper.getPositionValue(strValue[1], mItemsValueStep)];
					return valueNumeric.concat(Strings.DOT).concat(valueDecimal);
				} 
				else 
				{
					// has DOT . so get the value with the integer part.
					String [] strValue = Services.Strings.split(value, Strings.DOT);
					return mItemsValue[GxWheelHelper.getPositionValue(strValue[0], mItemsValue)];
				}
			} else {
				return mItemsValue[GxWheelHelper.getPositionValue(value, mItemsValue)];
			}
		}
		
		return value;
	}

	@Override
	public String getGxValue(String displayValue) {
		return displayValue;
	}

	//Check is the number is out of range
	private String checkNumberOutOfRange(String value) {
		try {
			if (Services.Strings.hasValue(value)) {
				if (Float.parseFloat(value) < mMinValue)
					return mInitialMinValue;
				if (Float.parseFloat(value) > mMaxValue)
					return mInitialMaxValue;
			} else {
				return mInitialMinValue;
			}
			return value;
		} catch (NumberFormatException ex) {
			return value;
		}
	}

	@Override
	public void setViewAdapter(String displayValue, GxWheelPicker wheelControlNumeric, GxWheelPicker wheelControlDecimal) {
		if (numberIsFloat()) {
			wheelControlDecimal.setViewAdapter(mItemsValueStep);
		}
		wheelControlNumeric.setViewAdapter(mItemsValue);
		setCurrentItemWheelControl(displayValue, wheelControlNumeric, wheelControlDecimal);
	}

	private void setCurrentItemWheelControl(String displayValue, GxWheelPicker wheelControlNumeric, GxWheelPicker wheelControlDecimal) {
		if (displayValue.contains(Strings.DOT)) {
			String [] strValue = Services.Strings.split(displayValue, Strings.DOT);
			wheelControlNumeric.setCurrentItem(GxWheelHelper.getPositionValue(strValue[0], mItemsValue));
			wheelControlDecimal.setCurrentItem(GxWheelHelper.getPositionValue(strValue[1], mItemsValueStep));
		} else {
			wheelControlNumeric.setCurrentItem(GxWheelHelper.getPositionValue(displayValue, mItemsValue));
		}
	}

	@Override
	public String getCurrentStringValue(GxWheelPicker wheelControlNumeric, GxWheelPicker wheelControlDecimal) {
		if (numberIsFloat()) {
			int itemPositionNumeric = wheelControlNumeric.getCurrentItem();
			int itemPositionDecimal = wheelControlDecimal.getCurrentItem();
			String value = mItemsValue[itemPositionNumeric];
			value = value.concat(Strings.DOT).concat(mItemsValueStep[itemPositionDecimal]);
			value = checkNumberOutOfRange(value);
			return value;
		} else {
			int itemPosition = wheelControlNumeric.getCurrentItem();
			return mItemsValue[itemPosition];
		}
	}

	@Override
	public void onFirstSetGxValue() {
	}

	@Override
	public List<String> getDependencies() {
		return new ArrayList<>();
	}

	@Override
	public void onDependencyValueChanged(String name, Object value) {
	}

	@Override
	public void onRefresh() {
	}
}
