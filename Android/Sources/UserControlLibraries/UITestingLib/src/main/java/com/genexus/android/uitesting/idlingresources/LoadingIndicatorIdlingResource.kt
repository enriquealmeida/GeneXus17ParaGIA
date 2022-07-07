package com.genexus.android.uitesting.idlingresources

import com.genexus.android.core.activities.ActivityHelper
import java.util.TimerTask

internal class LoadingIndicatorIdlingResource : TimedIdlingResource() {
    init {
        startChecking(object : TimerTask() {
            override fun run() {
                val currentActivity = ActivityHelper.getCurrentActivity()
                isIdle = if (currentActivity == null)
                    false
                else
                    IdleChecking.isLoadingIndicatorIdle(currentActivity) || IdleChecking.isAlertDialogIdle(currentActivity, null)
            }
        })
    }
}
