package com.genexus.android.core.controls.maps.common

interface IMapViewCamera<CameraUpdate, K : IMapLocation, V : IMapLocationBounds<K>, U : MapItemBase<K>, MapViewData : MapDataBase<U, K, V>> {
    fun update(mapData: MapViewData?, lastMapCenter: K?)
}
