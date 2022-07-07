package com.genexus.android.core.base.metadata.enums;

/**
 * App Entry Types
 */
public final class GxObjectTypes
{
	// Directly callable as application.
	public static final String WORK_WITH_GUID = "15cf49b5-fc38-4899-91b5-395d02d79889";
	public static final String DASHBOARD_GUID = "9bdcc055-174e-4af6-96cb-a2ceef6c5f09";
	public static final String SD_PANEL_GUID = "d82625fd-5892-40b0-99c9-5c8559c197fc";

	// Other GX objects.
	private static final String DATA_PROVIDER_GUID = "2a9e9aba-d2de-4801-ae7f-5e3819222daf";
	private static final String QUERY_GUID = "926a06b9-3417-4ab4-9f8c-09c2f626bb1c";
	private static final String PROCEDURE_GUID = "84a12160-f59b-4ad7-a683-ea4481ac23e9";
	private static final String TRANSACTION_GUID = "1db606f2-af09-4cf9-a3b5-b481519d28f6";
	private static final String WEB_PANEL_GUID = "c9584656-94b6-4ccd-890f-332d11fc2c25";
	private static final String API_GUID = "c163e562-42c6-4158-ad83-5b21a14cf30e";
	private static final String VARIABLE_OBJECT_GUID = "00000000-0000-0000-0000-000000000000";

	public static final short NONE = 0;

	public static final short PROCEDURE = 1;
	public static final short TRANSACTION = 2;
	public static final short WEBPANEL = 3;
	public static final short SDPANEL = 4;
	public static final short DASHBOARD = 5;
	public static final short API = 6;
	public static final short DATAPROVIDER = 7;
	public static final short QUERY = 8;

	public static final short VARIABLE_OBJECT = 10;

	private static short getGxObjectTypeFromGuid(String guid)
	{
		if (guid.equals(TRANSACTION_GUID))
			return TRANSACTION;
		if (guid.equals(WEB_PANEL_GUID))
			return WEBPANEL;
		if (guid.equals(DASHBOARD_GUID))
			return DASHBOARD;
		if (guid.equals(WORK_WITH_GUID) || guid.equals(SD_PANEL_GUID))
			return SDPANEL; // SDPanels are merged with WWSD.
		if (guid.equals(API_GUID))
			return API;
		if (guid.equals(PROCEDURE_GUID))
			return PROCEDURE;
		if (guid.equals(DATA_PROVIDER_GUID))
			return DATAPROVIDER;
		if (guid.equals(QUERY_GUID))
			return QUERY;
		if (guid.equals(VARIABLE_OBJECT_GUID))
			return VARIABLE_OBJECT;
		return NONE;
	}

	public static short getGxObjectTypeFromName(String name)
	{
		if (name != null && name.length() > 37)
		{
			String guidObject = name.substring(0, 36);
			return getGxObjectTypeFromGuid(guidObject);
		}
		return NONE;
	}
}
