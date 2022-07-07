package com.genexus.android.controls.maps.mapbox

import com.genexus.android.core.controls.maps.common.IMapLocationBounds
import com.mapbox.mapboxsdk.geometry.LatLngBounds

class MapLocationBounds(val latLngBounds: LatLngBounds) : IMapLocationBounds<MapLocation> {

    override fun southwest(): MapLocation {
        return MapLocation(latLngBounds.southWest)
    }

    override fun northeast(): MapLocation {
        return MapLocation(latLngBounds.northEast)
    }
}
