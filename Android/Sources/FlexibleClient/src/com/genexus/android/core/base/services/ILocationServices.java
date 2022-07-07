package com.genexus.android.core.base.services;

import android.app.Activity;
import android.location.Location;

import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.controls.maps.LocationApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

public interface ILocationServices {

	String[] getRequiredPermissions();
	String[] getRequestPermissions();

	Location getLastKnownLocation();
	JSONObject getCurrentLocation(Activity activity, int minAccuracy, int timeout, boolean includeHeadingAndSpeed, boolean ignoreErrors, boolean showMessages, LocationApi apiType);

	void onLocationChange(Location location);
	void addOnLocationChangeListener(IOnLocationChangeListener listener);
	void removeOnLocationChangeListener(IOnLocationChangeListener listener);

	void setProvider(ILocationProvider locationProvider);
	void clearProvider();

	int getAuthorizationStatus();
	boolean isAuthorized();
	boolean isEnabled();
	boolean isReady();

	boolean isTracking();
	void startTracking(Activity activity, int changesInterval, int minDistance, int actionTimeInterval, String action, int accuracy, boolean useForegroundService);
	void endTracking();
	void restoreTracking(boolean isTracking, Date lastLocationActionTime, String action, int actionInterval);

	void createProximityAlert(String name, String description, String geolocation, int radius, String expirationDateTime, String actionName, Boolean addToDatabase, int paUniqueId);
	boolean setProximityAlerts(EntityList proximityAlerts, LocationApi apiType);
	void clearProximityAlerts();
	void resetProximityAlerts();
	EntityList getProximityAlerts(LocationApi apiType);
	Entity getCurrentProximityAlert(LocationApi apiType);
	void setCurrentProximityAlert(int geofenceRequestId);

	JSONArray getLocationHistory(Date startDate, LocationApi apiType);
	void clearLocationHistory();

	JSONArray geocodeAddress(String address, LocationApi apiType);
	JSONArray reverseGeocodeAddress(String locationString);

	interface IOnLocationChangeListener {
		void onLocationChange(Location location);
	}
}
