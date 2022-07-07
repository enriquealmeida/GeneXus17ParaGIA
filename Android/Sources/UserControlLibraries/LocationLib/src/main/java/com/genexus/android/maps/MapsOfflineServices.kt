package com.genexus.android.maps

import android.content.Context
import com.genexus.android.core.base.services.IMaps
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.controls.maps.common.IMapsProvider
import com.genexus.android.core.controls.maps.common.IOfflineRegionManager

class MapsOfflineServices(private val appContext: Context?) : IMaps.Offline {
    override fun getInstance(): IOfflineRegionManager? {
        val offlineProvider = Services.Maps.getProvider() as? IMapsProvider.Offline ?: return null
        return offlineProvider.getInstance(appContext)
    }

    override fun isOfflineGeographicDataSupported(): Boolean {
        val provider = Services.Maps.getProvider() as? IMapsProvider.Offline
        return provider?.isOfflineGeographicDataSupported ?: false
    }
}
