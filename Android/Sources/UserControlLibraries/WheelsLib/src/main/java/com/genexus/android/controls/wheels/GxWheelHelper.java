package com.genexus.android.controls.wheels;

import java.math.BigDecimal;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

public class GxWheelHelper {

	public static String getCorrectWheelValue(String strWheelValue, String strStepValue) {
		String [] strStepValueSplit = Services.Strings.split(strStepValue,Strings.DOT);
		if (strWheelValue.contains(Strings.DOT)) {
			String [] strValue = Services.Strings.split(strWheelValue,Strings.DOT);
			strWheelValue = strValue[0].concat(Strings.DOT).concat(addZerosDecimal(strValue[1], String.valueOf(strStepValueSplit[1]), false));
		} else {
			strWheelValue = strWheelValue.concat(Strings.DOT).concat(addZerosDecimal(Strings.ZERO, String.valueOf(strStepValueSplit[1]), false));
		}
		return strWheelValue;
	}

    public static String addZerosDecimal(String value, String stepValue, boolean forward) {
    	int lengthValue = value.length();
    	int lengthDecimals = stepValue.length();
    	if (lengthValue > lengthDecimals) {
    		value = round(Double.valueOf(value),lengthDecimals).toString();
    		lengthValue = value.length();
    	}
    	while (lengthValue < lengthDecimals) {
    		if (forward)
    			value = Strings.ZERO.concat(value);
    		else
    			value = value.concat(Strings.ZERO);
    		lengthValue = value.length();
    	}
    	return value;
    }

    private static BigDecimal round(double value, int decimals)
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

    public static int getPositionValue(String value, String[] itemsValue) {
		int start = 0;
		int end = itemsValue.length - 1;
		int position;

		Integer intValue = Services.Strings.tryParseInt(value);
		if (intValue == null)
			return start;

		 while (start <= end) {
			 position = (start + end) / 2;
		     if (itemsValue[position].equalsIgnoreCase(value))
		    	 return position;
		     else if (itemsValue[start].equalsIgnoreCase(value))
		    	 return start;
		     else if (itemsValue[end].equalsIgnoreCase(value))
		    	 return end;
		     else if (Integer.parseInt(itemsValue[position]) < intValue)
		    	 start = position + 1;
		     else
		    	 end = position - 1;
		 }
		 if (start >= itemsValue.length)
			 return itemsValue.length -1;
		 return start;
	}

    public static int getPositionValue(String[] arrayItems, String value) {
		for (int i = 0; i < arrayItems.length; i++) {
			if (arrayItems[i].equalsIgnoreCase(value)) {
				return i;
			}
		}
		return 0;
	}

}
