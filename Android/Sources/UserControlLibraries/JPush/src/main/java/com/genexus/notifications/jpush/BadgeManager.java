package com.genexus.notifications.jpush;

import android.content.Context;

import com.artech.base.services.Services;

import me.leolin.shortcutbadger.ShortcutBadger;

public class BadgeManager {
    private static final String PREFERENCE_BADGE_COUNT = "badgeCount";

    public static void removeBadge(Context context) {
        Services.Preferences.setLong(PREFERENCE_BADGE_COUNT, 0);
        ShortcutBadger.removeCount(context);
    }

    public static void setBadge(Context context, int count) {
        Services.Preferences.setLong(PREFERENCE_BADGE_COUNT, count);
        ShortcutBadger.applyCount(context, count);
    }

    public static void updateBadgeCount(Context context, int addCount) {
        int badgeCount = (int)Services.Preferences.getLong(PREFERENCE_BADGE_COUNT, 0);
        setBadge(context, Math.max(0, badgeCount + addCount));
    }
}
