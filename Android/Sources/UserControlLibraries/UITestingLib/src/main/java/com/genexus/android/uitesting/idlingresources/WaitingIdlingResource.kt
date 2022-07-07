package com.genexus.android.uitesting.idlingresources

import androidx.test.espresso.IdlingRegistry
import java.util.TimerTask

internal class WaitingIdlingResource(millis: Int) : TimedIdlingResource() {

    private val waitTime = millis.toLong()
    private val startTime = System.currentTimeMillis()

    init {
        startChecking(object : TimerTask() {
            override fun run() {
                val elapsed = System.currentTimeMillis() - startTime
                isIdle = elapsed >= waitTime
                if (isIdle) IdlingRegistry.getInstance().unregister(this@WaitingIdlingResource)
            }
        })
    }
}
