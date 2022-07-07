package com.genexus.coreexternalobjects.geolocation;

import android.Manifest;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;

import com.artech.android.GooglePlayServicesHelper;
import com.artech.android.PermissionUtil;
import com.artech.android.device.IGeoLocationHelper;
import com.artech.base.services.Services;
import com.artech.base.utils.Strings;
import com.artech.base.utils.ThreadUtils;
import com.genexus.coreexternalobjects.geolocation.fused.LocationFusedProviderHelper;
import com.google.android.gms.location.LocationRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SuppressWarnings("ResourceType") // This is to avoid the "missing FINE_LOCATION permission in manifest" lint warning.
public class LocationHelper implements IGeoLocationHelper {
	@RequiresApi(Build.VERSION_CODES.Q)
	private static final String[] ADDITIONAL_REQUEST_PERMISSIONS_API29 = new String[] { Manifest.permission.ACCESS_BACKGROUND_LOCATION };
	private static final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
	private static final int SECONDS_FOR_OLD_LOCATION = 20;

	private final Context mAppContext;
	private final ArrayList<IOnLocationChangeListener> mListeners;
	private Location mNewCurrentLocation = null;

	public static LocationFusedProviderHelper sFusedLocationHelper = null;

	public LocationHelper(Context appContext) {
		mAppContext = appContext;
		mListeners = new ArrayList<>();
	}

	@Override
	public boolean createFusedLocationHelper() {
		if (!GooglePlayServicesHelper.isPlayServicesAvailable(mAppContext)) {
			return false;
		}

		Services.Log.info("createFusedLocationHelper", "Use fused Helper, get.");
		if (sFusedLocationHelper == null) {
			Services.Log.info("createFusedLocationHelper", "Use fused Helper, create.");
			sFusedLocationHelper = new LocationFusedProviderHelper(mAppContext);
		} else if (!sFusedLocationHelper.isLocationClientConnected()) {
			Services.Log.info("createFusedLocationHelper", "Use fused Helper, reconnecting.");
			sFusedLocationHelper.init();
		}

		return true; // Either created now or we already had it.
	}

	@Override
	public Location getLastKnownLocation() {
		if (!PermissionUtil.checkSelfPermissions(mAppContext, REQUIRED_PERMISSIONS)) {
			Services.Log.error("Location.getLastKnownLocation() called without appropriate permissions");
			return null;
		}

		Location locationResult = null;
		if (sFusedLocationHelper != null)
			locationResult = sFusedLocationHelper.getLastLocation();

		if (locationResult == null)
			locationResult = getLastKnownLocationFromProviders();

		return locationResult;
	}

	@Override
	public String[] getRequiredPermissions() {
		return REQUIRED_PERMISSIONS;
	}

