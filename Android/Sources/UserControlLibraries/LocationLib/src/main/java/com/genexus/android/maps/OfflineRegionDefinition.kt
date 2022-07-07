package com.genexus.android.maps

class OfflineRegionDefinition constructor(
    val regionName: String,
    val center: String?,
    val northEast: String?,
    val southWest: String?,
    val radius: Int,
    val minZoom: Int,
    val maxZoom: Int,
    var status: Int,
    var regionObject: Any? // OfflineRegion object holder
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OfflineRegionDefinition

        if (regionName != other.regionName) return false

        if (center.isNullOrEmpty()) {
            if (northEast != other.northEast && southWest != other.southWest) return false
        } else {
            if (center != other.center) return false
        }

        if (radius != other.radius) return false
        if (minZoom != other.minZoom) return false
        if (maxZoom != other.maxZoom) return false

        return true
    }

    override fun hashCode(): Int {
        var result = regionName.hashCode()

        if (center.isNullOrEmpty()) {
            if (northEast != null && southWest != null) {
                result = 31 * result + northEast.hashCode()
                result = 31 * result + southWest.hashCode()
            }
        } else {
            result = 31 * result + center.hashCode()
        }

        result = 31 * result + radius
        result = 31 * result + minZoom
        result = 31 * result + maxZoom
        return result
    }
}
