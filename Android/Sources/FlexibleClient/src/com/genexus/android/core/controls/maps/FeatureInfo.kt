package com.genexus.android.core.controls.maps

import com.genexus.android.core.base.utils.Strings
import com.genexus.android.core.controls.maps.common.IMapLocation
import com.genexus.android.core.controls.maps.common.MapItemBase
import com.genexus.android.core.controls.maps.common.MapLayer
import com.genexus.android.core.controls.maps.common.MapPinHelper

class FeatureInfo(val Data: MapItemBase<*>?) {
    var id: String? = Strings.EMPTY
    var lineWidth: Float? = null
    var strokeColor: Int? = null
    var fillColor: Int? = null
    var dashPattern: IntArray? = null
    var type: MapLayer.FeatureType? = null
    var geodesic = false
    var points = mutableListOf<IMapLocation>()
    var holes: List<List<IMapLocation>>? = null
    var draggable = false
    var icon: MapPinHelper.ResourceOrBitmap? = null

    init {
        @Suppress("UNCHECKED_CAST")
        Data?.let { points = it.locationList as MutableList<IMapLocation> }
    }
}
