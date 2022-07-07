package com.genexus.android.core.externalobjects

import com.genexus.android.core.actions.ApiAction
import com.genexus.android.core.base.utils.Strings
import com.genexus.android.core.externalapi.ExternalApi
import com.genexus.android.core.externalapi.ExternalApi.IMethodInvoker
import com.genexus.android.core.externalapi.ExternalApiResult
import com.genexus.android.core.ui.navigation.CallOptionsHelper

class DynamicCallAPI(action: ApiAction?) : ExternalApi(action) {
    private val methodSetOption = IMethodInvoker { parameters ->
        val objectName = getObjectName(parameters[0].toString())
        val callOption = parameters[1].toString()
        val value = parameters[2].toString()
        CallOptionsHelper.setCallOption(objectName, callOption, value)
        ExternalApiResult.SUCCESS_CONTINUE
    }

    init {
        addMethodHandler(METHOD_SET_OPTION, 3, methodSetOption)
    }

    companion object {
        const val OBJECT_NAME = "GeneXus.Common.DynamicCall"
        const val METHOD_SET_OPTION = "SetOption"

        fun getObjectName(callName: String): String {
            // detect if it is a link and convert it to object name
            if (Strings.starsWithIgnoreCase(callName, "sd:")) {
                var objectName = callName.substring(3)
                val index = objectName.indexOf("?")
                if (index >= 0)
                    objectName = objectName.substring(0, index)
                return objectName
            }
            return callName
        }
    }
}
