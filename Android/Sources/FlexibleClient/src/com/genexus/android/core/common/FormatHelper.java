package com.genexus.android.core.common;

import java.math.BigDecimal;
import java.util.Date;

import com.genexus.android.core.base.metadata.DataItem;
import com.genexus.android.core.base.metadata.enums.DataTypes;
import com.genexus.android.core.base.metadata.expressions.ExpressionFormatHelper;
import com.genexus.android.core.base.services.IValuesFormatter;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.ParametersStringUtil;
import com.genexus.android.core.base.utils.Strings;

public class FormatHelper
{
	public static IValuesFormatter getFormatter(DataItem dataItem)
	{
		return new Formatter(dataItem);
	}

	private static class Formatter implements IValuesFormatter
	{
		private final DataItem mDefinition;

		private Formatter(DataItem definition)
		{
			mDefinition = definition;
		}

		@Override
		public CharSequence format(String value)
		{
			return formatValue(value, mDefinition);
		}

		@Override
		public boolean needsAsync() { return false; }
	}

	public static CharSequence formatValue(String value, DataItem dataItem)
	{
		if (value == null)
			return Strings.EMPTY;

		if (dataItem == null)
			return value;

		if (dataItem.getIsEnumeration())
			return ParametersStringUtil.getDescriptionOfEnum(dataItem.getBaseType().getEnumValues(), value);

		String type = dataItem.getDataTypeName().getDataType();
		String picture = dataItem.getInputPicture();
		int length = dataItem.getLength();
		int decimals = dataItem.getDecimals();
		return formatValue(value, type, picture, length, decimals);
	}

	public static CharSequence formatValue(String value, String type, String picture, int length, int decimals)
	{
		if (value != null)
		{
			if (type.equals(DataTypes.DATE))
			{
				Date date = Services.Strings.getDate(value);
				return formatDate(date, type, picture);
			}
			else if (DataTypes.isTime(type, length))
			{
				Date time = Services.Strings.getDateTime(value, true, decimals>=8, decimals==12);
				return formatDate(time, type, picture);
			}
			else if (type.equals(DataTypes.DTIME) || type.equals(DataTypes.DATETIME))
			{
				Date dateTime = Services.Strings.getDateTime(value, false, decimals>=8, decimals==12);
				return formatDate(dateTime, type, picture);
			}
			else if (type.equals(DataTypes.NUMERIC))
			{
				return formatNumber(value, picture);
			}
		}

		return value;
	}

	private static String formatNumber(String value, String picture)
	{
		if (!Services.Strings.hasValue(picture))
			return value;

		if (!Services.Strings.hasValue(value))
			value = Strings.ZERO;

		try
		{
			BigDecimal numericValue = new BigDecimal(value);
			return formatNumber(numericValue, picture);
		}
		catch (NumberFormatException ex)
		{
			Services.Log.warning(String.format("Unexpected numeric value: '%s'.", value), ex);
			return value;
		}
	}

	public static String formatNumber(BigDecimal value, String picture)
	{
		if (value == null)
			value = BigDecimal.ZERO;

		String str = ExpressionFormatHelper.formatNumber(value, picture);

		// Trim left spaces, looks better in Android that way.
		return str.trim();
	}

	public static String formatDate(Date date, String type, String picture)
	{
		if (type.equals(DataTypes.DATE))
		{
			return Services.Strings.getDateString(date, picture);
		}
		else if (type.equals(DataTypes.DTIME) || type.equals(DataTypes.DATETIME))
		{
			return Services.Strings.getDateTimeString(date, picture);
		}
		else if (type.equals(DataTypes.TIME))
		{
			return Services.Strings.getTimeString(date, picture);
		}
		else
		{
			// Default = date.
			return Services.Strings.getDateString(date, picture);
		}
	}
}
