package com.genexus.android.live_editing.commands;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.genexus.android.core.base.metadata.WorkWithDefinition;
import com.genexus.android.core.base.metadata.languages.Language;
import com.genexus.android.core.base.metadata.loader.MetadataParser;
import com.genexus.android.core.base.metadata.loader.WorkWithMetadataLoader;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeDefinition;
import com.genexus.android.core.base.metadata.theme.TransformationDefinition;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

class MetadataHelper {
    public static void addThemeClass(@NonNull ThemeDefinition theme, @Nullable ThemeClassDefinition parentDefinition,
                                     @NonNull INodeObject newMetadata) {
        ThemeClassDefinition newDefinition = MetadataParser.readOneStyleAndChildren(theme, parentDefinition, newMetadata);
        theme.putClass(newDefinition);
    }

    public static void replaceThemeClass(@NonNull ThemeDefinition theme, @Nullable ThemeClassDefinition parentDefinition,
                                         @NonNull String previousThemeClassName, @NonNull INodeObject newMetadata) {
        theme.removeClass(previousThemeClassName);
        addThemeClass(theme, parentDefinition, newMetadata);
    }

    public static void replaceTransformation(@NonNull ThemeDefinition theme, @NonNull String transformationName,
                                             @NonNull INodeObject newMetadata) {
        theme.removeTransformation(transformationName);
        TransformationDefinition newDefinition = new TransformationDefinition(newMetadata);
        theme.putTransformation(newDefinition);
    }

    public static void replaceTranslation(@NonNull Language currentLanguage, @NonNull INodeObject newMetadata) {
        String message = newMetadata.getString("M");
        String translation = newMetadata.getString("T");

        if (Strings.hasValue(translation)) {
            currentLanguage.add(message, translation);
        } else {
            currentLanguage.remove(message);
        }
    }

	public static void replacePattern(@NonNull String objName, @NonNull INodeObject newMetadata) {
        WorkWithMetadataLoader wwLoader = new WorkWithMetadataLoader();
        WorkWithDefinition newDefinition = wwLoader.loadJSON(newMetadata);
        if (newDefinition != null) {
            newDefinition.setName(objName);
            Services.Application.getDefinition().putPattern(newDefinition, wwLoader, null);
        }
    }

	public static boolean checkCurrentThemeName(ThemeDefinition currentTheme, String targetThemeName) {
        String currentThemeName = currentTheme.getName();
        return Strings.areEqual(currentThemeName, targetThemeName);
    }

    public static boolean checkCurrentLanguage(Language currentLanguage, String targetLanguageName) {
        String currentLanguageName = currentLanguage.getName();
        return Strings.areEqual(currentLanguageName, targetLanguageName);
    }
}
