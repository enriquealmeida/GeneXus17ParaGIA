package com.genexus.android.notifications.hms;

import android.content.Context;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.framework.GenexusModule;
import com.genexus.android.hms.HMSHelper;

public class HmsOneSignalModule implements GenexusModule {

	@Override
	public void initialize(Context context)
	{
		if (HMSHelper.isHmsAvailable(context))
		{
			Services.Log.info("HmsOneSignalModule , HMS available, using OneSignal for HMS");
		}
		else
		{
			Services.Log.info("HmsOneSignalModule , HMS NOT available, using OneSignal base module");
		}
	}
}
