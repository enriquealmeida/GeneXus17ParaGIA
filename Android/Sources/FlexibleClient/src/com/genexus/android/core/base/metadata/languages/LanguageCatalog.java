package com.genexus.android.core.base.metadata.languages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.text.TextUtils;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

public class LanguageCatalog implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final ArrayList<Language> mLanguages;
	private String mDefaultLanguage;

	private Language mCurrentLanguage;
	private String mLocalesAtLastCalculation;

	public LanguageCatalog()
	{
		mLanguages = new ArrayList<>();
	}

	public void add(Language language)
	{
		mLanguages.add(language);
		mLocalesAtLastCalculation = null; // Reset cached calculation.
	}

	public void setDefault(String defaultLanguage)
	{
		mDefaultLanguage = defaultLanguage;
	}

	public Language getCurrentLanguage()
	{
		// According to Android doc (http://developer.android.com/reference/java/util/Locale.html)
		// Since the user's locale changes dynamically, avoid caching this value (Locale.getDefault()).
		List<Locale> locales = Services.Language.getLocales();
		String localesString = TextUtils.join(",", locales);

		if (!localesString.equals(mLocalesAtLastCalculation))
		{
			// Need to (re)calculate current language (this can be cached).
			mCurrentLanguage = calculateCurrentLanguage(locales);
			mLocalesAtLastCalculation = localesString;
		}

		return mCurrentLanguage;
	}

	/**
	 * Maps the current Java locale to a GeneXus language.
	 */
	private Language calculateCurrentLanguage(List<Locale> locales)
	{
		if (mLanguages.size() == 0)
			return null;

		if (mLanguages.size() == 1)
			return mLanguages.get(0); // A fairly common case when we don't use translation (default).

		// Try with every one of the user's locales, in order of preference.
		for (Locale locale : locales)
		{
			String languageCode = locale.getLanguage();
			String countryCode = locale.getCountry();

			// Try to match for exact language and country, or for exact language and any country.
			ArrayList<Language> languagesForLanguageCode = new ArrayList<>();
			for (Language language : mLanguages)
			{
				if (languageCode.equalsIgnoreCase(language.getLanguageCode()))
					languagesForLanguageCode.add(language);
			}

			if (Strings.hasValue(countryCode) && languagesForLanguageCode.size() > 1)
			{
				for (Language language : languagesForLanguageCode)
				{
					if (countryCode.equalsIgnoreCase(language.getCountryCode()))
						return language;
				}
			}

			// Prefer "this language but some other country" to "another language".
			if (languagesForLanguageCode.size() >= 1)
				return languagesForLanguageCode.get(0);
		}

		// No candidates, return default language.
		return getLanguage(mDefaultLanguage);
	}

	public Language getLanguage(String name)
	{
		for (Language language : mLanguages)
		{
			if (name.equalsIgnoreCase(language.getName()))
				return language;
		}

		return null;
	}

	public String getTranslation(String message)
	{
		return getTranslation(message, getCurrentLanguage());
	}

	public String getTranslation(String message, String language)
	{
		return getTranslation(message, getLanguage(language));
	}

	private String getTranslation(String message, Language language)
	{
		if (!Services.Strings.hasValue(message))
			return message;

		if (language == null)
			return message;

		return language.getTranslation(message);
	}

	public String getExpressionTranslation(String expression)
	{
		if (!Services.Strings.hasValue(expression))
			return expression;

		Language language = getCurrentLanguage();
		if (language == null)
			return expression;

		return language.getExpressionTranslation(expression);
	}
}
