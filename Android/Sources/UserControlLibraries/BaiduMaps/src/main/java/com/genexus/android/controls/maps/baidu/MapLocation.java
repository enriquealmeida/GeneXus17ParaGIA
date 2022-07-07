package com.genexus.android.controls.maps.baidu;

import android.util.Pair;

import com.genexus.android.core.controls.maps.common.IMapLocation;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

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

	public static List<LatLng> listToLatLng(List<IMapLocation> locations) {
		ArrayList<LatLng> list = new ArrayList<>();
		for (IMapLocation location : locations)
			list.add(((MapLocation) location).getLatLng());

		return list;
	}

	static List<List<LatLng>> toLatLngLists(List<List<IMapLocation>> list) {
		List<List<LatLng>> latLngLists = new ArrayList<>();
		for (List<IMapLocation> aList : list)
			latLngLists.add(listToLatLng(aList));

		return latLngLists;
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
