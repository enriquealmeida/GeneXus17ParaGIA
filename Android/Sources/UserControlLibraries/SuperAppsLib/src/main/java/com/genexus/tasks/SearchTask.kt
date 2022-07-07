package com.genexus.tasks

import com.genexus.android.core.base.services.Services
import com.genexus.android.core.superapps.MiniApp
import com.genexus.android.core.superapps.MiniAppCollection
import com.genexus.android.core.superapps.errors.SearchError
import com.genexus.android.core.tasking.Task
import org.json.JSONException
import java.util.Locale

internal class SearchTask(private val serviceName: String) : Task<MiniAppCollection, SearchError>("MiniAppSearching") {
    fun search(parameters: Map<String, String>): Task<MiniAppCollection, SearchError> {
        execute run@{
            val provisioning = Services.SuperApps.getProvisioningUrl()
            if (provisioning.isEmpty()) {
                onFailure(SearchError.INVALID_REQUEST)
                return@run
            }

            val stringBuilder = StringBuilder(provisioning).append(BASE).append(serviceName).append("?")
            var i = 1
            for (parameter in parameters) {
                stringBuilder.append(parameter.key.lowercase(Locale.getDefault())).append("=")
                stringBuilder.append(Services.HttpService.uriEncode(parameter.value))
                if (i < parameters.size) stringBuilder.append("&")
                i++
            }

            val provisioningServiceUrl = stringBuilder.toString()
            val response = Services.HttpService.getMiniApps(provisioningServiceUrl, Services.SuperApps.getId(), Services.SuperApps.getVersion())
            if (response == null) {
                onFailure(SearchError.INVALID_RESPONSE)
                return@run
            }

            try {
                val miniAppCollection = MiniAppCollection()
                for (j in 0 until response.length()) {
                    val miniAppObject = response.getJSONObject(j)
                    miniAppCollection.add(MiniApp(miniAppObject, true))
                }
                onSuccess(miniAppCollection)
            } catch (ex: JSONException) {
                Services.Log.warning("Error parsing MiniApp collection", ex)
                onFailure(SearchError.INVALID_RESPONSE)
            }
        }
        return this
    }

    companion object {
        private const val BASE = "superapp/v1/"
    }
}
