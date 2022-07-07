package com.genexus.android.controls.maps.mapbox

import com.genexus.android.core.base.model.Entity
import com.genexus.android.core.controllers.ViewData
import com.genexus.android.core.controls.maps.GxMapViewDefinition
import com.genexus.android.core.controls.maps.common.MapDataBase
import com.genexus.android.core.controls.maps.common.MapLayer.FeatureType

internal class GxMapViewData(mapDefinition: GxMapViewDefinition?, itemsData: ViewData?) :
    MapDataBase<GxMapViewItem, MapLocation, MapLocationBounds>(itemsData, MapUtils(mapDefinition)) {

    override fun newMapItem(
        locationList: MutableList<MapLocation?>,
        itemData: Entity,
        featureType: FeatureType,
        layerId: String?,
        themeClassName: String?
    ): GxMapViewItem {
        return GxMapViewItem(locationList, itemData, featureType, layerId, themeClassName)
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
