package com.genexus.android.controls.maps.google;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controls.maps.common.IMapLocation;
import com.genexus.android.maps.LocationPickerActivityBase;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class LocationPickerActivity extends LocationPickerActivityBase<MapView, GoogleMap, Marker>
	implements OnMapClickListener, OnMarkerDragListener {

	public LocationPickerActivity() {
		super(new MapOptions());
	}

	@Override
	public void initialize(Bundle savedInstanceState, boolean myLocationEnabled, String mapType, int zoom, Pair<Double, Double> locationLatLng) {
		MapView mapView = getMapView();
		if (GoogleMapsHelper.checkGoogleMapsV2(this) && mapView != null) {
			mapView.onCreate(savedInstanceState);
			mapView.getMapAsync(googleMap -> {
				setMap(googleMap);
				googleMap.setOnMapClickListener(LocationPickerActivity.this);
				googleMap.setOnMarkerDragListener(LocationPickerActivity.this);
				googleMap.setMyLocationEnabled(myLocationEnabled);
				googleMap.setMapType(GoogleMapsHelper.mapTypeToGoogleMapType(mapType));
				if (locationLatLng != null)
					setPointOnMap(new MapLocation(locationLatLng.first, locationLatLng.second), zoom);
			});
		}
	}

	@Override
	public void setPointOnMap(@NonNull IMapLocation location, int zoom) {
		GoogleMap map = getMap();
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
		List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,
			Place.Field.LAT_LNG, Place.Field.ADDRESS);

		Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
			.build(this);

		startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
			if (data == null)
				throw new IllegalArgumentException("Place data returned is empty");

			if (resultCode == AutocompleteActivity.RESULT_OK) {
				Place place = Autocomplete.getPlaceFromIntent(data);
				Services.Log.debug("Place selected: " + place.toString());
				if (place.getLatLng() == null)
					Services.Log.warning("Place data doesn't contain LatLng value");
				else
					setPointOnMap(new MapLocation(place.getLatLng()), 0);
			} else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
				Status status = Autocomplete.getStatusFromIntent(data);
				Services.Log.error(status.toString());
			}
		}
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
