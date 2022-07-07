package com.genexus.android.core.externalobjects.application

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.externalobjects.LocalNotificationsAPI

class CoreBootReceiverLocalNotifications : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Services.Log.debug("CoreExternalObjects", "ResetAlertsInAlarmManager")
        LocalNotificationsAPI.resetAlerts(context)
    }
}
