package com.genexus.android.core.utils

import com.genexus.android.core.base.utils.Strings
import java.util.concurrent.atomic.AtomicBoolean

class UITestingUtils {
    companion object {
        private var runningUnderTest = AtomicBoolean(false)

        var dialogPresent = false

        var currentTest = Strings.EMPTY
            private set
        var currentTestClass = Strings.EMPTY
            private set
        var visualTestingServer = Strings.EMPTY

        fun setCurrentTest(testClass: String, testName: String) {
            currentTest = testName
            currentTestClass = testClass
        }

        @Synchronized
        fun setRunningUnderTest(value: Boolean) {
            runningUnderTest.set(value)
        }

        @Synchronized
        fun isRunningUnderTest(): Boolean {
            return runningUnderTest.get()
        }
    }
}
