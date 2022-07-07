package com.genexus.android.location.geolocation.fused;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.location.geolocation.tracking.TrackingData;

public class LocationFusedProviderReceiver extends BroadcastReceiver {

	private static final String TAG = "LocationFusedProviderReceiver";

	public LocationFusedProviderReceiver() {
		Services.Log.debug(TAG, "constructor");
		boolean isTracking = Services.Location.isTracking();
		if (!isTracking) {
			Services.Log.debug(TAG, "constructor. restore from session ");
			TrackingData.getInstance().restoreTrackingData();
		}

		Services.Log.debug(TAG, "constructor. isTracking " + isTracking);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras == null) {
			Services.Log.warning(TAG, "onReceive with no extras. Did you forget the foreground service?");
			return;
		}

		Location location = (Location) extras.get(com.google.android.gms.location.FusedLocationProviderApi.KEY_LOCATION_CHANGED);
		Services.Log.debug(TAG, "onReceive onLocationChanged - Location: " + location);
		Services.Log.debug(TAG, "isTracking : " + Services.Location.isTracking());
		Services.Location.onLocationChange(location);
	}
}
