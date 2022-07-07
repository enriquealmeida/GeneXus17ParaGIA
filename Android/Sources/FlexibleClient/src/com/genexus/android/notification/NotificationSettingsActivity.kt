package com.genexus.android.notification

import android.os.Bundle
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import com.genexus.android.core.actions.UIContext
import com.genexus.android.core.activities.IntentFactory
import com.genexus.android.core.base.metadata.enums.Connectivity
import com.genexus.android.core.base.metadata.enums.DisplayModes
import com.genexus.android.core.base.metadata.loader.MetadataLoader
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.common.PreferencesHelper
import com.genexus.android.core.init.AppInitRunnable
import com.genexus.android.core.utils.TaskRunner

class NotificationSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.categories.contains(NOTIFICATIONS_CATEGORY))
            TaskRunner.execute { listener.appFinishedLoading(AppInitRunnable.checkAndLoadApplicationResult()) }
    }

    val listener: Listener = object : Listener {
        override fun appFinishedLoading(success: Boolean) {
            if (!success) {
                Services.Log.error("App failed to load. Notification Panel won't be executed")
                finish()
            }

            executeSettingsPanel()
        }
    }

    @UiThread
    private fun executeSettingsPanel() {
        val notificationsPanelGuid = Services.Preferences.getString(PreferencesHelper.PREFERENCES_NOTIFICATIONS_PANEL)
        val notificationsPanelName = MetadataLoader.getObjectName(notificationsPanelGuid)
        val definition = Services.Application.definition.getView(notificationsPanelName)
        var connectivity = definition.connectivitySupport
        if (connectivity == Connectivity.Inherit) connectivity = Connectivity.Online
        val intent = IntentFactory.getIntent(
            UIContext.base(this, connectivity), definition,
            null, DisplayModes.VIEW, null
        )
        startActivity(intent)
        finish()
    }

    interface Listener {
        fun appFinishedLoading(success: Boolean)
    }

    companion object {
        // Value is not declared in Intent class
        private const val NOTIFICATIONS_CATEGORY = "android.intent.category.NOTIFICATION_PREFERENCES"
    }
}
