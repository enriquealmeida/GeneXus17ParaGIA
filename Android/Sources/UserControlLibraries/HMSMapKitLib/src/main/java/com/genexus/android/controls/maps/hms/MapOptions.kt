package com.genexus.android.controls.maps.hms

import com.genexus.android.core.controls.maps.common.IMapOptions
import com.huawei.hms.maps.HuaweiMapOptions

class MapOptions : IMapOptions<HuaweiMapOptions> {
    override fun getInstance(): HuaweiMapOptions {
        return HuaweiMapOptions()
    }
}
