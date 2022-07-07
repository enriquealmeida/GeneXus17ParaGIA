package com.genexus.android.core.controls.maps.common;

public interface IMapLocationBounds<LocationT extends IMapLocation>
{
	LocationT southwest();
	LocationT northeast();
}
