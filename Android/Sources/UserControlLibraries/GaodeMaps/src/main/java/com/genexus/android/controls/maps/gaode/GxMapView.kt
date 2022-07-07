package com.genexus.android.controls.maps.gaode

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.amap.api.maps.AMap
import com.amap.api.maps.AMap.OnMapLoadedListener
import com.amap.api.maps.CameraUpdate
import com.amap.api.maps.MapView
import com.amap.api.maps.model.BitmapDescriptor
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.maps.model.Polygon
import com.amap.api.maps.model.PolygonOptions
import com.amap.api.maps.model.Polyline
import com.amap.api.maps.model.PolylineOptions
import com.genexus.android.core.controls.maps.GxMapViewDefinition
import com.genexus.android.core.ui.Coordinator
import com.genexus.android.maps.GxMapViewBase

@SuppressLint("ViewConstructor")
internal class GxMapView(context: Context, coordinator: Coordinator, definition: GxMapViewDefinition) :
    GxMapViewBase<AMap, MapView, CameraUpdate, Marker, Polyline, Polygon,
        MarkerOptions, PolylineOptions, PolygonOptions, BitmapDescriptor, AMap.OnMarkerDragListener,
        MapLocation, MapLocationBounds, GxMapViewItem, GxMapViewData>
    (context, MapOptions(), definition, coordinator) {

    private var gaodeMapType = 0
    private lateinit var gaodeMap: AMap

    // TODO: Pass on events: onSaveInstanceState(Bundle) & onLowMemory().
    override fun onMapCreate(mapView: MapView) {
        super.onMapCreate(mapView)
        mapView.onCreate(null)
        gaodeMap = mapView.map
        gaodeMap.let {
            it.setOnMapLoadedListener(mapLoadedListener)
            it.setOnCameraChangeListener(cameraChangeListener)
            it.setOnMapClickListener(onMapClickListener)
        }
    }

    override fun onBeforeUpdate() {
        super.onBeforeUpdate()
        map?.let {
            gaodeMapType = GaodeMapsHelper.mapTypeToGaodeMapType(mapType)
            it.mapType = gaodeMapType
            it.isTrafficEnabled = showTrafficLayer
        }
    }

    private val markerDragListener: AMap.OnMarkerDragListener = object : AMap.OnMarkerDragListener {
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

    private val onMapClickListener = AMap.OnMapClickListener { latLng: LatLng ->
        onMapClick(MapLocation(latLng.latitude, latLng.longitude))
    }

    private val cameraChangeListener: AMap.OnCameraChangeListener = object : AMap.OnCameraChangeListener {
        override fun onCameraChange(position: CameraPosition) {
            this@GxMapView.onCameraChange()
        }

        override fun onCameraChangeFinish(position: CameraPosition) {
            onCameraIdle(MapLocation(position.target.latitude, position.target.longitude))
        }
    }

    private val mapLoadedListener = OnMapLoadedListener {
        setMap(gaodeMap, markerDragListener)
    }

    override fun setMapType(type: String?) {
        super.setMapType(type)
        map?.let {
            gaodeMapType = GaodeMapsHelper.mapTypeToGaodeMapType(type)
            it.mapType = gaodeMapType
        }
    }

    override fun getMyLocationRunnable(): Runnable {
        return Runnable {
            map?.let {
                it.isMyLocationEnabled = true
                it.setOnMyLocationChangeListener(onLocationChangeListener)
            }
        }
    }

    private val onLocationChangeListener = AMap.OnMyLocationChangeListener { location: Location? ->
        if (location == null)
            return@OnMyLocationChangeListener

        var resourceId = R.drawable.pin_here_arrow
        val customResourceId = myLocationImageResourceId
        if (customResourceId != 0)
            resourceId = customResourceId

        geographiesManager.addOrUpdateLocationMarker(location, resourceId)
    }

    override fun onStart() {}

    override fun onResume() {
        mapView.onResume()
    }

    override fun onPause() {
        mapView.onPause()
    }

    override fun onStop() {}

    override fun onDestroy() {
        mapView.onDestroy()
    }
}
