package com.genexus.android.uitesting.getters

import android.view.View
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.util.HumanReadables
import androidx.test.espresso.util.TreeIterables
import com.genexus.android.core.base.utils.Strings
import org.hamcrest.Matcher
import java.util.concurrent.TimeoutException

class Root {

    private val messageMatcher = withId(android.R.id.message)

    fun isShowingMessage(): Boolean {
        return try {
            waitForView()
            true
        } catch (ignored: PerformException) {
            /* Method should return false instead of crashing the test when no dialog is present.
                 Problem is that if no custom 'waitFor(timeout)' is performed, Espresso will wait
                  at least 60 seconds for the view to appear before crashing. */
            false
        }
    }

    fun getMessage(): String {
        var message = Strings.EMPTY
        val action = object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(TextView::class.java)
            }

            override fun getDescription(): String {
                return "get dialog message"
            }

            override fun perform(uiController: UiController?, view: View) {
                message = (view as TextView).text.toString()
            }
        }

        waitForView()
        onView(messageMatcher).inRoot(isDialog()).perform(action)
        return message
    }

    private fun waitForView() {
        onView(isRoot()).perform(waitFor(TIMEOUT))
    }

    private fun waitFor(millis: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "wait for $millis milliseconds"
            }

            override fun perform(uiController: UiController, view: View) {
                uiController.loopMainThreadUntilIdle()
                val startTime = System.currentTimeMillis()
                val endTime = startTime + millis

                do {
                    for (child in TreeIterables.breadthFirstViewTraversal(view))
                        if (messageMatcher.matches(child))
                            return

                    uiController.loopMainThreadForAtLeast(50)
                } while (System.currentTimeMillis() < endTime)

                throw PerformException.Builder()
                    .withActionDescription(this.description)
                    .withViewDescription(HumanReadables.describe(view))
                    .withCause(TimeoutException())
                    .build()
            }
        }
    }

    companion object {
        private const val TIMEOUT = 3000
    }
}
