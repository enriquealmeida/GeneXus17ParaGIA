package com.genexus.android.live_editing.commands;

import android.app.Activity;
import androidx.annotation.NonNull;

import com.genexus.android.layout.GxTheme;
import com.genexus.android.core.base.metadata.theme.ThemeDefinition;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controls.IGxThemeable;
import com.genexus.android.live_editing.support.ILiveEditingImageManager;
import com.google.gson.annotations.SerializedName;

public class CommandThemeTransformationChanged implements IServerCommand {
	@SerializedName("ObjName")
	private final String mThemeName;
	@SerializedName("TransformName")
	private final String mTransformationName;
	@SerializedName("Data")
	private final INodeObject mNewMetadata;

	public CommandThemeTransformationChanged(@NonNull String themeName,
											 @NonNull String transformationName,
											 @NonNull INodeObject newMetadata) {
		mThemeName = themeName;
		mTransformationName = transformationName;
		mNewMetadata = newMetadata;
	}

	@Override
	public boolean execute(ILiveEditingImageManager liveEditingImageManager) {
		ThemeDefinition currentTheme = Services.Themes.getCurrentTheme();
		if (!MetadataHelper.checkCurrentThemeName(currentTheme, mThemeName))
			return false; // Ignore not current theme

		MetadataHelper.replaceTransformation(currentTheme, mTransformationName, mNewMetadata);
		return true;
	}

	@Override
	public void applyChanges(final Activity activity) {
		// Apply transformation changes to each control that has a theme class that uses this transformation.
		for (IGxThemeable control : ControlsUtils.getControlsWithTransformationName(activity, mTransformationName)) {
			// Re-apply the control's theme class definition in order to re-apply its associated transformation.
			GxTheme.applyStyle(control, control.getThemeClass(), true);
		}
	}

	@Override
	public boolean shouldInspectUIAfterApplyingChanges() {
		return true;
	}
}
