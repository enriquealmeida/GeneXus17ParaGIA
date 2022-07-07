package com.genexus.android.controls.maps.baidu;

import android.os.Bundle;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.genexus.android.core.controls.maps.common.IMapLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.genexus.android.maps.LocationPickerActivityBase;

import org.apache.commons.lang.NotImplementedException;

import static com.genexus.android.controls.maps.baidu.GxMapView.MARKER_CAMERA_ANIMATION_DURATION;

public class LocationPickerActivity extends LocationPickerActivityBase<MapView, BaiduMap, Marker>
	implements BaiduMap.OnMapClickListener, BaiduMap.OnMarkerDragListener {

	public LocationPickerActivity() {
		super(new MapOptions());
	}

	@Override
	public void initialize(Bundle savedInstanceState, boolean myLocationEnabled, String mapType, int zoom, Pair<Double, Double> locationLatLng) {
		MapView mapView = getMapView();
		if (mapView != null) {
			BaiduMap map = mapView.getMap();
			setMap(map);
			map.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			map.setOnMapClickListener(this);
			mapView.showZoomControls(true);
			mapView.setFocusable(true);
			mapView.setEnabled(true);
			mapView.setClickable(true);
			map.setMyLocationEnabled(myLocationEnabled);
			map.setMapType(BaiduMapsHelper.mapTypeToBaiduMapType(mapType));
			if (locationLatLng != null)
				setPointOnMap(new MapLocation(locationLatLng.first, locationLatLng.second), zoom);
		}
	}

	@Override
	public void setPointOnMap(@NonNull IMapLocation location, int zoom) {
		BaiduMap map = getMap();
		if (map == null)
			return;

		LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
		if (getMarker() != null) {
			map.clear();
			setMarker(null);
		}

		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(point);
		markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.red_markers));
		markerOptions.draggable(true);

		MapStatusUpdate cameraUpdate;
		if (zoom > 0)
			cameraUpdate = MapStatusUpdateFactory.newLatLngZoom(point, zoom);
		else
			cameraUpdate = MapStatusUpdateFactory.newLatLng(point);

		map.animateMapStatus(cameraUpdate, MARKER_CAMERA_ANIMATION_DURATION);
		setMarker((Marker) map.addOverlay(markerOptions));
		setPickedLocation(new MapLocation(point));
	}

	@Override
	public void showAutocompleteSearchBox() {
		throw new NotImplementedException("Not supported by Baidu");
	}

	@Override
	public void onMapClick(LatLng latLng) {
		setPointOnMap(new MapLocation(latLng), 0);
	}

	@Override
	public void onMapPoiClick(MapPoi mapPoi) {
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
