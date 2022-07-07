package com.genexus.android.location.geolocation;

import android.content.Context;
import android.location.Location;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.GeoFormats;
import com.genexus.android.core.controls.maps.LocationApi;
import com.genexus.android.location.geolocation.db.ProximityAlert;
import com.genexus.android.location.geolocation.db.ProximityAlertsSQLiteHelper;
import com.genexus.android.location.geolocation.geofence.Geofence;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.google.android.gms.location.Geofence.NEVER_EXPIRE;

class ProximityAlertsHelper {

	private ProximityAlertsSQLiteHelper mProximityAlertsDB;
	private ProximityAlert mCurrentProximityAlert = null;

	private static ProximityAlertsHelper sInstance;

	public static synchronized ProximityAlertsHelper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new ProximityAlertsHelper();
			sInstance.mProximityAlertsDB = new ProximityAlertsSQLiteHelper(context);
		}

		return sInstance;
	}

	private ProximityAlertsHelper() { }

	public List<Geofence> setProximityAlerts(EntityList proximityAlerts, LocationApi apiType) {
		if (proximityAlerts == null)
			return null;

		List<Geofence> geofences = new ArrayList<>();
		for (Entity proximityAlert : proximityAlerts) {
			String geoLocation = (String) proximityAlert.getProperty(Constants.SDT_PROXIMITY_ALERTS_GEOLOCATION);
			if (apiType == LocationApi.MAPS) geoLocation = GeoFormats.geopointToGeolocation(geoLocation);
			if (GeoFormats.parseGeolocation(geoLocation) == null) {
				Services.Log.warning(String.format("Invalid geolocation format in '%s'", geoLocation));
				continue;
			}

			String name = (String) proximityAlert.getProperty(Constants.SDT_PROXIMITY_ALERTS_NAME);
			String description = (String) proximityAlert.getProperty(Constants.SDT_PROXIMITY_ALERTS_DESCRIPTION);
			Integer radius = Integer.parseInt((String) proximityAlert.getProperty(Constants.SDT_PROXIMITY_ALERTS_RADIUS));
			String expirationTime = (String) proximityAlert.getProperty(Constants.SDT_PROXIMITY_ALERTS_EXPIRATION_TIME);
			String actionName = (String) proximityAlert.getProperty(Constants.SDT_PROXIMITY_ALERTS_ACTION_NAME);

			geofences.add(createProximityAlertsAddToGeoFences(name, description, geoLocation,
					radius, expirationTime, actionName, true, 0));
		}

		return geofences;
	}

	public void clearProximityAlerts() {
		mProximityAlertsDB.deleteAllProximityAlerts();
	}

	public @NonNull	EntityList getProximityAlerts(LocationApi apiType) {
		EntityList alerts = new EntityList();
		List<ProximityAlert> proximityAlerts = getProximityAlerts();
		for (ProximityAlert pa : proximityAlerts)
			alerts.add(proximityAlertToEntity(pa, apiType));

		return alerts;
	}

	public @Nullable ProximityAlert getCurrentProximityAlert() {
		return mCurrentProximityAlert;
	}

	public ProximityAlert getProximityAlert(int id) {
		return mProximityAlertsDB.getProximityAlerts(id);
	}

	public void setCurrentProximityAlert(int geofenceId) {
		setCurrentProximityAlert(getProximityAlert(geofenceId));
	}

	public void setCurrentProximityAlert(ProximityAlert proximityAlert) {
		mCurrentProximityAlert = proximityAlert;
	}

	public @NonNull Entity getCurrentProximityAlertEntity(LocationApi apiType) {
		return proximityAlertToEntity(getCurrentProximityAlert(), apiType);
	}

	private @NonNull List<ProximityAlert> getProximityAlerts() {
		return mProximityAlertsDB.getAllProximityAlerts();
	}

	private Entity proximityAlertToEntity(ProximityAlert pa, LocationApi apiType) {
		Entity alert = EntityFactory.newSdt(getProximityAlertsSDTFQN(apiType));
		if (pa == null)
			return alert;

		alert.setProperty(Constants.SDT_PROXIMITY_ALERTS_NAME, pa.getName());
		alert.setProperty(Constants.SDT_PROXIMITY_ALERTS_DESCRIPTION, pa.getDescription());

		String geolocation = pa.getGeolocation();
		if (apiType == LocationApi.MAPS) {
			Pair<Double, Double> latLong = GeoFormats.parseGeolocation(geolocation);
			if (latLong != null)
				geolocation = GeoFormats.buildGeopoint(latLong.first, latLong.second);
		}

		alert.setProperty(Constants.SDT_PROXIMITY_ALERTS_GEOLOCATION, geolocation);
		alert.setProperty(Constants.SDT_PROXIMITY_ALERTS_RADIUS, pa.getRadius().toString());
		alert.setProperty(Constants.SDT_PROXIMITY_ALERTS_EXPIRATION_TIME, pa.getExpirationDateTime());
		alert.setProperty(Constants.SDT_PROXIMITY_ALERTS_ACTION_NAME, pa.getActionName());

		return alert;
	}


	public Geofence createProximityAlertsAddToGeoFences(String name, String description, String geolocation,
	                                                    Integer radius, String expirationDateTime, String actionName,
	                                                    Boolean addToDatabase, int paUniqueId) {
		ProximityAlert pa = new ProximityAlert();
		pa.setName(name);
		pa.setDescription(description);
		pa.setGeolocation(geolocation);
		pa.setRadius(radius);
		pa.setExpirationDateTime(expirationDateTime);
		pa.setActionName(actionName);

		// if already exits in database, not insert again.
		int uniqueId;
		if (addToDatabase)
			uniqueId = (int) mProximityAlertsDB.addProximityAlert(pa);
		else
			uniqueId = paUniqueId;

		//set proximity alert in the android geofence system

		//DateTime
		Date date = Services.Strings.getDateTime(expirationDateTime);
		long expiration = NEVER_EXPIRE;
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);

			Calendar calendarToday = Calendar.getInstance();
			//use today date

			expiration = calendar.getTimeInMillis() - calendarToday.getTimeInMillis();
		}
		if (expiration <= 0) {
			expiration = NEVER_EXPIRE;
		}

		Location geofenceLocation = GeoFormats.getLocationFromString(geolocation);

		Services.Log.debug("create geofence " + uniqueId + " location " + geofenceLocation);
		Services.Log.debug("create geofence " + uniqueId + " radius " + radius);
		Services.Log.debug("create geofence " + uniqueId + " expiration milliseconds " + expiration);
		return new Geofence(uniqueId, geofenceLocation, radius, expiration);
	}

	// method to re enable proximity alerts after reboot
	public List<Geofence> resetProximityAlerts() {
		//online method
		List<ProximityAlert> proximityAlerts = getProximityAlerts();
		Services.Log.debug("reSetProximityAlertsInGeofencesStatic ");
		if (proximityAlerts.size() == 0)
			return null;

		Services.Log.debug("add proximityAlerts to geofences, size " + proximityAlerts.size());
		List<Geofence> geofences = new ArrayList<>();
		for (ProximityAlert pa : proximityAlerts) {
			//re create, already exits in database.
			geofences.add(createProximityAlertsAddToGeoFences(pa.getName(), pa.getDescription(), pa.getGeolocation(),
					pa.getRadius(), pa.getExpirationDateTime(), pa.getActionName(), false, pa.getId()));
		}

		return geofences;
	}

	private String getProximityAlertsSDTFQN(LocationApi apiType) {
		if (apiType == LocationApi.MAPS)
			return "GeneXus.Common.LocationProximityAlert";

		return "GeneXus.Common.GeolocationProximityAlert";
	}
}
