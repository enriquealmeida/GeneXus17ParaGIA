package com.genexus.android.core.externalobjects

import com.genexus.android.core.base.services.Services
import com.genexus.android.remoteconfig.RemoteConfig
import com.genexus.android.remoteconfig.RemoteConfigCompletedListener
import java.util.Date
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

class RemoteConfigAPIOffline {
    companion object {

        @JvmStatic
        fun lastSuccessfulFetch(): Date {
            return RemoteConfig.lastSuccessfulFetch()
        }

        @JvmStatic
        fun lastFetchStatus(): Long {
            return RemoteConfig.lastFetchStatus()
        }

        @JvmStatic
        fun hasValue(key: String?): Boolean {
            return RemoteConfig.hasValue(key)
        }

        @JvmStatic
        fun getStringValue(key: String): String {
            return RemoteConfig.getStringValue(key)
        }

        @JvmStatic
        fun getIntegerValue(key: String): Int {
            return RemoteConfig.getIntegerValue(key)
        }

        @JvmStatic
        fun getDecimalValue(key: String): Double {
            return RemoteConfig.getDecimalValue(key)
        }

        @JvmStatic
        fun getBooleanValue(key: String): Boolean {
            return RemoteConfig.getBooleanValue(key)
        }

        @JvmStatic
        fun getDateValue(key: String): Date {
            return RemoteConfig.getDateValue(key)
        }

        @JvmStatic
        fun getDateTimeValue(key: String): Date {
            return RemoteConfig.getDateTimeValue(key)
        }

        @JvmStatic
        fun fetch(): Boolean {
            return executeWaiting(Runnable { RemoteConfig.fetch(completedListener) })
        }

        @JvmStatic
        fun apply(): Boolean {
            return executeWaiting(Runnable { RemoteConfig.apply(completedListener) })
        }

        private fun executeWaiting(runnable: Runnable): Boolean {
            lock.lock()
            try {
                Thread(runnable).run()
                condition.await(3000, TimeUnit.MILLISECONDS)
            } catch (ignored: InterruptedException) {
            } finally {
                lock.unlock()
            }
            return result ?: false
        }

        private var result: Boolean? = null
        private val lock: Lock = ReentrantLock()
        private val condition = lock.newCondition()
        private val completedListener: RemoteConfigCompletedListener = object : RemoteConfigCompletedListener {
            override fun onSuccess() {
                finishExecution(true)
            }

            override fun onFailure(exception: Exception?) {
                exception?.let { Services.Log.error(it) }
                finishExecution(false)
            }

            private fun finishExecution(success: Boolean) {
                lock.lock()
                result = success
                condition.signal()
            }
        }
    }
}
