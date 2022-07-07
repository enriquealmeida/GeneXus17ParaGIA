package com.genexus.android.live_editing.commands;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.genexus.android.core.activities.ActivityThemePropertiesHelper;
import com.genexus.android.layout.GxTheme;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeDefinition;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.IGxThemeable;
import com.genexus.android.live_editing.support.ILiveEditingImageManager;
import com.google.gson.annotations.SerializedName;

import static com.genexus.android.core.base.metadata.theme.ApplicationBarClassExtensionKt.APPBAR_CLASS_NAME;
import static com.genexus.android.core.base.metadata.theme.ApplicationClassExtensionKt.APP_CLASS_NAME;

public class CommandThemeStyleChanged implements IServerCommand {
	@SerializedName("ObjName")
	private final String mThemeName;
	@SerializedName("StyleName")
	private final String mClassName;
	@SerializedName("Parent")
	private final String mParentClassName;
	@SerializedName("Data")
	private final INodeObject mNewMetadata;

	public CommandThemeStyleChanged(@NonNull String themeName, @NonNull String className,
									@NonNull String parentClassName,
									@NonNull INodeObject newMetadata) {
		mThemeName = themeName;
		mClassName = className;
		mParentClassName = parentClassName;
		mNewMetadata = newMetadata;
	}

	@Override
	public boolean execute(ILiveEditingImageManager liveEditingImageManager) {
		ThemeDefinition currentTheme = Services.Themes.getCurrentTheme();
		if (!MetadataHelper.checkCurrentThemeName(currentTheme, mThemeName))
			return false; // Ignore not current theme

		ThemeClassDefinition classDef = currentTheme.getClass(mClassName);
		ThemeClassDefinition parentClassDef = Strings.hasValue(mParentClassName) ? currentTheme.getClass(mParentClassName) : null;
		// New class has been added
		if (classDef == null) {
			// No new root classes allowed
			if (parentClassDef != null) {
				MetadataHelper.addThemeClass(currentTheme, parentClassDef, mNewMetadata);
				return true;
			} else {
				return false;
			}
			// Existent class has been modified
		} else {
			MetadataHelper.replaceThemeClass(currentTheme, parentClassDef, mClassName, mNewMetadata);
			return true;
		}
	}

	@Override
	public void applyChanges(final Activity activity) {
		String newThemeClassName = mNewMetadata.getString("Name");
		String oldThemeClassName = mClassName;
		ThemeClassDefinition newThemeClassDefinition = getNewThemeClass(newThemeClassName);
		if (newThemeClassDefinition == null) {
			Services.Log.error(String.format("Cannot find new theme class '%s'", newThemeClassName));
			return;
		}

		String rootThemeClassName = newThemeClassDefinition.getRootName();
		if (APP_CLASS_NAME.equals(rootThemeClassName) || APPBAR_CLASS_NAME.equals(rootThemeClassName)) {
			ActivityThemePropertiesHelper.applyChanges(activity);
		} else {
			for (IGxThemeable control : ControlsUtils.getControlsWithThemeClassName(activity, oldThemeClassName)) {
				GxTheme.applyStyle(control, newThemeClassDefinition);
			}
		}

		applyChangesToChildren(activity, mNewMetadata);
	}

	private void applyChangesToChildren(final Activity activity, INodeObject node) {
		for (INodeObject childNode : node.optCollection("Styles")) {
			String themeClassName = childNode.getString("Name");
			ThemeClassDefinition newThemeClassDefinition = getNewThemeClass(themeClassName);
			if (newThemeClassDefinition == null) {
				Services.Log.warning(String.format("Cannot find new theme class '%s'", themeClassName));
				continue;
			}

			for (IGxThemeable control : ControlsUtils.getControlsWithThemeClassName(activity, themeClassName)) {
				GxTheme.applyStyle(control, newThemeClassDefinition);
			}

			applyChangesToChildren(activity, childNode);
		}
	}

	@Override
	public boolean shouldInspectUIAfterApplyingChanges() {
		return true;
	}

	private @Nullable ThemeClassDefinition getNewThemeClass(String newThemeClassName) {
		//First time we try to retrieve the new theme class might be null, so retry - Issue 82568
		ThemeDefinition currentTheme = Services.Themes.getCurrentTheme();
		ThemeClassDefinition newThemeClassDefinition = null;
		int attempts = 5;
		while (attempts > 0 && newThemeClassDefinition == null) {
			newThemeClassDefinition = currentTheme.getClass(newThemeClassName);
			attempts -= 1;
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				Services.Log.error(e);
			}
		}

		return newThemeClassDefinition;
	}
}
