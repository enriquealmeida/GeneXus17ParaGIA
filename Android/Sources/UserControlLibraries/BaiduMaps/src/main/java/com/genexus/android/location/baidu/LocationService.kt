package com.genexus.android.location.baidu

import android.content.Context
import android.webkit.WebView
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer

class LocationService(context: Context) {
    private val lockObject = Any()
    private lateinit var client: LocationClient
    private var customOptions: LocationClientOption? = null
    val sdkVersion: String by lazy { client.version }

    fun registerListener(listener: BDAbstractLocationListener?) {
        if (listener != null)
            client.registerLocationListener(listener)
    }

    fun unregisterListener(listener: BDAbstractLocationListener?) {
        if (listener != null)
            client.unRegisterLocationListener(listener)
    }

    fun start() {
        synchronized(lockObject) {
            if (!isStarted())
                client.start()
        }
    }

    fun stop() {
        synchronized(lockObject) {
            if (isStarted())
                client.stop()
        }
    }

    fun isStarted(): Boolean {
        return client.isStarted
    }

    private val defaultLocationClientOption = LocationClientOption().apply {
        locationMode = LocationClientOption.LocationMode.Hight_Accuracy
        isOpenGps = true
        isLocationNotify = false
        setCoorType("bd09ll")
        setScanSpan(3000)
        setIsNeedAddress(true)
        setIsNeedLocationDescribe(true)
        setIsNeedLocationDescribe(true)
        setIsNeedLocationPoiList(true)
        setIsNeedAltitude(false)
        setNeedDeviceDirect(false)
        setIgnoreKillProcess(true)
        SetIgnoreCacheException(false)
    }

    fun getLastKnownLocation(): BDLocation? {
        return client.lastKnownLocation
    }

    fun setLocationOption(option: LocationClientOption?): Boolean {
        if (option != null) {
            stop()
            customOptions = option
            client.locOption = customOptions
            return true
        }
        return false
    }

    fun enableAssistantLocation(webView: WebView?) {
        client.enableAssistantLocation(webView)
    }

    fun disableAssistantLocation() {
        client.disableAssistantLocation()
    }

    fun getCustomOption(): LocationClientOption? {
        if (customOptions == null)
            customOptions = LocationClientOption()

        return customOptions
    }

    fun requestLocation() {
        client.requestLocation()
    }

    fun requestHotSpotState(): Boolean {
        return client.requestHotSpotState()
    }

    init {
        SDKInitializer.initialize(context)
        SDKInitializer.setCoordType(CoordType.BD09LL)
        synchronized(lockObject) {
            client = LocationClient(context)
            client.locOption = defaultLocationClientOption
        }
    }
}
