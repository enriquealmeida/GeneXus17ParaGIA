package com.genexus.android.core.controls.maps.common

interface IGeographiesServerLoader<MapViewData> {
    fun update(mapData: MapViewData?, useAnimation: Boolean)
    fun setGeographiesLoadedCallback(geographiesLoadedCallback: OnGeographiesLoadedCallback)
}
