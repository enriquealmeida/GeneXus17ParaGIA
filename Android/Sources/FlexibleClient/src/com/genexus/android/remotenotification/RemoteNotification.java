package com.genexus.android.remotenotification;
import com.genexus.android.core.base.utils.NameMap;

/**
 * Register Ads providers here, the Ads.addProvider must be called at app creation time, taking into account the controls used in the app
 */
public class RemoteNotification
{
	private static NameMap<IRemoteNotificationProvider> sRemoteNotificationProviders;
	private static IRemoteNotificationProvider sDefaultProvider;

	static
	{
		sRemoteNotificationProviders = new NameMap<>();
	}

	public static void addProvider(IRemoteNotificationProvider provider)
	{
		sRemoteNotificationProviders.put(provider.getId(), provider);
	}

	public static void setDefaultProvider(IRemoteNotificationProvider provider)
	{
		addProvider(provider);
		sDefaultProvider = provider;
	}

	public static IRemoteNotificationProvider getDefaultProvider()
	{
		if (sDefaultProvider == null && sRemoteNotificationProviders.size() != 0)
			sDefaultProvider = sRemoteNotificationProviders.firstEntry().getValue();

		return sDefaultProvider;
	}

	static IRemoteNotificationProvider getProvider(String id)
	{
		return sRemoteNotificationProviders.get(id);
	}
}
