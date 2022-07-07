package com.genexus.android.core.common

import com.genexus.android.core.base.services.Services

class ValidateAppServerUri(private val mServerUrl: String, private val mListener: ValidateAppServerUriListener) : Runnable {

    override fun run() {
        if (!Services.HttpService.isOnline) {
            mListener.onCheckApplicationUriResult(mServerUrl, NO_CONNECTION)
            return
        }

        if (Services.HttpService.checkApplicationUri(Services.Application.get().name, mServerUrl))
            mListener.onCheckApplicationUriResult(mServerUrl, VALID_URL)
        else
            mListener.onCheckApplicationUriResult(mServerUrl, INVALID_URL)
    }

    companion object {
        const val VALID_URL = 0
        const val INVALID_URL = 1
        const val NO_CONNECTION = 2
    }
}
