package com.genexus.android.location.baidu

import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.controls.maps.LocationRequestParameters
import com.genexus.android.core.controls.maps.LocationUpdatesService
import com.genexus.android.location.baidu.Utils.toLocationFormat

class BaiduLocationUpdatesService : LocationUpdatesService() {

    private var locationService = LocationService(Services.Application.appContext)

    override fun requestUpdates(locationRequest: LocationRequestParameters) {
        locationService.registerListener(listener)
        locationService.start()
    }

    override fun removeUpdates() {
        locationService.stop()
        locationService.unregisterListener(listener)
    }

    override fun calculateLastLocation() {
        listener.onReceiveLocation(locationService.getLastKnownLocation())
    }

    private val listener = object : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation?) {
            onNewLocation(toLocationFormat(location))
        }
    }
}
