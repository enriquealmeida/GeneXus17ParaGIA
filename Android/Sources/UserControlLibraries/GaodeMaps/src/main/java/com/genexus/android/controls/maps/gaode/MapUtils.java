package com.genexus.android.controls.maps.gaode;

import android.util.Pair;

import java.util.List;

import com.amap.api.maps.model.LatLngBounds;
import com.genexus.android.core.controls.maps.GxMapViewDefinition;
import com.genexus.android.core.controls.maps.common.MapUtilsBase;

class MapUtils extends MapUtilsBase<MapLocation, MapLocationBounds> {
	protected MapUtils(GxMapViewDefinition definition) {
		super(definition);
	}

	@Override
	protected List<MapLocation> newMapLocationList(List<Pair<Double, Double>> geography) {
		return MapLocation.listFrom(geography);
	}

	@Override
	protected MapLocation newMapLocation(double latitude, double longitude) {
		return new MapLocation(latitude, longitude);
	}

	@Override
	public MapLocationBounds newMapBounds(List<MapLocation> locations) {
		// Override to use LatLngBounds implementation directly.
		LatLngBounds.Builder builder = LatLngBounds.builder();
		for (MapLocation location : locations)
			builder.include(location.getLatLng());

		return new MapLocationBounds(builder.build());
	}

	@Override
	protected MapLocationBounds newMapBounds(MapLocation southwest, MapLocation northeast) {
		LatLngBounds latLngBounds = new LatLngBounds(southwest.getLatLng(), northeast.getLatLng());
		return new MapLocationBounds(latLngBounds);
	}
}
