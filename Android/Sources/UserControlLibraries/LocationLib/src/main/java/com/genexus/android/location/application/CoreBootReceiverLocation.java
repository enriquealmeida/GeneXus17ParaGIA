package com.genexus.android.location.application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.genexus.android.core.base.services.Services;

public class CoreBootReceiverLocation extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Services.Log.debug("LocationLib", "ResetProximityAlertsInGeofences");
		Services.Location.resetProximityAlerts();
	}
}
