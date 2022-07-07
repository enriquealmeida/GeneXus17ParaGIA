package com.genexus.android.core.base.metadata;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.Vector;

import com.genexus.android.core.base.metadata.enums.DataTypes;
import com.genexus.android.core.base.model.PropertiesObject;
import com.genexus.android.core.base.model.ValueCollection;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.NameMap;
import com.genexus.android.core.base.utils.Strings;

public class BasicDataType implements ITypeDefinition, Serializable
{
	//TODO: null date now return null, see what is the correct value to return.
	public static final String NULL_DATE = "0000-00-00";
	public static final String NULL_DATETIME = "0000-00-00T00:00:00";
	public static final String NULL_DATETIME_MILLISECONDS = "0000-00-00T00:00:00.000";

	private static final long serialVersionUID = 1L;

	private final PropertiesObject mProps;

	public BasicDataType(NameMap<Object> props)
	{
		mProps = new PropertiesObject(props);
	}

	@Override
	public String getType()
	{
		return (String) mProps.getProperty("Type");
	}

	@Override
	public int getLength()
	{
		return mProps.optIntProperty("Length");
	}

	@Override
	public int getDecimals()
	{
		return mProps.optIntProperty("Decimals");
	}

	@Override
	public boolean getSigned()
	{
		return false;
	}

	@Override
	public boolean getIsEnumeration()
	{
		return false;
	}

	@Override
	public Object getProperty(String propName)
	{
		return mProps.getProperty(propName);
	}

	@Override
	public String getName()
	{
		return getType();
	}

	@Override
	public ITypeDefinition getBaseType()
	{
		return null;
	}

	@Override
	public Vector<EnumValuesDefinition> getEnumValues()
	{
		return null;
	}

	@Override
	public Object getEmptyValue(boolean isCollection)
	{
		String dataType = getType();
		if (dataType != null)
		{
			if (!isCollection)
			{
				if (dataType.equalsIgnoreCase(DataTypes.DATETIME) || dataType.equalsIgnoreCase(DataTypes.DTIME) || dataType.equalsIgnoreCase(DataTypes.TIME))
					return NULL_DATETIME;
				else if (dataType.equalsIgnoreCase(DataTypes.DATE))
					return NULL_DATE;
				else if (dataType.equalsIgnoreCase("boolean"))
					return "false";
				else if (dataType.equalsIgnoreCase(DataTypes.GUID))
					return new UUID(0L, 0L).toString();
				if (dataType.equals(DataTypes.NUMERIC))
					return Strings.ZERO;
			}
			else
				return new ValueCollection(this);
		}

		return Strings.EMPTY;
	}

	@Override
	public boolean isEmptyValue(Object value)
	{
		if (value instanceof ValueCollection && ((ValueCollection)value).size() == 0)
			return true;

		if (value != null)
		{
			// Special cases: numeric can be "0" or "0.0" or similar.
			if (DataTypes.NUMERIC.equalsIgnoreCase(getType()) && getDecimals() != 0)
			{
				BigDecimal number = Services.Strings.tryParseDecimal(value.toString());
				if (number != null && number.floatValue() == 0f)
					return true;
			}

			String strValue = value.toString();
			String strDefault = getEmptyValue(false).toString();
			return strValue.equals(strDefault);
		}
		else
			return true;
	}
}
