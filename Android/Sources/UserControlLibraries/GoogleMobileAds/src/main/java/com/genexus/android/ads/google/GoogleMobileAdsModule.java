package com.genexus.android.ads.google;

import android.content.Context;

import com.genexus.android.core.controls.ads.Ads;
import com.genexus.android.core.framework.GenexusModule;

/**
 * GoobleMobile Ads Module.
 */
public class GoogleMobileAdsModule implements GenexusModule
{
	@Override
	public void initialize(Context context)
	{
		GoogleMobileAdsProvider adMob = new GoogleMobileAdsProvider();
		Ads.addProvider(adMob);
		Ads.setDefaultProvider(adMob);
	}
}
