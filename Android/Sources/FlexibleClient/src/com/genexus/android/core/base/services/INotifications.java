package com.genexus.android.core.base.services;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.DrawableRes;

public interface INotifications {
	void handleNotification(String title, String content, String action, String parameters,
							String executionTime);

	/**
	 * Creates and displays a Notification with some preset properties, such as the Lollipop color
	 * and notification icon (uses the "Android Notification Icon" as defined in the main object if
	 * available, otherwise the gx_notification_default small icon).
	 */
	void showNotification(int id, String title, String content, Intent actionIntent);
	void showOngoingNotification(int id, String title, String content, @DrawableRes int drawableId,
								 boolean showIndeterminateProgress);
	void closeOngoingNotification(int id);

	void executeNotificationAction(Activity activity, String action, String parameters, String fromObject);
}
