package com.genexus.android.ar

import android.content.Context
import com.genexus.android.core.externalapi.ExternalApiDefinition
import com.genexus.android.core.externalapi.ExternalApiFactory
import com.genexus.android.core.framework.GenexusModule

const val TAG = "GxAugmentedReality"

class AugmentedRealityModule : GenexusModule {

    override fun initialize(context: Context) {
        val arExternalObject = ExternalApiDefinition(
            AugmentedRealityExternalObject.NAME,
            AugmentedRealityExternalObject::class.java
        )
        ExternalApiFactory.addApi(arExternalObject)
    }
}
