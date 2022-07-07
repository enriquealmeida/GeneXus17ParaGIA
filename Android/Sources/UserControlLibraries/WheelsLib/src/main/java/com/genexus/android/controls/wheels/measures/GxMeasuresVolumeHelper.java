package com.genexus.android.controls.wheels.measures;

import org.json.JSONException;
import org.json.JSONObject;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.controls.wheels.R;

public class GxMeasuresVolumeHelper implements IGxMeasuresHelper{

	private static int mLitersCubicToCentimeters = 1000;
	
	private String mUnit = MeasureVolume.MEASURE_VOLUME_CUBIC_CENTIMETERS;
	private String mCurrentUnit = MeasureVolume.MEASURE_VOLUME_CUBIC_CENTIMETERS;
	private double mValue = 0;
	private double mCurrentValue = 0;
		
	private static String mPrefixCubicCentimeters = "cm3";
	private static String mPrefixLiters = "lt";
	
	static final class MeasureVolume {
		public static final String MEASURE_VOLUME_CUBIC_CENTIMETERS = "Cubic Cm";
		public static final String MEASURE_VOLUME_LITERS = "Liters";
	}
	
	private double changeCubicCentimetersToLiters(double cubicCentimeters) {
		return cubicCentimeters / mLitersCubicToCentimeters;
	}
	
	private double changeLitersToCubicCentimeters(double cubicCentimeters) {
		return cubicCentimeters * mLitersCubicToCentimeters;
	}
	
	@Override
	public String getDisplayValue(double value) {
		return getDisplayValue(value, mUnit);
	}
	
	private String getDisplayValue(double value, String unit) {
		if (unit.equalsIgnoreCase(MeasureVolume.MEASURE_VOLUME_CUBIC_CENTIMETERS))
			return String.valueOf(GxMeasuresHelper.getNumericByDouble(value)).concat(Strings.SPACE).concat(mPrefixCubicCentimeters);
		else if (unit.equalsIgnoreCase(MeasureVolume.MEASURE_VOLUME_LITERS))
			return String.valueOf(value).concat(Strings.SPACE).concat(mPrefixLiters);
		return Strings.EMPTY;
	}
	
	// Get display value in the "Action" button
	@Override
	public String getCurrentStringValue(int currentItemNumeric, int currentItemDecimal) {
		mCurrentValue = getCurreentValue(currentItemNumeric, currentItemDecimal);
		return getDisplayValue(mCurrentValue, mCurrentUnit);
	}
	
	private double getCurreentValue(int currentItemNumeric, int currentItemDecimal) {
		if (mCurrentUnit.equalsIgnoreCase(MeasureVolume.MEASURE_VOLUME_CUBIC_CENTIMETERS))
			return Double.parseDouble(String.valueOf(currentItemNumeric));
		else if (mCurrentUnit.equalsIgnoreCase(MeasureVolume.MEASURE_VOLUME_LITERS))
			if (currentItemDecimal < 100)
				if (currentItemDecimal < 10)
					// If the unit is liters and value of the decimal is less than 10 will add a 00 after the "."
					return Double.parseDouble(String.valueOf(currentItemNumeric).concat(Strings.DOT).concat(Strings.ZERO).concat(Strings.ZERO).concat(String.valueOf(currentItemDecimal)));
				else
					//If the unit is liters and value of the decimal is less than 100 will add a 0 after the "."
					return Double.parseDouble(String.valueOf(currentItemNumeric).concat(Strings.DOT).concat(Strings.ZERO).concat(String.valueOf(currentItemDecimal)));
			else
				return Double.parseDouble(String.valueOf(currentItemNumeric).concat(Strings.DOT).concat(String.valueOf(currentItemDecimal)));
		return 0;
	}

