package com.genexus.android.superapps

import android.location.Location
import com.genexus.GXBaseCollection
import com.genexus.GXGeospatial
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.Strings
import com.genexus.android.core.superapps.MiniAppCollection
import com.genexus.android.core.superapps.errors.SearchError
import com.genexus.android.core.tasking.OnCompleteListener
import com.genexus.android.core.tasking.Task
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object ProvisioningAPIOffline {
    @JvmStatic
    fun serverURL(): String {
        return Services.SuperApps.getProvisioningUrl()
    }

    @JvmStatic
    fun getByText(text: String?, start: Int?, count: Int?): GXBaseCollection<*> {
        return executeWaiting {
            Services.SuperApps.searchByText(text ?: Strings.EMPTY, start ?: DEF_START, count ?: DEF_COUNT)
                .addOnCompleteListener(searchCallback)
        }
    }

    @JvmStatic
    fun getByLocation(center: GXGeospatial?, radius: Int?, start: Int?, count: Int?): GXBaseCollection<*> {
        val remoteHandle = Services.Application.get().remoteHandle
        if (center == null)
            return MiniAppCollection().toBaseCollection(remoteHandle)

        val location = Location("GX").apply { latitude = center.latitude; longitude = center.longitude }
        return executeWaiting {
            Services.SuperApps.searchByLocation(location, radius ?: DEF_RADIUS, start ?: DEF_START, count ?: DEF_COUNT)
                .addOnCompleteListener(searchCallback)
        }
    }

    @JvmStatic
    fun getByTag(tag: String?, start: Int?, count: Int?): GXBaseCollection<*> {
        return executeWaiting {
            Services.SuperApps.searchByTag(tag ?: Strings.EMPTY, start ?: DEF_START, count ?: DEF_COUNT)
                .addOnCompleteListener(searchCallback)
        }
    }

    @JvmStatic
    fun getFeatured(start: Int?, count: Int?): GXBaseCollection<*> {
        return executeWaiting {
            Services.SuperApps.searchFeatured(start ?: DEF_START, count ?: DEF_COUNT)
                .addOnCompleteListener(searchCallback)
        }
    }

    private fun executeWaiting(runnable: Runnable): GXBaseCollection<*> {
        return runBlocking {
            suspendCoroutine {
                continuation = it
                runnable.run()
            }
        }
    }

    private var continuation: Continuation<GXBaseCollection<*>>? = null
    private val searchCallback = object : OnCompleteListener<MiniAppCollection, SearchError> {
        override fun onComplete(task: Task<MiniAppCollection, SearchError>) {
            if (task.isSuccessful)
                onSuccess(task.result!!)
            else
                onFailure()
        }

        private fun onSuccess(miniApps: MiniAppCollection) {
            finishExecution(miniApps)
        }

        private fun onFailure() {
            finishExecution(MiniAppCollection())
        }

        private fun finishExecution(miniApps: MiniAppCollection) {
            continuation?.resume(miniApps.toBaseCollection(Services.Application.get().remoteHandle))
        }
    }

    private const val DEF_RADIUS = 0
    private const val DEF_START = 0
    private const val DEF_COUNT = 10
}
