package com.genexus.android.controls.wheels.measures;

import org.json.JSONException;
import org.json.JSONObject;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.controls.wheels.R;

public class GxMeasuresWeightHelper implements IGxMeasuresHelper {

	private static double mKilogramToPound = 2.20462262;
	
	private String mUnit = MeasureWeight.MEASURE_WEIGHT_KILOGRAM;
	private String mCurrentUnit = MeasureWeight.MEASURE_WEIGHT_KILOGRAM;
	private double mValue = 0;
	private double mCurrentValue = 0;
	
	static final class MeasureWeight {
		public static final String MEASURE_WEIGHT_POUND = "Pounds";
		public static final String MEASURE_WEIGHT_KILOGRAM = "Kilograms";
	}
	
	private static String mPrefixKilogram = "kg";
	private static String mPrefixPound = "lb";
	
	private double changeKilogramToPound (double kilogram) {
		return GxMeasuresHelper.round(kilogram * mKilogramToPound, 1).doubleValue();
	}
	
	private double changePoundToKilogram (double pound) {
		return GxMeasuresHelper.round(pound / mKilogramToPound, 1).doubleValue();
	}

	@Override
	public String getDisplayValue(double value) {
		return getDisplayValue(value, mUnit);
	}
	
	private String getDisplayValue(double value, String unit) {
		if (unit.equalsIgnoreCase(MeasureWeight.MEASURE_WEIGHT_KILOGRAM))
			return String.valueOf(value).concat(Strings.SPACE).concat(mPrefixKilogram);
		else if (unit.equalsIgnoreCase(MeasureWeight.MEASURE_WEIGHT_POUND))
			return String.valueOf(value).concat(Strings.SPACE).concat(mPrefixPound);
		return Strings.EMPTY;
	}
	
	// Get display value in the "Action" button
	@Override
	public String getCurrentStringValue(int currentItemNumeric, int currentItemDecimal) {
		mCurrentValue = getCurreentValue(currentItemNumeric, currentItemDecimal);
		return getDisplayValue(mCurrentValue, mCurrentUnit);
	}

	private double getCurreentValue(int currentItemNumeric, int currentItemDecimal) {
		return Double.parseDouble(String.valueOf(currentItemNumeric).concat(Strings.DOT).concat(String.valueOf(currentItemDecimal)));
	}
	
	@Override
	public String getGxValue(String valueKey, String unitKey, String convertedValueKey) {
		double value = mValue;
		JSONObject valueJSONObject = new JSONObject();
		try {
			valueJSONObject.put(valueKey, value);
			valueJSONObject.put(convertedValueKey, value);
			if (mUnit.equalsIgnoreCase(MeasureWeight.MEASURE_WEIGHT_KILOGRAM)) {
				valueJSONObject.put(unitKey, mPrefixKilogram);
			} else if (mUnit.equalsIgnoreCase(MeasureWeight.MEASURE_WEIGHT_POUND)) {
				valueJSONObject.put(unitKey, mPrefixPound);
			}
		} catch (JSONException e) {
		}
		return valueJSONObject.toString();
	}

	@Override
	public String getTextButtonChange() {
		String toView = Strings.EMPTY;
		if (mCurrentUnit.equalsIgnoreCase(MeasureWeight.MEASURE_WEIGHT_KILOGRAM))
			toView = String.format(Services.Strings.getResource(R.string.GXM_ConvertTo), MeasureWeight.MEASURE_WEIGHT_POUND);
		else if (mCurrentUnit.equalsIgnoreCase(MeasureWeight.MEASURE_WEIGHT_POUND))
			toView = String.format(Services.Strings.getResource(R.string.GXM_ConvertTo), MeasureWeight.MEASURE_WEIGHT_KILOGRAM);
		return toView;
	}
	
	@Override
	public double getValueKey(double valueKey, String valueUnitKey) {
		setValueUnitKey(valueUnitKey);
		mValue =  valueKey;
		return mValue;
	}

	private void setValueUnitKey(String valueUnitKey) {
		if (valueUnitKey.equalsIgnoreCase(mPrefixPound)) {
			mUnit = MeasureWeight.MEASURE_WEIGHT_POUND;
		} else if (valueUnitKey.equalsIgnoreCase(mPrefixKilogram)) {
			mUnit = MeasureWeight.MEASURE_WEIGHT_KILOGRAM;
		} else {
			//the default value Kilograms
			mUnit = MeasureWeight.MEASURE_WEIGHT_KILOGRAM;
		}
	}
	
	@Override
	public void changeValue(int currentItemNumeric, int currentItemDecimal) {
		mCurrentValue = getCurreentValue(currentItemNumeric, currentItemDecimal);
		if (mCurrentUnit.equalsIgnoreCase(MeasureWeight.MEASURE_WEIGHT_KILOGRAM)) {
			//kilograms to pounds
			mCurrentValue = changeKilogramToPound(mCurrentValue);
			mCurrentUnit = MeasureWeight.MEASURE_WEIGHT_POUND;
		} else if (mCurrentUnit.equalsIgnoreCase(MeasureWeight.MEASURE_WEIGHT_POUND)) {
			//pounds to kilograms
			mCurrentValue = changePoundToKilogram(mCurrentValue);
			mCurrentUnit = MeasureWeight.MEASURE_WEIGHT_KILOGRAM;
		}
	}

	@Override
	public void setValueInWheelControl(GxMeasuresControl linearLayout) {
		double value = mCurrentValue;
		if (mCurrentUnit.equalsIgnoreCase(MeasureWeight.MEASURE_WEIGHT_KILOGRAM)) {
			linearLayout.setWheelControlViewAdapter(0, 199, 0, 9, GxMeasuresHelper.getNumericByDouble(value), GxMeasuresHelper.getDecimalByDouble(value));
		} else if (mCurrentUnit.equalsIgnoreCase(MeasureWeight.MEASURE_WEIGHT_POUND)) {
			linearLayout.setWheelControlViewAdapter(0, 440, 0, 9, GxMeasuresHelper.getNumericByDouble(value), GxMeasuresHelper.getDecimalByDouble(value));
		} else {
			linearLayout.setWheelControlViewAdapter(0, 199, 0, 9, GxMeasuresHelper.getNumericByDouble(value), GxMeasuresHelper.getDecimalByDouble(value));
		}
	}

	@Override
	public void okClickListener() {
		mValue = mCurrentValue;
		mUnit = mCurrentUnit;
	}

	@Override
	public void actionClickListener() {
		mCurrentValue = mValue;
		mCurrentUnit = mUnit;
	}
}
