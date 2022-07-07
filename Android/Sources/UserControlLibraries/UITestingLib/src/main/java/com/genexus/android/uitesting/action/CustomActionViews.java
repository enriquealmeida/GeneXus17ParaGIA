package com.genexus.android.uitesting.action;

import android.view.View;

import com.genexus.android.uitesting.visual.TakeSnapshotAction;

import androidx.annotation.NonNull;
import androidx.test.espresso.ViewAction;

import static androidx.test.espresso.action.ViewActions.actionWithAssertions;

public class CustomActionViews {
    /**
     * Returns an action that updates the text attribute of a {@code DataBoundControl}.
     * <br>
     * <br>
     * View preconditions:
     * <ul>
     * <li>must be displayed on screen
     * <li>must be assignable from {@code DataBoundControl}
     * <li>must have a descendant that is assignable from {@code GxEditText}</li>
     * </ul>
     */
    public static ViewAction replaceText(@NonNull String stringToBeSet) {
        return actionWithAssertions(new ReplaceTextGenexusAction(stringToBeSet));
    }

    /**
     * Returns an action that opens a {@code DatePicker} dialog for a {@code GxDateTimeEdit}
     * attribute.
     * <br>
     * <br>
     * View preconditions:
     * <ul>
     * <li>must be displayed on screen
     * <li>must be assignable from {@code DataBoundControl}
     * <li>must have a descendant that is assignable from {@code GxDateTimeEdit}</li>
     * </ul>
     */
    public static ViewAction openDateDialog() {
        return actionWithAssertions(new OpenDateDialogAction());
    }

    /**
     * Returns an action that opens a {@code TimePicker} dialog for a {@code GxDateTimeEdit}
     * attribute.
     * <br>
     * <br>
     * View preconditions:
     * <ul>
     * <li>must be displayed on screen
     * <li>must be assignable from {@code DataBoundControl}
     * <li>must have a descendant that is assignable from {@code GxDateTimeEdit}</li>
     * </ul>
     */
    public static ViewAction openTimeDialog() {
        return actionWithAssertions(new OpenTimeDialogAction());
    }

    public static ViewAction customScrollTo(Class<? extends View> clazz) {
    	return actionWithAssertions(new CustomScrollAction(clazz));
	}

	/**
	 * Returns an action that selects a value in a {@code RadioGroupControl} or {@code SpinnerControl}
	 * attribute.
	 * <br>
	 * <br>
	 * View preconditions:
	 * <ul>
	 * <li>must be displayed on screen
	 * <li>must be assignable from {@code RadioButton} or {@code SpinnerControl}
	 * </ul>
	 */
    public static ViewAction selectValue(String value) {
    	return actionWithAssertions(new SelectValueAction(value));
	}

	public static ViewAction takeSnapshot(String reference) {
    	return actionWithAssertions(new TakeSnapshotAction(reference));
	}
}
