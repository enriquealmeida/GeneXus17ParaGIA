package com.genexus.android.location;

import java.util.Vector;

import androidx.annotation.NonNull;
import json.org.json.JSONObject;

import com.genexus.android.core.activities.ActivityHelper;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.GeoFormats;
import com.genexus.android.core.base.utils.ReflectionHelper;
import com.genexus.GXBaseCollection;
import com.genexus.GXSimpleCollection;
import com.genexus.android.core.controls.maps.LocationApi;
import com.genexus.android.location.geolocation.Constants;
import com.genexus.xml.GXXMLSerializable;

import org.json.JSONArray;
import org.json.JSONException;

import static com.genexus.android.LocationAccuracy.COARSE;

@SuppressWarnings({"unused", "WeakerAccess"})
public class GeolocationAPIOffline {

	private static final LocationApi GEOLOCATION = LocationApi.GEOLOCATION;

	public static double getLatitude(String geolocation) {
		return Double.parseDouble(GeoFormats.getLatitudeFromLocation(geolocation));
	}

	public static double getLongitude(String geolocation) {
		return Double.parseDouble(GeoFormats.getLongitudeFromLocation(geolocation));
	}

	public static int getDistance(String startLocation, String endLocation) {
		return GeoFormats.getDistanceFromLocations(startLocation, endLocation);
	}

	public static Vector<String> getAddress(String location) {
		Vector<String> addressesVector = new GXSimpleCollection<>();
		JSONArray addressCollection = Services.Location.reverseGeocodeAddress(location);
		for (int i = 0; i < addressCollection.length(); i++) {
			try {
				String address = addressCollection.getString(i);
				addressesVector.add(address);
			} catch (JSONException ex) {
				Services.Log.error(ex.getMessage());
			}
		}
		return addressesVector;
	}

	public static Vector<String> getLocation(String address) {
		Vector<String> geolocationVector = new GXSimpleCollection<>();
		JSONArray geolocationCollection = Services.Location.geocodeAddress(address, GEOLOCATION);
		for (int i = 0; i < geolocationCollection.length(); i++) {
			try {
				String location = geolocationCollection.getString(i);
				geolocationVector.add(location);
			} catch (JSONException ex) {
				Services.Log.error(ex.getMessage());
			}
		}
		return geolocationVector;
	}

	public static Object getmylocation(Integer minAccuracy, Integer timeout, Boolean includeHeadingAndSpeed) {
		return getmylocation(minAccuracy, timeout, includeHeadingAndSpeed, false);
	}

	public static Object getmylocation(Integer minAccuracy, Integer timeout, Boolean includeHeadingAndSpeed, Boolean ignoreErrors) {
		return MapsAPIOffline.Companion.executeRequestingPermission(Services.Location.getRequiredPermissions(), new String[]{COARSE},
			() -> internalGetMyLocation(minAccuracy, timeout, includeHeadingAndSpeed, ignoreErrors),
			() -> newGeolocationInfo());
	}

	private static Object internalGetMyLocation(Integer minAccuracy, Integer timeout, Boolean includeHeadingAndSpeed, Boolean ignoreErrors) {
		GXXMLSerializable geolocationInfo = newGeolocationInfo();

		// Convert json to GXXMLSerializable (SDT)
		org.json.JSONObject geolocationInfoJSONObj = Services.Location.getCurrentLocation(ActivityHelper.getCurrentActivity(),
				minAccuracy, timeout, includeHeadingAndSpeed, ignoreErrors, false, GEOLOCATION);

		if (geolocationInfoJSONObj != null)
			geolocationInfo.fromJSonString(geolocationInfoJSONObj.toString());

		return geolocationInfo;
	}

	private static @NonNull
	GXXMLSerializable newGeolocationInfo() {
		Class<?> clazz = ReflectionHelper.getClass(Object.class, Constants.CORE_MODULES_PACKAGE_OLD + "." + Constants.SDT_GEOLOCATION_INFO);
		if (clazz == null)
			throw new IllegalStateException(Constants.SDT_GEOLOCATION_INFO + " class could not be loaded!");

		Object obj = ReflectionHelper.createDefaultInstance(clazz, true);

		if (obj == null)
			throw new IllegalStateException(Constants.SDT_GEOLOCATION_INFO + " class could not be instantiated!");

		return (GXXMLSerializable) obj;
	}

	//START Proximity Alerts methods
	public static void clearproximityalerts() {
		Services.Location.clearProximityAlerts();
	}

	public static Object getcurrentproximityalert() {
		// use Core module sdt name
		Class<?> clazz = ReflectionHelper.getClass(Object.class, Constants.CORE_MODULES_PACKAGE_OLD + "." + Constants.SDT_GEOLOCATION_PROXIMITY_ALERT);
		if (clazz != null) {
			Object sdtGeoLocationInfo = ReflectionHelper.createDefaultInstance(clazz, true);

			// Convert json to GXXMLSerializable (SDT)
			Entity alert = Services.Location.getCurrentProximityAlert(GEOLOCATION);
			GXXMLSerializable result = (GXXMLSerializable) sdtGeoLocationInfo;
			if (result != null)
				result.fromJSonString(alert.toString());

			return sdtGeoLocationInfo;
		}

		Services.Log.error(String.format("getcurrentproximityalert fails, cannot get %s class", Constants.SDT_GEOLOCATION_PROXIMITY_ALERT));
		return null;
	}

