package com.genexus.android.controls.maps.mapbox

import android.content.Context
import com.genexus.android.core.base.services.OnMapsConnectedCallback
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.framework.GenexusModule

class MapboxModule : GenexusModule, OnMapsConnectedCallback {
    override fun initialize(context: Context) {
        Services.Application.servicesLinker.registerMapsConnectedCallback(this)
    }

    override fun onMapsConnected() {
        Services.Maps.addProvider(MapsProvider())
    }
}
