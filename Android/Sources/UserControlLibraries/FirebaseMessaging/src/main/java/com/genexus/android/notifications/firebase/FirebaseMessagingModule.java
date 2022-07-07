package com.genexus.android.notifications.firebase;

import android.content.Context;

import com.genexus.android.remotenotification.RemoteNotification;
import com.genexus.android.core.framework.GenexusModule;

public class FirebaseMessagingModule implements GenexusModule {
    @Override
    public void initialize(Context context)
    {
        FirebaseMessagingProvider notificationProvider = new FirebaseMessagingProvider();
        RemoteNotification.addProvider(notificationProvider);
        RemoteNotification.setDefaultProvider(notificationProvider);
    }
}
