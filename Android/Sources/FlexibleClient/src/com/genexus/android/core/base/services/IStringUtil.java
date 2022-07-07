package com.genexus.android.core.base.services;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;

public interface IStringUtil
{
	/**
     * Return the string value associated with a particular resource ID.
	 */
	String getResource(int resourceId);

	/**
     * Return the string value associated with a particular resource ID,
     * substituting the format arguments as defined in {@link java.util.Formatter}
     * and {@link java.lang.String#format}.
	 */
	String getResource(int resourceId, Object... formatArgs);

    /**
     * <p>Repeat a String {@code repeat} times to form a new String.</p>
     *
     * <pre>
     * StringUtils.repeat(null, 2) = null
     * StringUtils.repeat("", 0)   = ""
     * StringUtils.repeat("", 2)   = ""
     * StringUtils.repeat("a", 3)  = "aaa"
     * StringUtils.repeat("ab", 2) = "abab"
     * StringUtils.repeat("a", -2) = ""
     * </pre>
     *
     * @param str  the String to repeat, may be null
     * @param repeat  number of times to repeat str, negative treated as zero
     * @return a new String consisting of the original String repeated,
     *  {@code null} if null String input
     */
    String repeat(final String str, final int repeat);

	/**
	 * Splits this string using the supplied separator.
	 * Unlike Java's String.split(), separator is <em>not</em> treated as a regular expression.
	 * @param str Input string
	 * @param separator Character to use as separator.
	 */
	String[] split(String str, char separator);

	/**
	 * Splits this string using the supplied separator.
	 * Unlike Java's String.split(), separator is <em>not</em> treated as a regular expression.
	 * @param str Input string.
	 * @param separator String to use as separator.
	 */
	String[] split(String str, String separator);

	String join(Iterable<?> components, String separator);

	/**
	 * Encodes a list of strings into a single string (suitable for serialization).
	 */
	String encodeStringList(List<String> list, char separator);

	/**
	 * Decodes a string (created via encodeStringList) into a list of strings.
	 */
	List<String> decodeStringList(String encoded, char separator);

	String convertStreamToString(InputStream stream);

	long valueOf(String str);

	/**
	 * Given a string and a format, it returns whether or not the string represents a valid date.
	 * @return true if str is parsed successfully according to the format passed.
	 */
	boolean isDateFormatValid(String str, String format);

	Date getDate(String dateString);
	Date getDateTime(String dateTimeString);
	Date getDateTime(String dateTimeString, boolean shouldNotConvertOnlyTime, boolean hasSeconds, boolean hasMilliseconds);

	String getDateString(Date date, String inputType);
	String getDateTimeString(Date dateTime, String inputType);
	String getTimeString(Date time, String inputType);

	String getDateTimeStringForServer(Date date);
	String getDateTimeStringForServer(Date date, boolean shouldNotConvertOnlyTime, boolean hasMilliseconds);
	String getDateStringForServer(Date date);

	boolean hasValue(CharSequence str);

	/**
	 * Returns true if the supplied string is a representation of a true value
	 * (such as "True", "true", "1", &c), false when it's a representation of a false value,
	 * and null otherwise.
	 */
	Boolean tryParseBoolean(String str);

	/**
	 * Returns true if the supplied string is a representation of a true value
	 * (such as "True", "true", "1", &c), false when it's a representation of a false value,
	 * and the supplied default value otherwise.
	 */
	boolean tryParseBoolean(String str, boolean defaultValue);

	/**
	 * Returns true if the supplied string is a representation of a true value
	 * (such as "True", "true", "1", &c), false otherwise.
	 */
	boolean parseBoolean(String str);

	/**
	 * Similar to Integer.Parse(str) but will return null instead of throwing an exception if the
	 * string does not represent an integer.
	 */
	Integer tryParseInt(String str);
	int tryParseInt(String str, int defaultValue);

	/**
	 * Returns the double value if the supplied string is a representation of a double value,
	 * and null otherwise.
	 */
	Double tryParseDouble(String str);

	/**
	 * Returns the double value if the supplied string is a representation of a double value,
	 * and the supplied default value otherwise.
	 */
	double tryParseDouble(String str, double defaultValue);

	/**
	 * Returns the double value if the supplied string is a representation of a float value,
	 * and null otherwise.
	 */
	Float tryParseFloat(String str);

	/**
	 * Returns the double value if the supplied string is a representation of a float value,
	 * and the supplied default value otherwise.
	 */
	float tryParseFloat(String str, float defaultValue);

	/**
	 * Returns the index of the value among all the supplied options (0-based, case-insensitive comparison).
	 * @return The index of the value, or -1 if empty, null, or not present.
	 */
	int parseIntEnum(String value, String... options);

	/**
	 * Returns the index of the value among all the supplied options (0-based, case-insensitive comparison).
	 * @return The index of the value, or the value of defaultOption if empty, null, or not present.
	 */
	int parseIntEnum(String value, int defaultOption, String... options);

	Integer parseMeasureValue(String str, String unit);

	/**
	 * Returns the long value if the supplied string is a representation of a long value,
	 * and null otherwise.
	 */
	Long tryParseLong(String str);

	/**
	 * Returns the long value if the supplied string is a representation of a long value,
	 * and the supplied default value otherwise.
	 */
	long tryParseLong(String optStringProperty, long defaultValue);

	/**
	 * Returns the BigDecimal value if the supplied string is a representation of a BigDecimal value,
	 * and null otherwise.
	 */
	BigDecimal tryParseDecimal(String str);

	/**
	 * Returns the BigDecimal value if the supplied string is a representation of an UUID,
	 * and null otherwise.
	 */
	UUID tryParseGuid(String str);

	/**
	 * Gets an hexadecimal string representing the SHA-256 hash of the supplied string.
	 */
	String getStringHash(String value);

	/**
	 * Returns a marked-up CharSequence assuming that the input is HTML text.
	 */
	@NonNull CharSequence fromHtml(@NonNull String source);

	/**
	 * Tries to guess whether the supplied string contains HTML markup tags or not.
	 * If so, it is converted to a marked-up CharSequence, otherwise it is returned "as is".
	 * Should be used whenever we are not sure whether it's actually HTML or not.
	 */
	@NonNull CharSequence attemptFromHtml(@NonNull String source);
}
