package com.genexus.android.ads

import android.content.Intent
import com.genexus.android.core.actions.ApiAction
import com.genexus.android.core.externalapi.ExternalApi
import com.genexus.android.core.externalapi.ExternalApi.ISimpleMethodInvoker
import com.genexus.android.core.externalapi.ExternalApiResult

class AdsAPI(action: ApiAction?) : ExternalApi(action) {
    private val mMethodRequestInterstitial = ISimpleMethodInvoker { parameters: List<Any?> ->
        val adUnitId = parameters[0] as String?
        if (adUnitId != null) {
            AdsImpl.requestInterstitial(adUnitId)
        }
        ""
    }

    private val mMethodIsInterstitialReady = ISimpleMethodInvoker { parameters: List<Any?> ->
        val adUnitId = parameters[0] as String?
        if (adUnitId != null) {
            AdsImpl.isInterstitialReady(adUnitId)
        } else {
            false
        }
    }

    private val mMethodShowInterstitial: IMethodInvoker = object : IMethodInvokerWithActivityResult {
        override fun invoke(parameters: List<Any>): ExternalApiResult {
            val adUnitId = parameters[0] as String?
            return if (adUnitId != null && AdsImpl.showInterstitial(adUnitId))
                ExternalApiResult.SUCCESS_WAIT
            else
                ExternalApiResult.success(false)
        }

        override fun handleActivityResult(requestCode: Int, resultCode: Int, result: Intent?): ExternalApiResult {
            try {
                // Hack to give time for ActivityHelper to have the current activity so msg() works,
                // don't wait too much because this is the UI thread.
                Thread.sleep(500)
            } catch (ignored: Exception) {
            }
            return ExternalApiResult.success(true)
        }
    }

    companion object {
        private const val METHOD_REQUEST_INTERSTITIAL = "RequestInterstitial"
        private const val METHOD_IS_INTERSTITIAL_READY = "IsInterstitialReady"
        private const val METHOD_SHOW_INTERSTITIAL = "ShowInterstitial"
        const val EVENT_INTERSTITIAL_LOADED = "InterstitialLoaded"
        const val EVENT_INTERSTITIAL_FAILED_TO_LOAD = "InterstitialFailedToLoad"
        const val EVENT_INTERSTITIAL_CLICKED = "InterstitialClicked"
        const val EVENT_INTERSTITIAL_CLOSED = "InterstitialClosed"
        const val OBJECT_NAME = "GeneXus.SD.Ads"
    }

    init {
        addSimpleMethodHandler(METHOD_REQUEST_INTERSTITIAL, 1, mMethodRequestInterstitial)
        addSimpleMethodHandler(METHOD_IS_INTERSTITIAL_READY, 1, mMethodIsInterstitialReady)
        addMethodHandler(METHOD_SHOW_INTERSTITIAL, 1, mMethodShowInterstitial)
    }
}
