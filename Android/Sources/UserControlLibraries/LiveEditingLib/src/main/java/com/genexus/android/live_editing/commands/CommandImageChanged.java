package com.genexus.android.live_editing.commands;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.annotation.UiThread;

import com.genexus.android.live_editing.support.ILiveEditingImageManager;
import com.google.gson.annotations.SerializedName;

public class CommandImageChanged implements IServerCommand {
	@SerializedName("ObjName")
	private final String mImageName;

	public CommandImageChanged(@NonNull String imageName) {
		mImageName = imageName;
	}

	@Override
	public boolean execute(ILiveEditingImageManager liveEditingImageManager) {
		liveEditingImageManager.addImageToDirtyList(mImageName);
		return true;
	}

	@Override @UiThread
	public void applyChanges(Activity activity) {
		// TODO: Refrescar únicamente las imagenes que cambiaron.
		activity.recreate();
	}

	@Override
	public boolean shouldInspectUIAfterApplyingChanges() {
		return false;
	}
}
