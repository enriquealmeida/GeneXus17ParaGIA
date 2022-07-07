package com.genexus.android.location.geolocation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;

import com.genexus.android.LocationAccuracy;
import com.genexus.android.core.activities.ActivityHelper;
import com.genexus.android.ApiAuthorizationStatus;
import com.genexus.android.PermissionUtil;
import com.genexus.android.core.application.LifecycleListeners;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.base.services.IApplication;
import com.genexus.android.core.base.services.ILocationProvider;
import com.genexus.android.core.base.services.ILocationServices;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.GeoFormats;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.base.utils.ThreadUtils;
import com.genexus.android.core.controls.ProgressDialogFactory;
import com.genexus.android.core.controls.maps.LocationApi;
import com.genexus.android.core.controls.maps.LocationRequestParameters;
import com.genexus.android.location.R;
import com.genexus.android.location.geolocation.db.TrackingLocation;
import com.genexus.android.location.geolocation.db.TrackingSQLiteHelper;
import com.genexus.android.location.geolocation.geofence.Geofence;
import com.genexus.android.location.geolocation.provider.DefaultLocationProvider;
import com.genexus.android.location.geolocation.tracking.TrackingData;
import com.google.android.gms.location.LocationRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class LocationServices implements ILocationServices {

	@RequiresApi(Build.VERSION_CODES.Q)
	private static final String[] ADDITIONAL_REQUEST_PERMISSIONS_API29 = new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION};
	private static final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
	private static final String TAG = "LocationServices";
	private static final int SECONDS_FOR_OLD_LOCATION = 20;
	private static final int DEFAULT_ACCURACY = 20; //HIGH

	private final Context mAppContext;
	private final ArrayList<IOnLocationChangeListener> mListeners;

	private final ILocationProvider mDefaultProvider;
	private ILocationProvider mCustomLocationProvider = null;
	private Location mCurrentLocation;

	private boolean mIsTracking = false;
	private int mChangesInterval = 0;
	private int mMinDistance = 0;
	private String mAction = Strings.EMPTY;
	private int mActionTimeInterval = 0;
	private Date mLastLocationActionTime = new Date();
	private int mAccuracy = 20; //default for compatibility
	private boolean mUseForegroundService = false;

	private final TrackingSQLiteHelper mTrackingDBHelper;
	private final ProximityAlertsHelper mProximityAlertsHelper;
	private final TrackingData mTrackingData;
	private final ProgressDialogFactory.ProgressViewProvider mProgressViewProvider;

	public LocationServices(Context appContext) {
		mAppContext = appContext;
		mListeners = new ArrayList<>();
		mDefaultProvider = new DefaultLocationProvider(appContext);
		mTrackingDBHelper = TrackingSQLiteHelper.getInstance(appContext);
		mProximityAlertsHelper = ProximityAlertsHelper.getInstance(appContext);
		mTrackingData = TrackingData.getInstance();
		mProgressViewProvider = new ProgressDialogFactory().getViewProvider();
		Services.Application.getLifecycle().registerOnMetadataLoadFinished(new LifecycleListeners.MetadataLoading() {
			@Override
			public void onMetadataLoadFinished(IApplication application) {
				mCurrentLocation = mDefaultProvider.getLastKnownLocation();
				Services.Application.getLifecycle().unregisterOnMetadataLoadFinished(this);
			}
		});
	}

	@Override
	public JSONObject getCurrentLocation(Activity activity, int minAccuracy, int timeout, boolean includeHeadingAndSpeed,
	                                     boolean ignoreErrors, boolean showMessages, LocationApi apiType) {

		if (!ignoreErrors && !isEnabled()) {
			if (showMessages)
				Services.Messages.showMessage(Services.Strings.getResource(R.string.GXM_LocationServicesAreDisabled));

			return null;
		}

		int accuracy = parseMinAccuracyValue(minAccuracy);
		startReceivingUpdates(activity, showMessages, accuracy, timeout, includeHeadingAndSpeed, false);
		JSONObject location = getLocationGeolocationInfo(activity, accuracy, timeout, apiType);
		stopReceivingUpdates(!isTracking());

		// return value is set to Empty and execution of the composite block continues without error.
		if (location == null && ignoreErrors)
			location = new JSONObject();

		Services.Log.debug(TAG, "getMyLocation end return location.");
		return location;
	}

	private void startReceivingUpdates(Activity activity, boolean showDialog, int accuracy, int timeout,
	                                    boolean includeHeadingAndSpeed, boolean tracking) {
		if (activity != null && !isTracking()) {
			bindProviders();
			Services.Device.runOnUiThread(() -> requestLocationUpdates(activity, showDialog, accuracy,
				timeout, includeHeadingAndSpeed, tracking));
		}
	}

	private void stopReceivingUpdates(boolean force) {
		if (force) //Force means that updates will be stopped even though tracking is active
			Services.Device.runOnUiThread(this::removeLocationUpdates);
	}

	@Override
	public Location getLastKnownLocation() {
		return mCurrentLocation;
	}

	private void requestLocationUpdates(int minTime, int minDistance, boolean includeHeadingAndSpeed) {
		requestLocationUpdates(minTime, minDistance, DEFAULT_ACCURACY, false, includeHeadingAndSpeed);
	}

	private void requestLocationUpdates(Integer changesInterval, Integer minDistance, Integer accuracy,
										boolean useForegroundService, boolean includeHeadingSpeed) {
		if (!checkPermission())
			return;

		LocationRequestParameters requestParameters = getRequestParameters(changesInterval, minDistance, accuracy, useForegroundService, includeHeadingSpeed);
		if (mCustomLocationProvider == null || !mCustomLocationProvider.requestLocationUpdates(requestParameters))
			mDefaultProvider.requestLocationUpdates(requestParameters);
	}

	private void removeLocationUpdates() {
		if (mCustomLocationProvider == null || !mCustomLocationProvider.removeLocationUpdates())
			mDefaultProvider.removeLocationUpdates();

		if (mProgressViewProvider != null) {
			try {
				mProgressViewProvider.hideProgressIndicator(ActivityHelper.getCurrentActivity());
			}
			catch (IllegalArgumentException ex) {
				// probably already detached
				Services.Log.debug(TAG, "Cannot dismiss progress dialog " + ex.getMessage());
			}
		}
	}

	private void bindProviders() {
		if (mCustomLocationProvider != null && !mCustomLocationProvider.isReady())
			mCustomLocationProvider.bind();
		else if (!mDefaultProvider.isReady())
			mDefaultProvider.bind();
	}

	@Override
	public String[] getRequiredPermissions() {
		return REQUIRED_PERMISSIONS;
	}

	@Override
	public String[] getRequestPermissions() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			List<String> list = new ArrayList<>();
			list.addAll(Arrays.asList(REQUIRED_PERMISSIONS));
			list.addAll(Arrays.asList(ADDITIONAL_REQUEST_PERMISSIONS_API29));
			return list.toArray(new String[0]);
		}
		else {
			return REQUIRED_PERMISSIONS;
		}
	}

	@Override
	public void onLocationChange(Location location) {
		if (location != null) {
			Services.Log.debug(TAG, "onLocationChanged - Location: " + location.toString());
			mCurrentLocation = location;
			for (IOnLocationChangeListener listener : mListeners)
				listener.onLocationChange(location);

			// Keep location change in tracking array if were are tracking.
			// If have a tracking action and action interval has been complete, raise the action.
			// get the action and raise it.
			if (mIsTracking) {
				Services.Log.debug(TAG, "onLocationChanged - Add location to tracking : " + location.toString());
				mTrackingDBHelper.addLocation(location, mAppContext);

				//If have a tracking action and action interval has been complete, raise the action.
				if (Services.Strings.hasValue(mAction)) {
					Services.Log.debug(TAG, "has an action an new location");
					Date nowTime = new Date();
					long difLocInSeconds = getDiffInSeconds(mLastLocationActionTime.getTime(), nowTime.getTime());
					Services.Log.debug(TAG, "dif in seconds " + difLocInSeconds);
					if (difLocInSeconds > mActionTimeInterval) {
						Services.Log.debug(TAG, "time elapsed , raise new action " + mAction);
						// get the action and raise it.
						Services.Notifications.executeNotificationAction(ActivityHelper.getCurrentActivity(), mAction, Strings.EMPTY, null);
						Services.Log.debug(TAG, "reset last location action time");
						mLastLocationActionTime = new Date();
						mTrackingData.setTrackingDataDate(mLastLocationActionTime);
					}
				}
			}
		}
	}

	@Override
	public void setProvider(ILocationProvider locationProvider) {
		mCustomLocationProvider = locationProvider;
	}

	@Override
	public void clearProvider() {
		mCustomLocationProvider = null;
	}

	@Override
	public void addOnLocationChangeListener(IOnLocationChangeListener listener) {
		mListeners.add(listener);
	}

	@Override
	public void removeOnLocationChangeListener(IOnLocationChangeListener listener) {
		mListeners.remove(listener);
	}

	@Override
	public int getAuthorizationStatus() {
		return ApiAuthorizationStatus.getStatus(mAppContext, getStatusPermissions());
	}

	@Override
	public boolean isAuthorized() {
		return getAuthorizationStatus() == ApiAuthorizationStatus.AUTHORIZED;
	}

	// At least Coarse Location must be allowed in order for Status to be Authorized
	private String[] getStatusPermissions() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
			return new String[]{LocationAccuracy.COARSE};

		return getRequiredPermissions();
	}

	@Override
	public boolean isReady() {
		return (mCustomLocationProvider != null && mCustomLocationProvider.isReady()) || mDefaultProvider.isReady();
	}

	@Override
	public boolean isEnabled() {
		LocationManager aLocationManager = (LocationManager) mAppContext.getSystemService(Context.LOCATION_SERVICE);
		return aLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || aLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}

	@Override
	public boolean isTracking() {
		return mIsTracking;
	}

	@Override
	public void createProximityAlert(String name, String description, String geolocation, int radius,
	                                 String expirationDateTime, String actionName, Boolean addToDatabase, int paUniqueId) {
		Geofence geofence = mProximityAlertsHelper.createProximityAlertsAddToGeoFences(name, description, geolocation, radius,
				expirationDateTime, actionName, addToDatabase, paUniqueId);

		if (geofence != null)
			createGeofence(geofence.getId(), geofence.getLocation(), geofence.getRadius(), geofence.getExpiration());
	}

	@Override
	public boolean setProximityAlerts(EntityList proximityAlerts, LocationApi apiType) {
		if (isReady())
			return checkGeofences(mProximityAlertsHelper.setProximityAlerts(proximityAlerts, apiType));

		return false;
	}

	@Override
	public void clearProximityAlerts() {
		mProximityAlertsHelper.clearProximityAlerts();
		stopCheckingGeofences();
	}

	@Override
	public void resetProximityAlerts() {
		checkGeofences(mProximityAlertsHelper.resetProximityAlerts());
	}

	@Override
	public Entity getCurrentProximityAlert(LocationApi apiType) {
		return mProximityAlertsHelper.getCurrentProximityAlertEntity(apiType);
	}

	@Override
	public void setCurrentProximityAlert(int geofenceRequestId) {
		mProximityAlertsHelper.setCurrentProximityAlert(geofenceRequestId);
	}

	@Override
	public EntityList getProximityAlerts(LocationApi apiType) {
		return mProximityAlertsHelper.getProximityAlerts(apiType);
	}

	private boolean checkGeofences(List<Geofence> geofences) {
		if (geofences == null || geofences.isEmpty())
			return false;

		for (Geofence geofence : geofences)
			createGeofence(geofence.getId(), geofence.getLocation(), geofence.getRadius(), geofence.getExpiration());

		checkGeofences();
		return true;
	}

	private void checkGeofences() {
		if (mCustomLocationProvider == null || !mCustomLocationProvider.checkGeofences(mAppContext))
			mDefaultProvider.checkGeofences(mAppContext);
	}

	private void stopCheckingGeofences() {
		if (mCustomLocationProvider == null || !mCustomLocationProvider.stopCheckingGeofences(mAppContext))
			mDefaultProvider.stopCheckingGeofences(mAppContext);
	}

	private void createGeofence(int uniqueId, Location location, int radius, long expiration) {
		Services.Log.debug(TAG, "Add Geofence to check");
		if (mCustomLocationProvider == null || !mCustomLocationProvider.createGeofence(uniqueId, location, radius, expiration))
			mDefaultProvider.createGeofence(uniqueId, location, radius, expiration);
	}

	@Override
	public void startTracking(Activity activity, int changesInterval, int minDistance, int actionTimeInterval, String action, int accuracy, boolean useForegroundService) {
		mChangesInterval = changesInterval;
		mMinDistance = minDistance;
		mAction = action;
		mActionTimeInterval = actionTimeInterval;
		mAccuracy = accuracy;
		mUseForegroundService = useForegroundService;

		Services.Log.debug(TAG, "startTracking " + " minTime " + mChangesInterval + " minDistance " + mMinDistance);
		Services.Log.debug(TAG, "startTracking " + " Action " + mAction + " ActionInterval " + mActionTimeInterval);
		Services.Log.debug(TAG, "startTracking " + " TrackingAccuracy " + mAccuracy);

		mTrackingData.setTrackingData(true, mLastLocationActionTime, mAction, mActionTimeInterval);
		startReceivingUpdates(activity, false, 0, 0, true, true);
		mIsTracking = true;
	}

	@Override
	public void endTracking() {
		stopReceivingUpdates(true);
		mIsTracking = false;
		mTrackingData.clear();
	}

	@Override
	public void clearLocationHistory() {
		mTrackingDBHelper.deleteAllLocation();
	}

	@Override
	public JSONArray geocodeAddress(String address, LocationApi apiType) {
		List<Address> locations = null;
		Geocoder geocoder = new Geocoder(mAppContext, Locale.getDefault());
		try {
			locations = geocoder.getFromLocationName(address, 10);
		} catch (IOException e) {
			Services.Log.error(e);
		}

		JSONArray arrayResult = new JSONArray();
		if (locations != null && locations.size() > 0) {
			for (int j = 0; j < locations.size(); j++) {
				Address location = locations.get(j);
				if (apiType == LocationApi.MAPS)
					arrayResult.put(GeoFormats.buildGeopoint(location.getLatitude(), location.getLongitude()));
				else
					arrayResult.put(location.getLatitude() + Strings.COMMA + location.getLongitude());
			}
		}

		return arrayResult;
	}

	@Override
	public JSONArray reverseGeocodeAddress(String locationString) {
		Location location = GeoFormats.getLocationFromString(locationString);
		List<Address> addresses = null;
		Geocoder geocoder = new Geocoder(mAppContext, Locale.getDefault());
		try {
			addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 10);
		} catch (IOException e) {
			Services.Log.error(e);
		}

		JSONArray arrayResult = new JSONArray();
		if (addresses != null && addresses.size() > 0) {
			for (int j = 0; j < addresses.size(); j++) {
				Address address = addresses.get(j);
				StringBuilder result = new StringBuilder();

				for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
					if (address.getAddressLine(i) != null)
						result.append(address.getAddressLine(i)).append(Strings.NEWLINE);
				}
				if (address.getLocality() != null)
					result.append(address.getLocality()).append(Strings.NEWLINE);
				if (address.getPostalCode() != null)
					result.append(address.getPostalCode()).append(Strings.NEWLINE);
				if (address.getCountryName() != null)
					result.append(address.getCountryName()).append(Strings.NEWLINE);

				arrayResult.put(result.toString());
			}
		}

		return arrayResult;
	}

	private JSONObject getLocationGeolocationInfo(Context context, Integer minAccuracy, int timeout, LocationApi apiType) {
		/* If timeout is 0 the method will never return, so a default value has to be set.
		 30 is just a reasonable value when the developer doesn't explicitly decide how much time to wait for a valid location,
		 meaning it can be changed if needed. */
		if (timeout <= 0) timeout = 30;
		if (context == null) context = Services.Application.getAppContext();
		long difLocInSeconds = 0;
		Date startTime = new Date();
		Location location = getLastKnownLocation();
		if (location != null) {
			difLocInSeconds = getDiffInSeconds(location.getTime(), startTime.getTime());
			Services.Log.debug(TAG, "getLocationGeoLocationInfo - getLastKnownLocation 11 " + location.toString());
		}

		boolean isValidLocation = true;
		while (location == null //has no location
				|| (minAccuracy != 0 && (!location.hasAccuracy() || location.getAccuracy() > minAccuracy)) //has no accuracy
				|| difLocInSeconds > SECONDS_FOR_OLD_LOCATION // is old location
				|| !isValidLocation) { //is different for last know location
			ThreadUtils.sleep(500);
			Services.Log.debug(TAG, "getLocationGeoLocationInfo - wait one sec to new location to arrive 2");

			if (mCurrentLocation != null) {
				Location lastLocation = null;
				if (location != null) {
					lastLocation = location;
					if (lastLocation.getTime() == mCurrentLocation.getTime()) {
						isValidLocation = false;
						Services.Log.debug(TAG, "getLocationGeoLocationInfo - obtain new location 3a same time ");
					} else {
						isValidLocation = true;
						Services.Log.debug(TAG, "getLocationGeoLocationInfo - obtain new location 3b different time ");
					}
				}
				location = mCurrentLocation;
				Services.Log.debug(TAG, "getLocationGeoLocationInfo - obtain new location 3 " + mCurrentLocation.toString());
				// discard last know location, only new one is returned
				if (lastLocation != null)
					difLocInSeconds = getDiffInSeconds(location.getTime(), lastLocation.getTime());
				else
					difLocInSeconds = getDiffInSeconds(location.getTime(), startTime.getTime());
			}

			Date endTime = new Date();
			long difInSeconds = getDiffInSeconds(startTime.getTime(), endTime.getTime());
			if (difInSeconds > timeout) {
				Services.Log.debug(TAG, "getLocationGeoLocationInfo - break for timeout location 4 ");
				break;
			}
		}

		if (location == null)
			location = getLastKnownLocation();

		if (location != null) {
			Services.Log.debug(TAG, "getLocationInfo - Location: " + location.toString());
			return LocationEntityFactory.createLocationEntity(location, apiType, context);
		}

		return null;
	}

	private void requestLocationUpdates(Activity activity, boolean showDialog, int minAccuracy, int timeout,
	                                    boolean includeHeadingAndSpeed, boolean tracking) {
		if (showDialog)
			mProgressViewProvider.showProgressIndicator(activity, null, activity.getResources().getText(R.string.GXM_WaitingForLocation).toString());

		if (!tracking)
			requestLocationUpdates(timeout, minAccuracy, includeHeadingAndSpeed);
		else {
			mLastLocationActionTime = new Date();
			mTrackingData.setTrackingData(mIsTracking, mLastLocationActionTime, mAction, mActionTimeInterval);
			requestLocationUpdates(mChangesInterval, mMinDistance, mAccuracy, mUseForegroundService, includeHeadingAndSpeed);
		}
	}

	@Override
	public @NonNull JSONArray getLocationHistory(Date startDate, LocationApi apiType) {
		JSONArray arrayResult = new JSONArray();
		List<TrackingLocation> trackingLocations = mTrackingDBHelper.getAllLocations(apiType);
		if (trackingLocations.isEmpty())
			Services.Log.debug(TAG, "getLocationHistory trackingLocations empty");
		else {
			Services.Log.debug(TAG, "getLocationHistory trackingLocations size " + trackingLocations.size());
			for (int j = 0; j < trackingLocations.size(); j++) {
				TrackingLocation trackingLocation = trackingLocations.get(j);
				if (trackingLocation == null)
					continue;

				if (startDate != null && trackingLocation.getDateTimetime() <= startDate.getTime())
					Services.Log.debug(TAG, "getLocationHistory not add location for time restriction: " + trackingLocation.getGeolocationJson());
				else {
					try {
						JSONObject jsonObject = new JSONObject(trackingLocation.getGeolocationJson());
						arrayResult.put(jsonObject);
						if (startDate == null)
							Services.Log.debug(TAG, "getLocationHistory add location date is null");
					} catch (JSONException ex) {
						Services.Log.error(ex);
					}
				}
			}
			Services.Log.debug(TAG, "getLocationHistory - " + arrayResult.toString());
		}

		return arrayResult;
	}

	@Override
	public void restoreTracking(boolean isTracking, Date lastLocationActionTime, String action, int actionInterval) {
		mIsTracking = isTracking;
		mLastLocationActionTime = lastLocationActionTime;
		mAction = action;
		mActionTimeInterval = actionInterval;
		Services.Log.debug(TAG, "restoreTrackingData, isTracking " + isTracking + " sAction " + mAction + " ");
	}

	private LocationRequestParameters getRequestParameters(long updateInterval, int minDistance, int minAccuracy,
														   boolean useForegroundService, boolean includeHeadingSpeed) {
		// Create a new global location parameters object
		LocationRequest locationRequest = LocationRequest.create();

		//Set the update interval, only override when use tracking., if not update each 1 seconds
		locationRequest.setInterval(updateInterval);
		locationRequest.setFastestInterval(updateInterval);

		if (minDistance > 0)
			locationRequest.setSmallestDisplacement(minDistance);

		// from http://www.intelligrape.com/blog/googles-fused-location-api-for-android/
		// and https://developer.android.com/training/location/receive-location-updates.html
		Services.Log.debug(TAG, "initLocationRequest minAccuracy: " + minAccuracy);

		int priority = getLocationPriorityFromMinAccuracy(minAccuracy);
		locationRequest.setPriority(priority);

		return new LocationRequestParameters(locationRequest.getExpirationTime(), locationRequest.getFastestInterval(),
			locationRequest.getInterval(), locationRequest.getMaxWaitTime(), locationRequest.getNumUpdates(),
			locationRequest.getPriority(), locationRequest.getSmallestDisplacement(), useForegroundService, includeHeadingSpeed);
	}

	private boolean checkPermission() {
		if (PermissionUtil.checkAnyLocationPermission(mAppContext))
			return true;

		Services.Log.error(TAG, "Requested location without appropriate permissions");
		return false;
	}

	private int getLocationPriorityFromMinAccuracy(int minAccuracy) {
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

	private int parseMinAccuracyValue(int minAccuracy) {
		//Values used in getLocationPriorityFromMinAccuracy except for 3000 taken from mapping for Android accuracy in Spec
		if (minAccuracy == -1 || minAccuracy == -2 || minAccuracy == -3) return 20;
		if (minAccuracy == -4) return 100;
		if (minAccuracy == -5 || minAccuracy == -6 || minAccuracy == -7) return 3000;

		return minAccuracy;
	}

	private long getDiffInSeconds(long startTime, long endTime) {
		return (endTime - startTime) / 1000;
	}
}
