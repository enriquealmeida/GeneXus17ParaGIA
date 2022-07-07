package com.genexus.android.core.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import android.content.Context;
import androidx.annotation.NonNull;
import android.text.Html;

import com.genexus.android.core.base.metadata.BasicDataType;
import com.genexus.android.core.base.services.IStringUtil;
import com.genexus.android.core.base.services.base.BaseStringService;
import com.genexus.android.core.base.utils.HexEncoder;
import com.genexus.android.core.base.utils.Strings;

import org.apache.commons.io.IOUtils;

@SuppressWarnings({"deprecation", "DateFormatConstant"})
class StringUtil extends BaseStringService implements IStringUtil {
	private static final String DATE_PATTERN = "yyyy-MM-dd";
	private static final String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
	private static final String TIMESTAMP_PATTERN_NO_SECONDS = "yyyy-MM-dd HH:mm";
	private static final String TIMESTAMP_PATTERN_NO_MILLIS = "yyyy-MM-dd HH:mm:ss";
	private static final String HTTP_TIMESTAMP_PATTERN = "EEE, dd MMM yyyy HH:mm:ss z";

	private final Context mAppContext;

	public StringUtil(Context context) {
		mAppContext = context;
	}

	@Override
	public String getResource(int resourceId) {
		return mAppContext.getResources().getString(resourceId);
	}

	@Override
	public String getResource(int resourceId, Object... formatArgs) {
		return mAppContext.getResources().getString(resourceId, formatArgs);
	}

