package com.genexus.android.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.genexus.android.R;
import com.genexus.android.core.activities.IntentFactory;
import com.genexus.android.core.base.services.Services;

public class NotificationAlert extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		if (bundle == null) {
			throw new IllegalArgumentException("ID and NOTIFICATION extras are required to be passed" +
				"as arguments to use this BroadcastReceiver");
		}

		int id = bundle.getInt("ID", -1);
		if (id == -1) {
			throw new IllegalArgumentException("ID extra is missing");
		}

		String content = bundle.getString("NOTIFICATION");
		if (content == null) {
			throw new IllegalArgumentException("NOTIFICATION extra is missing");
		}

		String title = context.getString(R.string.app_name);
		Intent actionIntent = IntentFactory.getStartupActivity(context);

		Services.Notifications.showNotification(id, title, content, actionIntent);

		LocalNotificationsSQLiteHelper db  = LocalNotificationsSQLiteHelper.getInstance(context);
		db.deleteNotification(id);
	}
}
