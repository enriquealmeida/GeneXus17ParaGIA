package com.genexus.android.controls.maps.mapbox

import android.util.Pair
import com.genexus.android.core.controls.maps.common.IMapLocation
import com.mapbox.mapboxsdk.geometry.LatLng

class MapLocation(val latLng: LatLng) : IMapLocation {

    constructor(latitude: Double, longitude: Double) : this(LatLng(latitude, longitude))

    override fun equals(other: Any?): Boolean {
        if (super.equals(other)) return true
        if (other !is MapLocation) return false
        return latLng == other.latLng
    }

    override fun hashCode(): Int {
        return latLng.hashCode()
    }

    override fun toString(): String {
        return latLng.toString()
    }

    override fun getLatitude(): Double {
        return latLng.latitude
    }

    override fun getLongitude(): Double {
        return latLng.longitude
    }

    companion object {
        fun from(latLng: LatLng?): MapLocation? {
            return latLng?.let { MapLocation(it) }
        }

        fun listFrom(geography: List<Pair<Double?, Double?>>): List<MapLocation> {
            val list = ArrayList<MapLocation>()
            for (latLng in geography) list.add(MapLocation(LatLng(latLng.first!!, latLng.second!!)))
            return list
        }

        fun listToLatLng(locations: List<IMapLocation>): List<LatLng> {
            val list = ArrayList<LatLng>()
            for (location in locations) list.add((location as MapLocation).latLng)
            return list
        }

        fun toLatLngList(locations: List<MapLocation>): List<LatLng> {
            return listToLatLng(locations)
        }

        fun toLatLngLists(list: List<List<IMapLocation>>): MutableList<List<LatLng>> {
            val latLngLists: MutableList<List<LatLng>> = ArrayList()
            for (aList in list)
                latLngLists.add(listToLatLng(aList))
            return latLngLists
        }
    }
}
