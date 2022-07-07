package com.genexus.android.location.geolocation.geofence

import android.location.Location

data class Geofence(val id: Int, val location: Location, val radius: Int, val expiration: Long)
