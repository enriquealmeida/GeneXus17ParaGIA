package com.genexus.android.live_editing.commands;

import android.app.Activity;
import androidx.annotation.NonNull;

import com.genexus.android.live_editing.support.ILiveEditingImageManager;
import com.google.gson.annotations.SerializedName;

public class CommandKbOk implements IServerCommand {
	@SerializedName("Gzip")
	private final boolean mCompressionEnabled;

	public CommandKbOk(@NonNull boolean compressionEnabled) {
		mCompressionEnabled = compressionEnabled;
	}

	@Override
	public boolean execute(ILiveEditingImageManager liveEditingImageManager) {
		return false;
	}

	@Override
	public void applyChanges(Activity activity) {

	}

	@Override
	public boolean shouldInspectUIAfterApplyingChanges() {
		return false;
	}

	public boolean isCompressionEnabled() {
		return mCompressionEnabled;
	}
}
