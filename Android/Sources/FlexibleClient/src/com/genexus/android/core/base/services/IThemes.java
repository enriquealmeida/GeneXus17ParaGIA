package com.genexus.android.core.base.services;

import android.app.Activity;

import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeDefinition;

public interface IThemes {
    ThemeDefinition getCurrentTheme();
    String getCurrentThemeName();
    boolean setCurrentTheme(Activity activity, String name);
    void applyCurrentTheme(Activity activity);
	void applyCurrentThemeForced(Activity activity);

    ThemeClassDefinition getApplicationClass();
    ThemeClassDefinition getThemeClass(String className);

	/**
	 * @deprecated don't use custom classes else it won't work with design system
	 */
	@Deprecated
    <T extends ThemeClassDefinition> T getThemeClass(Class<T> t, String className);

    String calculateAppThemeName();
    void setDarkMode(boolean darkMode);
    void reset();
}
