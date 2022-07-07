package com.genexus.android.superapps.configuration

import com.genexus.android.core.base.services.Services
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

internal class MiniAppInvalidator {

    fun run(maxCount: Int, maxDays: Int) {
        Thread {
            if (maxCount > 0) deleteByCount(maxCount)
            if (maxDays > 0) deleteByTime(TimeUnit.DAYS.toNanos(maxDays.toLong()))
        }.start()
    }

    private fun deleteByCount(maxMiniAppCount: Int) {
        val cachedMiniApps = Services.SuperApps.getCachedMiniApps()
        if (cachedMiniApps.size <= maxMiniAppCount)
            return

        cachedMiniApps.sortBy { miniApp -> miniApp.lastUsedDate }
        for (i in 0 until cachedMiniApps.size - maxMiniAppCount) {
            val id = cachedMiniApps[i].id
            val version = cachedMiniApps[i].version
            if (id.isNullOrEmpty())
                continue

            if (Services.SuperApps.removeMiniApp(id, version))
                Services.Log.info("Removing '$id', version '$version' as configured max MiniApp count has been exceeded.")
        }
    }

    private fun deleteByTime(keepForNanos: Long) {
        for (miniApp in Services.SuperApps.getCachedMiniApps()) {
            val lastUsed = miniApp.lastUsedDate
            val id = miniApp.id
            val version = miniApp.version
            if (lastUsed == null || id.isNullOrEmpty())
                continue

            if (LocalDateTime.now().isAfter(lastUsed.plusNanos(keepForNanos))) {
                if (Services.SuperApps.removeMiniApp(id, version))
                    Services.Log.info("Removing '$id', version '$version' as configured expiration time has passed.")
            }
        }
    }
}
