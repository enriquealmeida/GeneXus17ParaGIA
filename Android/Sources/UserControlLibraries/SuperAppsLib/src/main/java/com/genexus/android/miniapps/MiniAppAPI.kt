package com.genexus.android.miniapps

import com.genexus.android.core.actions.ApiAction
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.externalapi.ExternalApi.IMethodInvoker
import com.genexus.android.core.externalapi.ExternalApiResult
import com.genexus.android.core.externalapi.superapps.MiniAppExternalApi

class MiniAppAPI(action: ApiAction?) : MiniAppExternalApi(action) {

    private val mExitHandler = IMethodInvoker {
        Services.SuperApps.exit(activity)
        ExternalApiResult.SUCCESS_CONTINUE
    }

    companion object {
        const val NAME = "GeneXusMiniApps.MiniApp"
        private const val METHOD_NAME_EXIT = "Exit"
    }

    init {
        addMethodHandler(METHOD_NAME_EXIT, 0, mExitHandler)
    }
}
