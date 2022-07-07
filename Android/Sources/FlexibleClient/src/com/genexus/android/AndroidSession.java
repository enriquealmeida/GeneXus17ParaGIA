package com.genexus.android;

import java.util.Map;
import java.util.TreeMap;

import com.artech.base.services.IAndroidSession;

public class AndroidSession implements IAndroidSession {

	private Map<String, Object> mValues = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
 
	@Override
	public String getValue(String key) {
		Object out = mValues.get(key);
		//Object out = getObjectAttribute(key);
		if	(out == null)
			return "";
		else
	 		return out.toString();
	}

	@Override
	public void setValue(String key, String value)
	{
		mValues.put(key, value);
	}

	@Override
	public void clear() {
		mValues.clear();
	}

	@Override
	public void destroy() {
		mValues = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	}

	@Override
	public void remove(String key)
	{
		mValues.remove(key);
	}

	@Override
	public Object getObject(String key)
	{
		return mValues.get(key);
	}

	@Override
	public void setObject(String key, Object value) {
		mValues.put(key, value);
	}
}
