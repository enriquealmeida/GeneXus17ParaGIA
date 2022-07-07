package com.genexus.android.controls.maps.google

import com.genexus.android.core.controls.maps.common.IMapOptions
import com.google.android.gms.maps.GoogleMapOptions

class MapOptions : IMapOptions<GoogleMapOptions> {
    override fun getInstance(): GoogleMapOptions {
        return GoogleMapOptions()
    }
}
