package com.genexus.android.core.base.metadata;

import java.util.List;

public interface ITypeDefinition
{
	String getName();

	String getType();
	int getLength();
	int getDecimals();
	boolean getSigned();

	boolean getIsEnumeration();
	List<EnumValuesDefinition> getEnumValues();

	Object getProperty(String propName);

	ITypeDefinition getBaseType();

	Object getEmptyValue(boolean isCollection);
	boolean isEmptyValue(Object value);
}
