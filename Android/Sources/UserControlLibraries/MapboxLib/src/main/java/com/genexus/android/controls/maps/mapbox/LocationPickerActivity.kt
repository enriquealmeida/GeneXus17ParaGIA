package com.genexus.android.controls.maps.mapbox

import android.os.Bundle
import android.util.Pair
import androidx.core.content.res.ResourcesCompat
import com.genexus.android.controls.maps.mapbox.GeographiesManager.Companion.DEFAULT_MARKER_ICON
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.controls.maps.common.IMapLocation
import com.genexus.android.maps.LocationPickerActivityBase
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.log.Logger
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolDragListener
import com.mapbox.mapboxsdk.plugins.annotation.Symbol
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.mapboxsdk.utils.BitmapUtils

class LocationPickerActivity :
    LocationPickerActivityBase<MapView, MapboxMap, Symbol>(MapOptions(Services.Application.appContext)),
    OnSymbolDragListener,
    MapboxMap.OnMapClickListener {

    private var symbolManager: SymbolManager? = null

    override fun initialize(savedInstanceState: Bundle?, myLocationEnabled: Boolean, mapType: String?, zoom: Int, locationLatLng: Pair<Double, Double>?) {
        Logger.setVerbosity(Logger.WARN)
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync { mapInstance ->
            map = mapInstance
            map?.let {
                val type = MapboxHelper.mapTypeToMapboxStyle(mapType)
                it.setStyle(type) { style: Style ->
                    BitmapUtils.getBitmapFromDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.mapbox_marker_icon_default,
                            null
                        )
                    )?.let { icon ->
                        style.addImage(DEFAULT_MARKER_ICON, icon, false)
                    }

                    symbolManager = SymbolManager(mapView!!, it, style)
                    symbolManager?.addDragListener(this@LocationPickerActivity)
                    it.addOnMapClickListener(this@LocationPickerActivity)
// 					it.setMyLocationEnabled(myLocationEnabled)
                    if (locationLatLng != null)
                        setPointOnMap(MapLocation(locationLatLng.first, locationLatLng.second), zoom)
                }
            }
        }
    }

    override fun setPointOnMap(location: IMapLocation, zoom: Int) {
        val point = LatLng(location.latitude, location.longitude)
        if (marker != null) {
            symbolManager?.delete(marker)
            marker = null
        }

        val markerOptions = SymbolOptions().withLatLng(point)
            .withDraggable(true)
            .withIconImage(DEFAULT_MARKER_ICON)

        val cameraUpdate = if (zoom > 0)
            CameraUpdateFactory.newLatLngZoom(point, zoom.toDouble())
        else
            CameraUpdateFactory.newLatLng(point)

        map?.moveCamera(cameraUpdate)
        marker = symbolManager?.create(markerOptions)
        setPickedLocation(MapLocation(point))
    }

    override fun showAutocompleteSearchBox() {
        TODO("Not supported by Mapbox")
    }

    override fun onMapClick(latLng: LatLng): Boolean {
        setPointOnMap(MapLocation(latLng), 0)
        return true
    }

    override fun onAnnotationDragStarted(annotation: Symbol?) {
        annotation?.let { setPickedLocation(MapLocation(it.latLng)) }
    }

    override fun onAnnotationDrag(annotation: Symbol?) {
        annotation?.let { setPickedLocation(MapLocation(it.latLng)) }
    }

    override fun onAnnotationDragFinished(annotation: Symbol?) {
        annotation?.let { setPickedLocation(MapLocation(it.latLng)) }
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        mapView?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mapView?.onDestroy()
        super.onDestroy()
    }
}
