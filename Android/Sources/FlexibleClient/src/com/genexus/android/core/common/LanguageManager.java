package com.genexus.android.core.common;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.genexus.android.core.activities.GxBaseActivity;
import com.genexus.android.core.application.LifecycleListeners;
import com.genexus.android.core.base.metadata.images.ImageCatalog;
import com.genexus.android.core.base.metadata.languages.Language;
import com.genexus.android.core.base.metadata.languages.LanguageCatalog;
import com.genexus.android.core.base.services.IApplication;
import com.genexus.android.core.base.services.ILanguage;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.providers.EntityDataProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

class LanguageManager implements ILanguage {
	private static final String APP_CURRENT_LANGUAGE = "ApplicationCurrentLanguage";
	private static final String APP_CURRENT_LOCALE_LANGUAGE = "ApplicationCurrentLocaleLanguage";
	private static final String APP_CURRENT_LOCALE_COUNTRY = "ApplicationCurrentLocaleCountry";

	private final IApplication mApplication;
	private List<Locale> mSystemLocales;
	private Locale mCurrentLocale;

	public LanguageManager(@NonNull IApplication application) {
		mApplication = application;
		mCurrentLocale = null;
		application.getLifecycle().registerApplicationLifecycleListener(mApplicationCallbacks);
		application.getLifecycle().addActivityListener(mActivityCallbacks);
	}

	@Override
	public void initialize(LanguageCatalog languages, ImageCatalog images) {
		mApplication.getDefinition().setImageCatalog(images);
		mApplication.getDefinition().setLanguageCatalog(languages);
	}

	@Override
	public String getCurrentLanguage() {
		Language currentLanguage = mApplication.getDefinition().getLanguageCatalog().getCurrentLanguage();
		return (currentLanguage != null) ? currentLanguage.getName() : null;
	}

	@Override
	public String getCurrentLanguageProperty(String property) {
		Language currentLanguage = mApplication.getDefinition().getLanguageCatalog().getCurrentLanguage();
		return (currentLanguage != null) ? currentLanguage.getProperties().get(property) : null;
	}

	@Override
	public String getTranslation(String message) {
		return mApplication.getDefinition().getLanguageCatalog().getTranslation(message);
	}

	@Override
	public String getTranslation(String message, String language) {
		return mApplication.getDefinition().getLanguageCatalog().getTranslation(message, language);
	}

	@Override
	public String getExpressionTranslation(String expression) {
		return mApplication.getDefinition().getLanguageCatalog().getExpressionTranslation(expression);
	}

	@Override
	public void setLanguageAndLocale(@NonNull Context context, String languageName, Locale locale,
									 boolean persistValues) {
		mCurrentLocale = locale;

		if (persistValues) {
			// store locale in app setting to restore at startup.
			Services.Preferences.setString(APP_CURRENT_LANGUAGE, languageName);
			Services.Preferences.setString(APP_CURRENT_LOCALE_LANGUAGE, locale.getLanguage());
			Services.Preferences.setString(APP_CURRENT_LOCALE_COUNTRY, locale.getCountry());
		}

		// Change locate of current app, actually change language on getCurrentLanguage recalculate
		updateLocale(context, mCurrentLocale, mSystemLocales);
		// Clear stored data since it may have translations
		clearCacheOnLanguageChange();
	}

	@Override
	public void setLocaleToSystemDefault(@NonNull Context context) {
		if (mSystemLocales.size() >= 1) {
			mCurrentLocale = mSystemLocales.get(0);

			//change locate of current app, actually change language when recalculate it in getCurrentLanguage
			updateLocale(context, mCurrentLocale, mSystemLocales);
			// Clear stored data since it may have translations
			clearCacheOnLanguageChange();
		}

		// Forget about selected locale, so that the default will be used later.
		Services.Preferences.setString(APP_CURRENT_LANGUAGE, "");
		Services.Preferences.setString(APP_CURRENT_LOCALE_LANGUAGE, "");
		Services.Preferences.setString(APP_CURRENT_LOCALE_COUNTRY, "");
	}

	@Override
	public void clearCacheOnLanguageChange() {
		final String APP_LANGUAGE = "ApplicationLanguage";

		String previousLanguage = Services.Preferences.getString(APP_LANGUAGE);
		String currentLanguage = getCurrentLanguage();

		if (previousLanguage == null || !previousLanguage.equalsIgnoreCase(currentLanguage)) {
			EntityDataProvider.clearAllCaches();
		}

		Services.Preferences.setString(APP_LANGUAGE, currentLanguage);
	}

