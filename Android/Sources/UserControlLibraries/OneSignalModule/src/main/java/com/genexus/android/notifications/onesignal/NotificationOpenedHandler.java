package com.genexus.android.notifications.onesignal;

import android.content.Context;
import android.content.Intent;

import com.genexus.android.core.activities.ActivityHelper;
import com.genexus.android.core.activities.IntentFactory;
import com.genexus.android.core.base.services.Services;
import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.genexus.android.core.activities.ActivityLauncher.INTENT_EXTRA_LINK_ACTION;
import static com.genexus.android.core.utils.FileUtils2.isHttp;

public class NotificationOpenedHandler implements OneSignal.OSNotificationOpenedHandler {
	private final Context mContext;

	NotificationOpenedHandler(Context context) {
		mContext = context;
	}

	@Override
	public void notificationOpened(OSNotificationOpenedResult result) {
		JSONObject data = result.getNotification().getAdditionalData();
		String actionId = result.getAction().getActionId();
		String link = result.getNotification().getLaunchURL();
		if (isHttp(link)) {
			startActivity(INTENT_EXTRA_LINK_ACTION, link);
		} else if (data == null) {
			// if no data (probably One Signal notification), notification call app startup by default
			callAppStartup();
		} else if (Services.Strings.hasValue(actionId) && actionId.startsWith("Silent:")) {
			// special silent action:
			JSONArray arrayDataToProcess = data.optJSONArray("uia");

			JSONObject dataToProcess = ParsingUtils.getActionJsonObject(actionId, arrayDataToProcess);
			if (dataToProcess == null)
				return;

			String action = ParsingUtils.parseAction(dataToProcess);
			String parameters = ParsingUtils.parseParameters(dataToProcess);
			if (action != null) {
				Services.Notifications.executeNotificationAction(ActivityHelper.getCurrentActivity(),
					action, parameters, null);
			}
		} else {
			JSONObject dataToProcess = null;
			if (Services.Strings.hasValue(actionId)) {
				//get the data for this action.
				JSONArray arrayDataToProcess = data.optJSONArray("uia");
				dataToProcess = ParsingUtils.getActionJsonObject(actionId, arrayDataToProcess);
			}

			if (dataToProcess == null)
				dataToProcess = ParsingUtils.parseDefaultAction(data);

			if (dataToProcess == null) {
				// if no action , notification call app startup by default
				callAppStartup();
				return;
			}

			String action = ParsingUtils.parseAction(dataToProcess);
			String parameters = ParsingUtils.parseParameters(dataToProcess);
			Services.Log.debug("notificationOpened action: " + action + " params " + parameters);
			startActivity(action, parameters);
		}
	}

	private void startActivity(String action, String parameters) {
		Intent actionIntent = IntentFactory.createNotificationActionIntent(mContext, action,
			parameters, null, true);
		mContext.startActivity(actionIntent);
	}

	private void callAppStartup() {
		Services.Log.debug("notificationOpened no action, call main. ");
		Intent intent = IntentFactory.getStartupActivity(mContext);
		// needed to call with this flag from this context
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
	}
}
