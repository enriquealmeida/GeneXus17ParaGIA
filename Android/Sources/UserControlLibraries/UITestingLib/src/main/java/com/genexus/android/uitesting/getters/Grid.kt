package com.genexus.android.uitesting.getters

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import com.genexus.android.core.fragments.GridContainer
import com.genexus.android.uitesting.matchers.ControlMatchers.withName
import com.genexus.android.uitesting.matchers.Selectors.onControl
import org.hamcrest.Matcher

class Grid(gridName: String, context: String?) {

    private val matcher = onControl(withName(gridName), gridName, context)

    fun getRowsCount(): Int {
        var count = 0
        val action = object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(GridContainer::class.java)
            }

            override fun getDescription(): String {
                return "get grid rows count"
            }

            override fun perform(uiController: UiController?, view: View) {
                val control = view as GridContainer
                count = control.getData().size
            }
        }

        matcher.perform(action)
        return count
    }
}
