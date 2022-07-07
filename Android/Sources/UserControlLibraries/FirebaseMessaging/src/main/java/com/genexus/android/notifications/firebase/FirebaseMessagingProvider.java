package com.genexus.android.notifications.firebase;

import android.content.Context;
import androidx.annotation.NonNull;

import com.genexus.android.notification.NotificationDeviceRegister;
import com.genexus.android.remotenotification.IRemoteNotificationProvider;
import com.genexus.android.core.base.metadata.GenexusApplication;
import com.google.firebase.iid.FirebaseInstanceId;

public class FirebaseMessagingProvider implements IRemoteNotificationProvider {
    @Override
    public @NonNull String getId() {
        return "firebase";
    }

    @Override
    @SuppressWarnings("deprecation") // TODO: Fix this deprecation!
    public void registerDevice(Context context, GenexusApplication genexusApplication) {
//        FirebaseMessaging.getInstance().subscribeToTopic("A");

        String token = FirebaseInstanceId.getInstance().getToken();
        String deviceTokenJson = MyFirebaseMessagingService.getDeviceTokenJson(token);
        NotificationDeviceRegister.registerWithServer(genexusApplication, deviceTokenJson);
    }
}
