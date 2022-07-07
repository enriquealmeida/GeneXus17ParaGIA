package com.genexus.android.controls.maps.baidu;

import android.app.Activity;
import android.content.Context;
import android.util.Pair;
import android.view.View;

import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.GeoFormats;
import com.genexus.android.core.controllers.ViewData;
import com.genexus.android.core.controls.maps.GxMapViewDefinition;
import com.genexus.android.core.controls.maps.common.IMapFeatureFactory;
import com.genexus.android.core.controls.maps.common.IMapLocation;
import com.genexus.android.core.controls.maps.common.IMapViewFactory;
import com.genexus.android.core.controls.maps.common.IMapsProvider;
import com.genexus.android.core.controls.maps.common.MapDataBase;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;

import java.util.List;

public class MapsProvider implements IMapsProvider {

	public static final String PROVIDER_ID_DIRECTIONS = "Baidu";
	private static final String PROVIDER_ID = "MAPS_BAIDU";

	@Override
	public String getId() {
		return PROVIDER_ID;
	}

	@Override
	public IMapViewFactory<MapView> getMapViewFactory() {
		return new MapViewFactory();
	}

	@Override
	public IMapFeatureFactory<MapView, BaiduMap> getMapFeatureFactory() {
		return new MapFeatureFactory();
	}

	@Override
	public Class<? extends Activity> getLocationPickerActivityClass() {
		return LocationPickerActivity.class;
	}

	@Override
	public String getMapImageUrl(String location, int width, int height, String mapType, int zoom, String language) {
		final String URL_FORMAT = "https://api.map.baidu.com/staticimage?markers=%s,%s&width=%s&height=%s&zoom=%s";

		//Baidu valid zoom values are between 3 and 19. Use default 15 if out of that range
		if (zoom < 3 || zoom > 19) {
			zoom = 15;
			Services.Log.debug("Using default '15' zoom value because parameter value was out of range");
		}

		// Baidu expects <longitude,latitude> instead of <latitude,longitude>.
		Pair<Double, Double> latlon = GeoFormats.parseGeolocation(location);
		if (latlon != null)
			return String.format(URL_FORMAT, latlon.second, latlon.first, width, height, zoom);
		else
			return null;
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
