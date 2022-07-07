package com.genexus.android.uitesting.logging

import com.genexus.android.core.utils.UITestingUtils
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class TestWatcher(visualTestingServer: String) : TestWatcher() {
    override fun starting(description: Description?) {
        if (description == null)
            return

        UITestingUtils.Companion.setCurrentTest(description.className, description.methodName)
        TestLogger.startingTest(description.methodName)
    }

    override fun finished(description: Description?) {
        if (description == null)
            return

        TestLogger.endingTest(description.methodName)
    }

    init {
        // IdlingPolicies.setMasterPolicyTimeoutWhenDebuggerAttached(true) // uncomment to be able to debug UI Test without Espresso timeout
        UITestingUtils.setRunningUnderTest(true)
        UITestingUtils.visualTestingServer = visualTestingServer
    }
}
