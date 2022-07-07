package com.genexus.android.controls.maps.hms

import android.content.Context
import com.genexus.android.core.base.services.OnMapsConnectedCallback
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.framework.GenexusModule

class HuaweiMapsModule : GenexusModule, OnMapsConnectedCallback {
    override fun initialize(context: Context) {
        if (HuaweiMapsHelper.checkHuaweiMaps(context))
            Services.Application.servicesLinker.registerMapsConnectedCallback(this)
    }

    override fun onMapsConnected() {
        // Add this provider only if HMS is available
        Services.Maps.addProvider(MapsProvider())
        // set this as Default map provider in runtime
        Services.Maps.setProviderIdOverride(MapsProvider().id)
    }
}
