package com.genexus.android.uitesting.logging

import com.genexus.GXutil
import com.genexus.android.core.base.utils.Strings

class TestLogger {
    companion object {
        fun startingTest(testName: String) {
            log("Starting test '$testName'")
        }

        fun endingTest(testName: String) {
            log("Ending test '$testName'")
        }

        fun startingCommand(commandName: String, parameters: List<Any?>?) {
            val params = getParametersText(parameters)
            log("Starting command '$commandName' $params")
        }

        fun endingCommand(commandName: String, duration: Long, executionFailed: Boolean) {
            val durationText = "${duration}ms"
            var text = "Ending command '$commandName' after $durationText "
            text += if (executionFailed) "FAILED" else "SUCCESS"
            log(text)
        }

        private fun log(text: String) {
            val log = "[$TAG] $text"
            println(log)
        }

        private fun getParametersText(params: List<Any?>?): String {
            val sb = StringBuilder("with parameters ")
            if (params.isNullOrEmpty() || params.filterNotNull().isEmpty()) {
                sb.append("[<none>]")
                return sb.toString()
            }

            val list = params.filterNotNull()
            sb.append("[")
            list.forEachIndexed { i, value ->
                val v = value.toString().replace(GXutil.newLine(), Strings.SPACE)
                sb.append("'$v'")
                if (i != list.size - 1)
                    sb.append(", ")
                else
                    sb.append("]")
            }

            return sb.toString()
        }

        private const val TAG = "UITestingLibLogger"
    }
}
