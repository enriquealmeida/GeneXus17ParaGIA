package com.genexus.android.superapps

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.genexus.android.core.activities.IntentParameters
import com.genexus.android.core.activities.IntentParameters.SuperApps
import com.genexus.android.core.activities.StartupActivity
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.superapps.MiniApp

internal class MiniAppStartupActivity : StartupActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        if (!intent.getBooleanExtra(SuperApps.LOAD, false))
            throw IllegalAccessException("MiniApp startup activity called but no LOAD parameter was found")

        super.onCreate(savedInstanceState)
    }

    override fun onNewIntent(intent: Intent?) {
        if (intent == null || !intent.getBooleanExtra(SuperApps.UNLOAD, false))
            throw IllegalAccessException("MiniApp startup activity called but no UNLOAD parameter was found")

        super.onNewIntent(intent)
        Services.Application.unloadMiniApp()
        Services.Application.lifecycle.notifyMiniApplicationStopped()
        finishAndRemoveTask()
    }

    companion object {
        fun getLoadIntent(context: Context, miniApp: MiniApp): Intent {
            return Intent(context, MiniAppStartupActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra(SuperApps.LOAD, true)
                putExtra(SuperApps.NAME, miniApp.name)
                putExtra(SuperApps.MAIN, miniApp.appEntry)
                putExtra(IntentParameters.SERVER_URL, miniApp.apiUri)
                putExtra(IntentParameters.RELOAD_METADATA, true)
            }
        }

        fun getExitIntent(context: Context): Intent {
            return Intent(context, MiniAppStartupActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                putExtra(SuperApps.UNLOAD, true)
            }
        }
    }
}
