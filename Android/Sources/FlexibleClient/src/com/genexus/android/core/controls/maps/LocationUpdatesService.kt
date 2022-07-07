package com.genexus.android.core.controls.maps

import android.app.ActivityManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import android.location.LocationListener
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.genexus.android.PermissionUtil
import com.genexus.android.R
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.PrimitiveUtils

/**
 * A bound and started service that is promoted to a foreground service when location updates have
 * been requested and all clients unbind.
 *
 * For apps running in the background on "O" devices, location is computed only once every 10
 * minutes and delivered batched every 30 minutes. This restriction applies even to apps
 * targeting "N" or lower which are run on "O" devices.
 *
 * This sample show how to use a long-running service for location updates. When an activity is
 * bound to this service, frequent location updates are permitted. When the activity is removed
 * from the foreground, the service promotes itself to a foreground service, and location updates
 * continue. When the activity comes back to the foreground, the foreground service stops, and the
 * notification assocaited with that service is removed.
 */
abstract class LocationUpdatesService : Service(), ILocationUpdatesService {

    /**
     * Used to check whether the bound activity has really gone away and not unbound as part of an
     * orientation change. We create a foreground service notification only if the former takes
     * place.
     */
    private var changingConfiguration = false
    private var notificationManager: NotificationManager? = null
    private var serviceHandler: Handler? = null
    private var lastKnownLocation: Location? = null
    private val binder: IBinder = LocalBinder()
    private val listeners = mutableListOf<LocationListener>()
    private var isOneTime = false
    var isForeground = false
        private set

    override fun onCreate() {
        super.onCreate()
        val handlerThread = HandlerThread(TAG).apply { start() }
        serviceHandler = Handler(handlerThread.looper)
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (PermissionUtil.checkAnyLocationPermission(applicationContext))
            calculateLastLocation()

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager?.let {
                val channel = NotificationChannel(
                    CHANNEL_ID, getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply { setSound(null, null) }
                it.createNotificationChannel(channel)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Services.Log.info(TAG, "Service started")
        isOneTime = intent?.getBooleanExtra(LocationProvider.KEY_ONE_TIME, false) ?: false
        val startedFromNotification = intent?.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION, false)

        // We got here because the user decided to remove location updates from the notification.
        if (startedFromNotification == true) {
            removeLocationUpdates()
            stopSelf()
        }

        // Tells the system to not try to recreate the service after it has been killed.
        return START_NOT_STICKY
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        changingConfiguration = true
    }

    /**
     * Called when a client (MainActivity in case of this sample) comes to the foreground
     * and binds with this service. The service should cease to be a foreground service
     * when that happens.
     */
    override fun onBind(intent: Intent?): IBinder? {
        Services.Log.info(TAG, "in onBind()")
        if (isForeground) stopForeground(true)
        changingConfiguration = false
        return binder
    }

    /**
     * Called when a client (MainActivity in case of this sample) returns to the foreground
     * and binds once again with this service. The service should cease to be a foreground
     * service when that happens.
     */
    override fun onRebind(intent: Intent?) {
        Services.Log.info(TAG, "in onRebind()")
        if (isForeground) stopForeground(true)
        changingConfiguration = false
        super.onRebind(intent)
    }

    /**
     * Called when the last client (MainActivity in case of this sample) unbinds from this
     * service. If this method is called due to a configuration change in MainActivity, we
     * do nothing. Otherwise, we make this service a foreground service.
     */
    override fun onUnbind(intent: Intent?): Boolean {
        Services.Log.info(TAG, "Last client unbound from service")
        if (!changingConfiguration && TrackingUtils.requestingLocationUpdates() && isForeground) {
            Services.Log.info(TAG, "Starting foreground service")
            startForeground(NOTIFICATION_ID, getNotification())
        }
        return true // Ensures onRebind() is called when a client re-binds.
    }

    override fun onDestroy() {
        serviceHandler?.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    override fun requestLocationUpdates(locationRequest: LocationRequestParameters) {
        requestLocationUpdates(null, locationRequest)
    }

    override fun requestLocationUpdates(locationListener: LocationListener?) {
        requestLocationUpdates(locationListener, null)
    }

    override fun requestLocationUpdates(locationListener: LocationListener?, locationRequest: LocationRequestParameters?) {
        val request = locationRequest ?: getDefaultRequest()
        Services.Log.info(TAG, "Requesting location updates")
        isForeground = request.useForegroundService
        TrackingUtils.setRequestingLocationUpdates(true)
        startService(Intent(applicationContext, this.javaClass))
        try {
            requestUpdates(request)
            locationListener?.let { listeners.add(it) }
        } catch (unlikely: SecurityException) {
            TrackingUtils.setRequestingLocationUpdates(false)
            Services.Log.error(TAG, "Lost location permission. Could not request updates.", unlikely)
        }
    }

    override fun removeLocationUpdates() {
        Services.Log.info(TAG, "Removing location updates")
        TrackingUtils.setRequestingLocationUpdates(false)
        try {
            removeUpdates()
        } catch (unlikely: SecurityException) {
            TrackingUtils.setRequestingLocationUpdates(true)
            Services.Log.error(TAG, "Lost location permission. Could not remove updates. $unlikely")
        }
    }

    override fun getLastKnownLocation(): Location? {
        return lastKnownLocation
    }

    /**
     * Returns true if this is a foreground service.
     * @param context The {@link Context}.
     */
    @Suppress("DEPRECATION")
    private fun serviceIsRunningInForeground(context: Context?): Boolean {
        val manager = context?.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (javaClass.name == service.service.className) {
                if (service.foreground)
                    return true
            }
        }
        return false
    }

    protected fun onNewLocation(location: Location?) {
        if (location == null)
            return

        Services.Log.info(TAG, "New location: $location")
        lastKnownLocation = location

        // Notify anyone listening for broadcasts about the new location.
        val intent = Intent(ACTION_BROADCAST)
        intent.putExtra(EXTRA_LOCATION, location)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)

        // Update notification content always, service is always running in foreground. check for safety
        if (serviceIsRunningInForeground(this))
            notificationManager?.notify(NOTIFICATION_ID, getNotification())

        Thread { Services.Location.onLocationChange(location) }.start()
        for (listener in listeners)
            listener.onLocationChanged(location)

        if (isOneTime) // Service started to calculate location only one time, stop it after getting it
            stopSelf()
    }

