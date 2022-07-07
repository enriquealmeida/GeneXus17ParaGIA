package com.genexus.android.core.controls.maps.common

import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition
import com.genexus.android.core.base.model.Entity

interface IMapRouteLayer<MapViewData> {
    fun calculateRoute(
        origin: String,
        destination: String,
        waypoints: List<String>?,
        travelMode: String,
        requestAlternativeRoutes: Boolean,
        shouldDrawOnMap: Boolean
    ): Entity

    fun addRouteLayer(mapData: MapViewData, travelMode: String, mapRouteClass: ThemeClassDefinition, zoomToLayer: Boolean)
    fun removeRouteLayer()
}
