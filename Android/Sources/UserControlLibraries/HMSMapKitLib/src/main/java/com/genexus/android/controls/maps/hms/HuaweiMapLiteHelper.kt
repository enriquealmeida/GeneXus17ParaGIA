package com.genexus.android.controls.maps.hms

import android.content.Context
import android.os.Bundle
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.GeoFormats
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.HuaweiMapOptions
import com.huawei.hms.maps.MapView
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.MarkerOptions

internal class HuaweiMapLiteHelper : OnMapReadyCallback {
    private var geoLocation: String? = null
    private var zoomLevel = 0
    private var mapView: MapView? = null

    fun createMapLite(context: Context?, location: String?, @Suppress("UNUSED_PARAMETER") mapType: String?, zoom: Int): MapView? {
        val huaweiMapOptions = HuaweiMapOptions()
        huaweiMapOptions.liteMode(true)
        huaweiMapOptions.compassEnabled(true)
        huaweiMapOptions.zoomControlsEnabled(false)
        huaweiMapOptions.scrollGesturesEnabled(false)
        huaweiMapOptions.zoomGesturesEnabled(false)
        mapView = MapView(context, huaweiMapOptions)
        geoLocation = location
        zoomLevel = zoom
        mapView?.onCreate(Bundle())
        mapView?.getMapAsync(this)
        return mapView
    }

    override fun onMapReady(map: HuaweiMap) {
        Services.Log.debug("onMapReady: HuaweiMap")

        // move the view to the cureent location and add a marker
        val latLon = GeoFormats.parseGeolocation(geoLocation)
        if (latLon != null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latLon.first, latLon.second), zoomLevel.toFloat()))
            val marketPosition = LatLng(latLon.first, latLon.second)
            map.addMarker(MarkerOptions().position(marketPosition))
        }
        map.setOnMapClickListener {
            Services.Log.debug("on click Huawei Lite Map")
            // call parent onclick, allow GxSDGeoLocation onClick works.
            mapView?.callOnClick()
        }
    }
}
