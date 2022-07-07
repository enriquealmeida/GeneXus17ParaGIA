package com.genexus.android.controls.maps.gaode

import com.amap.api.maps.AMapOptions
import com.genexus.android.core.controls.maps.common.IMapOptions

class MapOptions : IMapOptions<AMapOptions> {
    override fun getInstance(): AMapOptions {
        return AMapOptions()
    }
}
