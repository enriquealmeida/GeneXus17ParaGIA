package com.genexus.android.controls.maps.google;

import com.genexus.android.core.controls.maps.common.IMapLocationBounds;
import com.google.android.gms.maps.model.LatLngBounds;

class MapLocationBounds implements IMapLocationBounds<MapLocation>
{
	private final LatLngBounds mBounds;

	public MapLocationBounds(LatLngBounds bounds)
	{
		mBounds = bounds;
	}

	LatLngBounds getLatLngBounds()
	{
		return mBounds;
	}

	@Override
	public MapLocation southwest()
	{
		return new MapLocation(mBounds.southwest);
	}

	@Override
	public MapLocation northeast()
	{
		return new MapLocation(mBounds.northeast);
	}
}