	@Override
	public String getGxValue(String valueKey, String unitKey, String convertedValueKey) {
		double value = mValue;
		JSONObject valueJSONObject = new JSONObject();
		try {
			valueJSONObject.put(valueKey, value);
			valueJSONObject.put(convertedValueKey, value);
			if (mUnit.equalsIgnoreCase(MeasureVolume.MEASURE_VOLUME_CUBIC_CENTIMETERS)) {
				valueJSONObject.put(unitKey, mPrefixCubicCentimeters);
			} else if (mUnit.equalsIgnoreCase(MeasureVolume.MEASURE_VOLUME_LITERS)) {
				valueJSONObject.put(unitKey, mPrefixLiters);
			}
		} catch (JSONException e) {
		}
		return valueJSONObject.toString();
	}

	@Override
	public String getTextButtonChange() {
		String toView = Strings.EMPTY;
		if (mCurrentUnit.equalsIgnoreCase(MeasureVolume.MEASURE_VOLUME_CUBIC_CENTIMETERS))
			toView = String.format(Services.Strings.getResource(R.string.GXM_ConvertTo), MeasureVolume.MEASURE_VOLUME_LITERS);
		else if (mCurrentUnit.equalsIgnoreCase(MeasureVolume.MEASURE_VOLUME_LITERS))
			toView = String.format(Services.Strings.getResource(R.string.GXM_ConvertTo), MeasureVolume.MEASURE_VOLUME_CUBIC_CENTIMETERS);
		return toView;
	}

	@Override
	public double getValueKey(double valueKey, String valueUnitKey) {
		setValueUnitKey(valueUnitKey);
		mValue =  valueKey;
		return mValue;
	}

	private void setValueUnitKey(String valueUnitKey) {
		if (valueUnitKey.equalsIgnoreCase(mPrefixCubicCentimeters)) {
			mUnit = MeasureVolume.MEASURE_VOLUME_CUBIC_CENTIMETERS;
		} else if (valueUnitKey.equalsIgnoreCase(mPrefixLiters)) {
			mUnit = MeasureVolume.MEASURE_VOLUME_LITERS;
		} else {
			//the default value Cubic Centimeters
			mUnit = MeasureVolume.MEASURE_VOLUME_CUBIC_CENTIMETERS;
		}
	}
	
	@Override
	public void changeValue(int currentItemNumeric, int currentItemDecimal) {
		mCurrentValue = getCurreentValue(currentItemNumeric, currentItemDecimal);
		if (mCurrentUnit.equalsIgnoreCase(MeasureVolume.MEASURE_VOLUME_CUBIC_CENTIMETERS)) {
			//cubic centimeters to liters
			mCurrentValue = changeCubicCentimetersToLiters(mCurrentValue);
			mCurrentUnit = MeasureVolume.MEASURE_VOLUME_LITERS;
		} else if (mCurrentUnit.equalsIgnoreCase(MeasureVolume.MEASURE_VOLUME_LITERS)) {
			//liters to cubic centimeters
			mCurrentValue = changeLitersToCubicCentimeters(mCurrentValue);
			mCurrentUnit = MeasureVolume.MEASURE_VOLUME_CUBIC_CENTIMETERS;
		}
	}

	@Override
	public void setValueInWheelControl(GxMeasuresControl linearLayout) {
		double value = mCurrentValue;
		if (mCurrentUnit.equalsIgnoreCase(MeasureVolume.MEASURE_VOLUME_CUBIC_CENTIMETERS)) {
			linearLayout.setWheelControlViewAdapter(0, 99999, 0, 0, GxMeasuresHelper.getNumericByDouble(value), GxMeasuresHelper.getDecimalByDouble(value));
		} else if (mCurrentUnit.equalsIgnoreCase(MeasureVolume.MEASURE_VOLUME_LITERS)) {
			linearLayout.setWheelControlViewAdapter(0, 99, 0, 999, GxMeasuresHelper.getNumericByDouble(value), GxMeasuresHelper.getDecimalByDouble(value));
		} else {
			linearLayout.setWheelControlViewAdapter(0, 99999, 0, 0, GxMeasuresHelper.getNumericByDouble(value), GxMeasuresHelper.getDecimalByDouble(value));
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
