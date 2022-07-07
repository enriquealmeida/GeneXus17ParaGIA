package com.genexus.android.core.base.services;

import android.content.Context;
import androidx.annotation.NonNull;

import com.genexus.android.core.base.metadata.images.ImageCatalog;
import com.genexus.android.core.base.metadata.languages.LanguageCatalog;

import java.util.List;
import java.util.Locale;

public interface ILanguage {
	void initialize(LanguageCatalog languages, ImageCatalog images);

	/**
	 * Returns the current (GX) language name, calculated from the current device locale. May be null.
	 */
	String getCurrentLanguage();

	/**
	 * Returns the properties of the current (GX) language.
	 */
	String getCurrentLanguageProperty(String property);

	/**
	 * Gets the translation of a message in the current language.
	 */
	String getTranslation(String message);

	/**
	 * Gets the translation of a message in the specified language.
	 */
	String getTranslation(String message, String language);

	/**
	 * Gets the translation of an expression (by substituting translatable strings inside it).
	 */
	String getExpressionTranslation(String expression);

	/**
	 * Forces the application to use a specific locale instead of the system one.
	 */
	void setLanguageAndLocale(@NonNull Context context, String languageName, Locale locale, boolean persistValues);

	void setLocaleToSystemDefault(@NonNull Context context);

	void clearCacheOnLanguageChange();

	Context updateLocale(@NonNull Context context, @NonNull Locale currentLocale, @NonNull List<Locale> systemLocales);

	String getLocaleString(List<Locale> locales);

	/**
	 * Returns the device's preferred locales (language and country code).
	 */
	@NonNull List<Locale> getLocales();
}
