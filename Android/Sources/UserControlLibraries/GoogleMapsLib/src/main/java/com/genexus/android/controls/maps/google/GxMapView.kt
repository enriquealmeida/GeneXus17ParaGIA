package com.genexus.android.controls.maps.google

import android.annotation.SuppressLint
import android.content.Context
import com.genexus.android.core.controls.maps.GxMapViewDefinition
import com.genexus.android.core.ui.Coordinator
import com.genexus.android.maps.GxMapViewBase
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions

@SuppressLint("ViewConstructor")
internal class GxMapView(context: Context, coordinator: Coordinator, definition: GxMapViewDefinition) :
    GxMapViewBase<GoogleMap, MapView, CameraUpdate, Marker, Polyline, Polygon,
        MarkerOptions, PolylineOptions, PolygonOptions, BitmapDescriptor, GoogleMap.OnMarkerDragListener,
        MapLocation, MapLocationBounds, GxMapViewItem, GxMapViewData>
    (context, MapOptions(), definition, coordinator) {

    private var googleMapType = 0

    // TODO: Pass on events: onSaveInstanceState(Bundle) & onLowMemory().
    @Suppress("DEPRECATION")
    override fun onMapCreate(mapView: MapView) {
        super.onMapCreate(mapView)
        mapView.onCreate(null)
        mapView.getMapAsync { googleMap: GoogleMap ->
            googleMap.let {
                it.setOnCameraChangeListener(cameraChangeListener)
                it.setOnMapClickListener(onMapClickListener)
                it.setOnCameraIdleListener(cameraIdleListener)
                it.setOnCameraMoveListener(cameraMoveListener)
                setMap(it, markerDragListener)
            }
        }
    }

    override fun onBeforeUpdate() {
        super.onBeforeUpdate()
        map?.let {
            googleMapType = GoogleMapsHelper.mapTypeToGoogleMapType(mapType)
            it.mapType = googleMapType
            it.isTrafficEnabled = showTrafficLayer
        }
    }

    private val markerDragListener: GoogleMap.OnMarkerDragListener = object : GoogleMap.OnMarkerDragListener {
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

    private val onMapClickListener = GoogleMap.OnMapClickListener { latLng: LatLng ->
        onMapClick(MapLocation(latLng.latitude, latLng.longitude))
    }

    @Suppress("DEPRECATION")
    private val cameraChangeListener = GoogleMap.OnCameraChangeListener { onCameraChange() }
    private val cameraIdleListener = GoogleMap.OnCameraIdleListener { onCameraIdle(null) }
    private val cameraMoveListener = GoogleMap.OnCameraMoveListener { onCameraMove(null) }

    override fun setMapType(type: String?) {
        super.setMapType(type)
        map?.let {
            googleMapType = GoogleMapsHelper.mapTypeToGoogleMapType(type)
            it.mapType = googleMapType
        }
    }

    @Suppress("DEPRECATION")
    override fun getMyLocationRunnable(): Runnable {
        return Runnable {
            map?.let {
                it.isMyLocationEnabled = true
                // show my location icon and animate it
                if (myLocationImageResourceId != 0)
                    it.setOnMyLocationChangeListener(onMyLocationChangeListener)
            }
        }
    }

    @Suppress("DEPRECATION")
    private val onMyLocationChangeListener = GoogleMap.OnMyLocationChangeListener { location ->
        geographiesManager.addOrUpdateLocationMarker(location, myLocationImageResourceId)
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
