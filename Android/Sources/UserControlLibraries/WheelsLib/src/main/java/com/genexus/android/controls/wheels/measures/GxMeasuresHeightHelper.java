package com.genexus.android.controls.wheels.measures;

import org.json.JSONException;
import org.json.JSONObject;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.controls.wheels.R;

public class GxMeasuresHeightHelper implements IGxMeasuresHelper {
	private static final double INCHES_TO_METERS = 0.0254;
	private static final int FEET_TO_INCHES = 12;

	private static final String MEASURE_HEIGHT_FEET = "Feet";
	private static final String PREFIX_METERS = "m";
	private static final String PREFIX_FEET = "ft";
	private static final String PREFIX_INCHES = "in";

	private static final class MeasureHeight {
		static final String MEASURE_HEIGHT_METERS = "Meters";
		static final String MEASURE_HEIGHT_INCHES = "Inches";
	}

	private String mUnit = MeasureHeight.MEASURE_HEIGHT_METERS;
	private String mCurrentUnit = MeasureHeight.MEASURE_HEIGHT_METERS;
	private double mValue = 0; //saved the value in inches or meters
	private double mCurrentValue = 0;

	private double changeMetersToInches (double meters) {
		return GxMeasuresHelper.round(meters * (1 / INCHES_TO_METERS), 0).doubleValue();
	}

	private double changeFeetToInches (double feet) {
		return changeFeetToInches(feet, 1);
	}
	
	private double changeFeetToInches (double feet, int decimals) {
		int numeric = GxMeasuresHelper.getNumericByDouble(feet);
		int decimal = getDecimalByDouble(feet, MeasureHeight.MEASURE_HEIGHT_INCHES);
		double inches =  changeFeetToInches(numeric, decimal);
		return GxMeasuresHelper.round(inches, decimals).doubleValue();
	}

	private double changeFeetToInches(int numeric, int decimal) {
		return (numeric * FEET_TO_INCHES) + decimal;
	}

	public double changeInchesToMeters (double inches) {
		return GxMeasuresHelper.round(inches * INCHES_TO_METERS, 2).doubleValue();
	}

	// Get the numeric value to make the conversion from feet to inches
	public int getNumericFeetByInches(double inches) {
		return (int) (inches / FEET_TO_INCHES);
	}
	
	// Get the numeric value to rest the conversion from feet to inches
	public int getRestFeetByInches(double inches) {
		return (int) (inches % FEET_TO_INCHES);
	}

	public int getDecimalByDouble(double value) {
		return getDecimalByDouble(value, mCurrentUnit);
	}

	private int getDecimalByDouble(double value, String unit) {
		String [] strValue = Services.Strings.split(String.valueOf(value), Strings.DOT);
		if (strValue.length > 1) {
			if (unit.equalsIgnoreCase(MeasureHeight.MEASURE_HEIGHT_METERS) && strValue[1].length()==1)
				// If the unit is meters and the value is eg: 1.7, it is actually 1.70, I return 70 and not 7.
				return Integer.parseInt(strValue[1]) * 10;
			return Integer.parseInt(strValue[1]);
		}
		return 0;
	}
	
	// Get display value in the "Action" button
	@Override
	public String getCurrentStringValue(int currentItemNumeric, int currentItemDecimal) {
		mCurrentValue = getCurreentValue(currentItemNumeric, currentItemDecimal);
		return getDisplayValue(mCurrentValue, mCurrentUnit);
	}
	
	@Override
	public String getDisplayValue(double value) {
		return getDisplayValue(value, mUnit);
	}
	
	private String getDisplayValue(double value, String unit) {
		int numeric = 0;
		int decimal = 0;
		if (unit.equalsIgnoreCase(MeasureHeight.MEASURE_HEIGHT_METERS)) {
			numeric = GxMeasuresHelper.getNumericByDouble(value);
			decimal = getDecimalByDouble(value, unit);
			String strDecimal = String.valueOf(decimal);
			if (decimal < 10)
				strDecimal = Strings.ZERO.concat(strDecimal); //To print eg. 1.07 m and 1.7 m
			return String.valueOf(numeric).concat(Strings.DOT).concat(strDecimal).concat(Strings.SPACE).concat(PREFIX_METERS);
		} else if (unit.equalsIgnoreCase(MeasureHeight.MEASURE_HEIGHT_INCHES)) {
			numeric = getNumericFeetByInches(value);
			decimal = getRestFeetByInches(value);
			return String.valueOf(numeric).concat("'").concat(String.valueOf(decimal).concat("''").concat(Strings.SPACE).concat(PREFIX_FEET));
		}
		return Strings.EMPTY;
	}

