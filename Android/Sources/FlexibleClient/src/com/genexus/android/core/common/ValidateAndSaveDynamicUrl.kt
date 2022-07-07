package com.genexus.android.core.common

import com.genexus.android.core.base.services.Services

class ValidateAndSaveDynamicUrl {

    companion object {
        lateinit var serverUrl: String
        lateinit var actualListener: ValidateAppServerUriListener

        @JvmStatic
        fun execute(url: String, listener: ValidateAppServerUriListener) {
            serverUrl = if (url.contains("://")) url else "http://$url"
            actualListener = listener

            Thread(ValidateAppServerUri(serverUrl, internalListener), "Background").start()
        }

        private val internalListener: ValidateAppServerUriListener = object : ValidateAppServerUriListener {
            override fun onCheckApplicationUriResult(url: String, result: Int) {
                if (result == ValidateAppServerUri.VALID_URL)
                    Services.Application.saveNewDynamicServicesUrl(serverUrl)

                actualListener.onCheckApplicationUriResult(serverUrl, result)
            }
        }
    }
}
