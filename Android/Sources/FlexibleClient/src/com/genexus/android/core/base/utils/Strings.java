package com.genexus.android.core.base.utils;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

public class Strings
{
	private Strings() { }

	public static final String EMPTY = "";
	public static final String SPACE = " ";
	public static final String DOT = ".";
	public static final String COMMA = ",";
	public static final String ZERO = "0";
	public static final String ONE = "1";
	public static final String QUESTION = "?";
	public static final String AND = "&";
	public static final String EQUAL = "=";

	public static final String SINGLE_QUOTE = "'";
	public static final String DOUBLE_QUOTE = "\"";

	public static final String TRUE = "true";
	public static final String FALSE = "false";

	// Should be "\n" and "/"
	public static final String NEWLINE = System.getProperty("line.separator");
	public static final String PATH_SEPARATOR = System.getProperty("file.separator");

	public static boolean areEqual(String one, String two)
	{
		return areEqual(one, two, false);
	}

	public static boolean areEqual(String one, String two, boolean ignoreCase)
	{
		if (one != null)
			return (ignoreCase ? one.equalsIgnoreCase(two) : one.equals(two));
		else
			return (two == null);
	}

	public static boolean hasValue(CharSequence str)
	{
		return (str != null && str.length() != 0);
	}

	/**
	 * Returns the result of {@link Object#toString()} if obj is non-null, and a null string otherwise.
	 * Contrast to {@link String#valueOf(Object)} which returns the string "null" instead.
	 */
	public static String toString(Object obj)
	{
		return toString(obj, null);
	}

	/**
	 * Returns the result of {@link Object#toString()} if obj is non-null, and the specified
	 * nullString otherwise. Contrast to {@link String#valueOf(Object)} which returns the string "null".
	 */
	public static String toString(Object obj, String nullString)
	{
		return (obj != null ? obj.toString() : nullString);
	}

	public static String toLowerCase(String str)
	{
		if (str == null)
			return null;

		return str.toLowerCase(Locale.US);
	}

	public static boolean starsWithIgnoreCase(String str, String prefix)
	{
		if (str == null || prefix == null)
			return false;

		if (str.length() < prefix.length())
			return false;

		String strStart = str.substring(0, prefix.length());
		return strStart.equalsIgnoreCase(prefix);
	}

	public static boolean endsWithIgnoreCase(String str, String prefix)
	{
		if (str == null || prefix == null)
			return false;

		if (str.length() < prefix.length())
			return false;

		String strEnd = str.substring(str.length() - prefix.length(), str.length());
		return strEnd.equalsIgnoreCase(prefix);
	}

	/**
	 * Removes whitespace, including "non-breaking spaces" (char 160) from a String
	 */
	public static String trimAll(String s)
	{
		if (s == null)
			return null;

		return s.replace(String.valueOf((char)160), Strings.SPACE).trim();
	}

	public static boolean arrayContains(String[] array, String value, boolean ignoreCase)
	{
		for (String arr : array)
		{
			if (areEqual(arr, value, ignoreCase))
				return true;
		}

		return false;
	}

	public static Set<String> newSet(String... values)
	{
		TreeSet<String> set = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		if (values != null)
			Collections.addAll(set, values);

		return set;
	}

	public static String[] newArray(Set<String> values)
	{
		return values.toArray(new String[values.size()]);
	}

	public static boolean isNumeric(String value) {
		try {
			Double.parseDouble(value);
			return true;
		} catch(NumberFormatException e){
			return false;
		}
	}
}
