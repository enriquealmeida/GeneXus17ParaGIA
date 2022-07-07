package com.genexus.android.controls.maps.baidu

import com.baidu.mapapi.map.BaiduMapOptions
import com.genexus.android.core.controls.maps.common.IMapOptions

class MapOptions : IMapOptions<BaiduMapOptions> {
    override fun getInstance(): BaiduMapOptions {
        return BaiduMapOptions()
    }
}
