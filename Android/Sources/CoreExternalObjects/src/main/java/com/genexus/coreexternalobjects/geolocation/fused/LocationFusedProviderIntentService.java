package com.genexus.coreexternalobjects.geolocation.fused;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;

import com.artech.application.MyApplication;
import com.artech.base.services.Services;
import com.genexus.coreexternalobjects.geolocation.LocationActionsHelper;
import com.genexus.coreexternalobjects.geolocation.tracking.TrackingData;

public class LocationFusedProviderIntentService extends IntentService {

	protected static final String TAG = "LocationFusedProviderIntentService";

	private static LocationActionsHelper mLocationActionsHelper;

	public LocationFusedProviderIntentService() {
		super(TAG);
		mLocationActionsHelper = LocationActionsHelper.getInstance();

		Services.Log.info("LocationFusedProviderIntentService constructor");

		if (!mLocationActionsHelper.mIsTracking) {
			Services.Log.info("LocationFusedProviderIntentService constructor. restore from session ");

			TrackingData.restoreTrackingData();
		}

		Services.Log.info("LocationFusedProviderIntentService constructor. isTracking " + mLocationActionsHelper.mIsTracking);

	}

	@Override
	public void onCreate() {
		super.onCreate();
		Services.Log.info("LocationFusedProviderIntentService onCreate");
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onHandleIntent(Intent intent) {
		Location location = intent.getParcelableExtra(com.google.android.gms.location.FusedLocationProviderApi.KEY_LOCATION_CHANGED);
		Services.Log.info("LocationFusedProviderIntentService onReceive onLocationChanged");

		if (location != null) {
			//our location based code
			Services.Log.info("LocationFusedProviderIntentService onReceive onLocationChanged", "Location: " + location);
			Services.Log.info("LocationFusedProviderIntentService ", "isTracking : " + mLocationActionsHelper.mIsTracking);

			MyApplication.getInstance().getGeoLocationHelper().onLocationChange(getApplicationContext(), location);
		}
	}
}
