package com.genexus.android.controls.wheels.measures;

import java.math.BigDecimal;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

public class GxMeasuresHelper {
	
	static final String UNIT_KEY = "Unit";
	static final String VALUE_KEY = "Value";
	static final String CONVERTED_VALUE_KEY = "ConvertedValue";
	
	static final class MeasureType {
		static final String HEIGHT = "Height";
		static final String WEIGHT = "Weight";
		static final String TEMPERATURE = "Temperature";
		static final String VOLUME = "Volume";
	}
	
	public static BigDecimal round(double value, int decimals)
	{
		BigDecimal valueBigDecimal = new BigDecimal(value);
		return valueBigDecimal.setScale(decimals, BigDecimal.ROUND_HALF_UP);
	}

	public static int getNumericByDouble(double value) {
		BigDecimal valueBigDecimal = new BigDecimal(value);
		return valueBigDecimal.setScale(0, BigDecimal.ROUND_DOWN).intValue();
	}

	public static int getDecimalByDouble(double value) {
		String [] strValue = Services.Strings.split(String.valueOf(value), Strings.DOT);
		if (strValue.length > 1)
			return Integer.parseInt(strValue[1]);
		return 0;
	}

}
