package com.genexus.android.notifications.jpush;

import android.content.Context;

import androidx.annotation.NonNull;

import com.genexus.android.device.ClientInformation;
import com.genexus.android.notification.NotificationDeviceRegister;
import com.genexus.android.remotenotification.IRemoteNotificationProvider;
import com.genexus.android.core.base.metadata.GenexusApplication;
import com.genexus.android.core.base.services.Services;
import com.google.gson.JsonObject;

import cn.jpush.android.api.JPushInterface;

public class JPushProvider implements IRemoteNotificationProvider {
	private Context mContext;
	private GenexusApplication mGenexusApplication;

	@Override
	public @NonNull String getId() {
		return "jpush";
	}

	private static String sDeviceId = null;
	private static String sRegistrationId = null;
	private static int sRegisterTimes = 0;

	@Override
	public void registerDevice(Context context, GenexusApplication genexusApplication) {
		mContext = context;
		mGenexusApplication = genexusApplication;
		sDeviceId = JPushUtil.getDeviceId(context);
		JPushUtil.showLog("Device Id: " + sDeviceId);
		sRegistrationId = JPushInterface.getRegistrationID(context);
		if (!sRegistrationId.isEmpty())
			registerInServer();
		else
			registerInJPush();
	}

	private void registerInServer() {
		JPushUtil.showLog("Registration Id: " + sRegistrationId);
		Thread thread = new Thread(null, () -> {
			if (sDeviceId != null && sRegistrationId != null) {
				String deviceTokenJson = getDeviceTokenJson();
				NotificationDeviceRegister.registerWithServer(mGenexusApplication, deviceTokenJson);
			}
		}, "RegisterServerInBackground");
		thread.start();
	}

	private void registerInJPush() {
		Thread thread = new Thread(null, () -> {
			if (sRegisterTimes == 0) {
				JPushUtil.showLog("Trying to initialize in background process #" + sRegisterTimes);
				JPushInterface.init(mContext);
			} else
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			sRegisterTimes += 1;
			JPushUtil.showLog("Trying to register id in background process #" + sRegisterTimes);
			sRegistrationId = JPushInterface.getRegistrationID(mContext);
			if (!sRegistrationId.isEmpty())
				registerInServer();
			else if (sRegisterTimes < 60)
				registerInJPush();
			else
				JPushUtil.showToast("Get notification registration id fail.", Services.Application.getAppContext());
		}, "RegisterInJPushBackground");
		thread.start();
	}

	// json like token :
	// { DeviceToken, DeviceId,  DeviceType,
	// NotificationPlatform = "APNS" || "GCM" || "OneSignal"
	// NotificationPlatformId = "ID del Usuario en el Platform" (en caso que aplique)
	// }
	private static String getDeviceTokenJson() {
		JsonObject objectToSend = new JsonObject();
		objectToSend.addProperty("DeviceToken", sDeviceId);
		objectToSend.addProperty("DeviceId", ClientInformation.id());
		objectToSend.addProperty("DeviceType", 1);
		objectToSend.addProperty("NotificationPlatform", "JPush");
		objectToSend.addProperty("NotificationPlatformId", sRegistrationId);
		return objectToSend.toString();
	}
}
