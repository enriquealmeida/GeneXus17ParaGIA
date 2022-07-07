package com.genexus.android.remoteconfig.firebase

import com.genexus.android.core.base.services.Services
import com.genexus.android.remoteconfig.RemoteConfigCompletedListener
import com.genexus.android.remoteconfig.RemoteConfigProvider
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import java.util.Date

class FirebaseRemoteConfigProvider : RemoteConfigProvider {

    private lateinit var remoteConfig: FirebaseRemoteConfig

    override fun initialize(defaultValues: MutableMap<String, Any>?, fetchInterval: Long) {
        remoteConfig = Firebase.remoteConfig.apply {
            setConfigSettingsAsync(remoteConfigSettings { minimumFetchIntervalInSeconds = fetchInterval })
            defaultValues?.let { setDefaultsAsync(it) }
        }
    }

    override fun hasValue(key: String?): Boolean {
        return if (key.isNullOrEmpty())
            false
        else
            getStringValue(key).isNotEmpty()
    }

    override fun getStringValue(key: String): String {
        return remoteConfig.getString(key)
    }

    override fun getIntegerValue(key: String): Int {
        return remoteConfig.getLong(key).toInt()
    }

    override fun getDecimalValue(key: String): Double {
        return remoteConfig.getDouble(key)
    }

    override fun getBooleanValue(key: String): Boolean {
        return remoteConfig.getBoolean(key)
    }

    override fun getDateValue(key: String): Date? {
        return Services.Strings.getDate(getStringValue(key))
    }

    override fun getDateTimeValue(key: String): Date? {
        return Services.Strings.getDateTime(getStringValue(key))
    }

    override fun fetch(listener: RemoteConfigCompletedListener) {
        val fetchTask = remoteConfig.fetch()
        fetchTask.addOnCompleteListener {
            if (it.isSuccessful) {
                listener.onSuccess()
            } else {
                listener.onFailure(it.exception)
            }
        }
    }

    override fun apply(listener: RemoteConfigCompletedListener) {
        val fetchTask = remoteConfig.activate()
        fetchTask.addOnCompleteListener {
            if (it.isSuccessful) {
                listener.onSuccess()
            } else {
                listener.onFailure(it.exception)
            }
        }
    }
}
