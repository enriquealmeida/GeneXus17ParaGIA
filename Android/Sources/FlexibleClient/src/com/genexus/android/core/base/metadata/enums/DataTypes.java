package com.genexus.android.core.base.metadata.enums;

import com.genexus.android.core.base.metadata.BasicDataType;
import com.genexus.android.core.base.metadata.DataTypeName;
import com.genexus.android.core.base.metadata.ITypeDefinition;
import com.genexus.android.core.base.metadata.LevelDefinition;
import com.genexus.android.core.base.metadata.StructureDataType;
import com.genexus.android.core.base.metadata.StructureDefinition;
import com.genexus.android.core.base.metadata.loader.MetadataLoader;
import com.genexus.android.core.base.metadata.types.BusinessComponentDataType;
import com.genexus.android.core.base.metadata.types.SdtCollectionItemDataType;
import com.genexus.android.core.base.metadata.types.UserDefinedTypeDataType;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.NameMap;
import com.genexus.android.core.base.utils.Strings;

import java.util.ArrayList;
import java.util.List;

public final class DataTypes
{
	public static final String TEXT = "text";
	public static final String NUMERIC = "numeric";
	public static final String GUID = "guid";
	public static final String DATE = "date";
	public static final String DTIME = "dtime";
	public static final String TIME = "time";
	public static final String DATETIME = "datetime";
	public static final String BOOL = "bool";
	public static final String IMAGE = "bits";
	public static final String VIDEO = "video";
	public static final String BLOB = "binary";
	public static final String AUDIO = "audio";
	public static final String BLOBFILE = "binaryfile";
	public static final String SDT = "gx_sdt";
	public static final String BUSINESS_COMPONENT = "gx_buscomp";
	public static final String USER_TYPE = "gx_usrdeftyp";

	public static ITypeDefinition getDataTypeOf(NameMap<Object> item)
	{
		// It's a Domain (or Attribute)?
		String domainName = (String)item.get("Domain");
		if (Services.Strings.hasValue(domainName))
		{
			domainName = MetadataLoader.getObjectName(domainName);
			ITypeDefinition domain = Services.Application.getDefinition().getDomain(domainName);

			if (domain == null)
				domain = Services.Application.getDefinition().getAttribute(domainName);

			if (domain == null)
			{
				// Don't stop here if domain could not be loaded, maybe we can make do with the basic type.
				Services.Log.warning(String.format("Domain or attribute '%s' in data type definition could not be resolved.", domainName));
			}
			else
				return domain;
		}

		// It's an SDT or BC?
		String type = (String)item.get("Type");
		if (type != null)
		{
			if (type.equalsIgnoreCase(DataTypes.SDT))
			{
				String sdtName = (String)item.get("TypeName");
				StructureDataType dt = Services.Application.getDefinition().getSDT(sdtName);

				if (dt != null)
					return dt;

				// Support item of SDT collection as a special case.
				int separatorPos = sdtName.lastIndexOf(Strings.DOT);
				if (separatorPos != -1)
				{
					StructureDataType sdt = Services.Application.getDefinition().getSDT(sdtName.substring(0, separatorPos));
					if (sdt != null && sdt.isCollection()) {
						String collectionItemName = sdt.getRoot().getCollectionItemName();
						if (Strings.hasValue(collectionItemName) && collectionItemName.equalsIgnoreCase(sdtName.substring(separatorPos + 1)))
							return new SdtCollectionItemDataType(sdt);
					}
				}

				// Support any level.
				List<String> itemNameList = new ArrayList<>();
				while (separatorPos != -1) {
					itemNameList.add(0, sdtName.substring(separatorPos + 1));
					sdtName = sdtName.substring(0, separatorPos);
					StructureDataType sdt = Services.Application.getDefinition().getSDT(sdtName);
					if (sdt != null) {
						LevelDefinition level = sdt.getRoot();
						for (String itemName : itemNameList) {
							level = level.getLevel(itemName);
							if (level == null)
								break;
						}
						if (level != null)
							return new SdtCollectionItemDataType(sdt, level, level.isCollection());
					}
					separatorPos = sdtName.lastIndexOf(Strings.DOT);
				}

				Services.Log.warning(String.format("SDT type '%s' in data type definition could not be resolved.", sdtName));
				return null;
			}
			else if (type.equalsIgnoreCase(DataTypes.BUSINESS_COMPONENT))
			{
				String bcName = (String)item.get("TypeName");
				StructureDefinition bc = Services.Application.getDefinition().getBusinessComponent(bcName);

				if (bc != null)
					return new BusinessComponentDataType(bc);

				Services.Log.warning(String.format("BC type '%s' in data type definition could not be resolved.", bcName));
				return null;
			}
			else if (type.equalsIgnoreCase(DataTypes.USER_TYPE))
			{
				String typeName = (String)item.get("TypeName");
				return new UserDefinedTypeDataType(typeName);
			}
		}

		// Should be a basic type.
		return new BasicDataType(item);
	}

	public static boolean isMedia(String dataType) {
		return isImage(dataType) || isVideo(dataType) || isAudio(dataType) || isFile(dataType);
	}

	public static boolean isImage(String dataType)
	{
		return (dataType != null &&
				(dataType.equalsIgnoreCase(DataTypes.IMAGE) ||
				 dataType.equalsIgnoreCase("bitmap")));
	}

	public static boolean isVideo(String dataType) {
		return (dataType != null && dataType.equalsIgnoreCase(DataTypes.VIDEO));
	}

	public static boolean isAudio(String dataType) {
		return (dataType != null && dataType.equalsIgnoreCase(DataTypes.AUDIO));
	}

	public static boolean isFile(String dataType) {
		return (dataType != null && dataType.equalsIgnoreCase(DataTypes.BLOBFILE));
	}


	/**
	 * Returns true if the data type is any of the character-based GX types (char, varchar, longvarchar).
	 */
	public static boolean isCharacter(String dataType)
	{
		return (dataType != null &&
				(dataType.equalsIgnoreCase("char") ||
				 dataType.equalsIgnoreCase("character") ||
				 dataType.equalsIgnoreCase("vchar") ||
				 dataType.equalsIgnoreCase("varchar") ||
				 dataType.equalsIgnoreCase("longvarchar")));
	}

	public static boolean isCharacterDomain(String dataType)
	{
		return (dataType != null &&
				(dataType.equalsIgnoreCase(DataTypeName.ADDRESS) ||
						dataType.equalsIgnoreCase( DataTypeName.EMAIL) ||
						dataType.equalsIgnoreCase( DataTypeName.GEOLOCATION) ||
						dataType.equalsIgnoreCase(DataTypeName.PHONE) )	);
	}

	public static boolean isLongCharacter(String dataType)
	{
		return (dataType != null && dataType.equalsIgnoreCase("longvarchar"));
	}

	public static boolean isDateTime(String dataType)
	{
		return (dataType != null &&
				(dataType.equalsIgnoreCase(DATE) ||
				 dataType.equalsIgnoreCase(DTIME) ||
				 dataType.equalsIgnoreCase(DATETIME) ||
				 dataType.equalsIgnoreCase(TIME)));
	}

	public static boolean isTime(String dataType, int length) {
		if (dataType == null)
			return false;

		if (dataType.equals(DataTypes.DTIME) || dataType.equals(DataTypes.DATETIME))
			return length == 0; // datetime with only time
		else if (dataType.equals(DataTypes.TIME))
			return true;

		return false;
	}
}
