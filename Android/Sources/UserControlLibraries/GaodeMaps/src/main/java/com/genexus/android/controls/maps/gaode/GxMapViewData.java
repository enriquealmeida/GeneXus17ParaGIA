package com.genexus.android.controls.maps.gaode;

import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.controllers.ViewData;
import com.genexus.android.core.controls.maps.GxMapViewDefinition;
import com.genexus.android.core.controls.maps.common.MapDataBase;
import com.genexus.android.core.controls.maps.common.MapLayer;

import java.util.List;

class GxMapViewData extends MapDataBase<GxMapViewItem, MapLocation, MapLocationBounds> {
	private static final long serialVersionUID = 1L;

	public GxMapViewData(GxMapViewDefinition mapDefinition, ViewData itemsData) {
		super(itemsData, new MapUtils(mapDefinition));
	}

	@Override
	protected GxMapViewItem newMapItem(List<MapLocation> locationList, Entity itemData, MapLayer.FeatureType featureType,
	                                   String layerId, String themeClassName) {
		return new GxMapViewItem(locationList, itemData, featureType, layerId, themeClassName);
	}
}
