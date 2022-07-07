package com.genexus.android.live_editing.commands;

import android.app.Activity;
import androidx.annotation.UiThread;

import com.genexus.android.live_editing.support.ILiveEditingImageManager;

public interface IServerCommand {
	// TODO: Revisar interfaz. Seria mejor no pasar tantos params, que no todos los comandos
	// precisan.

	/**
	 * Returns whether changes should be applied to currently running activities.
	 */
	boolean execute(ILiveEditingImageManager liveEditingImageManager);

	@UiThread
	void applyChanges(Activity activity);

	/**
	 * Returns whether we should send a InspectUI command to refresh the screen in case a
	 * onGlobalLayoutChange might not be triggered.
	 */
	boolean shouldInspectUIAfterApplyingChanges();
}
