package com.genexus.android.location.geolocation;

import android.content.Context;
import android.location.Location;

import com.genexus.android.LocationAccuracy;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.GeoFormats;
import com.genexus.android.core.controls.maps.LocationApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class LocationEntityFactory {
	/**
	 * Returns the JSON representing a GeoLocationInfo SDT
	 */
	public static JSONObject createLocationEntity(Location location, Context context) {
		return createLocationEntity(location, LocationApi.GEOLOCATION, context);
	}

	public static JSONObject createLocationEntity(Location location, LocationApi apiType, Context context) {
		JSONObject jsonProperty = new JSONObject();
		try {
			String locationString = apiType == LocationApi.MAPS ? GeoFormats.buildGeopoint(location.getLatitude(), location.getLongitude())
					: GeoFormats.buildGeolocation(location.getLatitude(), location.getLongitude());

			jsonProperty.put(Constants.SDT_LOCATION_INFO_LOCATION, locationString);
			jsonProperty.put(Constants.SDT_LOCATION_INFO_DESCRIPTION, String.format("LocationInfo ('%s')", location.getProvider()));
			jsonProperty.put(Constants.SDT_LOCATION_INFO_TIME, Services.Strings.getDateTimeStringForServer(new Date(location.getTime())));
			jsonProperty.put(Constants.SDT_LOCATION_INFO_PRECISION, String.valueOf(location.getAccuracy()));
			boolean reducedAccuracy = LocationAccuracy.getStatus(context) != LocationAccuracy.FULL;
			jsonProperty.put(Constants.SDT_LOCATION_INFO_RESTRICTED_ACCURACY, String.valueOf(reducedAccuracy));
			float bearing = location.hasBearing() ? location.getBearing() : -1;
			jsonProperty.put(Constants.SDT_LOCATION_INFO_HEADING, String.valueOf(bearing));
			float speed = location.hasSpeed() ? location.getSpeed() : -1;
			jsonProperty.put(Constants.SDT_LOCATION_INFO_SPEED, String.valueOf(speed));
		} catch (JSONException e) {
			Services.Log.error("Error creating JSON for Location", "Exception in JSONObject.put()", e);
		}
		return jsonProperty;
	}
}