	@Override
	public String convertStreamToString(InputStream is) {
		final int BUFFER_SIZE = 8192; // Default value, just to avoid warning in emulator.

		/*
		 * To convert the InputStream to String we use the BufferedReader.readLine()
		 * method. We iterate until the BufferedReader return null which means
		 * there's no more data to read. Each line will appended to a StringBuilder
		 * and returned as String.
		 */
		if (is != null) {
			StringBuilder sb = new StringBuilder();
			String line;

			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8), BUFFER_SIZE);
				while ((line = reader.readLine()) != null) {
					sb.append(line).append("\n");
				}
			} catch (IOException e) {
			} finally {
				IOUtils.closeQuietly(is);
			}
			return sb.toString();
		} else {
			return Strings.EMPTY;
		}
	}

	private static String joinS(Iterable<?> pColl, String separator) {
		Iterator<?> oIter;
		if (pColl == null || !(oIter = pColl.iterator()).hasNext())
			return Strings.EMPTY;

		StringBuilder oBuilder = new StringBuilder(String.valueOf(oIter.next()));
		while (oIter.hasNext())
			oBuilder.append(separator).append(oIter.next());

		return oBuilder.toString();
	}

	private SimpleDateFormat getDateFormat() {
		return new SimpleDateFormat(DATE_PATTERN, Locale.US);
	}

	private SimpleDateFormat getDateTimeFormatMinutes() {
		return new SimpleDateFormat(TIMESTAMP_PATTERN_NO_SECONDS, Locale.US);
	}

	private SimpleDateFormat getDateTimeFormatSeconds() {
		return new SimpleDateFormat( TIMESTAMP_PATTERN_NO_MILLIS, Locale.US);
	}

	private SimpleDateFormat getDateTimeFormatMilliseconds() {
		return new SimpleDateFormat(TIMESTAMP_PATTERN, Locale.US);
	}

	@Override
	public boolean isDateFormatValid(String str, String format) {
		if (str == null) {
			return false;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
		dateFormat.setLenient(false);

		try {
			dateFormat.parse(str);
		} catch (ParseException e) {
			return false;
		}

		return true;
	}

	@Override
	public Date getDate(String value) {
		Date date = new Date();
		if (value != null && value.equalsIgnoreCase(BasicDataType.NULL_DATE)) {
			date = null;
		} else if (value != null) {
			ParsePosition pos = new ParsePosition(0);
			date = getDateFormat().parse(value, pos);
		}
		return date;
	}

	@Override
	public Date getDateTime(String value) {
		return getDateTime(value, false, true, false);
	}

	@Override
	public Date getDateTime(String value, boolean shouldNotConvertOnlyTime, boolean hasSeconds, boolean hasMilliseconds) {
		// If we get passed a DATE string (i.e. "2001-11-30"), parse it as Date.
		// Before this change it was returning null because a date string does not match the expected format.
		if (value != null && value.length() == BasicDataType.NULL_DATE.length())
			return getDate(value);

		Date date = new Date();

		// A DateTime of 00/00/00 00:00 is considered null. However, 00:00 is a valid TIME (the date is not important).
		if (value != null && !shouldNotConvertOnlyTime &&
				(value.equalsIgnoreCase(BasicDataType.NULL_DATETIME) || value.equalsIgnoreCase(BasicDataType.NULL_DATETIME_MILLISECONDS))) {
			date = null;
		} else if (value != null) {
			value = value.replace("0001-", "1970-");
			SimpleDateFormat formatter = getDateTimeFormatMinutes();
			if (hasMilliseconds && value.contains(".")) //Picture may have millis but retrieved value may not
				formatter = getDateTimeFormatMilliseconds();
			else if (hasSeconds)
				formatter = getDateTimeFormatSeconds();

			ParsePosition pos = new ParsePosition(0);
			date = formatter.parse(value, pos);
			if (date == null) {
				value = value.replace("T", Strings.SPACE);
				pos = new ParsePosition(0);
				date = formatter.parse(value, pos);
			}

			// Convert From UTC To Local Time
			if (date != null && useUtcConversion() && !shouldNotConvertOnlyTime) {
				long offset = TimeZone.getDefault().getOffset(date.getTime());
				date.setTime(date.getTime() + offset);
			}
		}
		return date;
	}

	@Override
	public String getDateString(Date date, String inputType) {
		if (date == null || !inputType.contains("/")) {
			return Strings.EMPTY;
		}

		String datePicture = inputType.split(Strings.SPACE, 2)[0];

		// Get the device's preferences for displaying the date.
		SimpleDateFormat preferredDateFormat = (SimpleDateFormat) android.text.format.DateFormat.getDateFormat(mAppContext);

		// Convert the pattern accordingly.
		String dateFormat = DateTimeFormatter.getDatePattern(datePicture, preferredDateFormat.toLocalizedPattern());

		return android.text.format.DateFormat.format(dateFormat, date).toString();
	}

	@Override
	public String getTimeString(Date date, String inputType) {
		if (date == null) {
			return Strings.EMPTY;
		}

		int timePictureIndex = inputType.contains(Strings.SPACE) ? 1 : 0;
		String timePicture = inputType.split(Strings.SPACE, 2)[timePictureIndex];

		// Get the device's preferences for displaying the time.
		boolean is24HourFormat = android.text.format.DateFormat.is24HourFormat(mAppContext);

		// Convert the pattern accordingly.
		String timeFormat = DateTimeFormatter.getTimePattern(timePicture, is24HourFormat);

		if (timeFormat.contains(".SSS")) {
			SimpleDateFormat formatMilliseconds = new SimpleDateFormat(timeFormat);
			return formatMilliseconds.format(date);
		} else {
			return android.text.format.DateFormat.format(timeFormat, date).toString();
		}
	}

	@Override
	public String getDateTimeString(Date date, String inputType) {
		String dateString = getDateString(date, inputType);
		String timeString = getTimeString(date, inputType);

		String dateTimeString;

		if (Strings.hasValue(dateString)) {
			dateTimeString = dateString + Strings.SPACE + timeString;
		} else {
			dateTimeString = timeString;
		}

		return dateTimeString;
	}

	@Override
	public String getDateStringForServer(Date date) {
		if (date == null)
			return BasicDataType.NULL_DATE;

		return getDateFormat().format(date);
	}

	@Override
	public String getDateTimeStringForServer(Date date) {
		return getDateTimeStringForServer(date, false, false);
	}

	@Override
	public String getDateTimeStringForServer(Date date, boolean shouldNotConvertOnlyTime, boolean hasMilliseconds) {
		if (date == null)
			return BasicDataType.NULL_DATETIME;

		// Make a copy of the Date object, since it may be modified below.
		date = new Date(date.getTime());

		// Convert From Local Time to UTC
		if (useUtcConversion()) {
			if (!shouldNotConvertOnlyTime) {
				long offset = TimeZone.getDefault().getOffset(date.getTime());
				date.setTime(date.getTime() - offset);
			} else {
				//reset date
				date = com.genexus.GXutil.resetDate(date);
			}
		}

		if (hasMilliseconds)
			return getDateTimeFormatMilliseconds().format(date);
		else
			return getDateTimeFormatSeconds().format(date);
	}

	@Override
	public String join(Iterable<?> components, String separator) {
		return StringUtil.joinS(components, separator);
	}

	@Override
	public String[] split(String str, char separator) {
		// Special case optimization.
		if (str.indexOf(separator) == -1)
			return new String[]{str};

		return split(str, Character.toString(separator));
	}

	@Override
	public String[] split(String str, String separator) {
		return str.split(Pattern.quote(separator));
	}

	@Override
	public long valueOf(String val) {
		return Long.parseLong(val);
	}

	private static DateFormat getHttpDateFormat() {
		// According to http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.3.1
		SimpleDateFormat dateFormat = new SimpleDateFormat(HTTP_TIMESTAMP_PATTERN, Locale.ENGLISH);
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		return dateFormat;
	}

	public static Date dateFromHttpFormat(String dateString) {
		try {
			return getHttpDateFormat().parse(dateString);
		} catch (ParseException | NumberFormatException ex) {
			return new Date(0);
		}
	}

	public static String dateToHttpFormat(Date date) {
		// For some reason, the conversion adds "GMT+00:00" to the end. We want just the "GMT" part.
		String dateString = getHttpDateFormat().format(date);
		return dateString.replace("GMT+00:00", "GMT");
	}

	public static String timeZoneOffsetID() {
		return TimeZone.getDefault().getID();
	}

	@Override
	public String getStringHash(String value) {
		MessageDigest digest;
		byte[] hashBytes;

		try {
			// Do NOT cache MessageDigest in a member variable. Instances of this class are not thread safe.
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("SHA-256 algorithm not found? This should never happen.", e);
		}

		hashBytes = digest.digest(value.getBytes(StandardCharsets.UTF_8));

		// Convert the hash bytes to a readable hexadecimal string.
		return HexEncoder.toHex(hashBytes);
	}

	@Override
	public @NonNull CharSequence fromHtml(@NonNull String source) {
		return Html.fromHtml(source);
	}

	@Override
	public @NonNull CharSequence attemptFromHtml(@NonNull String source) {
		// We cannot call fromHtml() directly because if the input is not actually HTML
		// then some characters can be lost, in particular chevrons and newlines.
		// So, we try to "guess" whether the input is indeed HTML or not.
		// If we decide it's not HTML then we don't try to parse it as HTML
		if (isHtmlSource(source))
			return fromHtml(source);
		else
			return source;
	}

	private static final String[] KNOWN_HTML_SIMPLE_TAGS = new String[]
			{"b", "big", "blockquote", "br", "cite", "dfn", "em", "h1", "h2", "h3", "h4", "h5",
					"h6", "i", "p", "small", "strike", "strong", "sub", "sup", "tt", "u"};

	private static final String[] KNOWN_HTML_COMPLEX_TAGS = new String[]
			{"a", "div", "font", "img"};

	/**
	 * Try to "guess" whether the input is HTML or not, by searching for some known
	 * HTML tags (this is also the set of tags that are processes by Html.fromHtml()).
	 */
	private static boolean isHtmlSource(@NonNull String str) {
		if (str.length() == 0)
			return false;

		String strLower = Strings.toLowerCase(str);

		for (String tag : KNOWN_HTML_SIMPLE_TAGS) {
			if (strLower.contains("<" + tag + ">"))
				return true;
		}

		for (String tag : KNOWN_HTML_COMPLEX_TAGS) {
			if (strLower.contains("<" + tag + " "))
				return true;
		}

		return false;
	}
}
