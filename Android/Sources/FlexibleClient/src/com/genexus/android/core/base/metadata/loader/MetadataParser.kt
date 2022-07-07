package com.genexus.android.core.base.metadata.loader

import android.content.Context
import com.genexus.android.core.base.metadata.DataProviderDefinition
import com.genexus.android.core.base.metadata.GxObjectDefinition
import com.genexus.android.core.base.metadata.ObjectParameterDefinition
import com.genexus.android.core.base.metadata.ProcedureDefinition
import com.genexus.android.core.base.metadata.enums.Connectivity
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition
import com.genexus.android.core.base.metadata.theme.ThemeClassFactory
import com.genexus.android.core.base.metadata.theme.ThemeDefinition
import com.genexus.android.core.base.metadata.theme.ThemeFontFamilyDefinition
import com.genexus.android.core.base.metadata.theme.ThemeTokensDefaultOptionsDefinition
import com.genexus.android.core.base.metadata.theme.ThemeTokensDefinition
import com.genexus.android.core.base.metadata.theme.TransformationDefinition
import com.genexus.android.core.base.serialization.INodeObject
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.Strings

object MetadataParser {
    @JvmStatic
    fun readConnectivity(obj: INodeObject): Connectivity {
        var connectivity = obj.optString("@idConnectivitySupport")
        if (!Services.Strings.hasValue(connectivity)) {
            // read the other format
            connectivity = obj.optString("idConnectivitySupport")
        }
        if (Services.Strings.hasValue(connectivity)) {
            if (connectivity.equals("idOffline", ignoreCase = true)) {
                return Connectivity.Offline
            } else if (connectivity.equals("idOnline", ignoreCase = true)) {
                return Connectivity.Online
            }
        }
        return Connectivity.Inherit
    }

    @JvmStatic
    fun readOneGxObject(obj: INodeObject): GxObjectDefinition {
        val objName = obj.getString("n")
        val objType = obj.optString("t") // Procedure (default) or Data Provider
        val gxObject = if (objType.equals("D", ignoreCase = true))
            DataProviderDefinition(objName)
        else
            ProcedureDefinition(objName)

        // Read Connectivity Support consider Inherit for old metadata without this information
        gxObject.connectivitySupport = readConnectivity(obj)
        for (procParam in obj.optCollection("p")) {
            // New or old format?
            val parameterName = procParam.getString("Name") ?: procParam.optString("n")
            val parameterMode = procParam.getString("m")
            val parDef = ObjectParameterDefinition(parameterName, parameterMode)
            parDef.readDataType(procParam)
            gxObject.parameters.add(parDef)
        }
        return gxObject
    }

    fun getTheme(context: Context, name: String): ThemeDefinition? {
        var theme = Services.Application.definition.getTheme(name)
        if (theme == null) {
            // do this for compatibility but it should already be created in ThemesLoadStep
            theme = readOneTheme(context, name, false) // isDesignSystem = false since it is for compatibility
            if (theme != null)
                Services.Application.definition.putTheme(theme)
        } else {
            // check if it was loaded, because only the default one is loaded in ThemesLoadStep
            if (!theme.isLoaded) {
                readOneTheme(context, theme)
            }
        }
        return theme
    }

    @JvmStatic
    fun readOneTheme(context: Context, name: String, isDesignSystem: Boolean): ThemeDefinition? {
        val theme = MetadataLoader.getDefinition(context, Strings.toLowerCase(name) + ".theme")
        return if (theme != null) readOneTheme(context, name, theme, isDesignSystem) else null
    }

    fun readOneTheme(context: Context, name: String, theme: INodeObject, isDesignSystem: Boolean): ThemeDefinition {
        val themeDef = ThemeDefinition(name, isDesignSystem)
        readOneTheme(context, themeDef, theme)
        return themeDef
    }

    private fun readOneTheme(context: Context, themeDef: ThemeDefinition) {
        val theme = MetadataLoader.getDefinition(context, Strings.toLowerCase(themeDef.name) + ".theme")
        if (theme != null) readOneTheme(context, themeDef, theme)
    }

    private fun readOneTheme(context: Context, themeDef: ThemeDefinition, theme: INodeObject) {
        val propertiesNode = theme.getNode("Properties")
        if (propertiesNode != null) {
            val darkThemeName = MetadataLoader.getAttributeName(propertiesNode.optString("dark_theme"))
            if (darkThemeName != null && darkThemeName != "(none)")
                themeDef.darkTheme = getTheme(context, darkThemeName)
        }

        for (jsonImport in theme.optCollection("Imports")) {
            val name = jsonImport.optString("Name")
            val type = jsonImport.optString("Type")
            val part = jsonImport.optString("Part")
            if (type == "DSO") {
                val importedTheme = getTheme(context, name)
                if (importedTheme != null) {
                    if (part == "all" || part == "styles") themeDef.putImportedStylesTheme(importedTheme)
                    if (part == "all" || part == "tokens") themeDef.putImportedTokensTheme(importedTheme)
                }
            }
        }

        for (jsonTokens in theme.optCollection("Tokens")) {
            val tokensDef = ThemeTokensDefinition(jsonTokens)
            themeDef.putTokens(tokensDef)
        }

        val tokensDefaultOptionsNode = theme.getNode("TokensDefaultOptions")
        if (tokensDefaultOptionsNode != null)
            themeDef.tokensDefaultOptions = ThemeTokensDefaultOptionsDefinition(tokensDefaultOptionsNode)

        var position = 1
        for (jsonStyle in theme.optCollection("Styles")) {
            val classDef = readOneStyleAndChildren(themeDef, null, jsonStyle)
            classDef.position = position++
            themeDef.putClass(classDef)
        }

        for (jsonTransformation in theme.optCollection("Transformations")) {
            val transformation = TransformationDefinition(jsonTransformation)
            themeDef.putTransformation(transformation)
        }

        for (jsonFonts in theme.optCollection("Fonts")) {
            val fontFamily = ThemeFontFamilyDefinition(jsonFonts)
            themeDef.putFontFamily(fontFamily)
        }

        themeDef.setAsLoaded()
    }

    @JvmStatic
    fun readOneStyleAndChildren(
        theme: ThemeDefinition,
        parentClass: ThemeClassDefinition?,
        styleJson: INodeObject
    ): ThemeClassDefinition {
        val className = styleJson.getString("Name")
        val themeClass = ThemeClassFactory.createClass(theme, className, parentClass)
        themeClass.name = className
        themeClass.deserialize(styleJson)
        for (obj in styleJson.optCollection("Styles")) {
            val classDef = readOneStyleAndChildren(theme, themeClass, obj)
            themeClass.childItems.add(classDef)
            theme.putClass(classDef)
        }
        return themeClass
    }
}
