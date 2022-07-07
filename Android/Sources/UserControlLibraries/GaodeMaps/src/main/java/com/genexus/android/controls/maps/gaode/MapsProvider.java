package com.genexus.android.controls.maps.gaode;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controllers.ViewData;
import com.genexus.android.core.controls.maps.GxMapViewDefinition;
import com.genexus.android.core.controls.maps.common.IMapFeatureFactory;
import com.genexus.android.core.controls.maps.common.IMapLocation;
import com.genexus.android.core.controls.maps.common.IMapViewFactory;
import com.genexus.android.core.controls.maps.common.IMapsProvider;
import com.genexus.android.core.controls.maps.common.MapDataBase;

import java.util.List;

import static com.genexus.android.controls.maps.gaode.GaodeMapsHelper.getUrl;

public class MapsProvider implements IMapsProvider {

	public static final String PROVIDER_ID_DIRECTIONS = "Gaode";
	private static final String PROVIDER_ID = "MAPS_GAODE";

	@Override
	public String getId() {
		return PROVIDER_ID;
	}

	@Override
	public IMapViewFactory<MapView> getMapViewFactory() {
		return new MapViewFactory();
	}

	@Override
	public IMapFeatureFactory<MapView, AMap> getMapFeatureFactory() {
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
	public Entity calculateDirections(Activity activity, String origin, String destination, List<String> waypoints, String transportType, Boolean requestAlternatives) {
		Services.Log.debug("Directions API not implemented");
		return EntityFactory.newEntity();
	}

	@Override
	public IMapLocation newMapLocation(double latitude, double longitude) {
		return new MapLocation(latitude, longitude);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public MapDataBase newMapData(GxMapViewDefinition definition, ViewData viewData) {
		return new GxMapViewData(definition, viewData);
	}
}
