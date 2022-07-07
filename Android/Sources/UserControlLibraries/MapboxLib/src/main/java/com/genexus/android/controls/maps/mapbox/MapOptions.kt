package com.genexus.android.controls.maps.mapbox

import android.content.Context
import com.genexus.android.core.controls.maps.common.IMapOptions
import com.mapbox.mapboxsdk.maps.MapboxMapOptions

class MapOptions(private val context: Context) : IMapOptions<MapboxMapOptions> {
    override fun getInstance(): MapboxMapOptions {
        return MapboxMapOptions.createFromAttributes(context)
    }
}
