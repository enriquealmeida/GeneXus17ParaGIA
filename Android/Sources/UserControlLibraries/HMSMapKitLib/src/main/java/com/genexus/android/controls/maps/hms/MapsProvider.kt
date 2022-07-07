package com.genexus.android.controls.maps.hms

import android.app.Activity
import android.content.Context
import android.view.View
import com.genexus.android.core.base.model.Entity
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.Strings
import com.genexus.android.core.controllers.ViewData
import com.genexus.android.core.controls.maps.GxMapViewDefinition
import com.genexus.android.core.controls.maps.common.IMapFeatureFactory
import com.genexus.android.core.controls.maps.common.IMapLocation
import com.genexus.android.core.controls.maps.common.IMapLocationBounds
import com.genexus.android.core.controls.maps.common.IMapViewFactory
import com.genexus.android.core.controls.maps.common.IMapsProvider
import com.genexus.android.core.controls.maps.common.MapDataBase
import com.genexus.android.core.controls.maps.common.MapItemBase
import com.genexus.controls.maps.hms.R
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapView
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class MapsProvider : IMapsProvider {
    override fun getId(): String {
        return PROVIDER_ID
    }

    override fun getMapViewFactory(): IMapViewFactory<MapView> {
        return MapViewFactory()
    }

    override fun getMapFeatureFactory(): IMapFeatureFactory<MapView, HuaweiMap> {
        return MapFeatureFactory()
    }

    override fun getLocationPickerActivityClass(): Class<out Activity> {
        return LocationPickerActivity::class.java
    }

    override fun getMapImageUrl(location: String, width: Int, height: Int, mapType: String, zoom: Int, language: String?): String? {
        // should use MapKit in Lite mode instead of url
        // https://developer.huawei.com/consumer/en/doc/development/HMS-Guides/hms-map-createmap#h1-1578453834912-0
        // return empty and return a map view lite in getMapLiteView
        return Strings.EMPTY
    }

    override fun getMapLiteView(context: Context, location: String, mapType: String, zoom: Int): View? {
        // return a mapview in lite mode.
        val helper = HuaweiMapLiteHelper()
        return helper.createMapLite(context, location, mapType, zoom)
    }

    override fun calculateDirections(activity: Activity, origin: String, destination: String, waypoints: List<String>?, transportType: String, requestAlternatives: Boolean): Entity {
        return MapRouteLayer(null, activity).calculateRoute(origin, destination, waypoints, transportType, requestAlternatives, false)
    }

    override fun newMapLocation(latitude: Double, longitude: Double): IMapLocation {
        return MapLocation(latitude, longitude)
    }

    override fun newMapData(
        definition: GxMapViewDefinition?,
        viewData: ViewData?
    ): MapDataBase<out MapItemBase<*>, *, out IMapLocationBounds<*>> {
        return GxMapViewData(definition, viewData)
    }

    companion object {
        private const val PROVIDER_ID = "MAPS_HMS_MAPKIT"
        const val PROVIDER_ID_DIRECTIONS = "Huawei"

        // static url from mapkit, only work with an enterprise pay api key
        fun getUrl(location: String?, width: Int, height: Int, @Suppress("UNUSED_PARAMETER") mapType: String?, zoom: Int): String {
            var lWidth = width
            var LHeight = height
            var urlExtra = Strings.EMPTY
            val displayDensity = Services.Application.appContext.resources.displayMetrics.density.toInt()
            if (displayDensity >= 2) {
                // According to https://developers.google.com/maps/documentation/staticmaps/#scale_values
                // request an image with a smaller size but higher scale, so that the "physically displayed"
                // map looks about the same.
                // The actual bitmap size in pixels will be the original width and height supplied to this method.
                lWidth = lWidth / 2
                LHeight = LHeight / 2
                urlExtra = "&scale=2"
            }
            val mapKey = Services.Strings.getResource(R.string.HuaweiServicesApiKey)
            val URL_FORMAT = "https://mapapi.cloud.huawei.com/mapApi/v1/mapService/getStaticMap?markers=%s&zoom=%s&width=%s&height=%s%s&key=%s"
            var mapKeyEncoded = mapKey
            try {
                mapKeyEncoded = URLEncoder.encode(mapKey, "UTF-8")
            } catch (exception: UnsupportedEncodingException) {
                Services.Log.error("Cannot encode Huawei mapkey ")
            }
            return String.format(URL_FORMAT, Services.HttpService.uriEncode(location), zoom, lWidth, LHeight, urlExtra, mapKeyEncoded)
        }
    }
}
