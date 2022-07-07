package com.genexus.android.core.controls.maps.common

import android.app.Activity
import com.genexus.android.core.controls.maps.GxMapViewDefinition

interface IMapFeatureFactory<MapViewT, MapT> {
    fun getGeographiesManager(map: MapT, mapView: MapViewT, definition: GxMapViewDefinition): IGeographiesManager<*, *>
    fun getGeographiesLoader(geographiesManager: IGeographiesManager<*, *>): IGeographiesServerLoader<*>
    fun getMapViewCamera(map: MapT, mapView: MapViewT): IMapViewCamera<*, *, *, *, *>
    fun getMapRouteLayer(gxMapView: IGxMapViewSupportLayers?, activity: Activity): IMapRouteLayer<*>?
    fun getMapUtils(definition: GxMapViewDefinition): MapUtilsBase<*, *>
}
