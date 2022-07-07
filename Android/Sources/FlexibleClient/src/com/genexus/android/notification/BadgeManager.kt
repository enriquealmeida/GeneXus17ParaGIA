package com.genexus.android.notification

import android.content.Context
import com.genexus.android.core.base.services.Services
import me.leolin.shortcutbadger.ShortcutBadger

object BadgeManager {
    private const val PREFERENCE_BADGE_COUNT = "badgeCount"

    fun removeBadge(context: Context) {
        Services.Preferences.setLong(PREFERENCE_BADGE_COUNT, 0)
        ShortcutBadger.removeCount(context)
    }

    fun setBadge(context: Context, count: Int) {
        Services.Preferences.setLong(PREFERENCE_BADGE_COUNT, count.toLong())
        ShortcutBadger.applyCount(context, count)
    }

    fun updateBadgeCount(context: Context, addCount: Int) {
        val badgeCount = Services.Preferences.getLong(PREFERENCE_BADGE_COUNT, 0).toInt()
        setBadge(context, Math.max(0, badgeCount + addCount))
    }
}
