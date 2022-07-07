package com.genexus.android.core.externalobjects;

import com.genexus.android.notification.Notification;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.ReflectionHelper;
import com.genexus.GXBaseCollection;

import java.util.List;

import json.org.json.JSONArray;
import json.org.json.JSONException;
import json.org.json.JSONObject;

@SuppressWarnings("rawtypes")
public class LocalNotificationsAPIOffline {
	public static int createAlerts(GXBaseCollection[] alerts) {
		GXBaseCollection collection = alerts[0];
		try {
			JSONArray jsonAlerts = (JSONArray) collection.GetJSONObject();
			for (int i = 0; i < jsonAlerts.length(); i++) {
				JSONObject alert;
				if (jsonAlerts.get(i) instanceof JSONObject)
					alert = jsonAlerts.getJSONObject(i);
				else
					alert = (JSONObject) collection.getStruct().get(i);

				LocalNotificationsAPI.createAlerts(Services.Application.getAppContext(), alert.optString("Text"), alert.optString("DateTime"));
			}
			return 1;
		} catch (JSONException e) {
			Services.Log.error(e);
			return 0;
		}
	}

	public static GXBaseCollection listAlerts() {
		Class<?> clazzType = ReflectionHelper.getClass(Object.class, "com.genexuscore.genexus.sd.notifications.SdtLocalNotificationsInfo_Item");
		GXBaseCollection alerts = new GXBaseCollection(clazzType, "LocalNotificationsInfo.Item", "LocalNotifications", Services.Application.get().getRemoteHandle());
		if (clazzType == null) {
			Services.Log.error("SdtLocalNotificationsInfo_Item not found ");
			return alerts;
		}

		List<Notification> notifications = LocalNotificationsAPI.getNotifications();
		JSONArray notificationsJson = new JSONArray();
		try {
			for (Notification n : notifications) {
				JSONObject jsonNotification = new JSONObject();
				jsonNotification.put("Text", n.getText());
				jsonNotification.put("DateTime", n.getDateTime());
				notificationsJson.put(jsonNotification);
			}
		} catch (JSONException e) {
			Services.Log.error(e);
		}

		alerts.fromJSonString(notificationsJson.toString());
		return alerts;
	}

	public static int removeAlerts(GXBaseCollection[] alerts) {
		GXBaseCollection collection = alerts[0];
		try {
			JSONArray jsonAlerts = (JSONArray) collection.GetJSONObject();
			for (int i = 0; i < jsonAlerts.length(); i++) {
				JSONObject alert;
				if (jsonAlerts.get(i) instanceof JSONObject)
					alert = jsonAlerts.getJSONObject(i);
				else
					alert = (JSONObject) collection.getStruct().get(i);

				LocalNotificationsAPI.removeAlert(Services.Application.getAppContext(), alert.optString("Text"), alert.optString("DateTime"));
			}
			return 1;
		} catch (JSONException e) {
			Services.Log.error(e);
			return 0;
		}
	}

	public static int removeAllAlerts() {
		LocalNotificationsAPI.removeAllAlerts(Services.Application.getAppContext());
		return 1;
	}
}