    /**
     * Returns the [NotificationCompat] used as part of the foreground service.
     */
    @Suppress("DEPRECATION")
    private fun getNotification(): Notification? {
        // Extra to help us figure out if we arrived in onStartCommand via the notification or not.
        val intent = Intent(this, this.javaClass)
        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true)
        return NotificationCompat.Builder(this, CHANNEL_ID).apply {
            val text: CharSequence = TrackingUtils.getLocationText(this@LocationUpdatesService, lastKnownLocation)
            setContentTitle(TrackingUtils.getLocationTitle(this@LocationUpdatesService))
            setContentText(text)
            setOngoing(true)
            setSmallIcon(Services.Resources.getResourceId("gx_notification_default", "drawable"))
            val customResourceId = Services.Resources.getResourceId("gx_notification_icon", "drawable")
            if (PrimitiveUtils.isNonZero(customResourceId)) setSmallIcon(customResourceId)
            setTicker(text)
            priority = Notification.PRIORITY_HIGH
            setWhen(System.currentTimeMillis())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                setChannelId(CHANNEL_ID)
        }.build()
    }

    private fun getDefaultRequest(): LocationRequestParameters {
        Services.Log.debug(TAG, "LocationRequest is null")
        return LocationRequestParameters.default()
    }

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        val service: LocationUpdatesService = this@LocationUpdatesService
    }

    protected abstract fun calculateLastLocation()
    protected abstract fun removeUpdates()
    protected abstract fun requestUpdates(locationRequest: LocationRequestParameters)

    companion object {
        protected const val PACKAGE_NAME = "com.genexus.coreexternalobjects.geolocation.tracking"
        protected const val CHANNEL_ID = "channel_locationUpdate"

        /**
         * The identifier for the notification displayed for the foreground service.
         * Must be an unique id per app. 164251 ongoing sync, 164252 ongoing tracking (this)
         */
        protected const val NOTIFICATION_ID = 164252

        protected const val EXTRA_STARTED_FROM_NOTIFICATION = "$PACKAGE_NAME.started_from_notification"
        val TAG: String = LocationUpdatesService::class.java.simpleName
        const val ACTION_BROADCAST = "$PACKAGE_NAME.broadcast"
        const val EXTRA_LOCATION = "$PACKAGE_NAME.location"
    }
}
