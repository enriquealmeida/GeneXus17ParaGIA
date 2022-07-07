package com.genexus.android.controls.maps.baidu

import android.app.Activity
import com.genexus.android.controls.maps.baidu.MapsProvider.PROVIDER_ID_DIRECTIONS
import com.genexus.android.core.controls.maps.common.IGxMapViewSupportLayers
import com.genexus.android.maps.DirectionsServiceRequest

internal class MapRouteLayer(mapView: IGxMapViewSupportLayers?, activity: Activity) :
    DirectionsServiceRequest<MapLocation, MapLocationBounds, GxMapViewItem, GxMapViewData>
    (mapView, activity, null, PROVIDER_ID_DIRECTIONS)
