package com.genexus.android.controls.maps.hms

import android.app.Activity
import com.genexus.android.core.controls.maps.GxMapViewDefinition
import com.genexus.android.core.controls.maps.common.IGxMapView
import com.genexus.android.core.controls.maps.common.IMapOptions
import com.genexus.android.core.controls.maps.common.IMapViewFactory
import com.genexus.android.core.ui.Coordinator
import com.genexus.controls.maps.hms.R
import com.huawei.hms.maps.HuaweiMapOptions
import com.huawei.hms.maps.MapView

internal class MapViewFactory : IMapViewFactory<MapView>() {
    override fun createGxMapView(activity: Activity, coordinator: Coordinator, definition: GxMapViewDefinition): IGxMapView? {
        return createInstance(activity) { GxMapView(activity, definition, coordinator) }
    }

    override fun createProviderMapView(activity: Activity, options: IMapOptions<*>): MapView? {
        return createInstance(activity) { MapView(activity, options.getInstance() as HuaweiMapOptions) }
    }

    override fun afterAddView(view: IGxMapView) {}

    override fun initializeSDK(activity: Activity): Boolean {
        return HuaweiMapsHelper.checkHuaweiMaps(activity)
    }

    override fun getApiKey(activity: Activity): String {
        return activity.resources.getString(R.string.HuaweiServicesApiKey)
    }
}
