package com.genexus.android.uitesting.idlingresources

import androidx.test.espresso.IdlingResource
import androidx.test.espresso.IdlingResource.ResourceCallback
import java.util.Timer
import java.util.TimerTask

internal abstract class TimedIdlingResource : IdlingResource {

    private var resourceCallback: ResourceCallback? = null
    private var task: TimerTask? = null
    private val timer = Timer()
    private var isReady = true
    protected var isIdle = true
        set(value) {
            field = value
            if (field) onTransitionToIdle()
        }

    private fun check() {
        require(task != null) { "Task has to be set at this point" }
        if (isReady) {
            isReady = false
            timer.scheduleAtFixedRate(task, 0, 250)
        }
    }

    private fun onTransitionToIdle() {
        if (isIdle && resourceCallback != null) {
            resourceCallback?.onTransitionToIdle()
            timer.cancel()
            isReady = true
        }
    }

    override fun isIdleNow(): Boolean {
        if (!isIdle)
            check()

        return isIdle
    }

    override fun getName(): String {
        return this.javaClass.name
    }

    override fun registerIdleTransitionCallback(callback: ResourceCallback) {
        resourceCallback = callback
    }

    protected fun startChecking(timerTask: TimerTask) {
        task = timerTask
        check()
    }
}
