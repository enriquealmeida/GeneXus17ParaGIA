package com.genexus.android.controls.maps.hms

import android.os.Bundle
import android.util.Pair
import com.genexus.android.core.controls.maps.common.IMapLocation
import com.genexus.android.maps.LocationPickerActivityBase
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapView
import com.huawei.hms.maps.model.BitmapDescriptorFactory
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.Marker
import com.huawei.hms.maps.model.MarkerOptions

class LocationPickerActivity :
    LocationPickerActivityBase<MapView, HuaweiMap, Marker>(MapOptions()),
    HuaweiMap.OnMarkerDragListener,
    HuaweiMap.OnMapClickListener {

    override fun initialize(
        savedInstanceState: Bundle?,
        myLocationEnabled: Boolean,
        mapType: String?,
        zoom: Int,
        locationLatLng: Pair<Double, Double>?
    ) {
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync { huaweiMap ->
            map = huaweiMap
            huaweiMap.setOnMapClickListener(this@LocationPickerActivity)
            huaweiMap.setOnMarkerDragListener(this@LocationPickerActivity)
            if (locationLatLng != null)
                setPointOnMap(MapLocation(locationLatLng.first, locationLatLng.second), zoom)
        }
    }

    override fun setPointOnMap(location: IMapLocation, zoom: Int) {
        val point = LatLng(location.latitude, location.longitude)
        marker?.let {
            map?.clear()
            marker = null
        }

        val markerOptions = MarkerOptions()
        markerOptions.position(point)
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        markerOptions.draggable(true)

        val cameraUpdate = if (zoom > 0)
            CameraUpdateFactory.newLatLngZoom(point, zoom.toFloat())
        else
            CameraUpdateFactory.newLatLng(point)

        map?.moveCamera(cameraUpdate)
        marker = map?.addMarker(markerOptions)
        setPickedLocation(MapLocation(point))
    }

    override fun showAutocompleteSearchBox() {
        TODO("Not supported by Huawei")
    }

    override fun onMapClick(latLng: LatLng) {
        setPointOnMap(MapLocation(latLng), 0)
    }

    override fun onMarkerDragStart(marker: Marker?) {
        marker?.let { setPickedLocation(MapLocation(marker.position)) }
    }

    override fun onMarkerDrag(marker: Marker?) {
        marker?.let { setPickedLocation(MapLocation(marker.position)) }
    }

    override fun onMarkerDragEnd(marker: Marker?) {
        marker?.let { setPickedLocation(MapLocation(marker.position)) }
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
