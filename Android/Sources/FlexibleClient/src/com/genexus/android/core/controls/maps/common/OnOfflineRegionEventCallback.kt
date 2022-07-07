package com.genexus.android.core.controls.maps.common

import com.genexus.android.core.base.model.Entity
import com.genexus.android.core.base.model.ValueCollection

interface OnOfflineRegionEventCallback {
    fun regionDownloadedSuccessfully(regionName: String, entity: Entity)
    fun regionDownloadingFailed(regionName: String, entity: Entity)
    fun regionsListed(regionList: ValueCollection)
}
