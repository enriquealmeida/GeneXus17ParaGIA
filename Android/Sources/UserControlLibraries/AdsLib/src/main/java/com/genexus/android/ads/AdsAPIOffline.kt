package com.genexus.android.ads

class AdsAPIOffline {
    companion object {
        @JvmStatic
        fun requestInterstitial(adUnitId: String) {
            AdsImpl.requestInterstitial(adUnitId)
        }

        @JvmStatic
        fun isInterstitialReady(adUnitId: String): Boolean {
            return AdsImpl.isInterstitialReady(adUnitId)
        }

        @JvmStatic
        fun showInterstitial(adUnitId: String): Boolean {
            return AdsImpl.showInterstitial(adUnitId)
        }
    }
}
