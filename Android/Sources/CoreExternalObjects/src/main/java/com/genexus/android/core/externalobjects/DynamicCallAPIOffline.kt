package com.genexus.android.core.externalobjects

import com.genexus.android.core.ui.navigation.CallOptionsHelper

class DynamicCallAPIOffline {
    companion object {
        @JvmStatic
        fun setOption(callName: String, callOption: String, value: String) {
            val objectName = DynamicCallAPI.getObjectName(callName)
            CallOptionsHelper.setCallOption(objectName, callOption, value)
        }
    }
}
