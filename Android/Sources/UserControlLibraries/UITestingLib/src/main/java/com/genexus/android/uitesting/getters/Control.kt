package com.genexus.android.uitesting.getters

import android.view.View
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withChild
import com.genexus.android.core.base.utils.Strings
import com.genexus.android.core.controls.DataBoundControl
import com.genexus.android.core.controls.GxCheckBox
import com.genexus.android.uitesting.matchers.ControlMatchers.withName
import com.genexus.android.uitesting.matchers.Selectors.onControl
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

class Control(controlName: String, context: String?) {

    private val matcher = onControl(withName(controlName), controlName, context)

    fun isEnabled(): Boolean {
        var enabled = false
        val action = object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(DataBoundControl::class.java)
            }

            override fun getDescription(): String {
                return "get control enabled"
            }

            override fun perform(uiController: UiController?, view: View) {
                val control = view as DataBoundControl
                enabled = control.isEnabled
            }
        }

        matcher.perform(action)
        return enabled
    }

    fun isVisible(): Boolean {
        var visible = false
        val action = object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(DataBoundControl::class.java)
            }

            override fun getDescription(): String {
                return "get control visible"
            }

            override fun perform(uiController: UiController?, view: View) {
                val control = view as DataBoundControl
                visible = control.visibility == View.VISIBLE
            }
        }

        try { matcher.perform(action) } catch (ignored: NoMatchingViewException) {}
        return visible
    }

    fun getValue(): String {
        var value = Strings.EMPTY
        val action = object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(DataBoundControl::class.java)
            }

            override fun getDescription(): String {
                return "get control value"
            }

            override fun perform(uiController: UiController?, view: View) {
                val control = view as DataBoundControl
                value = control.gxValue
            }
        }

        matcher.perform(action)
        return value
    }

    fun getCheckboxValue(): Boolean {
        var checked = false
        val action = object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return allOf(isAssignableFrom(DataBoundControl::class.java), withChild(isAssignableFrom(GxCheckBox::class.java)))
            }

            override fun getDescription(): String {
                return "get checkbox control value"
            }

            override fun perform(uiController: UiController?, view: View) {
                val control = (view as DataBoundControl).attribute as GxCheckBox
                checked = control.isChecked
            }
        }

        matcher.perform(action)
        return checked
    }
}
