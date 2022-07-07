package com.genexus.android.live_editing.commands;

import android.app.Activity;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.live_editing.support.Endpoint;
import com.genexus.android.live_editing.support.ILiveEditingImageManager;
import com.google.gson.annotations.SerializedName;

public class CommandServiceInfo implements IServerCommand {
	@SerializedName("Data")
	private final int mImagesPort;

	public CommandServiceInfo(int imagesPort) {
		mImagesPort = imagesPort;
	}

	@Override
	public boolean execute(ILiveEditingImageManager liveEditingImageManager) {
		Endpoint currentEndpoint = liveEditingImageManager.getCurrentEndpoint();
		if (currentEndpoint != null && mImagesPort > 30100 && mImagesPort <= 301500) {
			liveEditingImageManager.setCurrentEndpoint(new Endpoint(currentEndpoint.ip, mImagesPort));
			Services.Log.debug("CommandServiceInfo",
					String.format("Images service port changed to port %d.", mImagesPort));
		}

		return false;
	}

	@Override
	public void applyChanges(Activity activity) {

	}

	@Override
	public boolean shouldInspectUIAfterApplyingChanges() {
		return false;
	}
}
