package com.genexus.android.payments.wechat

import com.genexus.android.core.base.services.Services

class WeChatOffline {
    companion object {
        @JvmStatic
        fun isAvailable(): Boolean {
            return WeChatObject.Api(Services.Application.appContext) != null
        }
    }
}
