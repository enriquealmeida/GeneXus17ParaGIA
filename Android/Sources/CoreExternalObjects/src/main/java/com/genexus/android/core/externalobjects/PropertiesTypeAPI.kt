package com.genexus.android.core.externalobjects

import com.genexus.android.core.actions.ApiAction
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.Strings
import com.genexus.android.core.externalapi.ExternalApi
import com.genexus.android.core.externalapi.ExternalApiResult
import org.json.JSONException
import org.json.JSONObject

class PropertiesTypeAPI(action: ApiAction?) : ExternalApi(action) {

    private val propertiesMap = LinkedHashMap<String, Any>()

    private val count = IMethodInvoker {
        ExternalApiResult.success(propertiesMap.size)
    }

    private val fromJson = IMethodInvoker {
        val jsonString = it[0] as String

        try {
            val jsonObject = JSONObject(jsonString)
            for (key in jsonObject.keys())
                propertiesMap[key] = jsonObject[key]
        } catch (exception: JSONException) {
            Services.Log.error(exception)
            return@IMethodInvoker ExternalApiResult.FAILURE
        }

        ExternalApiResult.success(this)
    }

    private val toJson = IMethodInvoker {
        var jsonObject = JSONObject()

        try {
            for (value in propertiesMap)
                jsonObject.put(value.key, value.value)
        } catch (exception: JSONException) {
            Services.Log.error(exception)
            jsonObject = JSONObject()
        }

        ExternalApiResult.success(jsonObject.toString())
    }

    private val clear = IMethodInvoker {
        propertiesMap.clear()
        ExternalApiResult.success(this)
    }

    private val remove = IMethodInvoker {
        val key = it[0] as String
        propertiesMap.remove(key)
        ExternalApiResult.SUCCESS_CONTINUE
    }

    private val get = IMethodInvoker {
        val key = it[0] as String
        ExternalApiResult.success(propertiesMap[key] ?: Strings.EMPTY)
    }

    private val set = IMethodInvoker {
        val key = it[0] as String
        val value = it[1] as String
        propertiesMap[key] = value
        ExternalApiResult.SUCCESS_CONTINUE
    }

    override fun iterator(): Iterator<*> {
        return object : Iterator<PropertyTypeAPI> {
            var current = 0
            override fun hasNext(): Boolean { return current < propertiesMap.size }

            override fun next(): PropertyTypeAPI {
                val propertyTypeAPI = PropertyTypeAPI(action)
                val currentProperty = propertiesMap.toList()[current]
                propertyTypeAPI.key = currentProperty.first
                propertyTypeAPI.value = currentProperty.second
                current++
                return propertyTypeAPI
            }
        }
    }

    init {
        addReadonlyPropertyHandler(PROPERTY_COUNT, count)
        addMethodHandler(METHOD_FROM_JSON, 1, fromJson)
        addMethodHandler(METHOD_TO_JSON, 0, toJson)
        addMethodHandler(METHOD_CLEAR, 0, clear)
        addMethodHandler(METHOD_SET_EMPTY, 0, clear)
        addMethodHandler(METHOD_REMOVE, 1, remove)
        addMethodHandler(METHOD_GET, 1, get)
        addMethodHandler(METHOD_SET, 2, set)
    }

    companion object {
        const val TYPE_NAME = "Properties"
        const val PROPERTY_COUNT = "Count"
        const val METHOD_FROM_JSON = "FromJson"
        const val METHOD_TO_JSON = "ToJson"
        const val METHOD_SET_EMPTY = "SetEmpty"
        const val METHOD_CLEAR = "Clear"
        const val METHOD_REMOVE = "Remove"
        const val METHOD_GET = "Get"
        const val METHOD_SET = "Set"
    }
}
