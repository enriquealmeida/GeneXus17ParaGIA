package com.genexus.android.uitesting.idlingresources

import androidx.test.espresso.IdlingResource

class CustomIdlingResource : IdlingResource {

    private var isIdle = false
    private var resourceCallback: IdlingResource.ResourceCallback? = null

    fun stopIdling() {
        isIdle = true
        resourceCallback?.onTransitionToIdle()
    }

    override fun getName(): String {
        return this.javaClass.name
    }

    override fun isIdleNow(): Boolean {
        return isIdle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        resourceCallback = callback
    }
}
