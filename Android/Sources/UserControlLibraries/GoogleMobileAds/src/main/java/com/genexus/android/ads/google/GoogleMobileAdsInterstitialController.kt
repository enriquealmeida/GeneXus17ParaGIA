package com.genexus.android.ads.google

import com.genexus.android.ads.AdsAPI
import com.genexus.android.core.actions.ActionExecution
import com.genexus.android.core.actions.ExternalObjectEvent
import com.genexus.android.core.activities.ActivityHelper
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.controls.ads.IAdsInterstitialController
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.doubleclick.PublisherAdRequest
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd
import java.util.HashMap

object GoogleMobileAdsInterstitialController : IAdsInterstitialController {
    private enum class InterstitialState {
        Empty, Loading, Loaded, Failed, Shown, Closed
    }

    private class InterstitialData {
        var ad: PublisherInterstitialAd? = null
        var state = InterstitialState.Empty
    }

    const val TAG = "GoogleMobileAdsInterstitialController"
    private const val TEST_AD_UNIT = "/6499/example/interstitial"
    private val dataMap = HashMap<String, InterstitialData>()

    override fun requestInterstitial(adUnitId: String) {
        val xAdUnitId = checkTest(adUnitId)
        var data = dataMap[xAdUnitId]
        if (data == null) {
            data = InterstitialData()
            dataMap[xAdUnitId] = data
        }
        requestInterstitial(xAdUnitId, data)
    }

    override fun isInterstitialReady(adUnitId: String): Boolean {
        val data = dataMap[checkTest(adUnitId)]
        return data?.state == InterstitialState.Loaded
    }

    override fun showInterstitial(adUnitId: String): Boolean {
        val data = dataMap[checkTest(adUnitId)]
        if (data != null && data.state == InterstitialState.Loaded) {
            if (showInterstitial(data))
                return true
        } else {
            Services.Log.debug(TAG, "Ad Interstitial is not loaded, must be Requested before showing")
        }
        return false
    }

    private fun checkTest(adUnitId: String): String {
        return if (adUnitId == "TEST")
            TEST_AD_UNIT
        else
            adUnitId
    }

    private fun requestInterstitial(adUnitId: String, data: InterstitialData) {
        val activity = ActivityHelper.getCurrentActivity()
        if (activity != null && !(data.state == InterstitialState.Loaded || data.state == InterstitialState.Loading)) {
            data.state = InterstitialState.Loading
            data.ad = PublisherInterstitialAd(activity)
            data.ad?.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    data.state = InterstitialState.Loaded
                    ActionExecution.continueCurrent(activity, true, null) // continue SUCCESS_WAIT of mMethodShowInterstitial
                    val event = ExternalObjectEvent(AdsAPI.OBJECT_NAME, AdsAPI.EVENT_INTERSTITIAL_LOADED)
                    event.fire(listOf<Any?>(adUnitId))
                    Services.Log.debug(TAG, "onAdLoaded Event")
                }

                override fun onAdFailedToLoad(errorCode: Int) {
                    data.state = InterstitialState.Failed
                    ActionExecution.continueCurrent(activity, true, null) // continue SUCCESS_WAIT of mMethodShowInterstitial
                    val event = ExternalObjectEvent(AdsAPI.OBJECT_NAME, AdsAPI.EVENT_INTERSTITIAL_FAILED_TO_LOAD)
                    event.fire(listOf<Any?>(adUnitId))
                    Services.Log.error(TAG, "onAdFailedToLoad Event: $errorCode")
                }

                override fun onAdClicked() {
                    ActionExecution.continueCurrent(activity, true, null) // continue SUCCESS_WAIT of mMethodShowInterstitial
                    val event = ExternalObjectEvent(AdsAPI.OBJECT_NAME, AdsAPI.EVENT_INTERSTITIAL_CLICKED)
                    event.fire(listOf<Any?>(adUnitId))
                    Services.Log.debug(TAG, "onAdClicked Event")
                }

                override fun onAdClosed() {
                    data.state = InterstitialState.Closed
                    ActionExecution.continueCurrent(activity, true, null) // continue SUCCESS_WAIT of mMethodShowInterstitial
                    val event = ExternalObjectEvent(AdsAPI.OBJECT_NAME, AdsAPI.EVENT_INTERSTITIAL_CLOSED)
                    event.fire(listOf<Any?>(adUnitId))
                    Services.Log.debug(TAG, "onAdClosed Event")
                }
            }
            data.ad?.adUnitId = adUnitId
            activity.runOnUiThread {
                val adRequest = PublisherAdRequest.Builder().build()
                data.ad?.loadAd(adRequest)
                Services.Log.debug(TAG, "requestNewInterstitial $adUnitId")
            }
        }
    }

    private fun showInterstitial(data: InterstitialData): Boolean {
        val activity = ActivityHelper.getCurrentActivity() // Sometimes activity is null
            ?: return false
        activity.runOnUiThread {
            if (data.ad?.isLoaded == true) {
                data.state = InterstitialState.Shown
                data.ad?.show()
            } else {
                Services.Log.debug(TAG, "Ad Interstisial not loaded, so cannot show it")
            }
        }
        return true
    }
}
