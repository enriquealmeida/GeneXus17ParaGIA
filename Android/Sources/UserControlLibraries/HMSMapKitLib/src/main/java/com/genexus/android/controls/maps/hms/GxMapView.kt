package com.genexus.android.controls.maps.hms

import android.annotation.SuppressLint
import android.content.Context
import com.genexus.android.core.controls.maps.GxMapViewDefinition
import com.genexus.android.core.ui.Coordinator
import com.genexus.android.maps.GxMapViewBase
import com.huawei.hms.maps.CameraUpdate
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapView
import com.huawei.hms.maps.model.BitmapDescriptor
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.Marker
import com.huawei.hms.maps.model.MarkerOptions
import com.huawei.hms.maps.model.Polygon
import com.huawei.hms.maps.model.PolygonOptions
import com.huawei.hms.maps.model.Polyline
import com.huawei.hms.maps.model.PolylineOptions

@SuppressLint("ViewConstructor")
internal class GxMapView constructor(context: Context, definition: GxMapViewDefinition, coordinator: Coordinator) :
    GxMapViewBase<HuaweiMap, MapView, CameraUpdate, Marker, Polyline, Polygon, MarkerOptions, PolylineOptions, PolygonOptions,
        BitmapDescriptor, HuaweiMap.OnMarkerDragListener, MapLocation, MapLocationBounds, GxMapViewItem,
        GxMapViewData>(context, MapOptions(), definition, coordinator) {

    private var huaweiMapType = HuaweiMap.MAP_TYPE_NORMAL

    override fun onMapCreate(mapView: MapView) {
        super.onMapCreate(mapView)
        mapView.onCreate(null)
        mapView.getMapAsync {
            it.setOnMapClickListener(mapClickListener)
            it.setOnCameraMoveStartedListener(cameraChangeListener)
            it.setOnCameraIdleListener(cameraIdleListener)
            it.setOnCameraMoveListener(cameraMoveListener)
            setMap(it, markerDragListener)
        }
    }

    override fun onBeforeUpdate() {
        super.onBeforeUpdate()
        map?.let {
            huaweiMapType = HuaweiMapsHelper.mapTypeToHuaweiMapType(mapType)
            it.mapType = huaweiMapType
            it.isTrafficEnabled = showTrafficLayer
        }
    }

    private val markerDragListener: HuaweiMap.OnMarkerDragListener = object : HuaweiMap.OnMarkerDragListener {
        override fun onMarkerDragStart(marker: Marker) {
            onDragStarted(marker)
        }

        override fun onMarkerDrag(marker: Marker) {
            onDrag(marker)
        }

        override fun onMarkerDragEnd(marker: Marker) {
            onDragEnded(marker)
        }
    }

    private val mapClickListener = HuaweiMap.OnMapClickListener { latLng: LatLng? ->
        onMapClick(latLng?.let { MapLocation(it.latitude, it.longitude) })
    }

    private val cameraChangeListener = HuaweiMap.OnCameraMoveStartedListener {
        onCameraChange()
    }

    private val cameraIdleListener = HuaweiMap.OnCameraIdleListener {
        map?.let {
            val location = MapLocation(it.cameraPosition.target.latitude, it.cameraPosition.target.longitude)
            onCameraIdle(location)
        }
    }

    private val cameraMoveListener = HuaweiMap.OnCameraMoveListener {
        map?.let {
            val location = MapLocation(it.cameraPosition.target.latitude, it.cameraPosition.target.longitude)
            onCameraMove(location)
        }
    }

    override fun setMapType(type: String?) {
        super.setMapType(type)
        huaweiMapType = HuaweiMapsHelper.mapTypeToHuaweiMapType(mapType)
        map?.mapType = huaweiMapType
    }

    override fun getMyLocationRunnable(): Runnable = Runnable {
        map?.isMyLocationEnabled = true
    }

    override fun onStart() {
        mapView.onStart()
    }

    override fun onResume() {
        mapView.onResume()
    }

    override fun onPause() {
        mapView.onPause()
    }

    override fun onStop() {
        mapView.onStop()
    }

    override fun onDestroy() {
        mapView.onDestroy()
    }
}
