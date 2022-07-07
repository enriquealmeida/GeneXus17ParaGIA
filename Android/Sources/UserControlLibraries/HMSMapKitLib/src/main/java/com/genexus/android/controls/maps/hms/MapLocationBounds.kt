package com.genexus.android.controls.maps.hms

import com.genexus.android.core.controls.maps.common.IMapLocationBounds
import com.huawei.hms.maps.model.LatLngBounds

internal class MapLocationBounds(val latLngBounds: LatLngBounds) : IMapLocationBounds<MapLocation> {
    override fun southwest(): MapLocation {
        return MapLocation(latLngBounds.southwest)
    }

    override fun northeast(): MapLocation {
        return MapLocation(latLngBounds.northeast)
    }
}
