package com.artech.android;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.artech.base.utils.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocaleHelper {
	public static String getLocaleString(List<Locale> locales) {
		ArrayList<String> languageCodes = new ArrayList<>();

		for (Locale locale : locales) {
			if (Strings.hasValue(locale.getCountry()))
				languageCodes.add(locale.getLanguage() + '-' + locale.getCountry()); // e.g. "en-US"

			languageCodes.add(locale.getLanguage()); // e.g. "en"
		}

		return TextUtils.join(",", languageCodes);
	}

	public static Context updateLocale(@NonNull Context context,
									   @NonNull Locale currentLocale,
									   @NonNull List<Locale> systemLocales) {
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

		return context.createConfigurationContext(config);
	}
}
