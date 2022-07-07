package com.genexus.android.controls.maps.hms

import android.content.Context
import android.graphics.Color
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition
import com.genexus.android.core.base.utils.Strings
import com.genexus.android.core.controls.maps.GxMapViewDefinition
import com.genexus.android.core.utils.ThemeUtils
import com.genexus.android.hms.HMSHelper
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.model.PolygonOptions
import com.huawei.hms.maps.model.PolylineOptions

internal object HuaweiMapsHelper {
    fun checkHuaweiMaps(context: Context?): Boolean {
        // Check that HMS is available.
        return HMSHelper.isHmsAvailable(context)
    }

    fun mapTypeToHuaweiMapType(mapType: String?): Int {
        if (Strings.hasValue(mapType)) {
            // Huawei maps not supported map types yet!.
            // from https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides-V5/android-sdk-map-type-0000001062273642-V5
            // supports two types of maps:
            // MAP_TYPE_NORMAL: standard map, which shows roads, artificial structures, and natural features such as rivers.
            // and 	MAP_TYPE_NONE: empty map without any data.
        }
        // always use normal type.
        return HuaweiMap.MAP_TYPE_NORMAL
    }

    fun mapTypeFromHuaweiMapType(huaweiMapType: Int): String {
        return when (huaweiMapType) {
            HuaweiMap.MAP_TYPE_HYBRID -> GxMapViewDefinition.MAP_TYPE_HYBRID
            HuaweiMap.MAP_TYPE_SATELLITE -> GxMapViewDefinition.MAP_TYPE_SATELLITE
            HuaweiMap.MAP_TYPE_TERRAIN -> GxMapViewDefinition.MAP_TYPE_TERRAIN
            else -> GxMapViewDefinition.MAP_TYPE_STANDARD
        }
    }

    fun applyClassToPolylineOptions(themeClassDefinition: ThemeClassDefinition?, polylineOptions: PolylineOptions) {
        themeClassDefinition?.let {
            val lineWidth = it.lineWidth
            if (lineWidth > 0) polylineOptions.width(lineWidth.toFloat())
            polylineOptions.color(ThemeUtils.getColorId(themeClassDefinition.strokeColor, Color.BLACK))
        }
    }

    fun applyClassToPolygonOptions(themeClassDefinition: ThemeClassDefinition?, polygonOptions: PolygonOptions) {
        themeClassDefinition?.let {
            val lineWidth = it.lineWidth
            if (lineWidth > 0) polygonOptions.strokeWidth(lineWidth.toFloat())
            polygonOptions.strokeColor(ThemeUtils.getColorId(themeClassDefinition.strokeColor, Color.BLACK))
            polygonOptions.fillColor(ThemeUtils.getColorId(themeClassDefinition.fillColor, Color.TRANSPARENT))
        }
    }
}
