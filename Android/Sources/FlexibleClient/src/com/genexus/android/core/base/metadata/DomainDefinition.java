package com.genexus.android.core.base.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.genexus.android.core.base.metadata.enums.ControlTypes;
import com.genexus.android.core.base.serialization.INodeCollection;
import com.genexus.android.core.base.serialization.INodeObject;

public class DomainDefinition extends DataTypeDefinition implements Serializable
{
	private static final long serialVersionUID = 1L;
	private final List<EnumValuesDefinition> mEnumValues = new ArrayList<>();

	public DomainDefinition(INodeObject jsonData)
	{
		super(jsonData);

		INodeCollection enumValues = jsonData.optCollection("EnumValues");
		for (int j = 0; j < enumValues.length() ; j++) {
			INodeObject objEnumValue = enumValues.getNode(j);

			String enumValueName = objEnumValue.getString("Name");
			String enumValueValue = objEnumValue.getString("Value");
			String enumValueDescription = objEnumValue.getString("Description");

			EnumValuesDefinition valuesDef = new EnumValuesDefinition();
			valuesDef.setName(enumValueName);
			valuesDef.setValue(enumValueValue);
			valuesDef.setDescription(enumValueDescription);

			mEnumValues.add(valuesDef);
		}

		if (enumValues.length() > 0)
			setProperty("IsEnumeration", "true");
	}

	@Override
	public List<EnumValuesDefinition> getEnumValues()
	{
		return mEnumValues;
	}

	public EnumValuesDefinition getEnumValueByName(String name)
	{
		for(int i = 0; i < mEnumValues.size(); i++)
		{
			EnumValuesDefinition rel = mEnumValues.get(i);
			if (rel.getName().equalsIgnoreCase(name))
				return rel;
		}

		return null;
	}

	public EnumValuesDefinition getEnumValueByValue(String value)
	{
		for(int i = 0; i < mEnumValues.size(); i++)
		{
			EnumValuesDefinition rel = mEnumValues.get(i);
			if(rel.getValue().equalsIgnoreCase(value))
				return rel;
		}
		return null;
	}


	@Override
	public boolean getIsEnumeration()
	{
		return mEnumValues != null && mEnumValues.size() > 0;
	}

	static boolean isSpecialDomain(ITypeDefinition domain)
	{
		//TODO data
		DataTypeName name = new DataTypeName(domain.getName());
		return !name.getControlType().equalsIgnoreCase(ControlTypes.TEXT_BOX)
				|| (name.getActions().size() != 0)
				|| domain.getIsEnumeration();
	}
}
