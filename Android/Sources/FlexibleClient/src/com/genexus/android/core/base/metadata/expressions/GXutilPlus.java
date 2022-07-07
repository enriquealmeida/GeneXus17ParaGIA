package com.genexus.android.core.base.metadata.expressions;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.util.Pair;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.GeoFormats;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.maps.common.MapLayer;
import com.genexus.DecimalUtil;
import com.genexus.GXutil;
import com.genexus.LocalUtil;

import androidx.annotation.NonNull;

/**
 * Additional functions for which the GX Java Generator generates code instead.
 * Having them as functions with no additional parameters simplifies FunctionHelper.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
class GXutilPlus
{
	private static LocalUtil sLocalUtil;
	private static String sLocalUtilLanguage;

	static LocalUtil getLocalUtil()
	{
		// Check that language hasn't changed since the LocalUtil object was created.
		if (sLocalUtil != null && sLocalUtilLanguage != null && !sLocalUtilLanguage.equals(Services.Language.getCurrentLanguage()))
			sLocalUtil = null;

		if (sLocalUtil == null)
		{
			String decimalPoint = getLanguageProperty("LangDecimalPoint", ".");
			String dateFormat =  mapLanguageDateFormatToLocalUtilDateFormat(getLanguageProperty("LangDateFormat", "ENG"));
			String timeFormat = getLanguageProperty("LangTimeFormat", "24");
			int firstYear2K = 40; // TODO: This should be read from metadata too.

			String languageCode = Services.Language.getCurrentLanguage();
			if (Strings.hasValue(languageCode))
				languageCode = Strings.toLowerCase(languageCode.substring(0, 3));
			else
				languageCode = "eng";

			sLocalUtil = LocalUtil.getLocalUtil(decimalPoint.charAt(0), dateFormat, timeFormat, firstYear2K, languageCode);
			sLocalUtilLanguage = Services.Language.getCurrentLanguage();
		}

		return sLocalUtil;
	}

	private static String getLanguageProperty(String property, String defaultValue)
	{
		String value = Services.Language.getCurrentLanguageProperty(property);
		if (!Strings.hasValue(value))
			value = defaultValue;

		return value;
	}

	public static long decimalToInteger(BigDecimal value)
	{
		return value.longValue();
	}

	public static UUID emptyGuid()
	{
		return new UUID(0, 0);
	}

	public static String functionRGB(int value1, int value2, int value3)
	{
		return String.format("#%02x%02x%02x", value1, value2, value3);
	}

	public static String guidToString(UUID value)
	{
		return (value != null ? value.toString() : Strings.EMPTY);
	}

	public static boolean isEmptyBoolean(boolean value)
	{
		//noinspection PointlessBooleanExpression
		return (value == false);
	}

	public static boolean setEmptyBoolean(boolean value)
	{
		return false;
	}

	public static boolean isEmptyDate(Date value)
	{
		return GXutil.nullDate().equals(value);
	}

	public static Date setEmptyDate(Date date)
	{
		return GXutil.nullDate();
	}

	public static boolean isEmptyGuid(UUID value)
	{
		return (new UUID(0L, 0L).equals(value));
	}

	public static UUID setEmptyGuid(UUID value)
	{
		return new UUID(0L, 0L);
	}

	public static boolean isEmptyNumber(BigDecimal value)
	{
		return (DecimalUtil.ZERO.compareTo(value) == 0);
	}

	public static BigDecimal setEmptyNumber(BigDecimal value)
	{
		return DecimalUtil.ZERO;
	}

	public static boolean isEmptyNumber(int value)
	{
		return (value == 0);
	}

	public static int setEmptyNumber(int value)
	{
		return 0;
	}

	public static boolean isEmptyString(String value)
	{
		return (GXutil.strcmp("", value) == 0);
	}

	public static String setEmptyString(String value)
	{
		return "";
	}

	public static String strIdentity(String value)
	{
		return value;
	}

	public static Date addDays(Date date, int days)
	{
		return GXutil.dtadd(date, days * 86400);
	}

	public static Date addHours(Date date, int hours)
	{
		return GXutil.dtadd(date, hours * 3600);
	}

	public static Date addMinutes(Date date, int minutes)
	{
		return GXutil.dtadd(date, minutes * 60);
	}

	public static String padl2(String text, int size)
	{
		return GXutil.padl(text, size, " ");
	}

	public static String padr2(String text, int size)
	{
		return GXutil.padr(text, size, " ");
	}

	public static String str1(BigDecimal value)
	{
		return str2(value, 10);
	}

	public static String str2(BigDecimal value, int length)
	{
		return GXutil.str(value, length, 0);
	}

	public static String substring2(String text, int start)
	{
		return GXutil.substring(text, start, -1);
	}

	public static int strSearch2(String a, String b)
	{
		return GXutil.strSearch(a, b, 1);
	}

	public static int strSearchRev2(String a, String b)
	{
		return GXutil.strSearchRev(a, b, -1);
	}

	public static String dtoc1(Date date)
	{
		// From evalfng.ari, "/" is fixed. Others depend on language.
		return getLocalUtil().dtoc(date, getDateFormat(), "/");
	}

	public static String ttoc1(Date d)
	{
		return ttoc2(d, 10);
	}

	public static String ttoc2(Date d, int dateLength)
	{
		return ttoc3(d, dateLength, 8);
	}

	public static String ttoc3(Date d, int dateLength, int timeLength)
	{
		// From evalfng.ari, "/" ":" " " are fixed. Others depend on language.
		return getLocalUtil().ttoc(d, dateLength, timeLength, getTimeFormat(), getDateFormat(), "/", ":", " ");
	}

	public static long strToInteger(String str)
	{
		return decimalToInteger(strToNumber(str));
	}

	public static @NonNull BigDecimal strToNumber(String str)
	{
		return strToNumber(str, ".");
	}

	public static @NonNull BigDecimal strToNumber(String str, String decimalSeparator)
	{
		if (!Strings.hasValue(str))
			return BigDecimal.ZERO;

		try
		{
			return new BigDecimal(str.trim());
		}
		catch (NumberFormatException e)
		{
			char separator = '.';
			if (decimalSeparator != null && decimalSeparator.length() == 1)
				separator = decimalSeparator.charAt(0);

			// Remove all chars except for numbers, decimal separator, and sign.
			StringBuilder cleanedStr = new StringBuilder(str.length());
			for (int i = 0; i < str.length(); i++)
			{
				char c = str.charAt(i);
				if (c == '-' && i == 0)
					cleanedStr.append('-');
				else if (!Strings.isNumeric(String.valueOf(c)))
					break;
				else if (c >= '0' && c <= '9')
					cleanedStr.append(c);
				else if (c == separator && cleanedStr.indexOf(String.valueOf(separator)) == -1)
					cleanedStr.append('.'); // use . as separator for BigDecimal parse
			}

			try
			{
				return new BigDecimal(cleanedStr.toString());
			}
			catch (NumberFormatException ex)
			{
				Services.Log.debug("stringToNumeric", String.format("Could not parse number from %s even after cleaning it down to %s", str, cleanedStr), ex);
				return BigDecimal.ZERO;
			}
		}
	}


	private static String mapLanguageDateFormatToLocalUtilDateFormat(String df)
	{
		// Copied from date_format_from_language/2 in dtfmt.ari
		if (Strings.hasValue(df))
		{
			if (df.equalsIgnoreCase("ENG"))
				return "MDY";
			else if (df.equalsIgnoreCase("SPA") || df.equalsIgnoreCase("ITA") || df.equalsIgnoreCase("POR"))
				return "DMY";
			else if (df.equalsIgnoreCase("CHS") || df.equalsIgnoreCase("CHT") || df.equalsIgnoreCase("JAP"))
				return "YMD";
			else if (df.equalsIgnoreCase("ANSI"))
				return "YMD";
		}

		return "DMY";
	}

	private static int getDateFormat()
	{
		String dateFormat = Services.Language.getCurrentLanguageProperty("LangDateFormat");
		dateFormat = mapLanguageDateFormatToLocalUtilDateFormat(dateFormat);
		return getLocalUtil().mapDateFormat(dateFormat);
	}

	private static int getTimeFormat()
	{
		String timeFormat = Services.Language.getCurrentLanguageProperty("LangTimeFormat");
		return (timeFormat != null && timeFormat.equals("12") ? 1 : 0);
	}

	public static String getDecimalSeparator()
	{
		String decimalSeparator = Services.Language.getCurrentLanguageProperty("LangDecimalPoint");
		if (Services.Strings.hasValue(decimalSeparator))
			return decimalSeparator; // decimal separator from language
		else
			return Strings.DOT; // default separator
	}

	public static String strToGeoPoint(String str) {
		return parseGeography(str, MapLayer.FeatureType.Point);
	}

	public static String strToGeoLine(String str) {
		return parseGeography(str, MapLayer.FeatureType.Polyline);
	}

	public static String strToGeoPolygon(String str) {
		return parseGeography(str, MapLayer.FeatureType.Polygon);
	}

	public static String strToGeography(String str) {
		MapLayer.FeatureType type = GeoFormats.guessFeatureType(str);
		if (type != null)
			return parseGeography(str, type);

		return Strings.EMPTY;
	}

	private static String parseGeography(String geo, MapLayer.FeatureType type) {
		if (!Strings.hasValue(geo))
			return Strings.EMPTY;

		List<Pair<Double, Double>> geography = GeoFormats.tryParse(geo);
		return GeoFormats.buildGeography(geography, type);
	}

	public static String createFromUrl(String str)
	{
		return str;
	}
}
