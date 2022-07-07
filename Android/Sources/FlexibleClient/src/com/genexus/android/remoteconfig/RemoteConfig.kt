package com.genexus.android.remoteconfig

import com.genexus.GXutil
import com.genexus.android.core.base.metadata.InstanceProperties
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.NameMap
import com.genexus.android.core.base.utils.Strings
import java.lang.Exception
import java.util.Date
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class RemoteConfig {

    companion object {
        private const val TAG = "RemoteConfig"
        private const val KEY_LAST_FETCH_STATUS = "FIREBASE_LAST_FETCH_STATUS"
        private const val KEY_LAST_FETCH_STAMP = "FIREBASE_LAST_FETCH_STAMP"

        private var millisInterval: Long = 86400000 // 24hs
        private var fetchCriteria = FetchCriteria.WhenActivated
        private var activationCriteria = ActivationCriteria.WhenLaunched
        private var defaultValues: NameMap<Any>? = null

        private var lastFetchMillis = Services.Preferences.getLong(KEY_LAST_FETCH_STAMP, 0L)
        private var fetchStatus: Long = Services.Preferences.getLong(KEY_LAST_FETCH_STATUS, -1)

        private var applyApiListener: RemoteConfigCompletedListener? = null
        private var fetchApiListener: RemoteConfigCompletedListener? = null

        var provider: RemoteConfigProvider? = null

        fun initialize(properties: InstanceProperties?) {
            provider?.let {
                properties?.let { props ->
                    fetchCriteria = props.remoteConfigFetchCriteria
                    activationCriteria = props.remoteConfigActivationCriteria
                    millisInterval = props.remoteConfigFetchIntervalMinutes.times(60000)
                    defaultValues = props.remoteConfigDefaults
                }

                it.initialize(defaultValues, millisInterval.div(1000))
                runFetchingJobIfNeeded()
                when {
                    shouldSyncAtStartup() -> applyAndFetchIfIntervalPassed()
                    shouldFetchAtStartup() -> fetchIfIntervalPassed(false)
                    shouldActivateAtStartup() -> apply()
                    else -> Services.Log.debug("RemoteConfig initialization is Manual")
                }
            }
        }

        fun lastSuccessfulFetch(): Date {
            return if (lastFetchMillis == 0L) GXutil.nullDate() else Date(lastFetchMillis)
        }

        fun lastFetchStatus(): Long {
            return fetchStatus
        }

        fun hasValue(key: String?): Boolean {
            return provider?.hasValue(key) ?: false
        }

        fun getStringValue(key: String): String {
            return provider?.getStringValue(key) ?: Strings.EMPTY
        }

        fun getIntegerValue(key: String): Int {
            return provider?.getIntegerValue(key) ?: 0
        }

        fun getDecimalValue(key: String): Double {
            return provider?.getDecimalValue(key) ?: 0.0
        }

        fun getBooleanValue(key: String): Boolean {
            return provider?.getBooleanValue(key) ?: false
        }

        fun getDateValue(key: String): Date {
            return provider?.getDateValue(key) ?: GXutil.nullDate()
        }

        fun getDateTimeValue(key: String): Date {
            return provider?.getDateTimeValue(key) ?: GXutil.nullDate()
        }

        fun fetch(listener: RemoteConfigCompletedListener) {
            fetchApiListener = listener
            fetch()
        }

        private fun fetch() {
            provider?.let {
                Services.Log.debug(
                    TAG,
                    "Executing RemoteConfig fetch as more than " +
                        "$millisInterval seconds passed from last fetch on $lastFetchMillis " +
                        "or Fetching is Manual"
                )

                it.fetch(fetchListener)
            }
        }

        private fun fetchIfIntervalPassed(force: Boolean) {
            if (shouldFetchGivenInterval(force))
                fetch()
        }

        fun apply(listener: RemoteConfigCompletedListener) {
            applyApiListener = listener
            apply()
        }

        private fun apply() {
            provider?.let {
                Services.Log.debug(TAG, "Executing RemoteConfig apply")
                it.apply(applyListener)
            }
        }

        private fun applyAndFetchIfIntervalPassed() {
            Services.Log.debug(TAG, "Applying values before fetching new ones")
            apply()
            fetchIfIntervalPassed(false)
        }

        private fun shouldSyncAtStartup(): Boolean {
            return shouldFetchAtStartup() && shouldActivateAtStartup()
        }

        private fun shouldFetchAtStartup(): Boolean {
            return fetchCriteria == FetchCriteria.WhenActivated
        }

        private fun shouldActivateAtStartup(): Boolean {
            return activationCriteria == ActivationCriteria.WhenLaunched
        }

        private fun shouldFetchGivenInterval(force: Boolean): Boolean {
            return if (lastFetchMillis == 0L)
                force
            else
                System.currentTimeMillis() - lastFetchMillis >= millisInterval && fetchCriteria != FetchCriteria.Manual
        }

        private fun runFetchingJobIfNeeded() {
            if (fetchCriteria == FetchCriteria.AfterElapsedTime) {
                Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
                    { fetchIfIntervalPassed(true) }, millisInterval, millisInterval, TimeUnit.MILLISECONDS
                )
            }
        }

        private val fetchListener: RemoteConfigCompletedListener = object : RemoteConfigCompletedListener {
            override fun onSuccess() {
                Services.Log.debug(TAG, "Fetching succeeded")
                fetchApiListener?.onSuccess()
                fetchApiListener = null

                if (activationCriteria == ActivationCriteria.Immediately) {
                    Services.Log.debug(TAG, "Immediately applying fetched values")
                    apply()
                }

                lastFetchMillis = System.currentTimeMillis()
                fetchStatus = FetchStatus.Success.value.toLong()
                finishFetch()
            }

            override fun onFailure(exception: Exception?) {
                Services.Log.error(TAG, "Values Fetching failed ")
                fetchApiListener?.onFailure(exception)
                fetchApiListener = null

                fetchStatus = FetchStatus.Failure.value.toLong()
                finishFetch()
            }

            private fun finishFetch() {
                Services.Preferences.setLong(KEY_LAST_FETCH_STATUS, fetchStatus)
                Services.Preferences.setLong(KEY_LAST_FETCH_STAMP, lastFetchMillis)
            }
        }

        private val applyListener: RemoteConfigCompletedListener = object : RemoteConfigCompletedListener {
            override fun onSuccess() {
                Services.Log.debug(TAG, "Values applied correctly")
                applyApiListener?.onSuccess()
                applyApiListener = null
            }

            override fun onFailure(exception: Exception?) {
                Services.Log.error(TAG, "Values were not applied")
                applyApiListener?.onFailure(exception)
                applyApiListener = null
            }
        }
    }

    enum class FetchCriteria {
        WhenActivated, AfterElapsedTime, Manual
    }

    enum class ActivationCriteria {
        WhenLaunched, Immediately, Manual
    }

    enum class FetchStatus(i: Int) {
        None(-1), Success(0), Failure(1);
        val value = i
    }
}
