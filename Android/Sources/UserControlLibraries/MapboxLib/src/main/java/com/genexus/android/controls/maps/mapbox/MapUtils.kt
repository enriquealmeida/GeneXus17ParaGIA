package com.genexus.android.controls.maps.mapbox

import android.util.Pair
import com.genexus.android.core.base.utils.GeoFormats
import com.genexus.android.core.controls.maps.GxMapViewDefinition
import com.genexus.android.core.controls.maps.common.MapUtilsBase
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds

internal class MapUtils constructor(definition: GxMapViewDefinition?) : MapUtilsBase<MapLocation, MapLocationBounds>(definition) {
    override fun newMapLocationList(geography: List<Pair<Double?, Double?>>): List<MapLocation> {
        return MapLocation.listFrom(geography)
    }

    override fun newMapLocation(latitude: Double, longitude: Double): MapLocation {
        return MapLocation(latitude, longitude)
    }

    override fun newMapBounds(locations: MutableList<MapLocation?>?): MapLocationBounds? {
        // Override to use LatLngBounds implementation directly.
        if (locations == null || locations.isEmpty())
            return null

        val builder = LatLngBounds.Builder()
        if (locations.size == 1) {
            val latLng = locations[0]?.latLng
            // Builder needs at least two points to be built, so if we have only one, we add it twice
            latLng?.let { builder.include(it) }
            latLng?.let { builder.include(it) }
        } else {
            for (location in locations)
                location?.let { builder.include(it.latLng) }
        }

        return MapLocationBounds(builder.build())
    }

    override fun newMapBounds(southwest: MapLocation?, northeast: MapLocation?): MapLocationBounds? {
        if (southwest == null || northeast == null)
            return null

        val latLngBounds = LatLngBounds.Builder()
            .include(southwest.latLng)
            .include(northeast.latLng)
            .build()
        return MapLocationBounds(latLngBounds)
    }

    companion object {
        private const val DEFAULT_ZOOM_LEVEL = 5

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
    }
}
