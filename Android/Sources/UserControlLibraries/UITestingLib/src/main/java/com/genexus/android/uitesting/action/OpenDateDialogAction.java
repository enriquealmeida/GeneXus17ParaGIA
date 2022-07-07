package com.genexus.android.uitesting.action;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import android.view.View;

import com.genexus.android.core.controls.DataBoundControl;
import com.genexus.android.core.controls.GxDateTimeEdit;
import com.genexus.diagnostics.Log;

import org.hamcrest.Matcher;

import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.allOf;

/**
 * Performs a click event on the Open Date Dialog button of the {@code GxDateTimeEdit}'s view. This
 * causes a {@code DatePicker} dialog to show up.
 */
class OpenDateDialogAction implements ViewAction {

	@Override
	public Matcher<View> getConstraints() {
		return allOf(
				isDisplayed(),
				isAssignableFrom(DataBoundControl.class),
				hasDescendant(isAssignableFrom(GxDateTimeEdit.class))
		);
	}

	@Override
	public String getDescription() {
		return "open date dialog";
	}

	@Override
	public void perform(UiController uiController, View view) {
		DataBoundControl control = (DataBoundControl) view;

		View attrView = control.getAttribute();

		if (!(attrView instanceof GxDateTimeEdit)) {
			Log.debug("Could not perform view action 'OpenDateDialog' on attribute field.");
			return;
		}

		GxDateTimeEdit dateTimeEdit = (GxDateTimeEdit) attrView;
		dateTimeEdit.getDateButton().performClick();
	}
}
