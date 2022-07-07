package com.genexus.android.core.base.model;

import java.io.Serializable;

import androidx.annotation.NonNull;

import com.artech.base.services.IPropertiesObject;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.NameMap;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.utils.Cast;

public class PropertiesObject implements Cloneable, Serializable, IPropertiesObject
{
	private static final long serialVersionUID = 1L;

	private NameMap<Object> mValues = new NameMap<>();

	private INodeObject mObject = null;
	private boolean mDeserialized = false;

	private NameMap<Boolean> mDirtyBySetMap = new NameMap<>();
	private NameMap<Boolean> mDirtyByChangeMap = new NameMap<>();

	public PropertiesObject()
	{
	}

	public PropertiesObject(NameMap<Object> props)
	{
		mValues = props;
	}

	public NameMap<Object> getInternalProperties()
	{
		internalDeserialize();
		return mValues;
	}

	protected NameMap<Object> cloneProperties()
	{
		return new NameMap<>(mValues);
	}

	public Iterable<String> getPropertyNames()
	{
		internalDeserialize();
		return mValues.keySet();
	}

	public Iterable<Object> getPropertyValues()
	{
		return mValues.values();
	}

	public void setInternalProperties(NameMap<Object> table)
	{
		mDeserialized = true;
		mValues = table;
	}

	@Override
	public boolean setProperty(String name, Object value)
	{
		if (name != null && value != null)
		{
			mValues.put(name, value);
			return true;
		}

		Services.Log.warning("PropertiesObject.setProperty", "Null key or value is not supported, ignoring.");
		return false;
	}

	@Override
	public Object getProperty(String name)
	{
		internalDeserialize();
		return mValues.get(name);
	}

	public <T> T getProperty(Class<T> type, String name)
	{
		Object value = getProperty(name);
		return Cast.as(type, value);
	}

	@Override
	public @NonNull String optStringProperty(String name) {
		return getStringProperty(name, Strings.EMPTY);
	}

	public String getStringProperty(String name, String defaultValue) {
		Object objValue = getProperty(name);
		if (objValue == null)
			return defaultValue;
		return objValue.toString();
	}

	protected void internalDeserialize()
	{
		if (!mDeserialized && mObject != null)
		{
			internalDeserialize(mObject);
			mDeserialized = true;
			mObject = null;
			resetDirties();
		}
	}

	protected void internalDeserialize(INodeObject data)
	{
		for (String attName : data.names())
		{
			if (data.isAtomic(attName))
				setProperty(attName, data.getString(attName));
		}
	}

	public void deserialize(INodeObject obj)
	{
		mObject = obj;
	}

	public int optIntProperty(String propName)
	{
		return optIntProperty(propName, 0);
	}

	public int optIntProperty(String propName, int defaultValue)
	{
		String lengthString = (String) getProperty(propName);
		if (lengthString == null)
			return defaultValue;
		try
		{
			return Integer.parseInt(lengthString);
		} catch (NumberFormatException ex)
		{
			return defaultValue;
		}
	}

	public long optLongProperty(String val)
	{
		String lengthString = (String) getProperty(val);
		if (lengthString == null)
			return 0;
		try
		{
			return Long.parseLong(lengthString);
		} catch (NumberFormatException ex)
		{
			return 0;
		}
	}

	public boolean optBooleanProperty(String propName)
	{
		return getBooleanProperty(propName, false);
	}

	public boolean getBooleanProperty(String propName, boolean defaultValue)
	{
		Object value = getProperty(propName);
		if (value instanceof Boolean)
			return (Boolean) value;

		if (value instanceof String)
			return Services.Strings.tryParseBoolean((String) value, defaultValue);

		return defaultValue;
	}

	public void resetDirties()
	{
		mDirtyBySetMap = new NameMap<>();
		mDirtyByChangeMap = new NameMap<>();
	}

	public Boolean isDirtyBySet(String name)
	{
		Boolean objValue = mDirtyBySetMap.get(name);
		if (objValue == null)
			objValue = false;
		return objValue;
	}

	public Boolean isDirtyByChange(String name)
	{
		Boolean objValue = mDirtyByChangeMap.get(name);
		if (objValue == null)
			objValue = false;
		return objValue;
	}

	public void setDirtyBySet(String name)
	{
		mDirtyBySetMap.put(name, true);
	}

	public void setDirtyByChange(String name)
	{
		mDirtyByChangeMap.put(name, true);
	}
}
