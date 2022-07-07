package com.genexus.android.controls.maps.google;

import android.util.Pair;

import com.genexus.android.core.base.utils.GeoFormats;
import com.genexus.android.core.controls.maps.GxMapViewDefinition;
import com.genexus.android.core.controls.maps.common.MapUtilsBase;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;

class MapUtils extends MapUtilsBase<MapLocation, MapLocationBounds> {

	protected MapUtils(GxMapViewDefinition definition) {
		super(definition);
	}

	@Override
	protected List<MapLocation> newMapLocationList(List<Pair<Double, Double>> geography) {
		return MapLocation.listFromDoublePair(geography);
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

	/**
	 * Decodes a {@link LatLng} instance from its string representation.
	 */
	public static LatLng stringToLatLng(String str) {
		Pair<Double, Double> coordinates = GeoFormats.parseGeolocation(str);
		if (coordinates != null)
			return new LatLng(coordinates.first, coordinates.second);
		else
			return null;
	}

	static LatLng calculateCenter(List<LatLng> points) {
		double centroidX = 0;
		double centroidY = 0;

		for (LatLng latLng : points) {
			centroidX += latLng.latitude;
			centroidY += latLng.longitude;
		}

		return new LatLng(centroidX / points.size(), centroidY / points.size());
	}
}
