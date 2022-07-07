package com.genexus.android.core.externalobjects

import com.genexus.android.core.actions.ApiAction
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.externalapi.ExternalApi
import com.genexus.android.core.externalapi.ExternalApi.IMethodInvoker
import com.genexus.android.core.externalapi.ExternalApiResult

class DesignSystemAPI(action: ApiAction?) : ExternalApi(action) {
    private val methodSetOption = IMethodInvoker { parameters ->
        val name = parameters[0].toString()
        val value = parameters[1].toString()
        Services.Application.definition.setOption(name, value)
        Services.Themes.applyCurrentThemeForced(activity)
        ExternalApiResult.SUCCESS_CONTINUE
    }

    private val methodClearOption = IMethodInvoker { parameters ->
        val name = parameters[0].toString()
        Services.Application.definition.clearOption(name)
        Services.Themes.applyCurrentThemeForced(activity)
        ExternalApiResult.SUCCESS_CONTINUE
    }

    init {
        addMethodHandler(METHOD_SET_OPTION, 2, methodSetOption)
        addMethodHandler(METHOD_CLEAR_OPTION, 1, methodClearOption)
    }

    companion object {
        const val OBJECT_NAME = "GeneXus.Common.UI.DesignSystem"
        const val METHOD_SET_OPTION = "SetOption"
        const val METHOD_CLEAR_OPTION = "ClearOption"
    }
}
