package com.genexus.android.notifications.onesignal;

import android.content.Context;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationReceivedEvent;
import com.onesignal.OneSignal;


import org.json.JSONObject;

public class CustomNotificationExtenderReceiver implements OneSignal.OSRemoteNotificationReceivedHandler {

	@Override
	public void remoteNotificationReceived(Context context, OSNotificationReceivedEvent notificationReceivedEvent) {
		OSNotification notification = notificationReceivedEvent.getNotification();

		String title = notification.getTitle();
		String content = notification.getBody();
		JSONObject data = notification.getAdditionalData();

		if (data == null) {
			notificationReceivedEvent.complete(notification);
			return;
		}

		JSONObject dataToProcess = ParsingUtils.parseDefaultAction(data);

		String action = dataToProcess != null ? ParsingUtils.parseAction(dataToProcess) : Strings.EMPTY;
		String parameters = dataToProcess != null ? ParsingUtils.parseParameters(dataToProcess) : Strings.EMPTY;
		String executionTime = dataToProcess != null ? ParsingUtils.parseExecutionTime(dataToProcess) : Strings.EMPTY;

		// process only silent notifications
		if (action != null && executionTime != null && executionTime.equalsIgnoreCase("1")) {
			Services.Notifications.handleNotification(title, content, action, parameters, executionTime);
			notificationReceivedEvent.complete(null);
		}

		// If complete isn't call within a time period of 25 seconds, OneSignal internal logic will show the original notification
		// If null is passed to complete
		notificationReceivedEvent.complete(notification);

	}
}
