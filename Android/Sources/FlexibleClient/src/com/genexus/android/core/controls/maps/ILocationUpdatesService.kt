package com.genexus.android.core.controls.maps

import android.location.Location
import android.location.LocationListener

interface ILocationUpdatesService {
    fun requestLocationUpdates(locationRequest: LocationRequestParameters)
    fun requestLocationUpdates(locationListener: LocationListener?)
    fun requestLocationUpdates(locationListener: LocationListener?, locationRequest: LocationRequestParameters?)
    fun removeLocationUpdates()
    fun getLastKnownLocation(): Location?
}
