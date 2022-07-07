package com.genexus.android.live_editing.commands;

import android.app.Activity;
import androidx.annotation.UiThread;

import com.genexus.android.live_editing.support.ILiveEditingImageManager;

public class CommandKBDoesNotMatchGUID implements IServerCommand {

	@Override
	public boolean execute(ILiveEditingImageManager liveEditingImageManager) {
		return false;
	}

	@Override @UiThread
	public void applyChanges(Activity activity) {

	}

	@Override
	public boolean shouldInspectUIAfterApplyingChanges() {
		return false;
	}
}
