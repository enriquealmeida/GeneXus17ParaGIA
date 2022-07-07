package com.genexus.android.core.externalobjects

import com.genexus.android.core.actions.ActionExecution
import com.genexus.android.core.actions.ApiAction
import com.genexus.android.core.base.metadata.expressions.Expression
import com.genexus.android.core.externalapi.ExternalApi
import com.genexus.android.core.externalapi.ExternalApiResult
import com.genexus.android.remoteconfig.RemoteConfig
import com.genexus.android.remoteconfig.RemoteConfigCompletedListener
import java.lang.Exception

class RemoteConfigAPI(action: ApiAction?) : ExternalApi(action) {

    private var fetchApiAction: ApiAction? = null
    private var applyApiAction: ApiAction? = null

    private val propertyGetLastSuccessfulFetch = IMethodInvoker {
        ExternalApiResult.success(RemoteConfig.lastSuccessfulFetch())
    }

    private val propertyGetLastFetchStatus = IMethodInvoker {
        ExternalApiResult.success(RemoteConfig.lastFetchStatus())
    }

    private val methodFetch = IMethodInvoker {
        fetchApiAction = getAction()
        RemoteConfig.fetch(completedListener)
        ExternalApiResult.SUCCESS_WAIT
    }

    private val methodApply = IMethodInvoker {
        applyApiAction = getAction()
        RemoteConfig.apply(completedListener)
        ExternalApiResult.SUCCESS_WAIT
    }

    private val methodHasValue = IMethodInvoker {
        ExternalApiResult.success(RemoteConfig.hasValue(it[0] as String?))
    }

    private val methodGetString = IMethodInvoker {
        ExternalApiResult.success(RemoteConfig.getStringValue(it[0] as String))
    }

    private val methodGetInteger = IMethodInvoker {
        ExternalApiResult.success(RemoteConfig.getIntegerValue(it[0] as String))
    }

    private val methodGetDecimal = IMethodInvoker {
        ExternalApiResult.success(RemoteConfig.getDecimalValue(it[0] as String))
    }

    private val methodGetBoolean = IMethodInvoker {
        ExternalApiResult.success(RemoteConfig.getBooleanValue(it[0] as String))
    }

    private val methodGetDate = IMethodInvoker {
        ExternalApiResult.success(RemoteConfig.getDateValue(it[0] as String))
    }

    private val methodGetDateTime = IMethodInvoker {
        ExternalApiResult.success(RemoteConfig.getDateTimeValue(it[0] as String))
    }

    private val completedListener: RemoteConfigCompletedListener = object : RemoteConfigCompletedListener {
        override fun onSuccess() {
            finishExecution(true)
        }

        override fun onFailure(exception: Exception?) {
            finishExecution(false)
        }

        private fun finishExecution(success: Boolean) {
            if (applyApiAction != null) {
                continueCurrent(applyApiAction!!, success)
                applyApiAction = null
            } else if (fetchApiAction != null) {
                continueCurrent(fetchApiAction!!, success)
                fetchApiAction = null
            }
        }

        private fun continueCurrent(apiAction: ApiAction, success: Boolean) {
            apiAction.setOutputValue(Expression.Value.newBoolean(success))
            ActionExecution.continueCurrent(activity, true, apiAction)
        }
    }

    init {
        addReadonlyPropertyHandler(PROPERTY_LAST_SUCCESSFUL_FETCH, propertyGetLastSuccessfulFetch)
        addReadonlyPropertyHandler(PROPERTY_LAST_FETCH_STATUS, propertyGetLastFetchStatus)
        addMethodHandler(METHOD_FETCH, 0, methodFetch)
        addMethodHandler(METHOD_APPLY, 0, methodApply)
        addMethodHandler(METHOD_HAS_VALUE, 1, methodHasValue)
        addMethodHandler(METHOD_GET_STRING_VALUE, 1, methodGetString)
        addMethodHandler(METHOD_GET_INTEGER_VALUE, 1, methodGetInteger)
        addMethodHandler(METHOD_GET_DECIMAL_VALUE, 1, methodGetDecimal)
        addMethodHandler(METHOD_GET_BOOLEAN_VALUE, 1, methodGetBoolean)
        addMethodHandler(METHOD_GET_DATE_VALUE, 1, methodGetDate)
        addMethodHandler(METHOD_GET_DATETIME_VALUE, 1, methodGetDateTime)
    }

    companion object {
        const val OBJECT_NAME = "GeneXus.SD.RemoteConfig"
        const val PROPERTY_LAST_SUCCESSFUL_FETCH = "LastSuccessfulFetch"
        const val PROPERTY_LAST_FETCH_STATUS = "LastFetchStatus"
        const val METHOD_FETCH = "Fetch"
        const val METHOD_APPLY = "Apply"
        const val METHOD_HAS_VALUE = "HasValue"
        const val METHOD_GET_STRING_VALUE = "GetStringValue"
        const val METHOD_GET_INTEGER_VALUE = "GetIntegerValue"
        const val METHOD_GET_DECIMAL_VALUE = "GetDecimalValue"
        const val METHOD_GET_BOOLEAN_VALUE = "GetBooleanValue"
        const val METHOD_GET_DATE_VALUE = "GetDateValue"
        const val METHOD_GET_DATETIME_VALUE = "GetDateTimeValue"
    }
}
