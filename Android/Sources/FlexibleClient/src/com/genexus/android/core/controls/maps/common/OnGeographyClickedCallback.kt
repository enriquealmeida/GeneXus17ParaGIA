package com.genexus.android.core.controls.maps.common

import com.genexus.android.core.controls.maps.common.MapLayer.MapFeature

interface OnGeographyClickedCallback {
    fun geographyClicked(mapFeature: MapFeature?): Boolean
}
