package com.genexus.android.notifications.firebase;

import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import com.genexus.android.core.activities.GenexusActivity;
import com.genexus.android.device.ClientInformation;
import com.genexus.android.notification.NotificationDeviceRegister;
import com.genexus.android.core.base.services.Services;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonObject;

import androidx.core.app.NotificationCompat;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMsgService";

    @Override
    public void onNewToken(String newToken) {
        super.onNewToken(newToken);
		Services.Log.debug(TAG, "New token: " + newToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(newToken);
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        String deviceTokenJson = getDeviceTokenJson(token);
        NotificationDeviceRegister.registerWithServer(Services.Application.get(), deviceTokenJson);
    }

    // json like token :
    // { DeviceToken, DeviceId,  DeviceType,
    // NotificationPlatform = "APNS" || "GCM" || "OneSignal"
    // NotificationPlatformId = "ID del Usuario en el Platform" (en caso que aplique)
    // }
    public static String getDeviceTokenJson(String token) {
        JsonObject objectToSend = new JsonObject();
        objectToSend.addProperty("DeviceToken", token);
        objectToSend.addProperty("DeviceId", ClientInformation.id());
        objectToSend.addProperty("DeviceType", 1);
        objectToSend.addProperty("NotificationPlatform", "Firebase");
        return objectToSend.toString();
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

		Services.Log.debug(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
			Services.Log.debug(TAG, "Message notification Title: " + remoteMessage.getNotification().getTitle() + " Body: " + remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
			Services.Log.debug(TAG, "Message data payload: " + remoteMessage.getData());

            String payload = remoteMessage.getData().get("payload");
            String title = remoteMessage.getData().get("title");
            String action = remoteMessage.getData().get("action");
            String parameters = remoteMessage.getData().get("parameters");
            String executionTime = remoteMessage.getData().get("executiontime");

            // Commented because they aren't needed, wasn't deleted just in case, they were used in GCM
//            String from = remoteMessage.getData().get("from");
//            String cmd = remoteMessage.getData().get("CMD");

            if (/* Check if data needs to be processed by long running job */ false) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob(getApplicationContext());
            } else {
                // Handle message within 10 seconds
                Services.Notifications.handleNotification(title, payload, action, parameters, executionTime);
            }
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    /**
     * Schedule a job using WorkManager.
     * NOT USED, keep it just in case in the future is needed
     */
    private void scheduleJob(Context context) {
    	String jobTag = "my-job-tag";
		OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                .addTag(jobTag)
                .build();
		WorkManager.getInstance(context)
			.enqueueUniqueWork(jobTag, ExistingWorkPolicy.KEEP, workRequest);
	}

    /**
     * Create and show a simple notification containing the received FCM message.
     * NOT USED, keep it just in case in the future is needed
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, GenexusActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        String channelId = "default_channel"; //getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
//                        .setSmallIcon(R.drawable.ic_stat_ic_notification)
                        .setContentTitle("FCM Message")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        android.app.NotificationManager notificationManager =
                (android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    android.app.NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
