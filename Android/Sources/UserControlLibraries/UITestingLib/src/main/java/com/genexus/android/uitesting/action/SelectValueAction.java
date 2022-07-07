package com.genexus.android.uitesting.action;

import android.view.View;
import android.widget.RadioButton;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import com.genexus.android.core.controls.SpinnerControl;
import com.genexus.android.core.controls.common.ValueItem;
import com.genexus.diagnostics.Log;

import org.hamcrest.Matcher;

import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anyOf;

public class SelectValueAction implements ViewAction {

	private String mValue;

	public SelectValueAction(String value) {
		this.mValue = value;
	}

	@Override
	public Matcher<View> getConstraints() {
		return allOf(
			isDisplayed(),
			anyOf(
				isAssignableFrom(RadioButton.class),
				isAssignableFrom(SpinnerControl.class)
			)
		);
	}

	@Override
	public String getDescription() {
		return "select value";
	}

	@Override
	public void perform(UiController uiController, View attrView) {
		if (attrView instanceof RadioButton) {
			RadioButton radioButton = (RadioButton) attrView;
			radioButton.performClick();
		} else if (attrView instanceof SpinnerControl) {
			SpinnerControl spinner = (SpinnerControl) attrView;
			int count = spinner.getAdapter().getCount();
			boolean valueFound = false;
			for (int i = 0; i<count; i++) {
				ValueItem item = (ValueItem) spinner.getAdapter().getItem(i);
				String itemValue = String.valueOf(item.Description);
				if (mValue.equals(itemValue)) {
					spinner.setSelection(i, true);
					valueFound = true;
					break;
				}
			}

			if (!valueFound)
				Log.warning(String.format("Value %s was not found in control", mValue));

		} else {
			Log.debug("Could not perform view action 'SelectValue' on attribute view.");
		}
	}
}
