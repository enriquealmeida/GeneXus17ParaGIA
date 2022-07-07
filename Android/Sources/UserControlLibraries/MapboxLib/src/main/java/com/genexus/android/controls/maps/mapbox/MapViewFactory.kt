package com.genexus.android.controls.maps.mapbox

import android.app.Activity
import com.genexus.android.core.controls.maps.GxMapViewDefinition
import com.genexus.android.core.controls.maps.common.IGxMapView
import com.genexus.android.core.controls.maps.common.IMapOptions
import com.genexus.android.core.controls.maps.common.IMapViewFactory
import com.genexus.android.core.ui.Coordinator
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMapOptions

internal class MapViewFactory : IMapViewFactory<MapView>() {
    override fun createGxMapView(activity: Activity, coordinator: Coordinator, definition: GxMapViewDefinition): IGxMapView? {
        return createInstance(activity) { GxMapView(activity, coordinator, definition) }
    }

    override fun createProviderMapView(activity: Activity, options: IMapOptions<*>): MapView? {
        return createInstance(activity) { MapView(activity, options.getInstance() as MapboxMapOptions) }
    }

    override fun afterAddView(view: IGxMapView) {}

    override fun initializeSDK(activity: Activity): Boolean {
        if (!MapboxHelper.checkMapbox(activity))
            return false

        Mapbox.getInstance(activity, getApiKey(activity))
        return true
    }

    override fun getApiKey(activity: Activity): String {
        return activity.resources.getString(R.string.MapsApiKey)
    }
}
