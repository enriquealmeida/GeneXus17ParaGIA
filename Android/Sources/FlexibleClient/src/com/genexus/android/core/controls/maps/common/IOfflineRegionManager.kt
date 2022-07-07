package com.genexus.android.core.controls.maps.common

import com.genexus.android.core.base.model.Entity
import com.genexus.android.core.base.model.ValueCollection

interface IOfflineRegionManager {
    fun downloadRegion(
        regionName: String?,
        centerGeopoint: String?,
        radius: Int,
        minZoom: Int,
        maxZoom: Int,
        style: String?,
        density: Float,
        listener: OnOfflineRegionEventCallback?
    ): Boolean

    fun downloadRegion(
        regionName: String?,
        northEastGeopoint: String?,
        southWestGeopoint: String?,
        minZoom: Int,
        maxZoom: Int,
        style: String?,
        density: Float,
        listener: OnOfflineRegionEventCallback?
    ): Boolean

    fun clearRegion(regionName: String?, updateRegions: Boolean)
    fun clearAllRegions()
    fun getDownloadedRegions(callback: OnOfflineRegionEventCallback?): ValueCollection?
    fun getSize(): Double
    fun getRegionStatus(regionName: String?): Entity
    fun pauseDownload(regionName: String?)
    fun cancelDownload(regionName: String?)
}
