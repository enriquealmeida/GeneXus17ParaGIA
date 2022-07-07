package com.genexus.android.location.geolocation.tracking

import android.content.Context
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.controls.maps.LocationRequestParameters
import com.genexus.android.core.controls.maps.LocationUpdatesService
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task

class DefaultLocationUpdatesService : LocationUpdatesService() {

    private val context = Services.Application.appContext
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?

    override fun requestUpdates(locationRequest: LocationRequestParameters) {
        if (fusedLocationClient != null)
            requestUpdatesFusedManager(locationRequest)
        else
            requestUpdatesLocationManager(locationRequest)
    }

    override fun removeUpdates() {
        if (fusedLocationClient != null)
            removeUpdatesFusedManager()
        else
            removeUpdatesLocationManager()
    }

    override fun calculateLastLocation() {
        if (fusedLocationClient == null)
            getLastLocationFromLocationManager()?.let { locationManagerListener.onLocationChanged(it) }
        else {
            fusedLocationClient.lastLocation.addOnCompleteListener { task: Task<Location?> ->
                if (!task.isSuccessful)
                    Services.Log.warning(TAG_FUSED, "Failed to get location.")
                else
                    task.result?.let { locationManagerListener.onLocationChanged(it) }
            }
        }
    }

    private fun requestUpdatesFusedManager(requestParameters: LocationRequestParameters) {
        Services.Log.debug(TAG_FUSED, "requestLocationUpdates using FusedLocationHelper.")
        fusedLocationClient.requestLocationUpdates(toLocationRequest(requestParameters), locationCallback, Looper.myLooper())
    }

    private fun removeUpdatesFusedManager() {
        Services.Log.debug(TAG_FUSED, "requestLocationUpdates using FusedLocationHelper.")
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun requestUpdatesLocationManager(requestParameters: LocationRequestParameters) {
        val provider = getBestProviderFromCriteria(requestParameters.includeHeadingSpeed)
        if (provider != null) {
            Services.Log.debug(TAG_SYSTEM, "requestLocationUpdates using provider: $provider")
            locationManager?.requestLocationUpdates(provider, requestParameters.interval, requestParameters.smallestDisplacement, locationManagerListener)
        }
    }

    private fun removeUpdatesLocationManager() {
        Services.Log.debug(TAG_SYSTEM, "removeLocationUpdates using locationManager.")
        locationManager?.removeUpdates(locationManagerListener)
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationManagerListener.onLocationChanged(locationResult.lastLocation)
        }
    }

    private val locationManagerListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Services.Log.debug(TAG, "onLocationChanged Location: $location")
            onNewLocation(location)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            Services.Log.debug(TAG_SYSTEM, "onStatusChanged Provider: $provider Status: $status")
        }

        override fun onProviderEnabled(provider: String) {
            Services.Log.debug(TAG_SYSTEM, "onProviderEnabled Provider: $provider")
        }

        override fun onProviderDisabled(provider: String) {
            Services.Log.debug(TAG_SYSTEM, "onProviderDisabled Provider: $provider")
        }
    }

    private fun getLastLocationFromLocationManager(): Location? {
        // Should get the last in time location, comparing location.getTime() ?
        val gpsLocation = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val networkLocation = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        // Get last location from providers
        val location = gpsLocation ?: networkLocation
        if (location != null) {
            Services.Log.info(TAG_SYSTEM, "getLastKnownLocation from GPS_PROVIDER or NETWORK_PROVIDER")
            return location
        }

        val passiveLocation = locationManager?.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
        if (passiveLocation != null) {
            Services.Log.info(TAG_SYSTEM, "getLastKnownLocation from PASSIVE_PROVIDER")
            return passiveLocation
        }

        val criteria = Criteria().apply { accuracy = Criteria.ACCURACY_FINE }
        val provider = locationManager?.getBestProvider(criteria, true) ?: return null
        return locationManager.getLastKnownLocation(provider)
    }

    private fun getBestProviderFromCriteria(includeHeadingAndSpeed: Boolean): String? {
        val criteria = Criteria().apply {
            accuracy = Criteria.ACCURACY_FINE
            isAltitudeRequired = false
            isBearingRequired = includeHeadingAndSpeed
            isCostAllowed = true
        }
        return locationManager?.getBestProvider(criteria, true)
    }

    private fun toLocationRequest(locationRequestParameters: LocationRequestParameters?): LocationRequest {
        return LocationRequest().apply {
            expirationTime = locationRequestParameters!!.expirationTime
            fastestInterval = locationRequestParameters.fastestInterval
            interval = locationRequestParameters.interval
            maxWaitTime = locationRequestParameters.maxWaitTime
            numUpdates = locationRequestParameters.numUpdates
            priority = locationRequestParameters.priority
            smallestDisplacement = locationRequestParameters.smallestDisplacement
        }
    }

    companion object {
        private const val TAG = "DefaultLocationUpdatesService"
        private const val TAG_SYSTEM = "SystemLocationManager"
        private const val TAG_FUSED = "FusedLocationManager"
    }
}
