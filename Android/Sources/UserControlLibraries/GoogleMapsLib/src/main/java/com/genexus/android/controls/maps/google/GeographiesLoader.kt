package com.genexus.android.controls.maps.google

import com.genexus.android.maps.GeographiesServerLoader

internal class GeographiesLoader(geographiesManager: GeographiesManager) :
    GeographiesServerLoader<MapLocation, MapLocationBounds, GxMapViewItem, GxMapViewData>(geographiesManager)
