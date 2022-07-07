package com.genexus.android.core.controls.ads;

import com.genexus.android.core.base.utils.NameMap;

/**
 * Register Ads providers here, the Ads.addProvider must be called at app creation time, taking into account the controls used in the app
 */
public class Ads
{
	private static NameMap<IAdsProvider> sAdsProviders;
	private static IAdsProvider sDefaultProvider;

	static
	{
		sAdsProviders = new NameMap<>();
	}

	public static void addProvider(IAdsProvider provider)
	{
		sAdsProviders.put(provider.getId(), provider);
	}

	public static void setDefaultProvider(IAdsProvider provider)
	{
		addProvider(provider);
		sDefaultProvider = provider;
	}

	public static IAdsProvider getDefaultProvider()
	{
		if (sDefaultProvider == null && sAdsProviders.size() != 0)
			sDefaultProvider = sAdsProviders.firstEntry().getValue();

		return sDefaultProvider;
	}

	static IAdsProvider getProvider(String id)
	{
		return sAdsProviders.get(id);
	}
}
