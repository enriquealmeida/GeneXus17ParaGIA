package com.genexus.android.core.base.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.genexus.android.core.base.metadata.DataItem;
import com.genexus.android.core.base.metadata.EnumValuesDefinition;
import com.genexus.android.core.base.metadata.StructureDefinition;
import com.genexus.android.core.base.services.Services;

public class ParametersStringUtil
{
	public static List<String> getKeyValuesFromFieldValues(Map<String, String> fieldValues, StructureDefinition mStructure)
	{
		ArrayList<String> keys = new ArrayList<>();

		for (DataItem att : mStructure.Root.getKeys())
		{
			String result = fieldValues.get(att.getName());
			if (result != null && result.length() > 0)
				keys.add(result);
		}

		return keys;
	}

	public static String getDescriptionOfEnum(List<EnumValuesDefinition> values, String valueText)
	{
		//Get description of Enum
		if (values != null)
		{
			for (int i = 0; i < values.size(); i++)
			{
				EnumValuesDefinition rel = values.get(i);
				if (rel.getValue().equalsIgnoreCase(valueText))
					return rel.getDescription();
			}
		}
		return valueText;
	}

    public static String getValueFromFilter(String filtersString, String value)
    {
		if (filtersString.length() == 0 || value.length() == 0)
			return Strings.EMPTY;

    	String [] vals = Services.Strings.split(filtersString, '&');
		for (String val : vals)
		{
			String[] oneVal = Services.Strings.split(val, '=');

			if (oneVal.length > 1 && oneVal[0].equalsIgnoreCase(value))
				return Services.HttpService.uriDecode(oneVal[1]);
		}

		return Strings.EMPTY;
	}
}
