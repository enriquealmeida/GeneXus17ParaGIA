package com.genexus.android.core.base.metadata.theme

import com.genexus.android.core.base.metadata.theme.MultiThemeClassDefinition.Companion.create
import com.genexus.android.core.base.utils.NameMap

class ThemeDefinition(val name: String, private val mIsDesignSystem: Boolean) {
    private val importedStylesThemesMap = NameMap<ThemeDefinition>()
    private val importedTokensThemesMap = NameMap<ThemeDefinition>()
    private val classes = NameMap<ThemeClassDefinition>()
    private val transformations = NameMap<TransformationDefinition>()
    private val fontFamilies = NameMap<ThemeFontFamilyDefinition>()
    private val tokens = NameMap<ThemeTokensDefinition>()
    var darkTheme: ThemeDefinition? = null
    var isLoaded = false
        private set

    fun setAsLoaded() {
        isLoaded = true
    }

    val importedStylesThemes: Collection<ThemeDefinition>
        get() = importedStylesThemesMap.values

    val importedTokensThemes: Collection<ThemeDefinition>
        get() = importedTokensThemesMap.values

    val applicationClass: ThemeClassDefinition
        get() {
            val appClass = getClass(APP_CLASS_NAME)
            return appClass ?: ThemeClassDefinition(this, null)
        }

    var tokensDefaultOptions: ThemeTokensDefaultOptionsDefinition? = null

    fun getClass(id: String?): ThemeClassDefinition? {
        if (id.isNullOrEmpty()) return null

        val className = normalizeClassName(id)
        var definition = classes[className]
        if (definition == null) {
            if (isCompositeClass(className)) {
                definition = create(className, this)
                putClass(definition)
            } else if (mIsDesignSystem) {
                // some classes may not be defined in the design system
                // create an empty one (also save it for later to reuse)
                definition = ThemeClassDefinition(this, null)
                definition.name = id
                putClass(definition)
            }
        }
        return definition
    }

    fun getTransformation(id: String): TransformationDefinition? {
        return transformations[id]
    }

    fun getFontFamily(id: String): ThemeFontFamilyDefinition? {
        var definition = fontFamilies[id]
        if (definition == null) {
            for (imported in importedStylesThemes) {
                definition = imported.getFontFamily(id)
                if (definition != null) break
            }
        }
        return definition
    }

    val resourceStyleName: String
        get() {
            val name = name
            var bestValue = ""
            var bestPoints = -1
            for (tokens in tokens.values) {
                val points = tokens.options.currentPoints
                if (points > bestPoints) {
                    bestValue = tokens.getResourceStyleNameSuffix()
                    bestPoints = points
                }
            }
            return name + bestValue
        }

    fun clearCache() {
        for (classDefinition in classes.values)
            classDefinition.clearCache()
    }

    fun getToken(name: String?): String {
        var bestValue = ""
        var bestPoints = -1
        for (tokens in allTokens) {
            val points = tokens.options.currentPoints
            if (points > bestPoints) {
                val value = tokens.getStringProperty(name, null)
                if (value != null) {
                    bestValue = value
                    bestPoints = points
                }
            }
        }
        return bestValue
    }

    private val allTokens: Collection<ThemeTokensDefinition>
        get() {
            val tokens = ArrayList(tokens.values)
            for (importedTheme in importedTokensThemesMap.values) tokens.addAll(importedTheme.allTokens)
            return tokens
        }

    fun putImportedStylesTheme(def: ThemeDefinition) {
        importedStylesThemesMap[def.name] = def
    }

    fun removeImportedStylesTheme(themeName: String) {
        importedStylesThemesMap.remove(themeName)
    }

    fun putImportedTokensTheme(def: ThemeDefinition) {
        importedTokensThemesMap[def.name] = def
    }

    fun removeImportedTokensTheme(themeName: String) {
        importedTokensThemesMap.remove(themeName)
    }

    fun clearImportedThemes() {
        importedStylesThemesMap.clear()
        importedTokensThemesMap.clear()
    }

    fun putTokens(def: ThemeTokensDefinition) {
        tokens[def.getResourceStyleNameSuffix()] = def
    }

    fun remoteTokens(tokensResourceSuffix: String) {
        tokens.remove(tokensResourceSuffix)
    }

    fun clearTokens() {
        tokens.clear()
        tokensDefaultOptions = null
        clearCache()
    }

    fun putClass(def: ThemeClassDefinition) {
        classes[normalizeClassName(def.name)] = def
    }

    fun removeClass(defName: String) {
        classes.remove(defName)
    }

    fun clearClasses() {
        classes.clear()
    }

    fun putTransformation(transformation: TransformationDefinition) {
        transformations[transformation.name] = transformation
    }

    fun removeTransformation(defName: String) {
        transformations.remove(defName)
    }

    fun clearTransformations() {
        transformations.clear()
    }

    fun putFontFamily(font: ThemeFontFamilyDefinition) {
        fontFamilies[font.name] = font
    }

    fun removeFontFamily(defName: String) {
        fontFamilies.remove(defName)
    }

    fun clearFontFamilies() {
        fontFamilies.clear()
    }

    fun mergeTo(theme: ThemeDefinition) {
        for (importedTheme in importedStylesThemesMap.values) theme.putImportedStylesTheme(importedTheme)
        for (importedTheme in importedTokensThemesMap.values) theme.putImportedTokensTheme(importedTheme)
        for (classDefinition in classes.values) {
            var def = classDefinition
            while (def != null) {
                def.theme = theme
                def = def.parentClass
            }
            theme.putClass(classDefinition)
        }
        for (transformationDefinition in transformations.values) theme.putTransformation(transformationDefinition)
        for (fontDefinition in fontFamilies.values) theme.putFontFamily(fontDefinition)
        for (tokensDefinition in tokens.values) theme.putTokens(tokensDefinition)
        if (tokensDefaultOptions != null) theme.tokensDefaultOptions = tokensDefaultOptions
        if (darkTheme != null) theme.darkTheme = darkTheme
        theme.clearCache()
    }
}
