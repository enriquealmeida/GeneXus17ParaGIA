package com.genexus.android.core.base.synchronization;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.SystemClock;

import com.genexus.android.core.base.services.Services;

/**
 * SynchronizationSendAlarm.
 */
public class SynchronizationSendAlarm extends BroadcastReceiver
{
	@Override
	@SuppressWarnings("deprecation")
	public void onReceive(Context context, Intent intent) 
	{
		Services.Log.debug("SynchronizationSendAlarm alarm onReceive");

		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			NetworkCapabilities networkCapabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());

			// Not realy network change, but force send pending events if any.
			SynchronizationWhenConnected.onNetworkStatusChanged(context, networkCapabilities);
		} else {
			android.net.NetworkInfo networkInfo = cm.getActiveNetworkInfo();

			// Not realy network change, but force send pending events if any.
			SynchronizationWhenConnected.onNetworkStatusChanged(context, networkInfo);
		}

		// cancel any other pending alarm
		cancelAlarm(context);
	}

	public void setAlarm(Context context)
    {
	    AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SynchronizationSendAlarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        //example
        //After after 30 seconds, every 1 minutes
        //am.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime()+ (1000 * 30), (1000 * 60) , pi);

        // run when metadata send sync do so
		// Only call send if time between send has elapsed.
		// calculate time dif
		long minTimeBetweenSends = Services.Application.get().getSynchronizerMinTimeBetweenSends();
		// minTimeBetweenSends in seconds
		long lastSend = Services.Sync.getSendLastTime();

		long whenToRaise = lastSend + (minTimeBetweenSends * 1000) ; // when the pi should be raise

		am.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime()+ (whenToRaise - lastSend), pi);

	}

    public void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, SynchronizationSendAlarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

}
