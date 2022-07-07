package com.genexus.android.core.base.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.genexus.android.core.base.model.PropertiesObject;

public class EnumDomainDefinition extends PropertiesObject implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final List<EnumValuesDefinition> mEnumValues = new ArrayList<>();

	public List<EnumValuesDefinition> getEnumValues()
	{
		return mEnumValues;
	}

	public EnumValuesDefinition getEnumValueByName(String name)
	{
		for (EnumValuesDefinition ev : mEnumValues)
		{
			if(ev.getName().equalsIgnoreCase(name))
				return ev;
		}

		return null;
	}

	public EnumValuesDefinition getEnumValueByValue(String value)
	{
		for (EnumValuesDefinition ev : mEnumValues)
		{
			if(ev.getValue().equalsIgnoreCase(value))
				return ev;
		}

		return null;
	}
}
