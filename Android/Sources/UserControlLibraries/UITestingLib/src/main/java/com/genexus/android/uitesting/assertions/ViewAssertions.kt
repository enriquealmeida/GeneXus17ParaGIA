package com.genexus.android.uitesting.assertions

import android.view.View
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.util.HumanReadables
import com.genexus.android.uitesting.visual.VerifySnapshotAssertion
import org.hamcrest.CoreMatchers.`is`

class ViewAssertions {
    companion object {
        fun verifySnapshot(reference: String): ViewAssertion {
            return VerifySnapshotAssertion(reference)
        }

        fun isNotPresent(): ViewAssertion = object : ViewAssertion {
            override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
                if (view == null || view.isNotVisible())
                    return

                var searchView: View = view
                while (searchView.parent != null && searchView.parent is View) {
                    searchView = searchView.parent as View
                    if (searchView.isNotVisible())
                        return
                }

                assertThat<Boolean>(
                    "View is present in the hierarchy and visible" + HumanReadables.describe(view),
                    true, `is`(false)
                )
            }
        }

        private fun View.isNotVisible(): Boolean {
            return this.visibility != View.VISIBLE
        }
    }
}
