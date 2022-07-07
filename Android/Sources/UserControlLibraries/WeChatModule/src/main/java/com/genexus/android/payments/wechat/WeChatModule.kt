package com.genexus.android.payments.wechat

import android.content.Context
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.externalapi.ExternalApiDefinition
import com.genexus.android.core.externalapi.ExternalApiFactory
import com.genexus.android.core.framework.GenexusModule

class WeChatModule : GenexusModule {
    override fun initialize(context: Context) {
        ExternalApiFactory.addApi(
            ExternalApiDefinition(
                WeChatApi.NAME,
                WeChatApi::class.java
            )
        )
        ExternalApiFactory.addApi(
            ExternalApiDefinition(
                WeChatPayApi.NAME,
                WeChatPayApi::class.java
            )
        )

        val loginProvider = WeChatLoginProvider()
        WeChatEntryActivity.loginProvider = loginProvider
        Services.Extensions.externalLoginManager.addProvider(loginProvider)
    }

    companion object {
        const val TAG = "WeChat"
    }
}
