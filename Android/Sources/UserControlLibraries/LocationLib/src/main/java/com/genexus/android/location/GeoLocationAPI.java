package com.genexus.android.location;

import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.controls.maps.LocationApi;

public class GeoLocationAPI extends LocationApiBase {
	public static final String OBJECT_NAME = "GeneXus.Common.Geolocation";
	public GeoLocationAPI(ApiAction action) { super(LocationApi.GEOLOCATION, action); }
}
