package com.genexus.android.core.base.services

import android.content.Context
import android.location.Location
import com.genexus.android.core.controls.maps.LocationRequestParameters

interface ILocationProvider {
    fun getLastKnownLocation(): Location?
    fun requestLocationUpdates(requestParameters: LocationRequestParameters): Boolean
    fun removeLocationUpdates(): Boolean

    fun bind()
    fun isReady(): Boolean

    fun checkGeofences(context: Context): Boolean
    fun stopCheckingGeofences(context: Context): Boolean
    fun createGeofence(uniqueId: Int, location: Location, radius: Int, expiration: Long): Boolean
}
