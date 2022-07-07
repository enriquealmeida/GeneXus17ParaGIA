package com.genexus.android.uitesting.action;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import android.view.View;
import android.widget.EditText;

import com.genexus.android.core.controls.DataBoundControl;
import com.genexus.android.core.controls.GxEditText;
import com.genexus.diagnostics.Log;

import org.hamcrest.Matcher;

import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static com.genexus.android.uitesting.utils.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.allOf;

/**
 * Replaces a Genexus' edit text property by setting the given String.
 */
public class ReplaceTextGenexusAction implements ViewAction {
    private final String mStringToBeSet;

    public ReplaceTextGenexusAction(String value) {
        checkNotNull(value);
        mStringToBeSet = value;
    }

    @Override
    public Matcher<View> getConstraints() {
        return allOf(
                isDisplayed(),
                isAssignableFrom(DataBoundControl.class),
                hasDescendant(isAssignableFrom(GxEditText.class))
        );
    }

    @Override
    public String getDescription() {
        return "replace text";
    }

    @Override
    public void perform(UiController uiController, View view) {
        DataBoundControl control = (DataBoundControl) view;
        View attrView = control.getAttribute();

        if (!(attrView instanceof EditText)) {
            Log.debug("Could not perform view action 'ReplaceText' on attribute field.");
            return;
        }

        GxEditText editText = (GxEditText) attrView;
        editText.setGxValue(mStringToBeSet);
    }
}
