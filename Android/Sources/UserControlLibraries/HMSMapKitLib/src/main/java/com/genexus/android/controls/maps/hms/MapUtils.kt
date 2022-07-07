package com.genexus.android.controls.maps.hms

import android.util.Pair
import com.genexus.android.core.base.utils.GeoFormats
import com.genexus.android.core.controls.maps.GxMapViewDefinition
import com.genexus.android.core.controls.maps.common.IMapLocation
import com.genexus.android.core.controls.maps.common.MapUtilsBase
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.LatLngBounds

internal class MapUtils(definition: GxMapViewDefinition?) : MapUtilsBase<MapLocation, MapLocationBounds>(definition) {
    override fun newMapLocationList(geography: List<Pair<Double, Double>>): List<MapLocation> {
        return MapLocation.listFrom(geography)
    }

    override fun newMapLocation(latitude: Double, longitude: Double): MapLocation {
        return MapLocation(latitude, longitude)
    }

    override fun newMapBounds(locations: MutableList<MapLocation?>): MapLocationBounds {
        // Override to use LatLngBounds implementation directly.
        val builder = LatLngBounds.builder()
        for (location in locations) builder.include(location?.latLng)
        return MapLocationBounds(builder.build())
    }

    override fun newMapBounds(southwest: MapLocation?, northeast: MapLocation?): MapLocationBounds {
        val latLngBounds = LatLngBounds(southwest?.latLng, northeast?.latLng)
        return MapLocationBounds(latLngBounds)
    }

    companion object {
        /**
         * Decodes a [LatLng] instance from its string representation.
         */
        fun stringToLatLng(str: String?): LatLng? {
            val coordinates = GeoFormats.parseGeolocation(str)
            return if (coordinates != null) LatLng(coordinates.first, coordinates.second) else null
        }

        fun calculateCenter(points: List<LatLng>): LatLng {
            var centroidX = 0.0
            var centroidY = 0.0
            for (latLng in points) {
                centroidX += latLng.latitude
                centroidY += latLng.longitude
            }
            return LatLng(centroidX / points.size, centroidY / points.size)
        }

        fun latLngListToPairList(latLngs: List<IMapLocation>): List<Pair<Double, Double>> {
            val points: MutableList<Pair<Double, Double>> = ArrayList()
            for (latLng in latLngs) points.add(Pair(latLng.latitude, latLng.longitude))
            return points
        }
    }
}
