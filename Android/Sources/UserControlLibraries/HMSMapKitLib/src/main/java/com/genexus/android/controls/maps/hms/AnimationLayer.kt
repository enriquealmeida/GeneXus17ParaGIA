package com.genexus.android.controls.maps.hms

import com.genexus.android.core.controls.maps.GxMapViewDefinition
import com.genexus.android.maps.IAnimationLayer
import com.genexus.android.maps.IMarkerAnimation
import com.huawei.hms.maps.model.Marker

internal class AnimationLayer(definition: GxMapViewDefinition) : IAnimationLayer<MapLocation, GxMapViewItem, Marker>(definition) {
    override fun getMarkerAnimation(marker: Marker): IMarkerAnimation<Marker> {
        return MarkerAnimation(marker)
    }
}
