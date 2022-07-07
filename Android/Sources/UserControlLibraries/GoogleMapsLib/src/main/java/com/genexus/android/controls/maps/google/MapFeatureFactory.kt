package com.genexus.android.controls.maps.google

import android.app.Activity
import com.genexus.android.core.controls.maps.GxMapViewDefinition
import com.genexus.android.core.controls.maps.common.IGeographiesManager
import com.genexus.android.core.controls.maps.common.IGeographiesServerLoader
import com.genexus.android.core.controls.maps.common.IGxMapViewSupportLayers
import com.genexus.android.core.controls.maps.common.IMapFeatureFactory
import com.genexus.android.core.controls.maps.common.IMapRouteLayer
import com.genexus.android.core.controls.maps.common.MapUtilsBase
import com.genexus.android.maps.MapViewCamera
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView

class MapFeatureFactory : IMapFeatureFactory<MapView, GoogleMap> {
    override fun getGeographiesManager(
        map: GoogleMap,
        mapView: MapView,
        definition: GxMapViewDefinition
    ): IGeographiesManager<*, *> {
        return GeographiesManager(map, mapView, definition)
    }

    override fun getGeographiesLoader(geographiesManager: IGeographiesManager<*, *>): IGeographiesServerLoader<*> {
        return GeographiesLoader(geographiesManager as GeographiesManager)
    }

    override fun getMapViewCamera(map: GoogleMap, mapView: MapView): MapViewCamera<*, *, *, *, *> {
        return GxMapViewCamera(map, mapView)
    }

    override fun getMapRouteLayer(
        gxMapView: IGxMapViewSupportLayers?,
        activity: Activity
    ): IMapRouteLayer<*> {
        return MapRouteLayer(gxMapView, activity)
    }

    override fun getMapUtils(definition: GxMapViewDefinition): MapUtilsBase<*, *> {
        return MapUtils(definition)
    }
}
