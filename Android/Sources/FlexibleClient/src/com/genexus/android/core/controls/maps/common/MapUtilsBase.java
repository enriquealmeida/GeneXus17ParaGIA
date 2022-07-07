package com.genexus.android.core.controls.maps.common;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;
import android.util.Pair;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controls.maps.GxMapViewDefinition;

public abstract class MapUtilsBase<LocationT extends IMapLocation, BoundsT extends IMapLocationBounds<LocationT>>
{
	private final GxMapViewDefinition mDefinition;

	private static final double DEFAULT_RADIUS_KM = 0.3;
	
	protected MapUtilsBase(GxMapViewDefinition definition)
	{
		mDefinition = definition;
	}

	public GxMapViewDefinition getMapDefinition()
	{
		return mDefinition;
	}

	protected abstract List<LocationT> newMapLocationList(List<Pair<Double, Double>> geography);
	protected abstract LocationT newMapLocation(double latitude, double longitude);
	protected abstract BoundsT newMapBounds(LocationT southwest, LocationT northeast);

	public BoundsT calculateBounds(LocationT currentCenter, List<LocationT> points, LocationT customCenter, Double customZoomRadius)
	{
		BoundsT box;
		LocationT myLocation = null;

		if (mDefinition.needsUserLocationForMapBounds() || mDefinition.getShowMyLocation()) {
			if (Services.Location != null)
				myLocation = newMapLocation(Services.Location.getLastKnownLocation());
			else
				Services.Log.error("Location Service isn't initialized for retrieving last known location");
		}

		LocationT boxCenter = null;
		ArrayList<LocationT> boxPoints = new ArrayList<>();
		Double boxRadius = null;

		// Restrict center choice if we don't have a location.
		int initialCenter = mDefinition.getInitialCenter();
		if (initialCenter == GxMapViewDefinition.INITIAL_CENTER_MY_LOCATION && myLocation == null)
			initialCenter = GxMapViewDefinition.INITIAL_CENTER_DEFAULT;

		// Restrict zoom choice if we don't have a location.
		int initialZoom = mDefinition.getInitialZoom();
		if (initialZoom == GxMapViewDefinition.INITIAL_ZOOM_NEAREST_POINT && myLocation == null)
			initialZoom = GxMapViewDefinition.INITIAL_ZOOM_DEFAULT;

		// Center choices: myself, custom, or none (determined by points to show).
		if (initialCenter == GxMapViewDefinition.INITIAL_CENTER_MY_LOCATION)
			boxCenter = myLocation;
		else if (initialCenter == GxMapViewDefinition.INITIAL_CENTER_CUSTOM && customCenter != null)
			boxCenter = customCenter;

		// Zoom choices: default (show all points), nearest (show me and nearest point), radius (from center).
		if (initialZoom == GxMapViewDefinition.INITIAL_ZOOM_DEFAULT)
			boxPoints.addAll(points);
		else if (initialZoom == GxMapViewDefinition.INITIAL_ZOOM_RADIUS && boxCenter==null)
		{
			// if initial zoom radius and no center, use all point as center
			if (points.size()>0)
				boxPoints.addAll(points);
			else if (currentCenter!=null)  // if no points , use current center as center
				boxPoints.add(currentCenter);
		}
		else if (initialZoom == GxMapViewDefinition.INITIAL_ZOOM_NEAREST_POINT)
		{
			LocationT nearest = getNearest(myLocation, points);
			if (nearest != null) // Can be null if points is empty.
				boxPoints.add(getNearest(myLocation, points));
		}

		if (myLocation != null)
			boxPoints.add(myLocation);

		if (mDefinition.getInitialZoom() == GxMapViewDefinition.INITIAL_ZOOM_RADIUS && customZoomRadius != null)
			boxRadius = (customZoomRadius / 1000.0); // customZoomRadius is in meters.

		if (boxCenter != null)
		{
			if (boxPoints.size() != 0)
				box = getBoundingBox(boxCenter, boxPoints);
			else
				box = getBoundingBox(boxCenter);
		}
		else
		{
			if (boxPoints.size() != 0)
				box = newMapBounds(boxPoints);
			else
				box = null;
		}

		if (box != null && boxRadius != null)
			box = getBoundingBox(getCenterOf(box), customZoomRadius / 1000.0); // customZoomRadius in M.

		// Try to avoid creating a "0-sized" box (which would be shown with a HUGE magnification).
		if (box != null && box.southwest().equals(box.northeast()))
			box = getBoundingBox(box.southwest(), DEFAULT_RADIUS_KM);

		return box;
	}

	private LocationT newMapLocation(Location location)
	{
		if (location != null)
			return newMapLocation(location.getLatitude(), location.getLongitude());
		else
			return null;
	}