	private final LifecycleListeners.Application mApplicationCallbacks = new LifecycleListeners.Application() {
		@Override
		public void onApplicationCreated(@NonNull IApplication application) {
			application.getLifecycle().unregisterApplicationLifecycleListener(this);
			Context context = application.getAppContext();
			// Store the default (Android) locale, to be able to restore it later.
			mSystemLocales = getLocales();
			Services.Log.debug("onApplicationCreate :" + mSystemLocales);

			// getLocate from setting if exits.
			String currentLanguageString = Services.Preferences.getString(APP_CURRENT_LANGUAGE);

			if (Services.Strings.hasValue(currentLanguageString)) {
				Services.Log.debug("Restore last language used :" + currentLanguageString);

				String currentLocaleLanguageString = Services.Preferences.getString(APP_CURRENT_LOCALE_LANGUAGE);
				String currentLocaleCountryString = Services.Preferences.getString(APP_CURRENT_LOCALE_COUNTRY);
				Locale locale = new Locale(currentLocaleLanguageString, currentLocaleCountryString);

				// Actually change locale
				setLanguageAndLocale(context, currentLanguageString, locale, false);
			} else {
				if (mCurrentLocale != null) {
					updateLocale(context, mCurrentLocale, mSystemLocales);
				}
			}
		}
	};

	private final Application.ActivityLifecycleCallbacks mActivityCallbacks = new Application.ActivityLifecycleCallbacks() {
		@Override
		public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
			if (mCurrentLocale != null && activity instanceof GxBaseActivity)
				updateLocale(activity, mCurrentLocale, mSystemLocales);
		}

		@Override public void onActivityStarted(@NonNull Activity activity) { }
		@Override public void onActivityResumed(@NonNull Activity activity) { }
		@Override public void onActivityPaused(@NonNull Activity activity) { }
		@Override public void onActivityStopped(@NonNull Activity activity) { }
		@Override public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) { }
		@Override public void onActivityDestroyed(@NonNull Activity activity) { }
	};

	@Override
	public Context updateLocale(@NonNull Context context, @NonNull Locale currentLocale, @NonNull List<Locale> systemLocales) {
		Resources resources = context.getResources();
		Configuration config = resources.getConfiguration();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			// Create a locales list with the current locale on top and remove any repetitions
			int size = systemLocales.size();
			if (!systemLocales.contains(currentLocale)) {
				size++;
			}
			Locale[] list = new Locale[size];
			list[0] = currentLocale;
			int i = 1;
			for (Locale locale : systemLocales) {
				if (!locale.equals(currentLocale)) {
					list[i] = (Locale) locale.clone();
					i++;
				}
			}
			// Set this list as the locales to use
			LocaleList localeList = new LocaleList(list);
			LocaleList.setDefault(localeList);
			config.setLocales(localeList);
		} else {
			Locale.setDefault(currentLocale);
			config.setLocale(currentLocale);
		}

		config.setLayoutDirection(currentLocale);
		updateAppResourcesConfig(config);

		return context.createConfigurationContext(config);
	}

	@Override
	public String getLocaleString(List<Locale> locales) {
		ArrayList<String> languageCodes = new ArrayList<>();

		for (Locale locale : locales) {
			if (Strings.hasValue(locale.getCountry()))
				languageCodes.add(locale.getLanguage() + '-' + locale.getCountry()); // e.g. "en-US"

			languageCodes.add(locale.getLanguage()); // e.g. "en"
		}

		return TextUtils.join(",", languageCodes);
	}

	@Override
	public @NonNull List<Locale> getLocales() {
		List<Locale> locales;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			LocaleList localeList = LocaleList.getDefault();
			locales = new ArrayList<>(localeList.size());
			for (int i = 0; i < localeList.size(); i++) {
				locales.add(localeList.get(i));
			}
		} else {
			locales = Collections.singletonList(Locale.getDefault());
		}

		return locales;
	}

	@SuppressWarnings("deprecation")
	private void updateAppResourcesConfig(Configuration config) {
		// from : https://proandroiddev.com/change-language-programmatically-at-runtime-on-android-5e6bc15c758
		// and https://github.com/YarikSOffice/lingver/blob/master/library/src/main/java/com/yariksoffice/lingver/UpdateLocaleDelegate.kt
		// for change the language in runtime, using only createConfigurationContext is not enough.
		// the current working way of change App Context config in runtime is using updateConfiguration()
		// Need to change the application main context, to work without reloading application.
		Context appContext = Services.Application.getAppContext();
		Resources appResources = appContext.getResources();
		appResources.updateConfiguration(config, appResources.getDisplayMetrics());
	}
}
