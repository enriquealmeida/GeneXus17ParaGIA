package com.genexus.android.superapps

import com.genexus.android.core.actions.ActionExecution
import com.genexus.android.core.actions.ApiAction
import com.genexus.android.core.base.model.Entity
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.externalapi.ExternalApi.IMethodInvoker
import com.genexus.android.core.externalapi.ExternalApiResult
import com.genexus.android.core.externalapi.superapps.SuperAppExternalApi
import com.genexus.android.core.superapps.MiniApp
import com.genexus.android.core.superapps.errors.LoadError
import com.genexus.android.core.tasking.OnCompleteListener
import com.genexus.android.core.tasking.Task

class MiniAppsAPI(action: ApiAction?) : SuperAppExternalApi(action) {

    private val handlerLoad = IMethodInvoker {
        val miniAppInfo = it[0] as Entity?
        if (miniAppInfo == null) {
            Services.Log.error("MiniApp information is null")
            return@IMethodInvoker ExternalApiResult.FAILURE
        }

        val miniAppEntryPoint = miniAppInfo.optStringProperty(MiniApp.FIELD_ENTRY_POINT)
        val miniAppServicesUrl = miniAppInfo.optStringProperty(MiniApp.FIELD_SERVICES_URL)
        val miniAppMetadata = miniAppInfo.optStringProperty(MiniApp.FIELD_METADATA)
        val miniAppSignature = miniAppInfo.optStringProperty(MiniApp.FIELD_SIGNATURE)
        val parameters = mutableListOf<String?>().apply {
            add(miniAppEntryPoint)
            add(miniAppServicesUrl)
            add(miniAppMetadata)
            add(miniAppSignature)
        }

        if (!check(parameters)) {
            Services.Log.error("MiniApp information is invalid")
            return@IMethodInvoker ExternalApiResult.FAILURE
        }

        val miniApp = MiniApp().apply {
            this.appEntry = miniAppEntryPoint
            this.metadataRemoteUrl = miniAppMetadata
            this.apiUri = miniAppServicesUrl
            this.signature = miniAppSignature
            this.name = miniAppInfo.optStringProperty(MiniApp.FIELD_NAME)
            this.iconUrl = miniAppInfo.optStringProperty(MiniApp.FIELD_ICON)
            this.bannerUrl = miniAppInfo.optStringProperty(MiniApp.FIELD_BANNER)
            this.cardUrl = miniAppInfo.optStringProperty(MiniApp.FIELD_CARD)
            this.description = miniAppInfo.optStringProperty(MiniApp.FIELD_DESCRIPTION)
            this.id = miniAppInfo.optStringProperty(MiniApp.FIELD_ID)
            this.version = miniAppInfo.optIntProperty(MiniApp.FIELD_VERSION)
        }

        Services.SuperApps.load(miniApp).addOnCompleteListener(loadCallback)
        ExternalApiResult.SUCCESS_WAIT
    }

    private val loadCallback = object : OnCompleteListener<Boolean, LoadError> {
        override fun onComplete(task: Task<Boolean, LoadError>) {
            if (task.isSuccessful)
                onSuccess()
            else
                onFailure()
        }

        private fun onSuccess() {
            ActionExecution.continueCurrent(activity, true, action)
        }

        private fun onFailure() {
            ActionExecution.cancelCurrent(action)
        }
    }

    private val handlerGetCached = IMethodInvoker {
        ExternalApiResult.success(Services.SuperApps.getCachedMiniApps().toEntityList())
    }

    private val handlerRemoveCached = IMethodInvoker {
        val id = it[0] as String
        val version = it[1] as String
        ExternalApiResult.success(Services.SuperApps.removeMiniApp(id, version.toInt()))
    }

    private val handlerClearCached = IMethodInvoker {
        ExternalApiResult.success(Services.SuperApps.clearCache())
    }

    private fun check(parameters: List<String?>): Boolean {
        for (p in parameters)
            if (p.isNullOrEmpty())
                return false

        return true
    }

    init {
        addMethodHandler(METHOD_LOAD, 1, handlerLoad)
        addMethodHandler(METHOD_GET_CACHED, 0, handlerGetCached)
        addMethodHandler(METHOD_REMOVE_CACHED, 2, handlerRemoveCached)
        addMethodHandler(METHOD_CLEAR_CACHED, 0, handlerClearCached)
    }

    companion object {
        const val NAME = "GeneXusSuperApps.MiniApps"

        private const val METHOD_LOAD = "Load"
        private const val METHOD_GET_CACHED = "GetCached"
        private const val METHOD_REMOVE_CACHED = "RemoveCached"
        private const val METHOD_CLEAR_CACHED = "ClearCached"
    }
}
