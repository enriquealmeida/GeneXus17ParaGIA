package com.genexus.android.ads

import android.content.Context
import com.genexus.android.core.externalapi.ExternalApiDefinition
import com.genexus.android.core.externalapi.ExternalApiFactory
import com.genexus.android.core.framework.GenexusModule

class AdsModule : GenexusModule {
    override fun initialize(context: Context) {
        ExternalApiFactory.addApi(ExternalApiDefinition(AdsAPI.OBJECT_NAME, AdsAPI::class.java))
    }
}
