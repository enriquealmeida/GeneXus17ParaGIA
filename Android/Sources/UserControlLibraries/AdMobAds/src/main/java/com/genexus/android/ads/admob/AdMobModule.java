package com.genexus.android.ads.admob;

import android.content.Context;

import com.genexus.android.core.controls.ads.Ads;
import com.genexus.android.core.framework.GenexusModule;

/**
 * AdMob Ads Module.
 */
public class AdMobModule implements GenexusModule
{
	@Override
	public void initialize(Context context)
	{
		AdMobAdProvider adMob = new AdMobAdProvider();

		Ads.addProvider(adMob);
		Ads.setDefaultProvider(adMob);
	}
}
