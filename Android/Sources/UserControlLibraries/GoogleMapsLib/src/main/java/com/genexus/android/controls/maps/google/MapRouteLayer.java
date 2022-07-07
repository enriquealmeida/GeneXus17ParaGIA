package com.genexus.android.controls.maps.google;

import android.app.Activity;

import com.genexus.android.maps.DirectionsServiceRequest;
import com.genexus.android.core.controls.maps.common.IGxMapViewSupportLayers;

import static com.genexus.android.controls.maps.google.MapsProvider.PROVIDER_ID_DIRECTIONS;

public class MapRouteLayer extends DirectionsServiceRequest<MapLocation, MapLocationBounds, GxMapViewItem, GxMapViewData> {
	public MapRouteLayer(IGxMapViewSupportLayers gxMapView, Activity activity) {
		super(gxMapView, activity, null, PROVIDER_ID_DIRECTIONS);
	}
}
