package com.genexus.android.superapps

import android.content.Context
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.externalapi.ExternalApiDefinition
import com.genexus.android.core.externalapi.ExternalApiFactory
import com.genexus.android.core.framework.GenexusModule
import com.genexus.android.miniapps.MiniAppAPI

class SuperAppsModule : GenexusModule {

    override fun initialize(context: Context) {
        // SuperApps
        ExternalApiFactory.addApi(ExternalApiDefinition(ProvisioningAPI.NAME, ProvisioningAPI::class.java))
        ExternalApiFactory.addApi(ExternalApiDefinition(MiniAppsAPI.NAME, MiniAppsAPI::class.java))

        // MiniApps
        ExternalApiFactory.addApi(ExternalApiDefinition(MiniAppAPI.NAME, MiniAppAPI::class.java))

        // Service
        Services.Application.servicesLinker.connectSuperApps(SuperAppsHelper(context))
    }
}
