package com.genexus.android.core.controls.maps.common;

import java.util.ArrayList;
import java.util.List;

import android.util.Pair;

import com.genexus.android.layout.DynamicProperties;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.GeoFormats;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controllers.ViewData;
import com.genexus.android.core.controls.maps.GxMapViewDefinition;
import com.genexus.android.core.utils.Cast;

public abstract class MapDataBase<ItemT extends MapItemBase<LocationT>, LocationT extends IMapLocation, BoundsT extends IMapLocationBounds<LocationT>> extends ArrayList<ItemT>
{
	private static final long serialVersionUID = 1L;

	private final MapUtilsBase<LocationT, BoundsT> mMapUtils;
	private LocationT mCustomCenter;
	private Double mZoomRadius;

	protected MapDataBase(ViewData itemsData, MapUtilsBase<LocationT, BoundsT> mapUtils)
	{
		mMapUtils = mapUtils;
		GxMapViewDefinition mapDefinition = mapUtils.getMapDefinition();

		for (Entity itemData : itemsData.getEntities())
		{
			ItemT item = newMapItem(itemData);
			if (item != null)
				add(item);

			if (mapDefinition.getInitialCenter() == GxMapViewDefinition.INITIAL_CENTER_CUSTOM)
				mCustomCenter = newMapLocation(itemData, mapDefinition.getCustomCenterExpression());

			if (mapDefinition.getInitialZoom() == GxMapViewDefinition.INITIAL_ZOOM_RADIUS)
				mZoomRadius = Services.Strings.tryParseDouble(Cast.as(String.class, itemData.getProperty(mapDefinition.getZoomRadiusExpression())));
		}
	}

	public ItemT newMapItem(Entity itemData) {
		GxMapViewDefinition mapDefinition = mMapUtils.getMapDefinition();
		String geolocationExpression = mapDefinition.getGeoLocationExpression();
		if (Strings.hasValue(geolocationExpression)) {
			String geolocation = Cast.as(String.class, itemData.getProperty(geolocationExpression));
			if (Strings.hasValue(geolocation)) {
				String layerId = Strings.EMPTY;
				String layerExpression = mapDefinition.getGeometryLayerExpression();
				if (Strings.hasValue(layerExpression))
					layerId = Cast.as(String.class, itemData.getProperty(layerExpression));

				DynamicProperties dynamicProperties = DynamicProperties.get(itemData);
				String themeClassName = dynamicProperties.getStringProperty(geolocationExpression, "Class");
				MapLayer.FeatureType featureType = GeoFormats.guessFeatureType(geolocation);
				if (featureType != null) {
					switch (featureType) {
						case Point:
							LocationT location = newMapLocation(geolocation);
							if (location != null) {
								List<LocationT> list = new ArrayList<>();
								list.add(location);
								return newMapItem(list, itemData, featureType, layerId, themeClassName);
							}
							break;
						case Polyline:
						case Polygon:
							List<LocationT> locationList = newMapLocationList(featureType, geolocation);
							if (locationList != null)
								return newMapItem(locationList, itemData, featureType, layerId, themeClassName);
					}
				}
			}
		}

		return null;
	}

	private List<LocationT> newMapLocationList(MapLayer.FeatureType featureType, String geolocationList) {
		List<Pair<Double, Double>> geography = null;

		if (featureType == MapLayer.FeatureType.Polyline)
			geography = GeoFormats.parseGeoline(geolocationList);
		else if (featureType == MapLayer.FeatureType.Polygon)
			geography = GeoFormats.parseGeoPolygon(geolocationList);

		return mMapUtils.newMapLocationList(geography);
	}

	private LocationT newMapLocation(Entity itemData, String geolocationExpression)
	{
		if (Strings.hasValue(geolocationExpression))
		{
			String geolocation = Cast.as(String.class, itemData.getProperty(geolocationExpression));
			if (Strings.hasValue(geolocation))
			{
				List<Pair<Double, Double>> coordinates = GeoFormats.tryParse(geolocation);
				if (coordinates != null && coordinates.size() == 1)
					return mMapUtils.newMapLocation(coordinates.get(0).first, coordinates.get(0).second);
			}
		}

		return null;
	}

	private LocationT newMapLocation(String geolocation)
	{
		Pair<Double, Double> coordinates = GeoFormats.parseGeopoint(geolocation);
		if (coordinates == null)
			coordinates = GeoFormats.parseGeolocation(geolocation);

		if (coordinates != null)
			return mMapUtils.newMapLocation(coordinates.first, coordinates.second);

		return null;
	}

	private LocationT newMapLocation(Pair<Double, Double> coordinates)
	{
		if (coordinates!=null)
			return mMapUtils.newMapLocation(coordinates.first, coordinates.second);
		return null;
	}

	protected abstract ItemT newMapItem(List<LocationT> locationList, Entity itemData, MapLayer.FeatureType featureType,
	                                    String layerId, String themeClassName);

	public BoundsT calculateBounds(Pair<Double, Double> currentCenter)
	{
		LocationT currentCenterLocation = newMapLocation(currentCenter);
		return mMapUtils.calculateBounds(currentCenterLocation, getLocations(), mCustomCenter, mZoomRadius);
	}

	public List<LocationT> getLocations()
	{
		ArrayList<LocationT> locations = new ArrayList<>();
		for (MapItemBase<LocationT> item : this)
			locations.addAll(item.getLocationList());
		return locations;
	}

	public LocationT getCustomCenter()
	{
		return mCustomCenter;
	}

	public Double getZoomRadius()
	{
		return mZoomRadius;
	}

}