	@Override
	public String[] getRequestPermissions() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			List<String> list = new ArrayList<String>();
			list.addAll(Arrays.asList(REQUIRED_PERMISSIONS));
			list.addAll(Arrays.asList(ADDITIONAL_REQUEST_PERMISSIONS_API29));
			return list.toArray(new String[list.size()]);
		}
		else {
			return REQUIRED_PERMISSIONS;
		}
	}

	@Override
	public String getLocationString(Location myLocation) {
		return myLocation != null ? myLocation.getLatitude() + Strings.COMMA + myLocation.getLongitude() : Strings.EMPTY;
	}

	@Override
	public JSONObject getLocationJsonGeoLocationInfo(Integer minAccuracy, Integer timeout, boolean includeHeadingAndSpeed) {
		Location location = getLocationGeoLocationInfo(minAccuracy, timeout, includeHeadingAndSpeed);
		JSONObject result = null;
		if (location != null) {
			result = LocationEntityFactory.createLocationEntity(location);
			Services.Log.info("getLocationInfo", "Location: " + location.toString());
		}
		return result;
	}

	@Override
	public void requestLocationUpdatesLocationManager(Integer minTime, Integer minDistance, boolean includeHeadingAndSpeed,
													  String action, Integer actionInterval) {
		LocationManager locationManager = (LocationManager) mAppContext.getSystemService(Context.LOCATION_SERVICE);
		String provider = getBestProviderFromCriteria(includeHeadingAndSpeed, locationManager);
		if (provider != null) {
			Services.Log.info("requestLocationUpdates", "using provider: " + provider);
			locationManager.requestLocationUpdates(provider, minTime, minDistance, mLocationListener);
		}
	}

	@Override
	public void removeLocationUpdatesFromLocationManager() {
		LocationManager locationManager = (LocationManager) mAppContext.getSystemService(Context.LOCATION_SERVICE);
		Services.Log.info("removeLocationUpdates", "using locationManager.");
		locationManager.removeUpdates(mLocationListener);
	}

	@Override
	public void requestLocationUpdatesFused(Integer minDistance) {
		Services.Log.info("requestLocationUpdates", "using FusedLocationHelper.");
		if (sFusedLocationHelper != null) {
			sFusedLocationHelper.requestLocationUpdates(mFusedLocationListener, minDistance);
		}
	}

	@Override
	public void removeLocationUpdatesFromFused() {
		if (sFusedLocationHelper != null) {
			sFusedLocationHelper.removeLocationUpdates(mFusedLocationListener);
		}
	}

	@Override
	public void onLocationChange(Context context, Location location) {
		if (location != null) {
			//	update my location
			Services.Log.info("onLocationChanged", "Location: " + location.toString());
			mNewCurrentLocation = location;

			for (IOnLocationChangeListener listener : mListeners) {
				listener.onLocationChange(location);
			}
		}
	}

	@Override
	public void addOnLocationChangeListener(IOnLocationChangeListener listener) {
		mListeners.add(listener);
	}

	@Override
	public void removeOnLocationChangeListener(IOnLocationChangeListener listener) {
		mListeners.remove(listener);
	}

	private Location getLocationGeoLocationInfo(Integer minAccuracy, Integer timeout, boolean includeHeadingAndSpeed) {
		Date startTime = new Date();

		//default to return
		Location location = null;

		LocationManager locationManager = (LocationManager) mAppContext.getSystemService(Context.LOCATION_SERVICE);
		String provider = getBestProviderFromCriteria(includeHeadingAndSpeed, locationManager);

		if (sFusedLocationHelper != null) {
			if (sFusedLocationHelper.isLocationClientConnected()) {
				location = sFusedLocationHelper.getLastLocation();
			} else {
				//wait until connected at least 1 seconds.
				if (timeout < 1)
					timeout = 1;
			}
		}
		if (provider != null && location == null) {
			Services.Log.info("getLocationGeoLocationInfo", "provider 1: " + provider);
			location = locationManager.getLastKnownLocation(provider);
		}

		long difLocInSeconds = 0;
		if (location != null) {
			difLocInSeconds = Utils.getDiffInSeconds(location.getTime(), startTime.getTime());
			Services.Log.info("getLocationGeoLocationInfo", "getLastKnownLocation 11 " + location.toString());
		}

		minAccuracy = parseMinAccuracyValue(minAccuracy);

		boolean isvalidLocation = true;
		while (location == null //has no location
			|| (minAccuracy != 0 && (!location.hasAccuracy() || location.getAccuracy() > minAccuracy)) //has not accuracy
			|| difLocInSeconds > SECONDS_FOR_OLD_LOCATION // is old location
			|| !isvalidLocation) { //is different for last know location
			//wait one sec to new location to arrive
			ThreadUtils.sleep(1000);
			Services.Log.info("getLocationGeoLocationInfo", "wait one sec to new location to arrive 2");

			if (mNewCurrentLocation != null) {
				Location lastLocation = null;
				if (location != null) {
					lastLocation = location;
					if (lastLocation.getTime() == mNewCurrentLocation.getTime()) {
						isvalidLocation = false;
						Services.Log.info("getLocationGeoLocationInfo", "obtain new location 3a same time ");
					} else {
						isvalidLocation = true;
						Services.Log.info("getLocationGeoLocationInfo", "obtain new location 3b different time ");
					}
				}
				location = mNewCurrentLocation;
				Services.Log.info("getLocationGeoLocationInfo", "obtain new location 3 " + mNewCurrentLocation.toString());
				// discard last know location, only new one is returned
				if (lastLocation != null)
					difLocInSeconds = Utils.getDiffInSeconds(location.getTime(), lastLocation.getTime());
				else
					difLocInSeconds = Utils.getDiffInSeconds(location.getTime(), startTime.getTime());
			}
			Date endTime = new Date();
			long difInSeconds = Utils.getDiffInSeconds(startTime.getTime(), endTime.getTime());
			if (difInSeconds > timeout) {
				Services.Log.info("getLocationGeoLocationInfo", "break for timeout location 4 ");
				break;
			}
		}

		//default to return
		if (location == null) {
			Services.Log.info("getLocationGeoLocationInfo", "location null 5 ");
			location = getLastKnownLocation();
			if (location != null)
				Services.Log.info("getLocationGeoLocationInfo", "get last know location 6 " + location.toString());
			else
				Services.Log.info("getLocationGeoLocationInfo", "get last know location 7 null ");
		}
		return location;
	}

	public int parseMinAccuracyValue(int minAccuracy) {
		//Values used in getLocationPriorityFromMinAccuracy except for 3000 taken from mapping for Android accuracy in Spec
		if (minAccuracy == -1 || minAccuracy == -2 || minAccuracy == -3) return 20;
		if (minAccuracy == -4) return 100;
		if (minAccuracy == -5 || minAccuracy == -6 || minAccuracy == -7) return 3000;

		return minAccuracy;
	}

	public static int getLocationPriorityFromMinAccuracy(int minAccuracy) {
		int priority = LocationRequest.PRIORITY_LOW_POWER;;
		if (minAccuracy <= 0) {
			switch (minAccuracy) {
				case -1:
				case -2:
				case -3:
					priority = LocationRequest.PRIORITY_HIGH_ACCURACY;
					break;
				case -4:
					priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
					break;
				case -5:
				case -6:
				case -7:
				case 0:
					priority = LocationRequest.PRIORITY_LOW_POWER;
					break;
			}
		} else if (minAccuracy <= 20)
			priority = LocationRequest.PRIORITY_HIGH_ACCURACY;
		else if (minAccuracy <= 100)
			priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;

		return priority;
	}

	private Location getLastKnownLocationFromProviders() {
		LocationManager aLocationManager = (LocationManager) mAppContext.getSystemService(Context.LOCATION_SERVICE);
		if (aLocationManager == null) {
			return null;
		}

		// Should get the last in time location, comparing location.getTime() ?
		Location gpsLocation = aLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		Location networkLocation = aLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		// Get last location from providers
		Location location = gpsLocation != null ? gpsLocation : networkLocation;
		if (location != null) {
			Services.Log.info("getLastKnownLocation", "get location from GPS_PROVIDER or NETWORK_PROVIDER ");
			return location;
		} else {
			//try get last location from passive location provider
			Location passiveLocation = aLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
			if (passiveLocation != null) {
				Services.Log.info("getLastKnownLocation", "get location from PASSIVE_PROVIDER");
				return passiveLocation;
			}
		}

		Criteria crit = new Criteria();
		crit.setAccuracy(Criteria.ACCURACY_FINE);
		String provider = aLocationManager.getBestProvider(crit, true);
		if (provider == null)
			return null;
		location = aLocationManager.getLastKnownLocation(provider);
		return location;
	}

	private static String getBestProviderFromCriteria(boolean includeHeadingAndSpeed, LocationManager locationManager) {
		//Calculate new location with the criteria.
		Criteria crit = new Criteria();
		crit.setAccuracy(Criteria.ACCURACY_FINE);
		crit.setAltitudeRequired(false);
		crit.setBearingRequired(includeHeadingAndSpeed);
		crit.setCostAllowed(true);
		return locationManager.getBestProvider(crit, true);
	}

	private final LocationListener mLocationListener = new LocationListener() {
		@Override
		@SuppressWarnings("deprecation")
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Services.Log.info("onStatusChanged", "Provider: " + provider + "Status: " + status);
			switch (status) {
				case LocationProvider.AVAILABLE:
					//MyApplication.getInstance().showMessage("GPS available again\n");
					break;
				case LocationProvider.OUT_OF_SERVICE:
					//MyApplication.getInstance().showMessage("GPS out of service\n");
					break;
				case LocationProvider.TEMPORARILY_UNAVAILABLE:
					//MyApplication.getInstance().showMessage("GPS temporarily unavailable\n");
					break;
			}
		}

		@Override
		public void onProviderEnabled(String provider) {
			//MyApplication.getInstance().showMessage("Provider: " + provider + " enabled");
			Services.Log.info("onProviderEnabled", "Provider: " + provider);
		}

		@Override
		public void onProviderDisabled(String provider) {
			//MyApplication.getInstance().showMessage("Provider: " + provider + " disabled");
			Services.Log.info("onProviderDisabled", "Provider: " + provider);
		}

		@Override
		public void onLocationChanged(Location location) {
			onLocationChange(mAppContext, location);
		}
	};

	private final com.google.android.gms.location.LocationListener mFusedLocationListener = location -> {
		//	update my location
		Services.Log.info("onLocationChanged", "Location: " + location.toString());
		mNewCurrentLocation = location;
		// TODO, call to onLocationChange(location);
	};
}
