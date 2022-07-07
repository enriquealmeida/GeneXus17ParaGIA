package com.genexus.android.notifications.onesignal;

import android.content.Context;

import androidx.annotation.NonNull;

import com.genexus.android.device.ClientInformation;
import com.genexus.android.notification.NotificationDeviceRegister;
import com.genexus.android.remotenotification.IRemoteNotificationProvider;
import com.genexus.android.core.base.metadata.GenexusApplication;
import com.genexus.android.core.base.services.Services;
import com.google.gson.JsonObject;
import com.onesignal.OSDeviceState;
import com.onesignal.OSSubscriptionObserver;
import com.onesignal.OSSubscriptionStateChanges;
import com.onesignal.OneSignal;

public class OneSignalProvider implements IRemoteNotificationProvider, OSSubscriptionObserver {
	private GenexusApplication mGenexusApplication;

	private boolean mSubscriptionObserverAdded = false;

	@Override
	public @NonNull String getId() {
		return "onesignal";
	}

	private static String mUserId = null;
	private static String mRegistrationId = null;

	@Override
	public void registerDevice(Context context, GenexusApplication genexusApplication) {
		mGenexusApplication = genexusApplication;

		OSDeviceState device = OneSignal.getDeviceState();

		String userId = device.getUserId();
		String pushToken = device.getPushToken();
		boolean couldRegister = callToRegisterInServer(userId, pushToken);
		if (!couldRegister)
		{
			if (!mSubscriptionObserverAdded) {
				Services.Log.debug("OneSignalProvider Could not register now, subscribe for modifications");
				OneSignal.addSubscriptionObserver(this);
				mSubscriptionObserverAdded = true;
			}
		}

	}

	private boolean callToRegisterInServer(String userId, String pushToken )
	{
		Services.Log.debug("OneSignalProvider User:" + userId);
		Services.Log.debug("OneSignalProvider RegistrationId:" + pushToken);
		if (userId!=null && pushToken!=null)
		{
			mRegistrationId = pushToken;
			mUserId = userId;
			reRegisterInServer();
			return true;
		}
		return false;
	}


	private void reRegisterInServer() {
		Thread thread = new Thread(null, () -> {
			if (mUserId != null && mRegistrationId != null) {
				String deviceTokenJson = getDeviceTokenJson();
				NotificationDeviceRegister.registerWithServer(mGenexusApplication, deviceTokenJson);
			}
		}, "RegisterInBackground");
		thread.start();
	}

	@Override
	public void onOSSubscriptionChanged(OSSubscriptionStateChanges stateChanges) {
		if (!stateChanges.getFrom().isSubscribed() &&
			stateChanges.getTo().isSubscribed()) {
			Services.Log.info("OneSignalProvider You've successfully subscribed to push notifications!");
			// get player ID and push token
			String userId = stateChanges.getTo().getUserId();
			String pushToken = stateChanges.getTo().getPushToken();
			callToRegisterInServer(userId, pushToken);
		}
		Services.Log.debug("Debug", "onOSPermissionChanged: " + stateChanges);
	}

	// json like token :
	// { DeviceToken, DeviceId,  DeviceType,
	// NotificationPlatform = "APNS" || "GCM" || "OneSignal"
	// NotificationPlatformId = "ID del Usuario en el Platform" (en caso que aplique)
	// }
	private static String getDeviceTokenJson() {
		JsonObject objectToSend = new JsonObject();
		objectToSend.addProperty("DeviceToken", mRegistrationId);
		objectToSend.addProperty("DeviceId", ClientInformation.id());
		objectToSend.addProperty("DeviceType", 1);
		objectToSend.addProperty("NotificationPlatform", "OneSignal");
		objectToSend.addProperty("NotificationPlatformId", mUserId);
		return objectToSend.toString();
	}
}