	// setproximityalerts, getproximityalerts from procedures

	public static GXBaseCollection getproximityalerts() {
		Class clazzType = ReflectionHelper.getClass(Object.class, Constants.CORE_MODULES_PACKAGE_OLD + "." + Constants.SDT_GEOLOCATION_PROXIMITY_ALERT);
		if (clazzType == null) {
			Services.Log.error(Constants.SDT_GEOLOCATION_PROXIMITY_ALERT + " not found ");
			return null;
		}

		//noinspection unchecked
		GXBaseCollection base = new GXBaseCollection(clazzType, "GeolocationProximityAlert",
				Constants.SDT_PROXIMITY_ALERTS_GEOLOCATION, Services.Application.get().getRemoteHandle());

		EntityList proximityAlerts = Services.Location.getProximityAlerts(GEOLOCATION);
		json.org.json.JSONArray jsonAlerts = new json.org.json.JSONArray();
		try {
			for (int i=0; i<proximityAlerts.size();i++) {
				Entity current = proximityAlerts.get(i);
				json.org.json.JSONObject proximityAlertJSONObj = new json.org.json.JSONObject();
				proximityAlertJSONObj.put(Constants.SDT_PROXIMITY_ALERTS_NAME, current.optStringProperty(Constants.SDT_PROXIMITY_ALERTS_NAME));
				proximityAlertJSONObj.put(Constants.SDT_PROXIMITY_ALERTS_DESCRIPTION, current.optStringProperty(Constants.SDT_PROXIMITY_ALERTS_DESCRIPTION));
				proximityAlertJSONObj.put(Constants.SDT_PROXIMITY_ALERTS_GEOLOCATION, current.optStringProperty(Constants.SDT_PROXIMITY_ALERTS_GEOLOCATION));
				proximityAlertJSONObj.put(Constants.SDT_PROXIMITY_ALERTS_RADIUS, current.optStringProperty(Constants.SDT_PROXIMITY_ALERTS_RADIUS));
				proximityAlertJSONObj.put(Constants.SDT_PROXIMITY_ALERTS_EXPIRATION_TIME, current.optStringProperty(Constants.SDT_PROXIMITY_ALERTS_EXPIRATION_TIME));
				proximityAlertJSONObj.put(Constants.SDT_PROXIMITY_ALERTS_ACTION_NAME, current.optStringProperty(Constants.SDT_PROXIMITY_ALERTS_ACTION_NAME));
				jsonAlerts.put(proximityAlertJSONObj);
			}
		} catch (json.org.json.JSONException e) {
			Services.Log.warning("Failed to build proximity alerts collection");
		}

		base.fromJSonString(jsonAlerts.toString());
		return base;
	}

	public static boolean setproximityalerts(final GXBaseCollection proximityAlerts) {
		if (!Services.Location.isReady())
			return false; // Cannot add geofences without GMS. We shouldn't even request permission to do so.

		return MapsAPIOffline.Companion.executeRequestingPermission(Services.Location.getRequestPermissions(),
				() -> internalSetProximityAlerts(proximityAlerts),
				() -> false);
	}

	private static boolean internalSetProximityAlerts(GXBaseCollection proximityAlerts) {
		try {
			//set alerts from procedures.
			json.org.json.JSONArray proximityAlertsJSONArray = (json.org.json.JSONArray) proximityAlerts.GetJSONObject();

			for (int i = 0; i < proximityAlertsJSONArray.length(); i++) {
				JSONObject proximityAlertJSONObj;
				if (proximityAlertsJSONArray.get(i) instanceof JSONObject) {
					proximityAlertJSONObj = proximityAlertsJSONArray.getJSONObject(i);
				} else {
					proximityAlertJSONObj = (JSONObject) proximityAlerts.getStruct().get(i);
				}

				String name = (String) proximityAlertJSONObj.get(Constants.SDT_PROXIMITY_ALERTS_NAME);
				String description = (String) proximityAlertJSONObj.get(Constants.SDT_PROXIMITY_ALERTS_DESCRIPTION);
				String geoLocation = (String) proximityAlertJSONObj.get(Constants.SDT_PROXIMITY_ALERTS_GEOLOCATION);
				Integer radius = Integer.parseInt(proximityAlertJSONObj.get(Constants.SDT_PROXIMITY_ALERTS_RADIUS).toString());
				String expirationTime = (String) proximityAlertJSONObj.get(Constants.SDT_PROXIMITY_ALERTS_EXPIRATION_TIME);
				String actionName = (String) proximityAlertJSONObj.get(Constants.SDT_PROXIMITY_ALERTS_ACTION_NAME);

				Services.Location.createProximityAlert(name, description, geoLocation,
					radius, expirationTime, actionName, true, 0);
			}

			return true;
		} catch (json.org.json.JSONException e) {
			return false;
		}
	}

	//END Proximity Alerts methods

	// Authorized, ServiceEnabled  methods and AuthorizationStatus property
	public static int authorizationStatus() {
		return MapsAPIOffline.authorizationStatus();
	}

	public static boolean authorized() {
		return MapsAPIOffline.authorized();
	}

	public static boolean serviceEnabled() {
		return MapsAPIOffline.serviceEnabled();
	}
}
