package com.genexus.android.controls.maps.baidu;

import android.graphics.Point;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.maps.GxMapViewBase;
import com.genexus.android.maps.MapViewCamera;
import com.genexus.android.core.controls.maps.common.IMapLocation;
import com.genexus.android.maps.MapItemViewHelper;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

class GxMapViewCamera extends MapViewCamera<MapStatusUpdate, MapLocation, MapLocationBounds, GxMapViewItem, GxMapViewData> {
	private final BaiduMap mMap;
	private final MapView mMapView;

	public GxMapViewCamera(BaiduMap map, MapView mapView) {
		mMap = map;
		mMapView = mapView;
	}

	@Override
	public void updateCamera(@NonNull MapLocationBounds bounds, int padding, int duration) {
		MapStatusUpdate updateStatus = MapStatusUpdateFactory.newLatLngBounds(bounds.getLatLngBounds());
		updateCamera(updateStatus, duration, null);
	}

	@Override
	public void updateCamera(@NonNull IMapLocation location, int zoom, int duration, GxMapViewBase.OnMarkerCameraUpdateCallback callback) {
		updateCamera(getCameraUpdate(location, zoom), duration, callback);
	}

	@Override
	public void updateCamera(@NonNull IMapLocation location, int zoom) {
		updateCamera(getCameraUpdate(location, zoom), 0, null);
	}

	@Override
	public void updateCamera(MapStatusUpdate mapStatusUpdate, int duration, GxMapViewBase.OnMarkerCameraUpdateCallback callback) {
		mMap.setOnMapStatusChangeListener(getCallback(callback));
		Services.Device.postOnUiThreadDelayed(() -> mMap.animateMapStatus(mapStatusUpdate, duration), 1000);
	}

	@Nullable
	@Override
	public Projection<MapLocation, MapLocationBounds> getProjection(MapLocation initialLocation) {
		if (mMap == null)
			return null;

		com.baidu.mapapi.map.Projection projection = mMap.getProjection();
		MapStatus cameraPosition = mMap.getMapStatus();

		MapLocation fromScreenLocation = new MapLocation(0D, 0D);
		if (initialLocation != null) {
			Point centerPoint = projection.toScreenLocation(new LatLng(initialLocation.getLatitude(), initialLocation.getLatitude()));
			centerPoint.y -= (int) (mMapView.getHeight() * MapItemViewHelper.MARKER_INFO_WINDOW_OFF_CENTER_FACTOR);
			fromScreenLocation = new MapLocation(projection.fromScreenLocation(centerPoint));
		}

		MapLocation center = new MapLocation(cameraPosition.target.latitude, cameraPosition.target.longitude);
		MapLocationBounds latLngBounds = new MapLocationBounds(cameraPosition.bound);
		int zoom = (int) cameraPosition.zoom;

		return new Projection<>(latLngBounds, center, fromScreenLocation, zoom);
	}

	private MapStatusUpdate getCameraUpdate(@NonNull IMapLocation location, int zoom) {
		LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
		MapStatusUpdate update;
		if (zoom > 0)
			update = MapStatusUpdateFactory.newLatLngZoom(latLng, zoom);
		else
			update = MapStatusUpdateFactory.newLatLng(latLng);

		return update;
	}

	private BaiduMap.OnMapStatusChangeListener getCallback(GxMapViewBase.OnMarkerCameraUpdateCallback callback) {
		return new BaiduMap.OnMapStatusChangeListener() {
			@Override
			public void onMapStatusChangeFinish(MapStatus mapStatus) {
				if (callback != null)
					callback.onFinish();
			}

			@Override
			public void onMapStatusChangeStart(MapStatus mapStatus) {
				if (callback != null)
					callback.onStart();
			}

			@Override
			public void onMapStatusChangeStart(MapStatus mapStatus, int i) {
				if (callback != null)
					callback.onStart();
			}

			@Override
			public void onMapStatusChange(MapStatus mapStatus) {

			}
		};
	}
}
