package com.genexus.android.controls.maps.hms

import android.util.Pair
import com.genexus.android.core.controls.maps.common.IMapLocation
import com.huawei.hms.maps.model.LatLng

internal class MapLocation(val latLng: LatLng) : IMapLocation {

    constructor(latitude: Double, longitude: Double) : this(LatLng(latitude, longitude)) {}

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
            return if (latLng != null) MapLocation(latLng) else null
        }

        fun listFromLatLng(latLngs: List<LatLng>): List<IMapLocation> {
            val list = ArrayList<IMapLocation>()
            for (latLng in latLngs) list.add(MapLocation(latLng))
            return list
        }

        fun listFrom(latLngs: List<Pair<Double, Double>>): List<MapLocation> {
            val list = ArrayList<MapLocation>()
            for (latLng in latLngs) list.add(MapLocation(LatLng(latLng.first, latLng.second)))
            return list
        }

        fun listToLatLng(locations: List<IMapLocation>): List<LatLng> {
            val list = ArrayList<LatLng>()
            for (location in locations) list.add((location as MapLocation).latLng)
            return list
        }

        fun toLatLngList(mapLocationList: List<MapLocation?>): List<LatLng> {
            val latLngList: MutableList<LatLng> = ArrayList()
            for (mapLocation in mapLocationList) latLngList.add(mapLocation!!.latLng)
            return latLngList
        }

        fun toLatLngLists(list: List<List<IMapLocation>>): MutableList<List<LatLng>> {
            val latLngLists = mutableListOf<List<LatLng>>()
            for (aList in list)
                latLngLists.add(listToLatLng(aList))

            return latLngLists
        }
    }
}
