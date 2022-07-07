package com.genexus.android.remoteconfig

import java.util.Date

interface RemoteConfigProvider {
    fun initialize(defaultValues: MutableMap<String, Any>?, fetchInterval: Long)

    fun hasValue(key: String?): Boolean
    fun getStringValue(key: String): String
    fun getIntegerValue(key: String): Int
    fun getDecimalValue(key: String): Double
    fun getBooleanValue(key: String): Boolean
    fun getDateValue(key: String): Date?
    fun getDateTimeValue(key: String): Date?

    fun fetch(listener: RemoteConfigCompletedListener)
    fun apply(listener: RemoteConfigCompletedListener)
}
