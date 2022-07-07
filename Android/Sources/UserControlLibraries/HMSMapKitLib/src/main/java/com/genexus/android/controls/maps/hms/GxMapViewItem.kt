package com.genexus.android.controls.maps.hms

import com.genexus.android.core.base.model.Entity
import com.genexus.android.core.controls.maps.common.MapItemBase
import com.genexus.android.core.controls.maps.common.MapLayer.FeatureType

internal class GxMapViewItem(
    locationList: List<MapLocation?>?,
    itemData: Entity?,
    featureType: FeatureType?,
    layerId: String?,
    themeClassName: String?
) : MapItemBase<MapLocation>(locationList, itemData, featureType, layerId, themeClassName)
