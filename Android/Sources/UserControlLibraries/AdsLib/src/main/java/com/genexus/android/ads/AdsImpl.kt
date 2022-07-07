package com.genexus.android.ads

import com.genexus.android.core.controls.ads.Ads

object AdsImpl {
    fun requestInterstitial(adUnitId: String) {
        Ads.getDefaultProvider()?.interstitialController?.requestInterstitial(adUnitId)
    }

    fun isInterstitialReady(adUnitId: String): Boolean {
        return Ads.getDefaultProvider()?.interstitialController?.isInterstitialReady(adUnitId) ?: false
    }

    fun showInterstitial(adUnitId: String): Boolean {
        return Ads.getDefaultProvider()?.interstitialController?.showInterstitial(adUnitId) ?: false
    }
}
