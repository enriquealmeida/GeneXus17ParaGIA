package com.genexus.tasks

import android.content.Context
import com.genexus.android.core.application.LifecycleListeners
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.superapps.MiniApp
import com.genexus.android.core.superapps.errors.LoadError
import com.genexus.android.core.tasking.Task
import com.genexus.android.superapps.MiniAppHelper
import com.genexus.android.superapps.configuration.MiniAppInvalidator

internal class LoadTask(private val context: Context, private val maxCount: Int, private val maxDays: Int) :
    Task<Boolean, LoadError>("MiniAppLoading") {

    fun load(miniApp: MiniApp): Task<Boolean, LoadError> {
        execute run@{
            val app = MiniAppHelper(miniApp, context)
            if (app.readyToLaunch()) {
                app.launch()
                onSuccess(true)
                return@run
            }

            val miniAppMetadataFile = app.downloadMetadata()
            if (miniAppMetadataFile == null) {
                Services.Log.error(String.format("MiniApp '%s' metadata download failed.", miniApp.appEntry))
                onFailure(LoadError.DOWNLOAD)
                return@run
            }

            app.setMetadataFile(miniAppMetadataFile)
            if (!app.isSignatureValid()) {
                Services.Log.error(String.format("MiniApp '%s' signature verification failed.", miniApp.appEntry))
                app.deleteCompressedMetadata()
                onFailure(LoadError.SIGNATURE)
                return@run
            }

            if (miniApp.apiUri.isEmpty() || miniApp.appEntry.isEmpty()) {
                Services.Log.error(String.format("MiniApp '%s' is not downloaded nor was it found on %s", miniApp.appEntry, miniApp.apiUri))
                onFailure(LoadError.INFORMATION)
                return@run
            }

            purge()
            app.launch()
            onSuccess(true)
        }
        return this
    }

    private fun purge() {
        val lifecycle = Services.Application.lifecycle
        lifecycle.registerMiniApplicationLifecycleListener(object : LifecycleListeners.MiniApp {
            override fun onMiniAppStarted() {
                MiniAppInvalidator().run(maxCount, maxDays)
                lifecycle.unregisterMiniApplicationLifecycleListener(this)
            }

            override fun onMiniAppStopped() {}
        })
    }
}
