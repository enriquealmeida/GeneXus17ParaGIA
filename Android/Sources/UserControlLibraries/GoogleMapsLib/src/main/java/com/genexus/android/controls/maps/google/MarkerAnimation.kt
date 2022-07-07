package com.genexus.android.controls.maps.google

import com.genexus.android.core.controls.maps.common.IMapLocation
import com.genexus.android.maps.IMarkerAnimation
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

class MarkerAnimation(marker: Marker) : IMarkerAnimation<Marker>(marker) {
    override fun getMarkerPosition(): IMapLocation {
        return MapLocation(marker.position.latitude, marker.position.longitude)
    }

    override fun setMarkerPosition(lat: Double, lng: Double) {
        marker.position = LatLng(lat, lng)
    }

    override fun setMarkerVisible(visible: Boolean) {
        marker.isVisible = visible
    }

    override fun setMarkerRotation(rotation: Float) {
        marker.rotation = rotation
    }

    override fun setMarkerAnchor(i: Float, k: Float) {
        marker.setAnchor(i, k)
    }
}
