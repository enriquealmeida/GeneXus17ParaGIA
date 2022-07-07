package com.genexus.android.core.base.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.genexus.android.core.base.metadata.enums.ActionTypes;
import com.genexus.android.core.base.metadata.enums.ControlTypes;
import com.genexus.android.core.base.metadata.enums.DataTypes;
import com.genexus.android.core.base.utils.NameMap;
import com.genexus.android.core.base.utils.Strings;

public class DataTypeName implements Serializable
{
	private static final long serialVersionUID = 1L;

	// Semantic domains
	public static final String ADDRESS = "GeneXus.Address";
	public static final String COMPONENT = "GeneXus.Component";
	public static final String EMAIL = "GeneXus.Email";
	public static final String GEOLOCATION = "GeneXus.Geolocation";
	public static final String HTML = "GeneXus.Html";
	public static final String PHONE = "GeneXus.Phone";
	public static final String URL = "GeneXus.Url";

	private static final Set<String> DOMAINS;
	private static final NameMap<String> CONTROLS_FOR_DOMAINS;
	private static final NameMap<String> ACTIONS_FOR_DOMAINS;

	private String mDataType;

	static
	{
		DOMAINS = Strings.newSet(ADDRESS, COMPONENT, EMAIL, GEOLOCATION, HTML, PHONE, URL);

		CONTROLS_FOR_DOMAINS = new NameMap<>();
		CONTROLS_FOR_DOMAINS.put(COMPONENT, ControlTypes.WEB_VIEW);
		CONTROLS_FOR_DOMAINS.put(EMAIL, ControlTypes.EMAIL_TEXT_BOX);
		CONTROLS_FOR_DOMAINS.put(GEOLOCATION, ControlTypes.LOCATION_CONTROL);
		CONTROLS_FOR_DOMAINS.put(HTML, ControlTypes.WEB_VIEW);
		CONTROLS_FOR_DOMAINS.put(PHONE, ControlTypes.PHONE_NUMERIC_TEXT_BOX);

		ACTIONS_FOR_DOMAINS = new NameMap<>();
		ACTIONS_FOR_DOMAINS.put(ADDRESS, ActionTypes.LOCATE_ADDRESS);
		ACTIONS_FOR_DOMAINS.put(EMAIL, ActionTypes.SEND_EMAIL);
		ACTIONS_FOR_DOMAINS.put(GEOLOCATION, ActionTypes.LOCATE_GEO_LOCATION);
		ACTIONS_FOR_DOMAINS.put(PHONE, ActionTypes.CALL_NUMBER);
		ACTIONS_FOR_DOMAINS.put(URL, ActionTypes.VIEW_URL);
	}

	public String getDataType()
	{
		return mDataType;
	}

	public DataTypeName(String dataType)
	{
		// Normalize standard types.
		dataType = Strings.toLowerCase(dataType);
		if (dataType.equalsIgnoreCase("int"))
			dataType = "numeric";
		if (dataType.equalsIgnoreCase("char"))
			dataType = "string";
		if (dataType.equalsIgnoreCase("vchar"))
			dataType = "string";
		if (dataType.equalsIgnoreCase("svchar"))
			dataType = "string";
		if (dataType.equalsIgnoreCase("boolean"))
			dataType = "bool";

		// Normalize old domains to new names, for (temporary) compatibility.
		if (DOMAINS.contains("GeneXus." + dataType))
			dataType = "GeneXus." + dataType;

		mDataType = dataType;
	}

	public List<String> getActions()
	{
		ArrayList<String> list = new ArrayList<>();
		if (Strings.hasValue(mDataType))
		{
			String action = ACTIONS_FOR_DOMAINS.get(mDataType);
			if (action != null)
				list.add(action);
		}

		return list;
	}

	public String getControlType()
	{
		if (Strings.hasValue(mDataType))
		{
			String domainControlType = CONTROLS_FOR_DOMAINS.get(mDataType);
			if (domainControlType != null)
				return domainControlType;

			// For basic types
			if (mDataType.equalsIgnoreCase(DataTypes.NUMERIC))
				return ControlTypes.NUMERIC_TEXT_BOX;
			else if (mDataType.equalsIgnoreCase(DataTypes.VIDEO))
				return ControlTypes.VIDEO_VIEW;
			else if (mDataType.equalsIgnoreCase(DataTypes.AUDIO))
				return ControlTypes.AUDIO_VIEW;
			else if (mDataType.equalsIgnoreCase(DataTypes.DATE) || mDataType.equalsIgnoreCase(DataTypes.DTIME) || mDataType.equalsIgnoreCase(DataTypes.TIME) || mDataType.equalsIgnoreCase(DataTypes.DATETIME))
				return ControlTypes.DATE_BOX;
			else if (DataTypes.isImage(mDataType))
				return ControlTypes.PHOTO_EDITOR;
			else
				return ControlTypes.TEXT_BOX;
		}

		return null;
	}
}
