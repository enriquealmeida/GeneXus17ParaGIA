package com.genexus.android.uitesting

import com.genexus.android.core.base.utils.ResultRunnable
import com.genexus.android.uitesting.logging.TestLogger

internal class TestCommand(private val commandName: String, vararg parameters: Any?) {

    private val params = parameters.asList()

    fun logAndRun(r: Runnable) {
        val rr = ResultRunnable { r.run() }
        logAndRun(rr)
    }

    fun <T> logAndRun(r: ResultRunnable<T>): T? {
        var executionFailed = false
        val startMillis = System.currentTimeMillis()
        val result: T?
        TestLogger.startingCommand(commandName, params)
        try {
            result = r.run()
        } catch (e: Throwable) {
            executionFailed = true
            throw e
        } finally {
            val duration = System.currentTimeMillis() - startMillis
            TestLogger.endingCommand(commandName, duration, executionFailed)
        }
        return result
    }
}
