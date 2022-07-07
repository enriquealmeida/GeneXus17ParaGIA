package com.genexus.android.controls.maps.mapbox

import android.app.Activity
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.Strings
import com.genexus.android.core.controls.maps.GxMapViewDefinition
import com.mapbox.mapboxsdk.maps.Style

@Suppress("UNUSED_PARAMETER")
internal object MapboxHelper {

    fun checkMapbox(activity: Activity?): Boolean {
        return true
    }

    fun mapTypeToMapboxStyle(mapType: String?): String {
        if (Strings.hasValue(mapType)) {
            when (mapType) {
                GxMapViewDefinition.MAP_TYPE_HYBRID -> return Style.SATELLITE_STREETS
                GxMapViewDefinition.MAP_TYPE_SATELLITE -> return Style.SATELLITE
                GxMapViewDefinition.MAP_TYPE_TERRAIN -> return Style.OUTDOORS
            }
        }
        return Style.MAPBOX_STREETS
    }

    fun mapTypeFromMapboxStyle(mapboxStyle: String?): String {
        return when (mapboxStyle) {
            Style.SATELLITE_STREETS -> GxMapViewDefinition.MAP_TYPE_HYBRID
            Style.SATELLITE -> GxMapViewDefinition.MAP_TYPE_SATELLITE
            Style.OUTDOORS -> GxMapViewDefinition.MAP_TYPE_TERRAIN
            else -> GxMapViewDefinition.MAP_TYPE_STANDARD
        }
    }

    @JvmStatic
    fun getUrl(location: String?, widthParm: Int, heightParm: Int, mapType: String?, zoom: Int, language: String?): String {
        var width = widthParm
        var height = heightParm
        val mapKey = GxMapViewDefinition.getApiKey()
        var urlExtra = Strings.EMPTY
        val bearing = 0
        val pitch = 0
        val displayDensity = Services.Application.appContext.resources.displayMetrics.density.toInt()
        if (displayDensity >= 2) {
            width /= 2
            height /= 2
            urlExtra = "&scale=2"
        }
        val url = "https://api.mapbox.com/styles/v1/mapbox/streets-v11/static/%s,%s,%s,%s/%sx%s?access_token=%s%s"
        return String.format(url, Services.HttpService.uriEncode(location), zoom, bearing, pitch, width, height, mapKey, urlExtra)
    }
}
