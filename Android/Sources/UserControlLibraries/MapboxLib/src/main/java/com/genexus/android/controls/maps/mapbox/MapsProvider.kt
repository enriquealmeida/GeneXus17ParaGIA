package com.genexus.android.controls.maps.mapbox

import android.app.Activity
import android.content.Context
import android.view.View
import com.genexus.android.controls.maps.mapbox.MapboxHelper.getUrl
import com.genexus.android.core.base.model.Entity
import com.genexus.android.core.controllers.ViewData
import com.genexus.android.core.controls.maps.GxMapViewDefinition
import com.genexus.android.core.controls.maps.common.IMapFeatureFactory
import com.genexus.android.core.controls.maps.common.IMapLocation
import com.genexus.android.core.controls.maps.common.IMapLocationBounds
import com.genexus.android.core.controls.maps.common.IMapViewFactory
import com.genexus.android.core.controls.maps.common.IMapsProvider
import com.genexus.android.core.controls.maps.common.IOfflineRegionManager
import com.genexus.android.core.controls.maps.common.MapDataBase
import com.genexus.android.core.controls.maps.common.MapItemBase
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap

class MapsProvider : IMapsProvider, IMapsProvider.Offline {

    override fun getId(): String {
        return PROVIDER_ID
    }

    override fun getMapViewFactory(): IMapViewFactory<MapView> {
        return MapViewFactory()
    }

    override fun getMapFeatureFactory(): IMapFeatureFactory<MapView, MapboxMap> {
        return MapFeatureFactory()
    }

    override fun getLocationPickerActivityClass(): Class<out Activity> {
        return LocationPickerActivity::class.java
    }

    override fun getMapImageUrl(location: String, width: Int, height: Int, mapType: String, zoom: Int, language: String?): String? {
        return getUrl(location, width, height, mapType, zoom, language)
    }

    override fun getMapLiteView(context: Context, location: String, mapType: String, zoom: Int): View? {
        return null
    }

    override fun calculateDirections(
        activity: Activity,
        origin: String,
        destination: String,
        waypoints: MutableList<String>?,
        transportType: String,
        requestAlternatives: Boolean
    ): Entity {
        return MapRouteLayer(null, activity).calculateRoute(
            origin, destination,
            waypoints, transportType, requestAlternatives, shouldDrawOnMap = false
        )
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
        private const val PROVIDER_ID = "MAPS_MAPBOX"
    }

    override fun getInstance(context: Context): IOfflineRegionManager {
        return OfflineRegionManager.getInstance(context)
    }

    override fun isOfflineGeographicDataSupported(): Boolean {
        return true
    }
}
