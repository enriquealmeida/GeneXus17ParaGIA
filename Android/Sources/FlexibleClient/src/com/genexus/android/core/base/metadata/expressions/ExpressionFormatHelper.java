package com.genexus.android.core.base.metadata.expressions;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.GXutil;

public class ExpressionFormatHelper
{
	static String toString(Expression.Value value)
	{
		Expression.Type type = value.getType();

		// &Numeric.ToString() is implemented as a special case here.
		// For all other types, the mapped functions are used instead.
		if (type.isNumeric()) {
			int length;
			int decimals;
			BigDecimal number = value.coerceToNumber();

			if (value.getDefinition() != null && value.getDefinition().getBaseType() != null) {
				length = value.getDefinition().getBaseType().getLength();
				decimals = value.getDefinition().getBaseType().getDecimals();
			} else {
				// If we don't have a definition, use as many digits as necessary to fit the number.
				length = number.precision() + 1; // +1 for the decimal dot.
				decimals = number.scale();
			}

			return GXutil.str(number, length, decimals);
		} else if (type.isDateTime()) {
			return GXutilPlus.getLocalUtil().format(value.coerceToDate(), value.getPicture());
		}

		throw new IllegalArgumentException(String.format("Unexpected type for ToString in ExpressionFormatHelper (%s).",type));
	}

	public static String toFormattedString(Expression.Value value)
	{
		Expression.Type type = value.getType();

		if (type == Expression.Type.STRING)
			return value.coerceToString();

		if (type.isNumeric())
			return formatNumber(value.coerceToNumber(), value.getPicture());

		if (type.isDateTime())
			return GXutilPlus.getLocalUtil().format(value.coerceToDate(), value.getPicture());

		if (type == Expression.Type.BOOLEAN)
			return GXutil.booltostr(value.coerceToBoolean());

		if (type == Expression.Type.GUID || type == Expression.Type.GEOPOINT || type == Expression.Type.GEOLINE || type == Expression.Type.GEOPOLYGON ||
			type == Expression.Type.GEOGRAPHY || type == Expression.Type.IMAGE)
			return value.coerceToString();

		throw new IllegalArgumentException(String.format("Unexpected type for ToFormattedString (%s).",type));
	}

	public static String toUrlParameterString(Expression.Value value)
	{
		Expression.Type type = value.getType();
		if (type.isDateTime()) {
			Date date = value.coerceToDate();
			SimpleDateFormat df;
			if (type == Expression.Type.DATE)
				df = new SimpleDateFormat("yyyyMMdd000000");
			else if (type == Expression.Type.TIME)
				df = new SimpleDateFormat("00010101HHmmss");
			else
				df = new SimpleDateFormat("yyyyMMddHHmmss");
			return df.format(date);
		}
		return value.coerceToString();
	}

	static String format(String formatString, List<Expression.Value> parameters)
	{
		String[] params = new String[9];
		Arrays.fill(params, Strings.EMPTY);
		for (int i = 0; i < parameters.size(); i++)
			params[i] = toFormattedString(parameters.get(i));

		return GXutil.format(formatString, params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8]);
	}

	public static String formatNumber(BigDecimal value, String picture)
	{
		return GXutilPlus.getLocalUtil().format(value, picture);
	}

	static String getDefaultPicture(Expression.Value value)
	{
		if (value.getType().isNumeric())
		{
			BigDecimal number = value.coerceToNumber().stripTrailingZeros();

			int length = number.precision();
			int decimals = number.scale();
			if (decimals != 0)
			{
				// Decimal picture. Z*9.9+
				int intLength = length - decimals;
				return Services.Strings.repeat("Z", intLength - 1) + "9" + "." + Services.Strings.repeat("9", decimals);
			}
			else
			{
				// Integer picture, Z*9
				return Services.Strings.repeat("Z", length - 1) + "9";
			}
		}
		else if (value.getType() == Expression.Type.DATE)
		{
			return "99/99/9999";
		}
		else if (value.getType() == Expression.Type.DATETIME)
		{
			return "99/99/9999 99:99:99";
		}
		else if (value.getType() == Expression.Type.TIME)
		{
			return "99:99:99";
		}

		throw new IllegalArgumentException(String.format("Cannot generate a default picture for value '%s'.", value));
	}

	public static @NonNull
	BigDecimal strToNumber(String str)
	{
		String decimalSeparator = GXutilPlus.getDecimalSeparator();
		return GXutilPlus.strToNumber(str, decimalSeparator);
	}
}
