package com.genexus.android.controls.maps.gaode;

import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.controls.maps.common.MapItemBase;
import com.genexus.android.core.controls.maps.common.MapLayer;

import java.util.List;

class GxMapViewItem extends MapItemBase<MapLocation> {
	protected GxMapViewItem(List<MapLocation> locationList, Entity itemData, MapLayer.FeatureType featureType,
	                        String layerId, String themeClassName) {
		super(locationList, itemData, featureType, layerId, themeClassName);
	}
}
