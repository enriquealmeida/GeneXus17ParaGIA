package com.genexus.android.core.fragments;

import java.util.HashMap;

import com.genexus.android.core.base.model.Entity;

class LayoutFragmentState
{
	private final String mUri;
	private Entity mData;
	private final HashMap<String, Object> mProperties;

	public LayoutFragmentState(String uri, Entity data)
	{
		mUri = uri;
		mData = data;
		mProperties = new HashMap<>();
	}

	public String getUri() { return mUri; }
	public Entity getData() { return mData; }
	public Object getProperty(String key) { return mProperties.get(key); }

	public void setData(Entity data) { mData = data; }
	public void setProperty(String key, Object value) { mProperties.put(key, value); }
}
