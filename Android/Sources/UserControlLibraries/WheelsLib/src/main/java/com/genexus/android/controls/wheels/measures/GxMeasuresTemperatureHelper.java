package com.genexus.android.controls.wheels.measures;

import org.json.JSONException;
import org.json.JSONObject;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.controls.wheels.R;

public class GxMeasuresTemperatureHelper implements IGxMeasuresHelper {
	
	private String mUnit = MeasureTemperature.MEASURE_TEMPERATURE_CELSIUS;
	private String mCurrentUnit = MeasureTemperature.MEASURE_TEMPERATURE_CELSIUS;
	private double mValue = 0;
	private double mCurrentValue = 0;
	
	private static String mPrefixCelcius = "ºC";
	private static String mPrefixFahrenheit = "ºF";
	
	static final class MeasureTemperature {
		public static final String MEASURE_TEMPERATURE_CELSIUS = "Celsius";
		public static final String MEASURE_TEMPERATURE_FAHRENHEIT = "Fahrenheit";
	}
	
	private double changeCelsiusToFahrenheit (double celcius) {
		return GxMeasuresHelper.round((celcius * 9/5) + 32, 1).doubleValue();
	}
	
	private double changeFahrenheitToCelsius (double fahrenheit) {
		return GxMeasuresHelper.round((fahrenheit - 32) * 5/9, 1).doubleValue();
	}
	
	@Override
	public String getDisplayValue(double value) {
		return getDisplayValue(value, mUnit);
	}
	
	private String getDisplayValue(double value, String unit) {
		if (unit.equalsIgnoreCase(MeasureTemperature.MEASURE_TEMPERATURE_CELSIUS))
			return String.valueOf(value).concat(Strings.SPACE).concat(mPrefixCelcius);
		else if (unit.equalsIgnoreCase(MeasureTemperature.MEASURE_TEMPERATURE_FAHRENHEIT))
			return String.valueOf(value).concat(Strings.SPACE).concat(mPrefixFahrenheit);
		return Strings.EMPTY;
	}

	// Get display value in the "Action" button
	@Override
	public String getCurrentStringValue(int currentItemNumeric, int currentItemDecimal) {
		mCurrentValue = getCurreentValue(currentItemNumeric, currentItemDecimal);
		return getDisplayValue(mCurrentValue, mCurrentUnit);
	}
	
	private double getCurreentValue(int currentItemNumeric, int currentItemDecimal) {
		return Double.parseDouble(String.valueOf(currentItemNumeric - 40).concat(Strings.DOT).concat(String.valueOf(currentItemDecimal)));
	}

	@Override
	public String getGxValue(String valueKey, String unitKey, String convertedValueKey) {
		double value = mValue;
		JSONObject valueJSONObject = new JSONObject();
		try {
			valueJSONObject.put(valueKey, value);
			valueJSONObject.put(convertedValueKey, value);
			if (mUnit.equalsIgnoreCase(MeasureTemperature.MEASURE_TEMPERATURE_CELSIUS)) {
				valueJSONObject.put(unitKey, mPrefixCelcius);
			} else if (mUnit.equalsIgnoreCase(MeasureTemperature.MEASURE_TEMPERATURE_FAHRENHEIT)) {
				valueJSONObject.put(unitKey, mPrefixFahrenheit);
			}
		} catch (JSONException e) {
		}
		return valueJSONObject.toString();
	}

	@Override
	public String getTextButtonChange() {
		String toView = Strings.EMPTY;
		if (mCurrentUnit.equalsIgnoreCase(MeasureTemperature.MEASURE_TEMPERATURE_CELSIUS))
			toView = String.format(Services.Strings.getResource(R.string.GXM_ConvertTo), MeasureTemperature.MEASURE_TEMPERATURE_FAHRENHEIT);
		else if (mCurrentUnit.equalsIgnoreCase(MeasureTemperature.MEASURE_TEMPERATURE_FAHRENHEIT))
			toView = String.format(Services.Strings.getResource(R.string.GXM_ConvertTo), MeasureTemperature.MEASURE_TEMPERATURE_CELSIUS);
		return toView;
	}

	@Override
	public double getValueKey(double valueKey, String valueUnitKey) {
		setValueUnitKey(valueUnitKey);
		mValue =  valueKey;
		return mValue;
	}

	private void setValueUnitKey(String valueUnitKey) {
		if (valueUnitKey.equalsIgnoreCase(mPrefixCelcius)) {
			mUnit = MeasureTemperature.MEASURE_TEMPERATURE_CELSIUS;
		} else if (valueUnitKey.equalsIgnoreCase(mPrefixFahrenheit)) {
			mUnit = MeasureTemperature.MEASURE_TEMPERATURE_FAHRENHEIT;
		} else {
			//the default value Celsius
			mUnit = MeasureTemperature.MEASURE_TEMPERATURE_CELSIUS;
		}
	}
	
	@Override
	public void changeValue(int currentItemNumeric, int currentItemDecimal) {
		mCurrentValue = getCurreentValue(currentItemNumeric, currentItemDecimal);
		if (mCurrentUnit.equalsIgnoreCase(MeasureTemperature.MEASURE_TEMPERATURE_CELSIUS)) {
			//Celsius to Fahrenheit
			mCurrentValue = changeCelsiusToFahrenheit(mCurrentValue);
			mCurrentUnit = MeasureTemperature.MEASURE_TEMPERATURE_FAHRENHEIT;
		} else if (mCurrentUnit.equalsIgnoreCase(MeasureTemperature.MEASURE_TEMPERATURE_FAHRENHEIT)) {
			//Fahrenheit to Celsius
			mCurrentValue = changeFahrenheitToCelsius(mCurrentValue);
			mCurrentUnit = MeasureTemperature.MEASURE_TEMPERATURE_CELSIUS;
		}
	}

	@Override
	public void setValueInWheelControl(GxMeasuresControl linearLayout) {
		double value = mCurrentValue;
		if (mCurrentUnit.equalsIgnoreCase(MeasureTemperature.MEASURE_TEMPERATURE_CELSIUS)) {
			linearLayout.setWheelControlViewAdapter(-40, 199, 0, 9, GxMeasuresHelper.getNumericByDouble(value) + 40, GxMeasuresHelper.getDecimalByDouble(value));
		} else if (mCurrentUnit.equalsIgnoreCase(MeasureTemperature.MEASURE_TEMPERATURE_FAHRENHEIT)) {
			linearLayout.setWheelControlViewAdapter(-40, 391, 0, 9, GxMeasuresHelper.getNumericByDouble(value) + 40, GxMeasuresHelper.getDecimalByDouble(value));
		} else {
			linearLayout.setWheelControlViewAdapter(-40, 199, 0, 9, GxMeasuresHelper.getNumericByDouble(value) + 40, GxMeasuresHelper.getDecimalByDouble(value));
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
