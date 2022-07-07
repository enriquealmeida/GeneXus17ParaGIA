package com.genexus.android.controls.maps.google;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.controllers.ViewData;
import com.genexus.android.core.controls.maps.GxMapViewDefinition;
import com.genexus.android.core.controls.maps.common.IMapFeatureFactory;
import com.genexus.android.core.controls.maps.common.IMapLocation;
import com.genexus.android.core.controls.maps.common.IMapViewFactory;
import com.genexus.android.core.controls.maps.common.IMapsProvider;
import com.genexus.android.core.controls.maps.common.IOfflineRegionManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

import java.util.List;

import static com.genexus.android.controls.maps.google.GoogleMapsHelper.getUrl;

public class MapsProvider implements IMapsProvider, IMapsProvider.Offline {

	public static final String PROVIDER_ID_DIRECTIONS = "Google";
	private static final String PROVIDER_ID = "MAPS_GOOGLE_V2";

	@Override
	public String getId() {
		return PROVIDER_ID;
	}

	@Override
	public IMapViewFactory<MapView> getMapViewFactory() {
		return new MapViewFactory();
	}

	@Override
	public IMapFeatureFactory<MapView, GoogleMap> getMapFeatureFactory() {
		return new MapFeatureFactory();
	}

	@Override
	public Class<? extends Activity> getLocationPickerActivityClass() {
		return LocationPickerActivity.class;
	}

	@Override
	public String getMapImageUrl(String location, int width, int height, String mapType, int zoom, String language) {
		return getUrl(location, width, height, mapType, zoom, language);
	}

	@Override
	public View getMapLiteView(Context context, String location, String mapType, int zoom) {
		return null;
	}

	@Override
	public Entity calculateDirections(Activity activity, String origin, String destination, List<String> waypoints,
	                                  String transportType, Boolean requestAlternatives) {
		if (origin != null && destination != null)
			return new MapRouteLayer(null, activity).calculateRoute(origin, destination,
					waypoints, transportType, requestAlternatives, false);

		return EntityFactory.newEntity();
	}

	@Override
	public IMapLocation newMapLocation(double latitude, double longitude) {
		return new MapLocation(latitude, longitude);
	}

	@Override
	public GxMapViewData newMapData(GxMapViewDefinition definition, ViewData viewData) {
		return new GxMapViewData(definition, viewData);
	}

	@Override
	public IOfflineRegionManager getInstance(Context context) {
		return null;
	}

	@Override
	public boolean isOfflineGeographicDataSupported() {
		return false;
	}
}
