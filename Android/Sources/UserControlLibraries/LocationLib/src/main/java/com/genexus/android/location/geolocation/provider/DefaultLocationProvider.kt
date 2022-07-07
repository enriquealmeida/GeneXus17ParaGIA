package com.genexus.android.location.geolocation.provider

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.Location
import com.genexus.android.GooglePlayServicesHelper
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.controls.maps.ILocationUpdatesService
import com.genexus.android.core.controls.maps.LocationProvider
import com.genexus.android.location.geolocation.geofence.GeofenceBroadcastReceiver
import com.genexus.android.location.geolocation.tracking.DefaultLocationUpdatesService
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task

class DefaultLocationProvider(context: Context) : LocationProvider(context), OnCompleteListener<Void> {

    private var geofenceList = mutableListOf<Geofence>()
    private val geofencingClient = LocationServices.getGeofencingClient(context)

    override fun bind() {
        if (!GooglePlayServicesHelper.isPlayServicesAvailable(context))
            Services.Log.warning(TAG, "PlayServices isn't available for Default location provider")
        else
            super.bind()
    }

    override fun checkGeofences(context: Context): Boolean {
        return try {
            // A pending intent that that is reused when calling removeGeofences(). This
            // pending intent is used to generate an intent when a matched geofence
            // transition is observed.
            geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent(context)).addOnCompleteListener(this)
            geofenceList.clear()
            true
        } catch (securityException: SecurityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            Services.Log.error(securityException)
            false
        }
    }

    override fun stopCheckingGeofences(context: Context): Boolean {
        return try {
            // This is the same pending intent that was used in addGeofences()
            geofencingClient.removeGeofences(getGeofencePendingIntent(context)).addOnCompleteListener(this)
            true
        } catch (securityException: SecurityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            Services.Log.error(securityException)
            false
        }
    }

    override fun createGeofence(uniqueId: Int, location: Location, radius: Int, expiration: Long): Boolean {
        geofenceList.add(
            Geofence.Builder() // Set the request ID of the geofence. This is a string to identify this geofence
                .setRequestId(uniqueId.toString())
                .setCircularRegion(location.latitude, location.longitude, radius.toFloat())
                .setExpirationDuration(expiration)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
                .build()
        )

        return true
    }

    private fun getGeofencingRequest(): GeofencingRequest? {
        val builder = GeofencingRequest.Builder()
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
        builder.addGeofences(geofenceList)
        return builder.build()
    }

    private fun getGeofencePendingIntent(context: Context): PendingIntent? {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }

    override fun onComplete(task: Task<Void>) {
        if (task.isSuccessful)
            Services.Log.debug(TAG, "geofences_change success")
        else
            Services.Log.debug(TAG, "geofences_change error: " + task.result)
    }

    override fun getServiceClass(): Class<out ILocationUpdatesService> {
        return DefaultLocationUpdatesService::class.java
    }

    companion object {
        private const val TAG = "DefaultLocationProvider"
    }
}
