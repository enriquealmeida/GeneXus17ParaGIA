package com.artech.android.device;

import android.content.Context;
import android.location.Location;

import org.json.JSONObject;

/**
 * Geolocations helper methods used in Flexible client. exported from core EO module
 */
public interface IGeoLocationHelper {
	boolean createFusedLocationHelper();
	Location getLastKnownLocation();
	String[] getRequiredPermissions();
	String[] getRequestPermissions();
	String getLocationString(Location location);
	JSONObject getLocationJsonGeoLocationInfo(Integer minAccuracy, Integer timeout, boolean includeHeadingAndSpeed);
	void requestLocationUpdatesLocationManager(Integer minTime, Integer minDistance, boolean includeHeadingAndSpeed, String action, Integer actionInterval);
	void removeLocationUpdatesFromLocationManager();
	void requestLocationUpdatesFused(Integer minDistance);
	void removeLocationUpdatesFromFused();
	void onLocationChange(Context context, Location location);

	void addOnLocationChangeListener(IOnLocationChangeListener listener);
	void removeOnLocationChangeListener(IOnLocationChangeListener listener);

	interface IOnLocationChangeListener {
		void onLocationChange(Location location);
	}
}
