package com.genexus.android.core.externalobjects;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.notification.LocalNotificationsSQLiteHelper;
import com.genexus.android.notification.Notification;
import com.genexus.android.notification.NotificationAlert;
import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.externalapi.ExternalApiResult;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
public class LocalNotificationsAPI extends ExternalApi {
	public static final String OBJECT_NAME = "GeneXus.SD.Notifications.LocalNotifications";

	private static final String METHOD_CREATE_ALERTS = "CreateAlerts";
	private static final String METHOD_LIST_ALERTS = "ListAlerts";
	private static final String METHOD_REMOVE_ALERTS = "RemoveAlerts";
	private static final String METHOD_REMOVE_ALL_ALERTS = "RemoveAllAlerts";

	private static AlarmManager mAlarmManager = (AlarmManager) Services.Application.getAppContext().getSystemService(Context.ALARM_SERVICE);
	private static LocalNotificationsSQLiteHelper mDatabase = LocalNotificationsSQLiteHelper.getInstance(Services.Application.getAppContext());

	public LocalNotificationsAPI(ApiAction action) {
		super(action);
		addMethodHandler(METHOD_CREATE_ALERTS, 1, mCreateAlerts);
		addMethodHandler(METHOD_LIST_ALERTS, 0, mListAlerts);
		addMethodHandler(METHOD_REMOVE_ALERTS, 1, mRemoveAlerts);
		addMethodHandler(METHOD_REMOVE_ALL_ALERTS, 0, mRemoveAllAlerts);
	}

	private final IMethodInvoker mCreateAlerts = parameters -> {
		EntityList notificationList = (EntityList) parameters.get(0);
		if (notificationList != null) {
			for (Entity notification : notificationList) {
				String text = (String) notification.getProperty("Text");
				String dateTime = (String) notification.getProperty("DateTime");
				createAlerts(getContext(), text, dateTime);
			}
		}
		return ExternalApiResult.SUCCESS_CONTINUE;
	};

	private final IMethodInvoker mListAlerts = parameters -> {
		EntityList alerts = new EntityList();
		alerts.setItemType(Expression.Type.SDT);
		List<Notification> notifications = mDatabase.getAllNotifications();
		for (Notification n : notifications) {
			Entity alert = EntityFactory.newSdt("GeneXus.SD.Notifications.LocalNotificationsInfo");
			alert.setProperty("DateTime", n.getDateTime());
			alert.setProperty("Text", n.getText());
			alerts.add(alert);
		}

		return ExternalApiResult.success(alerts);
	};

	private final IMethodInvoker mRemoveAlerts = parameters -> {
		EntityList alerts = (EntityList) parameters.get(0);
		if (alerts != null)
			for (Entity a : alerts)
				removeAlert(getContext(), a.optStringProperty("Text"), a.optStringProperty("DateTime"));

		return ExternalApiResult.SUCCESS_CONTINUE;
	};

	private final IMethodInvoker mRemoveAllAlerts = parameters -> {
		removeAllAlerts(getContext());
		return ExternalApiResult.SUCCESS_CONTINUE;
	};

	public static void createAlerts(Context context, String text, String dateTime) {
		Notification n = new Notification();
		n.setDateTime(dateTime);
		n.setText(text);

		Date date = Services.Strings.getDateTime(dateTime);
		if (date == null) {
			Services.Log.debug("empty or null date, handle as now date");  // show notification immediately.
			date = new Date();
			n.setDateTime(Services.Strings.getDateTimeStringForServer(date));
		}

		int uniqueId = (int) mDatabase.addNotification(n);
		PendingIntent pendingIntent = getPendingIntent(context, text, uniqueId);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		mAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
	}

	public static void removeAlert(Context context, String text, String dateTime) {
		// before remove alert, remove am alert
		Notification n = new Notification();
		n.setText(text);
		n.setDateTime(dateTime);
		int uniqueId = mDatabase.getId(n);
		mAlarmManager.cancel(getPendingIntent(context, text, uniqueId));
		mDatabase.deleteNotification(uniqueId);
	}

	public static void removeAllAlerts(Context context) {
		List<Notification> notifications = getNotifications();
		for (Notification n : notifications)
			removeAlert(context, n.getText(), n.getDateTime());

		mDatabase.deleteAllNotifications();
	}

	public static List<Notification> getNotifications() {
		return mDatabase.getAllNotifications();
	}

	// method to re enable notification after reboot
	public static void resetAlerts(Context context) {
		List<Notification> notifications = getNotifications();
		for (Notification n : notifications) {
			int uniqueId = mDatabase.getId(n);
			Services.Log.debug("Notification Id :" + uniqueId + " time " + n.getDateTime());
			Date date = Services.Strings.getDateTime(n.getDateTime());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			mAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), getPendingIntent(context, n.getText(), uniqueId));
		}
	}

	private static PendingIntent getPendingIntent(Context context, String text, int uniqueId) {
		Intent intent = new Intent(context, NotificationAlert.class);
		intent.putExtra("ID", uniqueId);
		intent.putExtra("NOTIFICATION", text);
		return PendingIntent.getBroadcast(context, uniqueId, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
	}
}
