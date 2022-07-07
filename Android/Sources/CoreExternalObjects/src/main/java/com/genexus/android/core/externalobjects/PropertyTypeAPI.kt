package com.genexus.android.core.externalobjects

import com.genexus.android.core.actions.ApiAction
import com.genexus.android.core.base.utils.Strings
import com.genexus.android.core.externalapi.ExternalApi
import com.genexus.android.core.externalapi.ExternalApiResult

class PropertyTypeAPI(action: ApiAction?) : ExternalApi(action) {

    var key = Strings.EMPTY
    var value: Any = Strings.EMPTY

    private val getKey = IMethodInvoker {
        ExternalApiResult.success(key)
    }

    private val setKey = IMethodInvoker {
        key = it[0] as String
        ExternalApiResult.SUCCESS_CONTINUE
    }

    private val getValue = IMethodInvoker {
        ExternalApiResult.success(value)
    }

    private val setValue = IMethodInvoker {
        value = it[0]
        ExternalApiResult.SUCCESS_CONTINUE
    }

    private val setEmpty = IMethodInvoker {
        key = Strings.EMPTY
        value = Strings.EMPTY
        ExternalApiResult.success(this)
    }

    init {
        addPropertyHandler(PROPERTY_KEY, getKey, setKey)
        addPropertyHandler(PROPERTY_VALUE, getValue, setValue)
        addMethodHandler(METHOD_SET_EMPTY, 0, setEmpty)
    }

    companion object {
        const val TYPE_NAME = "Property"
        const val PROPERTY_KEY = "Key"
        const val PROPERTY_VALUE = "Value"
        const val METHOD_SET_EMPTY = "SetEmpty"
    }
}
