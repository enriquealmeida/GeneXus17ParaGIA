package com.genexus.android.controls.maps.mapbox

import com.genexus.android.core.base.services.Services
import com.genexus.android.core.controls.maps.common.IMapLocation
import com.genexus.android.maps.GxMapViewBase
import com.genexus.android.maps.MapItemViewHelper
import com.genexus.android.maps.MapViewCamera
import com.mapbox.mapboxsdk.camera.CameraUpdate
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import kotlin.math.roundToInt

internal class GxMapViewCamera(private val map: MapboxMap?, private val mapView: MapView) :
    MapViewCamera<CameraUpdate, MapLocation, MapLocationBounds, GxMapViewItem, GxMapViewData>() {

    override fun updateCamera(bounds: MapLocationBounds, padding: Int, duration: Int) {
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds.latLngBounds, padding)
        updateCamera(cameraUpdate, duration, null)
    }

    override fun updateCamera(location: IMapLocation, zoom: Int) {
        updateCamera(getCameraUpdate(location, zoom), 0, null)
    }

    override fun updateCamera(
        location: IMapLocation,
        zoom: Int,
        duration: Int,
        callback: GxMapViewBase.OnMarkerCameraUpdateCallback?
    ) {
        updateCamera(getCameraUpdate(location, zoom), duration, callback)
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
        map?.let {
            val projection = it.projection
            val cameraPosition = it.cameraPosition

            var fromScreenLocation = MapLocation(0.toDouble(), 0.toDouble())
            initialLocation?.let {
                val centerPoint = projection.toScreenLocation(LatLng(initialLocation.latitude, initialLocation.longitude))
                centerPoint.y -= (mapView.height * MapItemViewHelper.MARKER_INFO_WINDOW_OFF_CENTER_FACTOR).roundToInt().toFloat()
                fromScreenLocation = MapLocation(projection.fromScreenLocation(centerPoint))
            }

            val center = MapLocation(cameraPosition.target.latitude, cameraPosition.target.longitude)
            val latLngBounds = MapLocationBounds(projection.visibleRegion.latLngBounds)
            val zoom = cameraPosition.zoom.toInt()

            return Projection(latLngBounds, center, fromScreenLocation, zoom)
        }

        return null
    }

    private fun getCameraUpdate(location: IMapLocation, zoom: Int): CameraUpdate {
        val latLng = LatLng(location.latitude, location.longitude)
        return if (zoom > 0)
            CameraUpdateFactory.newLatLngZoom(latLng, zoom.toDouble())
        else
            CameraUpdateFactory.newLatLng(latLng)
    }

    private fun getCallback(callback: GxMapViewBase.OnMarkerCameraUpdateCallback?): MapboxMap.CancelableCallback {
        return object : MapboxMap.CancelableCallback {
            override fun onCancel() {
                callback?.onCancel()
            }

            override fun onFinish() {
                callback?.onFinish()
            }
        }
    }
}
