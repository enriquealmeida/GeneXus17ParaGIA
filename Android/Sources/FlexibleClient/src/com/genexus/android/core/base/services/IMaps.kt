package com.genexus.android.core.base.services

import android.app.Activity
import android.view.View
import com.genexus.android.core.base.model.Entity
import com.genexus.android.core.controllers.ViewData
import com.genexus.android.core.controls.maps.GxMapViewDefinition
import com.genexus.android.core.controls.maps.common.IMapFeatureFactory
import com.genexus.android.core.controls.maps.common.IMapLocation
import com.genexus.android.core.controls.maps.common.IMapViewFactory
import com.genexus.android.core.controls.maps.common.IMapsProvider
import com.genexus.android.core.controls.maps.common.IOfflineRegionManager
import com.genexus.android.core.controls.maps.common.MapDataBase

interface IMaps {
    fun addProvider(provider: IMapsProvider)
    fun getMapViewFactory(): IMapViewFactory<*>?
    fun getMapFeatureFactory(): IMapFeatureFactory<*, *>?
    fun getLocationPickerActivityClass(): Class<out Activity>?
    fun getMapImageUrl(location: String?, width: Int, height: Int, mapType: String?, zoom: Int, language: String?): String?
    fun getMapLiteView(location: String?, mapType: String?, zoom: Int): View?
    fun calculateDirections(
        activity: Activity?,
        origin: String?,
        destination: String?,
        waypoints: List<String>?,
        transportType: String?,
        requestAlternatives: Boolean?
    ): Entity?
    fun getProviderId(): String?
    fun getProvider(): IMapsProvider?
    fun newMapLocation(lat: Double, lng: Double): IMapLocation?
    fun newMapData(definition: GxMapViewDefinition?, viewData: ViewData?): MapDataBase<*, *, *>?
    fun setProviderIdOverride(providerId: String)

    interface Offline {
        fun getInstance(): IOfflineRegionManager?
        fun isOfflineGeographicDataSupported(): Boolean
    }
}
