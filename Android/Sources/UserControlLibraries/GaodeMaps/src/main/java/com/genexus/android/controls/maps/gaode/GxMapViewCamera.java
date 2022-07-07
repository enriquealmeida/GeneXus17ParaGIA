package com.genexus.android.controls.maps.gaode;

import android.graphics.Point;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.maps.GxMapViewBase;
import com.genexus.android.maps.MapViewCamera;
import com.genexus.android.core.controls.maps.common.IMapLocation;
import com.genexus.android.maps.MapItemViewHelper;

class GxMapViewCamera extends MapViewCamera<CameraUpdate, MapLocation, MapLocationBounds, GxMapViewItem, GxMapViewData> {
	private final AMap mMap;
	private final MapView mMapView;

	public GxMapViewCamera(AMap map, MapView mapView) {
		mMap = map;
		mMapView = mapView;
	}

	@Override
	public void updateCamera(@NonNull MapLocationBounds bounds, int padding, int duration) {
		CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds.getLatLngBounds(), padding);
		updateCamera(update, duration, null);
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
	public void updateCamera(CameraUpdate cameraUpdate, int duration, GxMapViewBase.OnMarkerCameraUpdateCallback callback) {
		if (mMap != null) {
			try {
				if (duration == 0)
					mMap.animateCamera(cameraUpdate, getCallback(callback));
				else
					mMap.animateCamera(cameraUpdate, duration, getCallback(callback));

				return;
			} catch (IllegalStateException e) {
				Services.Log.error(e);
			}
		}

		setPendingUpdate(cameraUpdate);
	}

	@Nullable
	@Override
	public Projection<MapLocation, MapLocationBounds> getProjection(MapLocation initialLocation) {
		if (mMap == null)
			return null;

		com.amap.api.maps.Projection projection = mMap.getProjection();
		CameraPosition cameraPosition = mMap.getCameraPosition();

		MapLocation fromScreenLocation = new MapLocation(0D, 0D);
		if (initialLocation != null) {
			Point centerPoint = projection.toScreenLocation(new LatLng(initialLocation.getLatitude(), initialLocation.getLatitude()));
			centerPoint.y -= (int) (mMapView.getHeight() * MapItemViewHelper.MARKER_INFO_WINDOW_OFF_CENTER_FACTOR);
			fromScreenLocation = new MapLocation(projection.fromScreenLocation(centerPoint));
		}

		MapLocation center = new MapLocation(cameraPosition.target.latitude, cameraPosition.target.longitude);
		MapLocationBounds latLngBounds = new MapLocationBounds(projection.getVisibleRegion().latLngBounds);
		int zoom = (int) cameraPosition.zoom;

		return new Projection<>(latLngBounds, center, fromScreenLocation, zoom);
	}

	private CameraUpdate getCameraUpdate(@NonNull IMapLocation location, int zoom) {
		LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
		CameraUpdate update;
		if (zoom > 0)
			update = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
		else
			update = CameraUpdateFactory.newLatLng(latLng);

		return update;
	}

	private AMap.CancelableCallback getCallback(GxMapViewBase.OnMarkerCameraUpdateCallback callback) {
		return new AMap.CancelableCallback() {
			@Override
			public void onFinish() {
				if (callback != null)
					callback.onFinish();
			}

			@Override
			public void onCancel() {
				if (callback != null)
					callback.onCancel();
			}
		};
	}
}
