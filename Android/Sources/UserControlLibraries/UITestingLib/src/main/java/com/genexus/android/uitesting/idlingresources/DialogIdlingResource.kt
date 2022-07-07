package com.genexus.android.uitesting.idlingresources

import android.app.Dialog
import com.genexus.android.core.activities.ActivityHelper
import java.util.TimerTask

internal class DialogIdlingResource(dialog: Dialog?) : TimedIdlingResource() {
    init {
        startChecking(object : TimerTask() {
            override fun run() {
                val currentActivity = ActivityHelper.getCurrentActivity()

				/* When ActivityHelper.onPause is executed because permission request dialog is displayed, the current Activity
					cannot be null if running under test since otherwise the execution would get stuck at this point, as
					isIdleNow would always return false. As a result, TapSystemAlertIfShown method is never triggered
					and the test fails when timeout is reached. */
                isIdle = if (currentActivity == null)
                    false
                else
                    IdleChecking.isAlertDialogIdle(currentActivity, dialog)
            }
        })
    }
}
