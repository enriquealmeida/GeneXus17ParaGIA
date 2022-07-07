package com.genexus.android.core.controls.maps.common;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import java.util.List;

import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.controllers.ViewData;
import com.genexus.android.core.controls.maps.GxMapViewDefinition;

@SuppressWarnings("rawtypes")
public interface IMapsProvider {
	String getId();

	IMapViewFactory getMapViewFactory();
	IMapFeatureFactory getMapFeatureFactory();
	Class<? extends Activity> getLocationPickerActivityClass();
	String getMapImageUrl(String location, int width, int height, String mapType, int zoom, String language);
	Entity calculateDirections(Activity activity, String origin, String destination, List<String> waypoints, String transportType, Boolean requestAlternatives);
	IMapLocation newMapLocation(double latitude, double longitude);
	MapDataBase newMapData(GxMapViewDefinition definition, ViewData viewData);

	View getMapLiteView(Context context, String location, String mapType, int zoom);

	interface Offline {
		IOfflineRegionManager getInstance(Context context);
		boolean isOfflineGeographicDataSupported();
	}
}