	// Get the current value, if the unit is "Inches" return the value in inches.
	private double getCurreentValue(int currentItemNumeric, int currentItemDecimal) {
		if (mCurrentUnit.equalsIgnoreCase(MeasureHeight.MEASURE_HEIGHT_METERS))
			if (currentItemDecimal < 10)
				// If the unit is meters and value of the decimal is less than 10 will add a 0 after the "."
				return Double.parseDouble(String.valueOf(currentItemNumeric).concat(Strings.DOT).concat(Strings.ZERO).concat(String.valueOf(currentItemDecimal)));
			else
				return Double.parseDouble(String.valueOf(currentItemNumeric).concat(Strings.DOT).concat(String.valueOf(currentItemDecimal)));
		if (mCurrentUnit.equalsIgnoreCase(MeasureHeight.MEASURE_HEIGHT_INCHES))
			return changeFeetToInches(currentItemNumeric, currentItemDecimal);
		return 0;
	}
	
	@Override
	public String getGxValue(String valueKey, String unitKey, String convertedValueKey) {
		double value = mValue;
		JSONObject valueJSONObject = new JSONObject();
		try {
			if (mUnit.equalsIgnoreCase(MeasureHeight.MEASURE_HEIGHT_METERS)) {
				valueJSONObject.put(valueKey, value);
				valueJSONObject.put(unitKey, PREFIX_METERS);
				valueJSONObject.put(convertedValueKey, value);
			} else if (mUnit.equalsIgnoreCase(MeasureHeight.MEASURE_HEIGHT_INCHES)) {
				valueJSONObject.put(valueKey, value);
				valueJSONObject.put(unitKey, PREFIX_INCHES);
				valueJSONObject.put(convertedValueKey, changeInchesToMeters(value));
			}
		} catch (JSONException e) {
		}
		return valueJSONObject.toString();
	}

	@Override
	public String getTextButtonChange() {
		String toView = Strings.EMPTY;
		if (mCurrentUnit.equalsIgnoreCase(MeasureHeight.MEASURE_HEIGHT_METERS))
			toView = String.format(Services.Strings.getResource(R.string.GXM_ConvertTo), GxMeasuresHeightHelper.MEASURE_HEIGHT_FEET);
		else if (mCurrentUnit.equalsIgnoreCase(MeasureHeight.MEASURE_HEIGHT_INCHES))
			toView = String.format(Services.Strings.getResource(R.string.GXM_ConvertTo), MeasureHeight.MEASURE_HEIGHT_METERS);
		return toView;
	}

	@Override
	public double getValueKey(double valueKey, String valueUnitKey) {
		setValueUnitKey(valueUnitKey);
		mValue =  getCorrectValueKey(valueKey, valueUnitKey);
		return mValue;
	}
	
	private void setValueUnitKey(String valueUnitKey) {
		if (valueUnitKey.equalsIgnoreCase(PREFIX_INCHES) || valueUnitKey.equalsIgnoreCase("inches") || valueUnitKey.equalsIgnoreCase(PREFIX_FEET) || valueUnitKey.equalsIgnoreCase("feet")) {
			mUnit = MeasureHeight.MEASURE_HEIGHT_INCHES;
		} else if (valueUnitKey.equalsIgnoreCase("metros") || valueUnitKey.equalsIgnoreCase(PREFIX_METERS)) {
			mUnit = MeasureHeight.MEASURE_HEIGHT_METERS;
		} else {
			//the default value Meters
			mUnit = MeasureHeight.MEASURE_HEIGHT_METERS;
		}
	}
	
	private double getCorrectValueKey(double valueKey, String valueUnitKey) {
		if (valueUnitKey.equalsIgnoreCase("ft") || valueUnitKey.equalsIgnoreCase("feet")) {
			//change feet to inches
			valueKey = changeFeetToInches(valueKey);
		}
		return valueKey;
	}
	
	@Override
	public void changeValue(int currentItemNumeric, int currentItemDecimal) {
		mCurrentValue = getCurreentValue(currentItemNumeric, currentItemDecimal);
		if (mCurrentUnit.equalsIgnoreCase(MeasureHeight.MEASURE_HEIGHT_METERS)) {
			//meters to inches
			mCurrentValue = changeMetersToInches(mCurrentValue);
			mCurrentUnit = MeasureHeight.MEASURE_HEIGHT_INCHES;
		} else if (mCurrentUnit.equalsIgnoreCase(MeasureHeight.MEASURE_HEIGHT_INCHES)) {
			//inches to meters
			mCurrentValue = changeInchesToMeters(mCurrentValue);
			mCurrentUnit = MeasureHeight.MEASURE_HEIGHT_METERS;
		}
	}

	@Override
	public void setValueInWheelControl(GxMeasuresControl linearLayout) {
		double value = mCurrentValue;
		if (mCurrentUnit.equalsIgnoreCase(MeasureHeight.MEASURE_HEIGHT_METERS)) {
			linearLayout.setWheelControlViewAdapter(0, 2, 0, 99, GxMeasuresHelper.getNumericByDouble(value), getDecimalByDouble(value));
		} else if (mCurrentUnit.equalsIgnoreCase(MeasureHeight.MEASURE_HEIGHT_INCHES)) {
			linearLayout.setWheelControlViewAdapter(0, 7, 0, 11, getNumericFeetByInches(value), getRestFeetByInches(value));
		} else {
			linearLayout.setWheelControlViewAdapter(0, 2, 0, 99, GxMeasuresHelper.getNumericByDouble(value), getDecimalByDouble(value));
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
