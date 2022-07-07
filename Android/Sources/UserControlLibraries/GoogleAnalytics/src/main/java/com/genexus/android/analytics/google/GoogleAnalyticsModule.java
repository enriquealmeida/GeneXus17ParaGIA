package com.genexus.android.analytics.google;

import android.content.Context;

import com.genexus.android.analytics.Analytics;
import com.genexus.android.core.framework.GenexusModule;

/**
 * Google Analytics Module.
 */
public class GoogleAnalyticsModule implements GenexusModule
{
	@Override
	public void initialize(Context context)
	{
		Analytics.setProvider(new GoogleAnalyticsProvider(context));
	}
}
