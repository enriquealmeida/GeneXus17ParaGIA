package com.genexus.android.core.base.metadata.languages;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;

import com.genexus.android.core.base.utils.NameMap;

public class Language implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final String mName;
	private final NameMap<String> mProperties;
	private final HashMap<String, String> mTranslations;
	private final String mLanguageCode;
	private final String mCountryCode;

	public Language(String name, String languageCode, String countryCode)
	{
		mName = name;
		mProperties = new NameMap<>();
		mTranslations = new HashMap<>();
		mLanguageCode = languageCode;
		mCountryCode = countryCode;
	}

	public String getName() { return mName; }
	public String getLanguageCode() { return mLanguageCode; }
	public String getCountryCode() { return mCountryCode; }
	public NameMap<String> getProperties() { return mProperties; }

	public void add(String message, String translation)
	{
		mTranslations.put(message, translation);
	}
	
	public void remove(String message)
	{
		mTranslations.remove(message);
	}

	public String getTranslation(String message)
	{
		// Translation is performed by substituting the message by its translation,
		// maintaining spaces if present in the original string, but not considering
		// them for translation lookup.
		String messageKey = message.trim();
		String translation = mTranslations.get(messageKey);

		if (translation != null)
		{
			if (message.length() != messageKey.length())
			{
				return message.replace(messageKey, translation);
				/*
				 * In case replace(CharSequence) uses regular expressions?
				int startPosition = message.indexOf(messageKey);
				int endPosition = startPosition + messageKey.length();

				return message.substring(0, startPosition) + translation + message.substring(endPosition);
				*/
			}
			else
				return translation; // No spaces trimmed, translation is exact.
		}
		else
			return message; // No translation.
	}

	private static final String NON_TRANSLATABLE_MARKER = "!";
	private static final String SINGLE_QUOTE = "'";
	private static final String DOUBLE_QUOTE = "\"";

	public String getExpressionTranslation(String expression)
	{
		if (expression.startsWith(NON_TRANSLATABLE_MARKER))
			return expression.substring(NON_TRANSLATABLE_MARKER.length()); // String must not be translated. Just remove the marker.
		
		// An "expression" is actually a string between quotes.
		if (expression.length() >= 2 && 
			((expression.startsWith(SINGLE_QUOTE) && expression.endsWith(SINGLE_QUOTE)) ||
			 (expression.startsWith(DOUBLE_QUOTE) && expression.endsWith(DOUBLE_QUOTE))))
		{
			// Remove the quotes and translate.
			String message = expression.substring(1, expression.length() - 1);
			String translatedMessage = getTranslation(message);
		
			// Return the translation, re-adding the quotes. 
			char quoteChar = expression.charAt(0);
			return quoteChar + translatedMessage + quoteChar;
		}
		else
		{
			// Not a string expression. Return as-is.
			return expression;
		}
	}

	public Locale getLocale()
	{
		return new Locale(getLanguageCode(), getCountryCode());
	}
}
