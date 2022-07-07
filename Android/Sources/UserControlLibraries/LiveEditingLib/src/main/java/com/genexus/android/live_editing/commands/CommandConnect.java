package com.genexus.android.live_editing.commands;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class CommandConnect implements IClientCommand {
	@SerializedName("Data")
	private final Map<String, String> mData;

	public CommandConnect(String kbUUID, String kbName) {
		mData = new HashMap<>(2);
		mData.put("UUID", kbUUID);
		mData.put("Name", kbName);
	}
}
