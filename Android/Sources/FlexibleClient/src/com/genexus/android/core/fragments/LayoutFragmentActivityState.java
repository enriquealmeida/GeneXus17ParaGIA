package com.genexus.android.core.fragments;

import java.util.HashMap;

import com.genexus.android.core.utils.Cast;

public class LayoutFragmentActivityState
{
	private HashMap<String, LayoutFragmentState> mFragments = new HashMap<>();
	private HashMap<String, Object> mProperties = new HashMap<>();

	public void saveState(BaseFragment fragment)
	{
		LayoutFragmentState state = new LayoutFragmentState(fragment.getUri(), fragment.getContextEntity());
		fragment.saveFragmentState(state);
		mFragments.put(state.getUri(), state);
	}

	public LayoutFragmentState getState(String fragmentUri)
	{
		return mFragments.get(fragmentUri);
	}

	public Object getProperty(String key)
	{
		return mProperties.get(key);
	}

	public <T> T getProperty(Class<T> type, String key)
	{
		return Cast.as(type, mProperties.get(key));
	}

	public boolean getBooleanProperty(String key, boolean defaultValue)
	{
		Boolean value = getProperty(Boolean.class, key);
		return (value != null ? value : defaultValue);
	}

	public void setProperty(String key, Object value)
	{
		mProperties.put(key, value);
	}

	public void removeProperty(String key) {
		mProperties.remove(key);
	}
}
