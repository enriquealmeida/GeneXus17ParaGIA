package com.genexus.android.core.base.services

import android.content.pm.ShortcutInfo
import com.genexus.android.core.base.metadata.DashboardMetadata

interface IShortcuts {
    fun addShortcut(title: String, imageName: String, id: String?, objectName: String?, action: String?, link: String?)
    fun addShortcuts(menuObject: DashboardMetadata?)
    fun removeShortcut(shortcutId: String)
    fun removeShortcuts()
    fun listShortcuts(): List<ShortcutInfo?>?
}
