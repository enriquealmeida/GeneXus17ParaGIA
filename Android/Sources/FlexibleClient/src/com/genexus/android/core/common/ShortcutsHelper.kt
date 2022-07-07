package com.genexus.android.core.common

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.RequiresApi
import com.genexus.android.core.activities.IntentFactory
import com.genexus.android.core.base.metadata.DashboardMetadata
import com.genexus.android.core.base.services.IShortcuts
import com.genexus.android.core.base.services.Services

@RequiresApi(api = Build.VERSION_CODES.N_MR1)
internal class ShortcutsHelper(private val mAppContext: Context) : IShortcuts {

    private val mShortcutManager: ShortcutManager? = mAppContext.getSystemService(ShortcutManager::class.java)

    override fun addShortcut(title: String, imageName: String, id: String?, objectName: String?, action: String?, link: String?) {
        val shortcutInfo = buildShortcut(title, imageName, id, objectName, action, link)
        mShortcutManager?.addDynamicShortcuts(listOf(shortcutInfo))
    }

    private fun buildShortcut(title: String, imageName: String, id: String?, objectName: String?, targetAction: String?, link: String?): ShortcutInfo {
        val icon = Icon.createWithResource(mAppContext, Services.Resources.getResourceId(imageName, "drawable"))
        val intent = buildIntent(targetAction, objectName, link)
        return ShortcutInfo.Builder(mAppContext, id)
            .setShortLabel(title)
            .setIcon(icon)
            .setIntent(intent)
            .build()
    }

    private fun buildIntent(targetAction: String?, shortcutsObject: String?, link: String?): Intent {
        return IntentFactory.createNotificationActionIntent(mAppContext, targetAction, link, shortcutsObject, true)
    }

    override fun addShortcuts(menuObject: DashboardMetadata?) {
        val shortcuts: MutableList<ShortcutInfo> = ArrayList()
        for (dashboardItem in menuObject!!.items) {
            if (shortcuts.size == 5) {
                Services.Log.warning("More than five shortcuts have been declared but only the first five will be used.")
                break
            }

            val title = dashboardItem.title
            val imageName = dashboardItem.imageName
            val id = dashboardItem.name
            val action = dashboardItem.actionDefinition.name
            val link = dashboardItem.objectName
            val objectName = menuObject.objectName
            shortcuts.add(buildShortcut(title, imageName, id, objectName, action, link))
        }
        removeShortcuts()
        mShortcutManager?.dynamicShortcuts = shortcuts
    }

    override fun removeShortcut(shortcutId: String) {
        mShortcutManager?.removeDynamicShortcuts(listOf(shortcutId))
    }

    override fun removeShortcuts() {
        mShortcutManager?.removeAllDynamicShortcuts()
    }

    override fun listShortcuts(): List<ShortcutInfo?>? {
        return mShortcutManager?.dynamicShortcuts
    }
}
