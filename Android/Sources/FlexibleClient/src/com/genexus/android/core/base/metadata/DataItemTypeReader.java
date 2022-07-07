package com.genexus.android.core.base.metadata;

import com.genexus.android.core.base.metadata.enums.DataTypes;
import com.genexus.android.core.base.metadata.loader.MetadataLoader;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

class DataItemTypeReader
{
	public static void readDataType(DataItem item, INodeObject json)
	{
		item.setIsCollection(readBoolean(json, "isCollection"));
		if (!item.isCollection())
			item.setIsCollection(readBoolean(json, "IsCollection"));

		String domain = MetadataLoader.getObjectName(readString(json, "Domain"));
		String type = readString(json, "Type");
		String typeName = readString(json, "TypeName");

		if (type.equalsIgnoreCase(DataTypes.SDT) || type.equalsIgnoreCase(DataTypes.BUSINESS_COMPONENT) || type.equalsIgnoreCase(DataTypes.USER_TYPE))
		{
			item.setProperty("Type", type);
			item.setProperty("TypeName", typeName);
		}
		else
		{
			item.setProperty("Type", type);
			item.setProperty("Length", readString(json, "Length", Strings.ZERO));
			item.setProperty("Decimals", readString(json, "Decimals", Strings.ZERO));
			item.setProperty("Signed", readString(json, "Signed"));
			item.setProperty("InputPicture", readString(json, "picture", Strings.EMPTY));
			item.setProperty("IsPassword", readString(json, "isPassword"));

			if (Services.Strings.hasValue(domain))
				item.setProperty("Domain", domain);

			String maxUploadSize = readString(json, "MaximumUploadSize");
			if (Services.Strings.hasValue(maxUploadSize))
				item.setProperty("MaximumUploadSize", maxUploadSize);
		}

		item.setDataType(DataTypes.getDataTypeOf(item.getInternalProperties()));
	}

	private static String readString(INodeObject json, String key)
	{
		// In some cases the '@' is present and in some others it's not. Check for both.
		String value = json.optString(key);
		if (!Strings.hasValue(value))
			value = json.optString("@" + key);

		return value;
	}

	private static String readString(INodeObject json, String key, String defaultValue)
	{
		String value = readString(json, key);
		if (!Strings.hasValue(value))
			value = defaultValue;

		return value;
	}

	private static boolean readBoolean(INodeObject json, String key)
	{
		String strValue = readString(json, key);
		return Services.Strings.tryParseBoolean(strValue, false);

	}
}
