package com.genexus.android.live_editing.commands;

import com.genexus.android.live_editing.inspector.MasterLayoutData;
import com.google.gson.annotations.SerializedName;

public class CommandMasterLayout implements IClientCommand {
	@SerializedName("Data")
	private final MasterLayoutData mMasterLayoutData;

	public CommandMasterLayout(MasterLayoutData masterLayoutData) {
		mMasterLayoutData = masterLayoutData;
	}
}
