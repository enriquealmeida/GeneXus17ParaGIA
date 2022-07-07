package com.genexus.android.hms;

import android.content.Context;

import com.genexus.android.core.base.services.Services;
import com.google.android.gms.common.GoogleApiAvailability;
import com.huawei.hms.api.HuaweiApiAvailability;

public class HMSHelper  {

	public static boolean isHmsAvailable(Context context) {
		boolean isAvailable = false;
		if (null != context) {
			int result = HuaweiApiAvailability.getInstance().isHuaweiMobileServicesAvailable(context);
			isAvailable = (com.huawei.hms.api.ConnectionResult.SUCCESS == result);
		}
		Services.Log.info("isHmsAvailable: " + isAvailable);
		return isAvailable;
	}

	public static boolean isGmsAvailable(Context context) {
		boolean isAvailable = false;
		if (null != context) {
			int result = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
			isAvailable = (com.google.android.gms.common.ConnectionResult.SUCCESS == result);
		}
		Services.Log.info("isGmsAvailable: " + isAvailable);
		return isAvailable;
	}

}
