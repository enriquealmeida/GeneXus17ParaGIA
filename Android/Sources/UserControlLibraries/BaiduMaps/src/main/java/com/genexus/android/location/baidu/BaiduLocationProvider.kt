package com.genexus.android.location.baidu

import android.content.Context
import android.location.Location
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.controls.maps.ILocationUpdatesService
import com.genexus.android.core.controls.maps.LocationProvider

class BaiduLocationProvider(context: Context) : LocationProvider(context) {

    override fun checkGeofences(context: Context): Boolean {
        return notSupported()
    }

    override fun stopCheckingGeofences(context: Context): Boolean {
        return notSupported()
    }

    override fun createGeofence(uniqueId: Int, location: Location, radius: Int, expiration: Long): Boolean {
        return notSupported()
    }

    private fun notSupported(): Boolean {
        Services.Log.warning(TAG, "Geofences not supported by Baidu")
        return false
    }

    override fun getServiceClass(): Class<out ILocationUpdatesService> {
        return BaiduLocationUpdatesService::class.java
    }

    companion object {
        private const val TAG = "BaiduLocationProvider"
    }
}
