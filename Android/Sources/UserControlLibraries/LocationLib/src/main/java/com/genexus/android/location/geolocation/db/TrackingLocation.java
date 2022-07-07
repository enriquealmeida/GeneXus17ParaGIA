package com.genexus.android.location.geolocation.db;

import android.util.Pair;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.GeoFormats;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.maps.LocationApi;
import com.genexus.android.location.geolocation.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class TrackingLocation {
	private Integer id;
	private String geolocation;
	private long dateTime;

	public TrackingLocation() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public long getDateTimetime() {
		return dateTime;
	}

	public void setDateTimetime(long dateTime) {
		this.dateTime = dateTime;
	}

	public String getGeolocationJson() {
		return geolocation;
	}

	public void setGeolocationJson(String geolocation, LocationApi apiType) {
		this.geolocation = geolocation;
		if (apiType == LocationApi.MAPS)
			convertLocationToGeopoint();
	}

	private void convertLocationToGeopoint() {
		try {
			JSONObject jsonObject = new JSONObject(geolocation);
			String location = jsonObject.optString(Constants.SDT_LOCATION_INFO_LOCATION);
			if (Strings.hasValue(location)) {
				Pair<Double, Double> latLng = GeoFormats.parseGeolocation(location);
				if (latLng != null) {
					String geopoint = GeoFormats.buildGeopoint(latLng.first, latLng.second);
					jsonObject.put(Constants.SDT_LOCATION_INFO_LOCATION, geopoint);
					geolocation = jsonObject.toString();
				}
			}
		} catch (JSONException exception) {
			Services.Log.error(exception);
		}
	}
}
