package com.genexus.android.controls.maps.hms

import android.graphics.Point
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.controls.maps.common.IMapLocation
import com.genexus.android.maps.GxMapViewBase
import com.genexus.android.maps.MapItemViewHelper
import com.genexus.android.maps.MapViewCamera
import com.huawei.hms.maps.CameraUpdate
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapView
import com.huawei.hms.maps.model.LatLng

internal class GxMapViewCamera(private val map: HuaweiMap?, private val mapView: MapView) :
    MapViewCamera<CameraUpdate, MapLocation, MapLocationBounds, GxMapViewItem, GxMapViewData>() {

    override fun updateCamera(bounds: MapLocationBounds, padding: Int, duration: Int) {
        val update = CameraUpdateFactory.newLatLngBounds(bounds.latLngBounds, padding)
        updateCamera(update, duration, null)
    }

    override fun updateCamera(
        location: IMapLocation,
        zoom: Int,
        duration: Int,
        callback: GxMapViewBase.OnMarkerCameraUpdateCallback?
    ) {
        updateCamera(getCameraUpdate(location, zoom), duration, callback)
    }

    override fun updateCamera(location: IMapLocation, zoom: Int) {
        updateCamera(getCameraUpdate(location, zoom), 0, null)
    }

    override fun updateCamera(
        cameraUpdate: CameraUpdate,
        duration: Int,
        callback: GxMapViewBase.OnMarkerCameraUpdateCallback?
    ) {
        if (map != null) {
            try {
                if (duration == 0)
                    map.animateCamera(cameraUpdate, getCallback(callback))
                else
                    map.animateCamera(cameraUpdate, duration, getCallback(callback))
                return
            } catch (e: IllegalStateException) {
                Services.Log.error(e)
            }
        }
        pendingUpdate = cameraUpdate
    }

    override fun getProjection(initialLocation: MapLocation?): Projection<MapLocation, MapLocationBounds>? {
        if (map == null) return null
        val projection = map.projection
        val cameraPosition = map.cameraPosition
        var fromScreenLocation = MapLocation(0.0, 0.0)
        if (initialLocation != null) {
            val centerPoint: Point = projection.toScreenLocation(LatLng(initialLocation.latitude, initialLocation.longitude))
            centerPoint.y -= (mapView.height * MapItemViewHelper.MARKER_INFO_WINDOW_OFF_CENTER_FACTOR).toInt()
            fromScreenLocation = MapLocation(projection.fromScreenLocation(centerPoint))
        }
        val center = MapLocation(cameraPosition.target.latitude, cameraPosition.target.longitude)
        val latLngBounds = MapLocationBounds(projection.visibleRegion.latLngBounds)
        val zoom = cameraPosition.zoom.toInt()
        return Projection(latLngBounds, center, fromScreenLocation, zoom)
    }

    private fun getCameraUpdate(location: IMapLocation, zoom: Int): CameraUpdate {
        val latLng = LatLng(location.latitude, location.longitude)
        return if (zoom > 0)
            CameraUpdateFactory.newLatLngZoom(latLng, zoom.toFloat())
        else
            CameraUpdateFactory.newLatLng(latLng)
    }

    private fun getCallback(callback: GxMapViewBase.OnMarkerCameraUpdateCallback?): HuaweiMap.CancelableCallback {
        return object : HuaweiMap.CancelableCallback {
            override fun onFinish() {
                callback?.onFinish()
            }

            override fun onCancel() {
                callback?.onCancel()
            }
        }
    }
}
