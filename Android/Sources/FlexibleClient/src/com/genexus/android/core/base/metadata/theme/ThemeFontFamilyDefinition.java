package com.genexus.android.core.base.metadata.theme;

import com.genexus.android.core.base.serialization.INodeObject;

import java.io.Serializable;

public class ThemeFontFamilyDefinition implements Serializable {
	private final String mName;
	private final String mInternalName;
	private final String mFileName;

	public ThemeFontFamilyDefinition(INodeObject json) {
		mName = json.optString("Name");
		mInternalName = json.optString("InternalName");
		mFileName = json.optString("FileName");
	}

	public String getName() {
		return mName;
	}

	public String getInternalName() {
		return mInternalName;
	}

	public String getFileName() {
		return mFileName;
	}
}
