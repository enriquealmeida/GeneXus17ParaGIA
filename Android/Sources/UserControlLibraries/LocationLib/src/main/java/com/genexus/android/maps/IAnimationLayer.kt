package com.genexus.android.maps

import com.genexus.android.core.controls.maps.GxMapViewDefinition
import com.genexus.android.core.controls.maps.common.IMapLocation
import com.genexus.android.core.controls.maps.common.MapItemBase

abstract class IAnimationLayer<K : IMapLocation, V : MapItemBase<K>, Marker>(private val definition: GxMapViewDefinition) {
    private val markersAnimation = hashMapOf<Marker, IMarkerAnimation<Marker>>()

    fun getAnimationMarkerKey(mapData: V): String? {
        return mapData.data.optStringProperty(definition.animationKeyExpression)
    }

    fun animateMarkers(mapData: V, endPosition: K, marker: Marker) {
        var markerAnimation = markersAnimation[marker]
        if (markerAnimation == null) {
            markerAnimation = getMarkerAnimation(marker)
            markersAnimation[marker] = markerAnimation
        }
        val duration = getAnimationDuration(mapData)
        val endBehavior = getAnimationEndBehavior(mapData)
        markerAnimation.startAnimation(endPosition, duration, endBehavior)
    }

    private fun getAnimationDuration(mapData: V): Int {
        var duration = definition.animationDuration * 1000
        if (duration == 0 && definition.animationDurationExpression != null)
            duration = mapData.data.optIntProperty(definition.animationDurationExpression) * 1000
        return duration
    }

    private fun getAnimationEndBehavior(mapData: V): Int {
        var endBehavior = definition.animationEndBehavior
        if (endBehavior == 0 && definition.animationEndBehaviorExpression != null)
            endBehavior = mapData.data.optIntProperty(definition.animationEndBehaviorExpression)
        return endBehavior
    }

    abstract fun getMarkerAnimation(marker: Marker): IMarkerAnimation<Marker>
}
