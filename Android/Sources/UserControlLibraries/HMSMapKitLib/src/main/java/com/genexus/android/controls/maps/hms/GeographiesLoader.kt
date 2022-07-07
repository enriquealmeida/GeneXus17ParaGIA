package com.genexus.android.controls.maps.hms

import com.genexus.android.maps.GeographiesServerLoader

internal class GeographiesLoader(geographiesManager: GeographiesManager) :
    GeographiesServerLoader<MapLocation, MapLocationBounds, GxMapViewItem, GxMapViewData>(geographiesManager)
