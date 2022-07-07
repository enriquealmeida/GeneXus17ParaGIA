package com.genexus.android.core.activities;

public class IntentParameters
{
	public static final int REQUEST_CODE_PROMPT = 3;

	// TODO: Intent extra names should begin with the package name.
	public static final String DATA_VIEW = "DataView";
	public static final String PARAMETERS = "Parameters";
	public static final String MODE = "Mode";

	public static final String DASHBOARD_METADATA = "DashBoardMetadata";
	public static final String IS_SELECTING = "IsSelecting";

	public static final String IS_STARTUP_ACTIVITY = "IsStartupActivity";

	public static final String ATT_NAME = "AttName";
	public static final String RANGE_BEGIN = "RangeBegin";
	public static final String RANGE_END = "RangeEnd";
	public static final String FILTER_DEFAULT = "FilterDefault";
	public static final String FILTER_RANGE_FK = "FilterRangeFk";

	public static final String BC_FIELD_PARAMETERS = "BCFieldParameters";

	public static final String SERVER_URL = "ServerURL";

	public static final String RELOAD_METADATA = "ReloadMetadata";

	public static final String SEARCH_BOX_DEFINITION = "SearchBoxDefinition";

	/**
	 * Intent parameters associated to the Filters activity and related functionality.
	 */
	public static class Filters
	{
		public static final String DATA_SOURCE_ID = "DataSourceId";
		public static final String DATA_SOURCE = "DataSource";
		public static final String URI = "GxUri";
		public static final String FILTERS_FK = "FiltersFK";
	}

	/**
	 * Intent parameters used exclusively for communication with EntityService.
	 */
	public static class Service
	{
		public static final String DATA_VIEW_SESSION = "DataViewSession";
		public static final String DATA_PROVIDER = "DataProvider";
		public static final String REQUEST_TYPE = "RequestType";
		public static final String INTENT_FILTER = "IntentFilter";
		public static final String REQUEST_COUNT = "RequestCount";
	}

	/***
	 * Used to pass Connectivity Support property
	 */
	public static final String CONNECTIVITY = "Connectivity";

	/**
	 * Used to launch MiniApps
	 */
	public static class SuperApps {
		public static final String URL = "url";
		public static final String NAME = "name";
		public static final String MAIN = "main";
		public static final String LOAD = "load";
		public static final String UNLOAD = "unload";
		public static final String APP_DEFINITION = "com.genexus.superapps.APP_DEFINITION";
	}
}
