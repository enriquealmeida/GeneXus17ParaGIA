package com.genexus.android.core.externalobjects

import com.genexus.android.ApiAuthorizationStatus
import com.genexus.android.WithPermission
import com.genexus.android.core.actions.ActionExecution
import com.genexus.android.core.actions.ApiAction
import com.genexus.android.core.base.metadata.expressions.Expression
import com.genexus.android.core.base.model.ValueCollection
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.externalapi.ExternalApi
import com.genexus.android.core.externalapi.ExternalApi.IMethodInvoker
import com.genexus.android.core.externalapi.ExternalApiResult
import com.genexus.android.core.externalobjects.permissions.Permissions

class PermissionsAPI(apiAction: ApiAction) : ExternalApi(apiAction) {

    private val requestHandler = IMethodInvoker {
        val permission = Permissions(it[0] as String)
        var rationale: String? = null
        if (it.size > 1)
            rationale = it[1] as String

        request(permission, rationale)
    }

    private val requestManyHandler = IMethodInvoker {
        request(Permissions(it[0] as ValueCollection), null)
    }

    private val statusHandler = IMethodInvoker {
        val permission = Permissions(it[0] as String).toTypedArray()
        ExternalApiResult.success(ApiAuthorizationStatus.getStatus(context, permission))
    }

    private fun request(permissions: Permissions, rationale: String?): ExternalApiResult {
        Services.Device.runOnUiThread {
            val permissionBuilder = WithPermission.Builder<Void>(activity)
                .require(permissions.toTypedArray())
                .attachToActivityController()
                .setRequestCode(6373)
                .onSuccess(successRunnable)
                .onFailure(failedRunnable)

            rationale?.let { permissionBuilder.setRationale(it) }
            permissionBuilder.build().run()
        }

        return ExternalApiResult.SUCCESS_WAIT
    }

    private val successRunnable = Runnable {
        continueCurrent(true)
    }

    private val failedRunnable = Runnable {
        continueCurrent(false)
    }

    private fun continueCurrent(success: Boolean) {
        action?.let {
            it.setOutputValue(Expression.Value.newBoolean(success))
            ActionExecution.continueCurrent(it.activity, true, it)
        }
    }

    companion object {
        const val NAME = "Genexus.SD.Permissions"
        private const val METHOD_REQUEST = "Request"
        private const val METHOD_REQUEST_MANY = "RequestMany"
        private const val METHOD_STATUS = "GetStatus"
    }

    init {
        addMethodHandler(METHOD_REQUEST, 1, requestHandler)
        addMethodHandler(METHOD_REQUEST, 2, requestHandler)
        addMethodHandler(METHOD_REQUEST_MANY, 1, requestManyHandler)
        addMethodHandler(METHOD_STATUS, 1, statusHandler)
    }
}
