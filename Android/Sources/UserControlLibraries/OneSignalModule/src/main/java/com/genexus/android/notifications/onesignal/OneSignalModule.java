package com.genexus.android.notifications.onesignal;

import android.content.Context;

import com.genexus.android.remotenotification.RemoteNotification;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.framework.GenexusModule;
import com.onesignal.OneSignal;

public class OneSignalModule implements GenexusModule {

	@Override
	public void initialize(Context context)
	{
		// OneSignal Initialization
		OneSignal.initWithContext(context);
		OneSignal.setAppId(Services.Application.getAppContext().getString(R.string.OneSignalAppId));
		OneSignal.setNotificationOpenedHandler(new NotificationOpenedHandler(context));


		OneSignalProvider notificationProvider = new OneSignalProvider();

		RemoteNotification.addProvider(notificationProvider);
		RemoteNotification.setDefaultProvider(notificationProvider);
	}
}
