package com.genexus.android.superapps

import android.location.Location
import com.genexus.android.core.actions.ActionExecution
import com.genexus.android.core.actions.ApiAction
import com.genexus.android.core.base.metadata.expressions.Expression
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.GeoFormats
import com.genexus.android.core.externalapi.ExternalApi.IMethodInvoker
import com.genexus.android.core.externalapi.ExternalApiResult
import com.genexus.android.core.externalapi.superapps.SuperAppExternalApi
import com.genexus.android.core.superapps.MiniAppCollection
import com.genexus.android.core.superapps.errors.SearchError
import com.genexus.android.core.tasking.OnCompleteListener
import com.genexus.android.core.tasking.Task

class ProvisioningAPI(action: ApiAction?) : SuperAppExternalApi(action) {

    private val handlerGetByText = IMethodInvoker {
        val text = it[0] as String
        val start = it[1] as String
        val count = it[2] as String
        Services.SuperApps.searchByText(text, start.toInt(), count.toInt()).addOnCompleteListener(searchCallback)
        ExternalApiResult.SUCCESS_WAIT
    }

    private val handlerGetByLocation = IMethodInvoker {
        val geopoint = it[0] as String
        val radius = it[1] as String
        val start = it[2] as String
        val count = it[3] as String
        val location = GeoFormats.parseGeopoint(geopoint) ?: return@IMethodInvoker ExternalApiResult.FAILURE
        val center = Location("GX").apply { latitude = location.first; longitude = location.second }
        Services.SuperApps.searchByLocation(center, radius.toInt(), start.toInt(), count.toInt()).addOnCompleteListener(searchCallback)
        ExternalApiResult.SUCCESS_WAIT
    }

    private val handlerGetByTag = IMethodInvoker {
        val tag = it[0] as String
        val start = it[1] as String
        val count = it[2] as String
        Services.SuperApps.searchByTag(tag, start.toInt(), count.toInt()).addOnCompleteListener(searchCallback)
        ExternalApiResult.SUCCESS_WAIT
    }

    private val handlerGetFeatured = IMethodInvoker {
        val start = it[0] as String
        val count = it[1] as String
        Services.SuperApps.searchFeatured(start.toInt(), count.toInt()).addOnCompleteListener(searchCallback)
        ExternalApiResult.SUCCESS_WAIT
    }

    private val handlerServerUrl = IMethodInvoker {
        ExternalApiResult.success(Services.SuperApps.getProvisioningUrl())
    }

    private val searchCallback = object : OnCompleteListener<MiniAppCollection, SearchError> {
        override fun onComplete(task: Task<MiniAppCollection, SearchError>) {
            if (task.isSuccessful)
                onSuccess(task.result!!)
            else
                onFailure()
        }

        private fun onSuccess(miniApps: MiniAppCollection) {
            continueExecution(miniApps)
        }

        private fun onFailure() {
            continueExecution(MiniAppCollection())
        }

        private fun continueExecution(miniApps: MiniAppCollection) {
            action?.let {
                it.setOutputValue(Expression.Value.newValue(miniApps.toEntityList()))
                ActionExecution.continueCurrent(activity, true, it)
            }
        }
    }

    companion object {
        const val NAME = "GeneXusSuperApps.Provisioning"

        private const val METHOD_GET_TEXT = "GetByText"
        private const val METHOD_GET_LOCATION = "GetByLocation"
        private const val METHOD_GET_TAG = "GetByTag"
        private const val METHOD_GET_FEATURED = "GetFeatured"
        private const val PROPERTY_SERVER_URL = "ServerURL"
    }

    init {
        addMethodHandler(METHOD_GET_TEXT, 3, handlerGetByText)
        addMethodHandler(METHOD_GET_LOCATION, 4, handlerGetByLocation)
        addMethodHandler(METHOD_GET_TAG, 3, handlerGetByTag)
        addMethodHandler(METHOD_GET_FEATURED, 2, handlerGetFeatured)
        addReadonlyPropertyHandler(PROPERTY_SERVER_URL, handlerServerUrl)
    }
}
