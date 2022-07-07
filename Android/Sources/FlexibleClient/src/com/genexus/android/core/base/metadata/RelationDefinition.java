package com.genexus.android.core.base.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RelationDefinition implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String mBCRelated;
	private String mName;
	private final ArrayList<String> mKeys = new ArrayList<>();
	private final ArrayList<String> mInferredAtts = new ArrayList<>();

	public void setBCRelated(String bcRelated) {
		mBCRelated = bcRelated;
	}
	public String getBCRelated() {
		return mBCRelated;
	}

	public List<String> getInferredAtts() {
		return mInferredAtts;
	}

	public List<String> getKeys() {
		return mKeys;
	}
	public void setName(String string) {
		mName = string;
	}
	public String getName() {
		return mName;
	}
}
