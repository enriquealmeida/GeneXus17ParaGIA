package com.genexus.android.live_editing.commands;

import android.app.Activity;
import androidx.annotation.NonNull;

import com.genexus.android.core.activities.ActivityThemePropertiesHelper;
import com.genexus.android.layout.GxTheme;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeDefinition;
import com.genexus.android.core.base.serialization.INodeCollection;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.IGxThemeable;
import com.genexus.android.live_editing.support.ILiveEditingImageManager;
import com.google.gson.annotations.SerializedName;

public class CommandThemeColorChanged implements IServerCommand {
	@SerializedName("ObjName")
	private final String mThemeName;
	@SerializedName("Data")
	private final INodeCollection mNewClassesMetadata;

	public CommandThemeColorChanged(@NonNull String themeName,
									@NonNull INodeCollection newClassesMetadata) {
		mThemeName = themeName;
		mNewClassesMetadata = newClassesMetadata;
	}

	@Override
	public boolean execute(ILiveEditingImageManager liveEditingImageManager) {
		ThemeDefinition currentTheme = Services.Themes.getCurrentTheme();
		if (!MetadataHelper.checkCurrentThemeName(currentTheme, mThemeName))
			return false; // Ignore not current theme

		for (INodeObject newMetadata : mNewClassesMetadata) {
			String className = newMetadata.getString("Name");
			if (Strings.hasValue(className)) {
				ThemeClassDefinition parentDefinition = currentTheme.getClass(className)
						.getParentClass();
				MetadataHelper.replaceThemeClass(currentTheme, parentDefinition, className,
						newMetadata);
			}
		}
		return true;
	}

	@Override
	public void applyChanges(final Activity activity) {
		ActivityThemePropertiesHelper.applyChanges(activity);
		ThemeDefinition currentTheme = Services.Themes.getCurrentTheme();

		for (IGxThemeable control : ControlsUtils.getControlsWithThemeClass(activity)) {
			String themeClassName = control.getThemeClass().getName();
			ThemeClassDefinition newDefinition = currentTheme.getClass(themeClassName);
			GxTheme.applyStyle(control, newDefinition);
		}
	}

	@Override
	public boolean shouldInspectUIAfterApplyingChanges() {
		return true;
	}
}
