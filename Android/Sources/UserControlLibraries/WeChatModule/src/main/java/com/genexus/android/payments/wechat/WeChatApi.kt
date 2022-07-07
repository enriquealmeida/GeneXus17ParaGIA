package com.genexus.android.payments.wechat

import com.genexus.android.core.actions.ApiAction
import com.genexus.android.core.externalapi.ExternalApi
import com.genexus.android.core.externalapi.ExternalApiResult

class WeChatApi(action: ApiAction?) : ExternalApi(action) {
    private val isAvailableProperty = IMethodInvoker {
        ExternalApiResult.success(WeChatObject.Api(context) != null)
    }

    init {
        addReadonlyPropertyHandler(PROPERTY_IS_AVAILABLE, isAvailableProperty)
    }

    companion object {
        const val NAME = "GeneXus.Social.WeChat"
        private const val PROPERTY_IS_AVAILABLE = "IsAvailable"
    }
}
