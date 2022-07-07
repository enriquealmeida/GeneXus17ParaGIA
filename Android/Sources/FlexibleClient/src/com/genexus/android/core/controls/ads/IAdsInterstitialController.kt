package com.genexus.android.core.controls.ads

interface IAdsInterstitialController {
    fun requestInterstitial(adUnitId: String)
    fun isInterstitialReady(adUnitId: String): Boolean
    fun showInterstitial(adUnitId: String): Boolean
}
