package com.genexus.android.core.controls.maps.common;

import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.utils.Strings;

import java.util.List;

public abstract class MapItemBase<LocationT extends IMapLocation> {
	private final List<LocationT> mLocationList;
	private final Entity mData;
	private final MapLayer.FeatureType mFeatureType;
	private final String mLayerId;
	private final String mThemeClassName;

	protected MapItemBase(List<LocationT> locationList, Entity itemData, MapLayer.FeatureType featureType,
	                      String layerId, String themeClassName) {
		mLocationList = locationList;
		mData = itemData;
		mFeatureType = featureType;
		mLayerId = layerId;
		mThemeClassName = themeClassName == null ? Strings.EMPTY : themeClassName;
	}

	public List<LocationT> getLocationList() {
		return mLocationList;
	}

	public Entity getData() {
		return mData;
	}

	public MapLayer.FeatureType getFeatureType() {
		return mFeatureType;
	}

	public String getLayerId() {
		return mLayerId;
	}

	public String getThemeClassName() {
		return mThemeClassName;
	}
}
