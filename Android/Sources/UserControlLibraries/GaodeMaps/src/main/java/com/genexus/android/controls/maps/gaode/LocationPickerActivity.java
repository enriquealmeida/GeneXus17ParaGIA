package com.genexus.android.controls.maps.gaode;

import android.os.Bundle;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMarkerDragListener;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.genexus.android.core.controls.maps.common.IMapLocation;
import com.genexus.android.maps.LocationPickerActivityBase;

import org.apache.commons.lang.NotImplementedException;

public class LocationPickerActivity extends LocationPickerActivityBase<MapView, AMap, Marker>
	implements OnMapClickListener, OnMarkerDragListener {

	public LocationPickerActivity() {
		super(new MapOptions());
	}

	@Override
	public void initialize(Bundle savedInstanceState, boolean myLocationEnabled, String mapType, int zoom, Pair<Double, Double> locationLatLng) {
		MapView mapView = getMapView();
		if (mapView != null) {
			mapView.onCreate(savedInstanceState);
			AMap map = mapView.getMap();
			setMap(map);
			map.setOnMapClickListener(LocationPickerActivity.this);
			map.setOnMarkerDragListener(LocationPickerActivity.this);
			map.setMyLocationEnabled(myLocationEnabled);
			map.setMapType(GaodeMapsHelper.mapTypeToGaodeMapType(mapType));
			if (locationLatLng != null)
				setPointOnMap(new MapLocation(locationLatLng.first, locationLatLng.second), zoom);
		}
	}

	@Override
	public void setPointOnMap(@NonNull IMapLocation location, int zoom) {
		AMap map = getMap();
		if (map == null)
			return;

		LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
		if (getMarker() != null) {
			map.clear();
			setMarker(null);
		}

		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(point);
		markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
		markerOptions.draggable(true);

		CameraUpdate cameraUpdate;
		if (zoom > 0)
			cameraUpdate = CameraUpdateFactory.newLatLngZoom(point, zoom);
		else
			cameraUpdate = CameraUpdateFactory.newLatLng(point);

		map.moveCamera(cameraUpdate);
		setMarker(map.addMarker(markerOptions));
		setPickedLocation(new MapLocation(point));
	}

	@Override
	public void showAutocompleteSearchBox() {
		throw new NotImplementedException("Not supported by Gaode");
	}

	@Override
	public void onMapClick(LatLng point) {
		setPointOnMap(new MapLocation(point), 0);
	}

	@Override
	public void onMarkerDragStart(Marker marker) {
		setPickedLocation(new MapLocation(marker.getPosition()));
	}

	@Override
	public void onMarkerDrag(Marker marker) {
		setPickedLocation(new MapLocation(marker.getPosition()));
	}

	@Override
	public void onMarkerDragEnd(Marker marker) {
		setPickedLocation(new MapLocation(marker.getPosition()));
	}

	@Override
	protected void onResume() {
		super.onResume();
		MapView mapView = getMapView();
		if (mapView != null)
			mapView.onResume();
	}

	@Override
	protected void onPause() {
		MapView mapView = getMapView();
		if (mapView != null)
			mapView.onPause();

		super.onPause();
	}

	@Override
	protected void onDestroy() {
		MapView mapView = getMapView();
		if (mapView != null)
			mapView.onDestroy();

		super.onDestroy();
	}
}
