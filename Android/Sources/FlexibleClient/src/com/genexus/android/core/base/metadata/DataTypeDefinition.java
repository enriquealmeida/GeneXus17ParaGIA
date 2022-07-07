package com.genexus.android.core.base.metadata;

import java.io.Serializable;
import java.util.List;

import com.genexus.android.core.base.metadata.enums.DataTypes;
import com.genexus.android.core.base.model.PropertiesObject;
import com.genexus.android.core.base.serialization.INodeObject;

public abstract class DataTypeDefinition extends PropertiesObject implements ITypeDefinition, Serializable
{
	private static final long serialVersionUID = 1L;

	private ITypeDefinition mBaseDataType = null;

	public DataTypeDefinition(INodeObject jsonData)
	{
		super.deserialize(jsonData);
	}

	@Override
	public ITypeDefinition getBaseType()
	{
		if (mBaseDataType == null)
			mBaseDataType = DataTypes.getDataTypeOf(getInternalProperties());

		return mBaseDataType;
	}

	@Override
	public String getName() {
		return super.optStringProperty("Name");
	}

	@Override
	public String getType() {
		return getBaseType().getType();
	}

	@Override
	public int getLength() {
		return super.optIntProperty("Length");
	}

	@Override
	public int getDecimals() {
		return super.optIntProperty("Decimals");
	}

	@Override
	public boolean getSigned() {
		return super.optBooleanProperty("Signed");
	}

	@Override
	public boolean getIsEnumeration() {
		return super.optBooleanProperty("IsEnumeration");
	}

	@Override
	public List<EnumValuesDefinition> getEnumValues()
	{
		return null;
	}

	@Override
	public Object getProperty(String propName)
	{
		Object localValue = super.getProperty(propName);
		if (localValue == null && getBaseType() != null) // We don't have a value for this property so inherit from based on type
			localValue = getBaseType().getProperty(propName);
		return localValue;
	}

	public String getInputPicture() {
		return super.optStringProperty("InputPicture");
	}

	@Override
	public Object getEmptyValue(boolean isCollection)
	{
		if (getBaseType() != null)
			return getBaseType().getEmptyValue(isCollection);

		return null;
	}

	@Override
	public boolean isEmptyValue(Object value)
	{
		if (getBaseType() != null)
			return getBaseType().isEmptyValue(value);
		else
			return (value == null);
	}
}