	public BoundsT newMapBounds(List<LocationT> locations)
	{
		// The latitude is clamped between -80 degrees and +80 degrees inclusive.
		double minLatitude = +81;
		double maxLatitude = -81;

		// The longitude is clamped between -180 degrees and +180 degrees inclusive.
		double minLongitude = +181;
		double maxLongitude = -181;

		for (LocationT location : locations)
    	{
			double lat = location.getLatitude();
			minLatitude = (minLatitude > lat) ? lat : minLatitude;
			maxLatitude = (maxLatitude < lat) ? lat : maxLatitude;

			double lon = location.getLongitude();
			minLongitude = (minLongitude > lon) ? lon : minLongitude;
			maxLongitude = (maxLongitude < lon) ? lon : maxLongitude;
    	}

		LocationT southwest = newMapLocation(minLatitude, minLongitude);
		LocationT northeast = newMapLocation(maxLatitude, maxLongitude);

		return newMapBounds(southwest, northeast);
	}

	private double distanceBetween(LocationT point1, LocationT point2)
	{
		float[] results = new float[1];
		Location.distanceBetween(point1.getLatitude(), point1.getLongitude(), point2.getLatitude(), point2.getLongitude(), results);
		return results[0];
	}

	private LocationT getNearest(LocationT from, List<LocationT> points)
	{
		LocationT nearest = null;
		double nearestDistance = Double.MAX_VALUE;

		for (LocationT point : points)
		{
			double pointDistance = distanceBetween(from, point);
			if (pointDistance < nearestDistance)
			{
				nearest = point;
				nearestDistance = pointDistance;
			}
		}

		return nearest;
	}

	private BoundsT getBoundingBox(LocationT center)
	{
		ArrayList<LocationT> points = new ArrayList<>();
		points.add(center);
		return newMapBounds(points);
	}

	private BoundsT getBoundingBox(LocationT center, List<LocationT> points)
	{
		// Get the box that contains all points.
		BoundsT box = newMapBounds(points);

		// Extend it so that it is centered where desired.
		return extendBoxToCenterOn(box, center);
	}

	private BoundsT extendBoxToCenterOn(BoundsT box, LocationT center)
	{
		Pair<Double, Double> latitudes = extendSegmentToCenterAround(box.southwest().getLatitude(), box.northeast().getLatitude(), center.getLatitude());
		Pair<Double, Double> longitudes = extendSegmentToCenterAround(box.southwest().getLongitude(), box.northeast().getLongitude(), center.getLongitude());

		LocationT southwest = newMapLocation(latitudes.first, longitudes.first);
		LocationT northeast = newMapLocation(latitudes.second, longitudes.second);

		return newMapBounds(southwest, northeast);
	}

	private static Pair<Double, Double> extendSegmentToCenterAround(double start, double end, double center)
	{
		if (center < start)
			start = center - (end - center); // Move start to the left, so that it surpasses center and stands at the same distance as center does from end.
		else if (center > end)
			end = center + (center - start); // Move end to the right, so that it surpasses center and stands at the same distance as center does from start.
		else
		{
			// Center is already between start and end. Push the *closest* one from center further along.
			if ((end - center) < (center - start))
				end = center + (center - start);
			else
				start = center - (end - center);
		}

		return new Pair<>(start, end);
	}

	public BoundsT getDefaultBoundingBox(LocationT center)
	{
		return getBoundingBox(center, DEFAULT_RADIUS_KM);
	}
	
	public BoundsT getBoundingBox(LocationT center, double radiusKm)
	{
		ArrayList<LocationT> centerAndCardinals = new ArrayList<>();
		centerAndCardinals.add(center);

		centerAndCardinals.add(getPointAtDistanceAndBearing(center, radiusKm, 0)); // North
		centerAndCardinals.add(getPointAtDistanceAndBearing(center, radiusKm, Math.PI * 0.5)); // East
		centerAndCardinals.add(getPointAtDistanceAndBearing(center, radiusKm, Math.PI * 1.0)); // South
		centerAndCardinals.add(getPointAtDistanceAndBearing(center, radiusKm, Math.PI * 1.5)); // West

		return newMapBounds(centerAndCardinals);
	}

	private LocationT getPointAtDistanceAndBearing(LocationT from, double distanceKm, double bearingRadians)
	{
	    final double EARTH_RADIUS_KM = 6371.01;
	    double distRatio = distanceKm / EARTH_RADIUS_KM;
	    double distRatioSine = Math.sin(distRatio);
	    double distRatioCosine = Math.cos(distRatio);

	    double startLatRad = Math.toRadians(from.getLatitude());
	    double startLonRad = Math.toRadians(from.getLongitude());

	    double startLatCos = Math.cos(startLatRad);
	    double startLatSin = Math.sin(startLatRad);

	    double endLatRads = Math.asin((startLatSin * distRatioCosine) + (startLatCos * distRatioSine * Math.cos(bearingRadians)));

	    double endLonRads = startLonRad + Math.atan2(Math.sin(bearingRadians) * distRatioSine * startLatCos,
	            distRatioCosine - startLatSin * Math.sin(endLatRads));

	    return newMapLocation(Math.toDegrees(endLatRads), Math.toDegrees(endLonRads));
	}

	private LocationT getCenterOf(BoundsT box)
	{
		double centerLatitude = (box.southwest().getLatitude() + box.northeast().getLatitude()) / 2.0;
		double centerLongitude = (box.southwest().getLongitude() + box.northeast().getLongitude()) / 2.0;

		return newMapLocation(centerLatitude, centerLongitude);
	}
}
