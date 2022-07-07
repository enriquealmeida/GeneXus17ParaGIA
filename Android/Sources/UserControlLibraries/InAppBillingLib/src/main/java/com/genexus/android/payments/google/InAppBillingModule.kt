package com.genexus.android.payments.google

import android.content.Context
import com.genexus.android.core.externalapi.ExternalApiDefinition
import com.genexus.android.core.externalapi.ExternalApiFactory
import com.genexus.android.core.framework.GenexusModule

class InAppBillingModule : GenexusModule {
    override fun initialize(context: Context) {
        ExternalApiFactory.addApi(
            ExternalApiDefinition(
                StoreManager.OBJECT_NAME,
                StoreManager::class.java
            )
        )
    }
}
