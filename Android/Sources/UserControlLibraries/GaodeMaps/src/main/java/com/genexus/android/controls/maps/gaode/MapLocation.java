package com.genexus.android.controls.maps.gaode;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import com.amap.api.maps.model.LatLng;
import com.genexus.android.core.controls.maps.common.IMapLocation;

class MapLocation implements IMapLocation {
	private final LatLng mLatLng;

	public MapLocation(LatLng latLng) {
		mLatLng = latLng;
	}

	public MapLocation(double latitude, double longitude) {
		this(new LatLng(latitude, longitude));
	}

	public static MapLocation from(LatLng latLng) {
		if (latLng != null)
			return new MapLocation(latLng);
		else
			return null;
	}

	public static List<MapLocation> listFrom(List<Pair<Double, Double>> latLngs) {
		ArrayList<MapLocation> list = new ArrayList<>();
		for (Pair<Double, Double> latLng : latLngs)
			list.add(new MapLocation(new LatLng(latLng.first, latLng.second)));

		return list;
	}

	static List<List<LatLng>> toLatLngLists(List<List<IMapLocation>> list) {
		List<List<LatLng>> latLngLists = new ArrayList<>();
		for (List<IMapLocation> aList : list)
			latLngLists.add(toLatLngList2(aList));

		return latLngLists;
	}

	static List<LatLng> toLatLngList2(List<IMapLocation> locations) {
		ArrayList<LatLng> list = new ArrayList<>();
		for (IMapLocation location : locations)
			list.add(((MapLocation) location).getLatLng());

		return list;
	}

	@Override
	public boolean equals(Object o) {
		if (super.equals(o))
			return true;

		if (!(o instanceof MapLocation))
			return false;

		MapLocation other = (MapLocation) o;
		return mLatLng.equals(other.mLatLng);
	}

	@Override
	public int hashCode() {
		return mLatLng.hashCode();
	}

	@Override
	public String toString() {
		return mLatLng.toString();
	}

	LatLng getLatLng() {
		return mLatLng;
	}

	@Override
	public double getLatitude() {
		return mLatLng.latitude;
	}

	@Override
	public double getLongitude() {
		return mLatLng.longitude;
	}
}
