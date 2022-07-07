package com.genexus.android.core.common

import android.app.Activity
import com.genexus.android.core.activities.IGxThemeableActivity
import com.genexus.android.core.base.metadata.loader.MetadataParser
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition
import com.genexus.android.core.base.metadata.theme.ThemeDefinition
import com.genexus.android.core.base.services.IThemes
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.PlatformHelper

class ThemesManager : IThemes {
    private var appTheme: ThemeDefinition? = null
    private var appThemeCalculated = false
    private var inDarkMode = false // Selected in the device by the user
    private var setThemeWasUsed = false // GX SetTheme function used, theme manually set
    override fun getCurrentTheme(): ThemeDefinition? {
        if (!appThemeCalculated || appTheme == null)
            setTheme(calculateAppTheme())

        appTheme?.darkTheme?.let {
            if (useDarkThemeProperty())
                return it
        }

        return appTheme
    }

    private fun useDarkThemeProperty(): Boolean {
        if (setThemeWasUsed) return false

        return if (Services.Application.get().mainProperties != null &&
            !Services.Application.get().mainProperties.optBooleanProperty("idEnablePreferredColorScheme")
        ) {
            "AndroidBaseStyleDark" == Services.Application.get().mainProperties.optStringProperty("AndroidBaseStyle")
        } else {
            inDarkMode
        }
    }

    /**
     * @return true when automatic use of system color scheme is enabled
     */
    private val isDarkModeForDesignSystemEnabled: Boolean
        get() = Services.Application.get().mainProperties?.optBooleanProperty("idEnablePreferredColorScheme")
            ?: false

    override fun getCurrentThemeName(): String? {
        return currentTheme?.name
    }

    private fun setTheme(appTheme: ThemeDefinition?) {
        this.appTheme = appTheme
        appThemeCalculated = true
        updateDesignSystemDefaultOptions()
        updateDarkModeOnDesignSystemIfNeeded()
    }

    override fun setDarkMode(darkMode: Boolean) {
        inDarkMode = darkMode
        updateDarkModeOnDesignSystemIfNeeded()
    }

    private fun updateDarkModeOnDesignSystemIfNeeded() {
        if (appTheme != null && isDarkModeForDesignSystemEnabled) {
            // no need to use constants since it is only used here
            Services.Application.definition.setDefaultOption(
                "color-scheme",
                if (inDarkMode) "dark" else "light"
            )
        }
    }

    private fun updateDesignSystemDefaultOptions() {
        appTheme?.let { appTheme ->
            Services.Application.definition.clearAllDefaultOptions()

            fun update(theme: ThemeDefinition) {
                theme.importedTokensThemes.forEach { update(it) }
                theme.tokensDefaultOptions?.let { defaultOptions ->
                    for (pair in defaultOptions.internalProperties)
                        Services.Application.definition.setDefaultOption(pair.key, pair.value.toString())
                }
            }

            update(appTheme)
        }
    }

    override fun setCurrentTheme(activity: Activity, name: String): Boolean {
        val theme = MetadataParser.getTheme(activity, name)
        return if (theme != null) {
            setThemeWasUsed = true
            setTheme(theme)
            true
        } else {
            false
        }
    }

    override fun applyCurrentTheme(activity: Activity) {
        if (activity is IGxThemeableActivity) {
            activity.runOnUiThread {
                activity.applyCurrentTheme(false)
            }
        }
    }

    override fun applyCurrentThemeForced(activity: Activity) {
        if (activity is IGxThemeableActivity) {
            activity.runOnUiThread {
                activity.applyCurrentTheme(true)
            }
        }
    }

    override fun getApplicationClass(): ThemeClassDefinition? {
        val theme = currentTheme ?: return null
        return theme.applicationClass
    }

    override fun getThemeClass(className: String?): ThemeClassDefinition? {
        if (className.isNullOrEmpty()) return null
        val theme = currentTheme ?: return null

        // We accept "old" names (e.g. Attribute.Title) in case they were used as constant strings in GX code.
        val classNameNoDot = className.replace(".", "")
        val themeClass = theme.getClass(classNameNoDot)
        if (themeClass == null && !className.equals("(None)", ignoreCase = true))
            Services.Log.warning("Class '$className' not found in theme '${theme.name}'.")

        return themeClass
    }

    @Deprecated("Subclasses of ThemeClassDefinition are no longer supported in Design System")
    override fun <T : ThemeClassDefinition?> getThemeClass(t: Class<T>, className: String): T? {
        val themeClass = getThemeClass(className)
        return if (t.isInstance(themeClass)) t.cast(themeClass) else null
    }

    private fun calculateAppTheme(): ThemeDefinition? {
        val name = calculateAppThemeName()
        return if (!name.isNullOrEmpty())
            Services.Application.definition.getTheme(name)
        else
            null
    }

    override fun calculateAppThemeName(): String? {
        // Get the theme from the first applicable platform that has a set theme, if any.
        return PlatformHelper.getValidPlatforms().map { it.theme }
            .firstOrNull { !it.isNullOrEmpty() }
    }

    override fun reset() {
        appTheme = null
        appThemeCalculated = false
    }
}
