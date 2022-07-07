package com.genexus.android.live_editing.commands;

import android.app.Activity;

import com.genexus.android.core.base.metadata.languages.Language;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controls.IGxLocalizable;
import com.genexus.android.live_editing.support.ILiveEditingImageManager;
import com.google.gson.annotations.SerializedName;

public class CommandTranslationChanged implements IServerCommand {
	@SerializedName("ObjName")
	private final String mLanguageName;
	@SerializedName("Data")
	private final INodeObject mNewMetadata;

	public CommandTranslationChanged(String languageName, INodeObject newMetadata) {
		mLanguageName = languageName;
		mNewMetadata = newMetadata;
	}

	@Override
	public boolean execute(ILiveEditingImageManager liveEditingImageManager) {
		Language currentLanguage = Services.Application.getDefinition().getLanguageCatalog().
				getCurrentLanguage();
		if (!MetadataHelper.checkCurrentLanguage(currentLanguage, mLanguageName))
			return false;

		MetadataHelper.replaceTranslation(currentLanguage, mNewMetadata);
		return true;
	}

	@Override
	public void applyChanges(final Activity activity) {
		// Activity (to change the action bar title)
		if (activity instanceof IGxLocalizable) {
			((IGxLocalizable) activity).onTranslationChanged();
		}

		// Individual controls
		for (IGxLocalizable control : ControlsUtils.getLocalizableControls(activity)) {
			control.onTranslationChanged();
		}
	}

	@Override
	public boolean shouldInspectUIAfterApplyingChanges() {
		return true;
	}
}
