package com.genexus.android.analytics.hms;

import android.content.Context;

import com.genexus.android.analytics.Analytics;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.framework.GenexusModule;
import com.genexus.android.hms.HMSHelper;

public class HMSAnalyticsModule implements GenexusModule {
    @Override
    public void initialize(Context context)
    {
		if (HMSHelper.isHmsAvailable(context))
		{
			Services.Log.info("HMSAnalyticsModule , HMS available, using HiAnalytics");
			Analytics.setProvider(new HMSAnalyticsProvider(context));
		}
		else
		{
			Services.Log.info("HMSAnalyticsModule , HMS NOT available, DO NOY register HiAnalytics");
		}


    }
}
