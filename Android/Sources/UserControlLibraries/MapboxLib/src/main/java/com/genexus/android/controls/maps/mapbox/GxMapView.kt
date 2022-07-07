package com.genexus.android.controls.maps.mapbox

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.Gravity
import androidx.core.content.res.ResourcesCompat
import com.genexus.android.controls.maps.mapbox.MapboxHelper.mapTypeToMapboxStyle
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.controls.maps.GxMapViewDefinition
import com.genexus.android.core.ui.Coordinator
import com.genexus.android.maps.GxMapViewBase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mapbox.mapboxsdk.camera.CameraUpdate
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.log.Logger
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.Fill
import com.mapbox.mapboxsdk.plugins.annotation.FillOptions
import com.mapbox.mapboxsdk.plugins.annotation.Line
import com.mapbox.mapboxsdk.plugins.annotation.LineOptions
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolDragListener
import com.mapbox.mapboxsdk.plugins.annotation.Symbol
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.mapboxsdk.plugins.traffic.TrafficPlugin
import com.mapbox.mapboxsdk.utils.BitmapUtils

@SuppressLint("ViewConstructor")
internal class GxMapView
private constructor(context: Context, definition: GxMapViewDefinition, coordinator: Coordinator) :
    GxMapViewBase<MapboxMap, MapView, CameraUpdate, Symbol, Line, Fill, SymbolOptions, LineOptions, FillOptions,
        String, OnSymbolDragListener, MapLocation, MapLocationBounds, GxMapViewItem,
        GxMapViewData>(context, MapOptions(context), definition, coordinator) {

    private var mapStyle: Style? = null
    private var locationActionButton: FloatingActionButton? = null
    private var mapboxMapType: String? = null

    constructor(context: Context, coordinator: Coordinator, definition: GxMapViewDefinition) :
        this(context, definition, coordinator)

    override fun onMapCreate(mapView: MapView) {
        super.onMapCreate(mapView)
        Logger.setVerbosity(Logger.WARN)
        mapView.onCreate(null)
        mapView.getMapAsync { mapboxMap: MapboxMap? ->
            mapboxMap?.let { it ->
                it.addOnMapClickListener(mapClickListener)
                it.addOnCameraMoveListener(cameraChangeListener)
                it.addOnCameraIdleListener(cameraIdleListener)
                it.addOnCameraMoveListener(cameraMoveListener)
                setMapStyle(it, true)
            }
        }
    }

    override fun onBeforeUpdate() {
        super.onBeforeUpdate()
        map?.let { setMapStyle(it, false) }
    }

    private val markerDragListener: OnSymbolDragListener = object : OnSymbolDragListener {
        override fun onAnnotationDragStarted(annotation: Symbol) {
            onDragStarted(annotation)
        }

        override fun onAnnotationDrag(annotation: Symbol) {
            onDrag(annotation)
        }

        override fun onAnnotationDragFinished(annotation: Symbol) {
            onDragEnded(annotation)
        }
    }
    private val mapClickListener = MapboxMap.OnMapClickListener { latLng: LatLng? ->
        return@OnMapClickListener onMapClick(latLng?.let { MapLocation(it.latitude, it.longitude) })
    }
    private val cameraChangeListener = MapboxMap.OnCameraMoveListener {
        onCameraChange()
    }

    private val cameraIdleListener = MapboxMap.OnCameraIdleListener {
        map?.let {
            val location = MapLocation(it.cameraPosition.target.latitude, it.cameraPosition.target.longitude)
            onCameraIdle(location)
        }
    }

    private val cameraMoveListener = MapboxMap.OnCameraMoveListener {
        map?.let {
            val location = MapLocation(it.cameraPosition.target.latitude, it.cameraPosition.target.longitude)
            onCameraMove(location)
        }
    }

    override fun setMapType(type: String?) {
        super.setMapType(type)
        map?.let { setMapStyle(it, false) }
    }

    private fun setMapStyle(map: MapboxMap, shouldSetMap: Boolean) {
        val newType = mapTypeToMapboxStyle(mapType)
        if (mapboxMapType == newType && !shouldSetMap)
            return

        mapboxMapType = newType
        map.setStyle(mapboxMapType) { style: Style ->
            mapStyle = style
            val drawable = ResourcesCompat.getDrawable(context.resources, R.drawable.mapbox_marker_icon_default, null)
            BitmapUtils.getBitmapFromDrawable(drawable)?.let { it -> style.addImage(GeographiesManager.DEFAULT_MARKER_ICON, it, false) }
            if (showTrafficLayer) {
                val trafficPlugin = TrafficPlugin(mapView, map, style)
                trafficPlugin.setVisibility(true)
            }

            if (shouldSetMap) setMap(map, markerDragListener)
        }
    }

    @SuppressLint("MissingPermission")
    override fun getMyLocationRunnable(): Runnable = Runnable {
        if (locationActionButton == null) {
            val locationComponent = map?.locationComponent
            require(locationComponent != null) { "Location component cannot be null" }

            if (myLocationImageResourceId != 0) {
                val customLocationComponentOptions = LocationComponentOptions.builder(context)
                customLocationComponentOptions.foregroundDrawable(myLocationImageResourceId)
                val locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(context, mapStyle!!)
                        .locationComponentOptions(customLocationComponentOptions.build())
                        .build()
                locationComponent.activateLocationComponent(locationComponentActivationOptions)
            } else {
                locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions
                        .builder(context, mapStyle!!).build()
                )
            }

            locationComponent.isLocationComponentEnabled = true
            locationComponent.cameraMode = CameraMode.TRACKING
            locationComponent.renderMode = RenderMode.COMPASS

            val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            lp.gravity = Gravity.BOTTOM or Gravity.END
            lp.setMargins(0, 0, 32, 32)

            locationActionButton = FloatingActionButton(context).apply {
                layoutParams = lp
                compatPressedTranslationZ = 12f
                backgroundTintList = ColorStateList.valueOf(Color.WHITE)
                elevation = Services.Device.dipsToPixels(6).toFloat()
                setImageResource(R.drawable.mapbox_my_location)
                setOnClickListener {
                    val location = locationComponent.lastKnownLocation
                    if (location != null)
                        updateCamera(MapLocation(location.latitude, location.longitude), 0)
                }
                addView(this)
            }
        }
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
