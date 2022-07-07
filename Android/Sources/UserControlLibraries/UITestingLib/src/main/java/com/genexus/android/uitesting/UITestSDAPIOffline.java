package com.genexus.android.uitesting;

import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.ViewAction;

import com.genexus.android.core.base.services.Services;

import com.genexus.android.core.base.utils.ResultRunnable;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.usercontrols.GxSwitch;
import com.genexus.android.uitesting.action.CustomActionViews;
import com.genexus.android.uitesting.assertions.ViewAssertions;
import com.genexus.android.uitesting.getters.Condition;
import com.genexus.android.uitesting.getters.Control;
import com.genexus.android.uitesting.getters.Grid;
import com.genexus.android.uitesting.getters.Root;
import com.genexus.android.uitesting.idlingresources.WaitingIdlingResource;
import com.genexus.android.uitesting.utils.Preconditions;

import org.hamcrest.Matcher;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.doubleClick;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.PickerActions.setDate;
import static androidx.test.espresso.contrib.PickerActions.setTime;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.genexus.android.uitesting.action.CustomActionViews.openDateDialog;
import static com.genexus.android.uitesting.action.CustomActionViews.openTimeDialog;
import static com.genexus.android.uitesting.action.CustomActionViews.replaceText;
import static com.genexus.android.uitesting.action.CustomActionViews.takeSnapshot;
import static com.genexus.android.uitesting.matchers.ControlMatchers.withGroupActionTarget;
import static com.genexus.android.uitesting.matchers.ControlMatchers.withName;
import static com.genexus.android.uitesting.matchers.DataMatchers.withItemCount;
import static com.genexus.android.uitesting.matchers.Selectors.onControl;
import static com.genexus.android.uitesting.matchers.Selectors.onGrid;
import static com.genexus.android.uitesting.matchers.ViewMatchers.withActionTarget;
import static com.genexus.android.uitesting.utils.Misc.getTextMatcher;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;

public class UITestSDAPIOffline {
	public static void back() {
		pressBack();
	}

	public static void tap(String target) {
		tap(target, null);
	}

	public static void tap(String target, String context) {
		Runnable tap = () -> onControl(withActionTarget(target), target, context)
				.perform(click());
		logAndRun(tap, "tap", target, context);
	}

	public static void longTap(String target) {
		longTap(target, null);
	}

	public static void longTap(String target, String context) {
		Runnable longTap = () -> onControl(withActionTarget(target), target, context)
				.perform(longClick());
		logAndRun(longTap, "longTap", target, context);
	}

	public static void doubleTap(String target) {
		doubleTap(target, null);
	}

	public static void doubleTap(String target, String context) {
		Runnable doubleTap = () -> onControl(withActionTarget(target), target, context)
				.perform(doubleClick());
		logAndRun(doubleTap, "doubleTap", target, context);
	}

	public static void fill(String controlName, String value) {
		fill(controlName, value, null);
	}

	public static void fill(String controlName, String value, String context) {
		Runnable fill = () -> onControl(withName(controlName), controlName, context)
				.perform(replaceText(value));
		logAndRun(fill, "fill", controlName, value, context);
	}

	public static void pickDate(String controlName, Short year, Byte month, Byte day) {
		pickDate(controlName, year, month, day, null);
	}

	public static void pickDate(String controlName, Short year, Byte month, Byte day, String context) {
		Runnable pickDate = () -> {
			onControl(withName(controlName), controlName, context)
					.perform(openDateDialog());
			onView(withClassName(equalTo(DatePicker.class.getName())))
					.perform(setDate(year, month, day));
			onView(withId(android.R.id.button1))
					.perform(click());
		};
		logAndRun(pickDate, "pickDate", controlName, year, month, day, context);
	}

	public static void pickTime(String controlName, Byte hour, Byte minutes) {
		pickTime(controlName, hour, minutes, null);
	}

	public static void pickTime(String controlName, Byte hour, Byte minutes, String context) {
		Runnable pickTime = () -> {
			onControl(withName(controlName), controlName, context)
					.perform(openTimeDialog());
			onView(withClassName(equalTo(TimePicker.class.getName())))
					.perform(setTime(hour, minutes));
			onView(withId(android.R.id.button1))
					.perform(click());
		};
		logAndRun(pickTime, "pickTime", controlName, hour, minutes, context);
	}

	public static void pickDateTime(String controlName, Short year, Byte month, Byte day, Byte hour, Byte minutes) {
		pickDateTime(controlName, year, month, day, hour, minutes, null);
	}

	public static void pickDateTime(String controlName, Short year, Byte month, Byte day, Byte hour, Byte minutes, String context) {
		pickDate(controlName, year, month, day, context);
		pickTime(controlName, hour, minutes, context);
	}

	public static void selectValue(String controlName, String value) {
		selectValue(controlName, value, null);
	}

	public static void selectValue(String controlName, String value, String context) {
		Runnable selectValue = () -> onControl(withGroupActionTarget(controlName, value), controlName, context)
				.perform(CustomActionViews.selectValue(value));
		logAndRun(selectValue, "selectValue", controlName, value, context);
	}

	public static void swipe(Byte swipeDirection) {
		swipe(swipeDirection, null, null);
	}

	public static void swipe(Byte swipeDirection, String controlName) {
		swipe(swipeDirection, controlName, null);
	}

	public static void swipe(Byte swipeDirection, String controlName, String context) {
		Runnable swipe = () -> {
			ViewAction action;
			switch (swipeDirection) {
				case 1:
					action = swipeUp();
					break;
				case 2:
					action = swipeDown();
					break;
				case 3:
					action = swipeLeft();
					break;
				case 4:
					action = swipeRight();
					break;
				default:
					throw new IllegalArgumentException("Invalid swipe direction");
			}

			Matcher<View> viewMatcher = controlName == null ? isRoot() : withName(controlName);
			onControl(viewMatcher, controlName, context)
					.perform(action);
		};
		logAndRun(swipe, "swipe", swipeDirection, controlName, context);
	}

	public static void wait(Integer milliseconds) {
		Runnable wait = () -> IdlingRegistry.getInstance().register(new WaitingIdlingResource(milliseconds));
		logAndRun(wait, "wait", milliseconds);
	}

	public static void verifyText(String text) {
		verifyText(text, true, null);
	}

	public static void verifyText(String text, Boolean expected) {
		verifyText(text, expected, null);
	}

	public static void verifyText(String text, Boolean expected, String context) {
		Runnable verifyText = () -> onControl(withText(getTextMatcher(text)), text, context)
				.check(expected ? matches(isDisplayed()) : doesNotExist());
		logAndRun(verifyText, "verifyText", text, expected, context);
	}

	public static void verifyGridRowsCount(String gridControlName, Integer value) {
		verifyGridRowsCount(gridControlName, value, true, null);
	}

	public static void verifyGridRowsCount(String gridControlName, Integer value, Boolean expected) {
		verifyGridRowsCount(gridControlName, value, expected, null);
	}

	public static void verifyGridRowsCount(String gridControlName, Integer value, Boolean expected, String context) {
		Runnable verifyGridRowsCount = () -> onGrid(withName(gridControlName), context)
				.check(matches(expected ? withItemCount(value) : not(withItemCount(value))));
		logAndRun(verifyGridRowsCount, "verifyGridRowsCount", gridControlName, value, expected, context);
	}

	public static void verifyCheckbox(String controlName, Boolean value) {
		verifyCheckbox(controlName, value, null);
	}

	public static void verifyCheckbox(String controlName, Boolean value, String context) {
		Runnable verifyCheckbox = () -> onControl(withName(controlName), controlName, context)
				.check(matches(hasDescendant(value ? isChecked() : not(isChecked()))));
		logAndRun(verifyCheckbox, "verifyCheckbox", controlName, value, context);
	}

	public static void verifyControlValue(String controlName, String value) {
		verifyControlValue(controlName, value, true, null);
	}

	public static void verifyControlValue(String controlName, String value, Boolean expected) {
		verifyControlValue(controlName, value, expected, null);
	}

	public static void verifyControlValue(String controlName, String value, Boolean expected, String context) {
		Runnable verifyControlValue = () -> {
			Matcher<String> stringMatcher = getTextMatcher(value);
			Matcher<View> matcher = expected ? withText(stringMatcher) : not(withText(stringMatcher));
			onControl(withName(controlName), controlName, context)
					.check(matches(
							Preconditions.isDateTime(value) ?
									allOf(
											hasDescendant(expected ? withText(Services.Strings.split(value, ",")[0].trim())
													: not(withText(Services.Strings.split(value, ",")[0].trim()))),
											hasDescendant(expected ? withText(Services.Strings.split(value, ",")[1].trim())
													: not(withText(Services.Strings.split(value, ",")[1].trim())))
									) : anyOf(
									matcher,
									hasDescendant(matcher),
									hasDescendant(allOf(
											withClassName(equalTo(GxSwitch.class.getName())),
											((expected && (value.equals("1") || value.equalsIgnoreCase("true")))
													|| (!expected && (value.equals("0") || value.equalsIgnoreCase("false"))))
													? isChecked() : not(isChecked())))
							)
					));
		};
		logAndRun(verifyControlValue, "verifyControlValue", controlName, value, expected, context);
	}

	public static void verifyControlEnabled(String controlName) {
		verifyControlEnabled(controlName, true, null);
	}

	public static void verifyControlEnabled(String controlName, Boolean expected) {
		verifyControlEnabled(controlName, expected, null);
	}

	public static void verifyControlEnabled(String controlName, Boolean expected, String context) {
		Runnable verifyControlEnabled = () -> onControl(withName(controlName), controlName, context)
				.check(matches(expected ? isEnabled() : not(isEnabled())));
		logAndRun(verifyControlEnabled, "verifyControlEnabled", controlName, expected, context);
	}

	public static void verifyControlVisible(String controlName) {
		verifyControlVisible(controlName, true, null);
	}

	public static void verifyControlVisible(String controlName, Boolean expected) {
		verifyControlVisible(controlName, expected, null);
	}

	public static void verifyControlVisible(String controlName, Boolean expected, String context) {
		Runnable verifyControlVisible = () -> onControl(withName(controlName), controlName, context)
				.check(expected ? matches(isDisplayed()) : ViewAssertions.Companion.isNotPresent());
		logAndRun(verifyControlVisible, "verifyControlVisible", controlName, expected, context);
	}

	public static void verifyMsg(String text) {
		verifyMsg(text, true);
	}

	public static void verifyMsg(String text, Boolean expected) {
		Runnable verifyMsg = () -> {
			Matcher<String> matcher = getTextMatcher(text);
			onControl(withText(matcher), text, null)
					.inRoot(isDialog())
					.check(expected ? matches(isDisplayed()) : doesNotExist());
		};
		logAndRun(verifyMsg, "verifyMsg", text, expected);
	}

	public static void verifyScreenshot(String reference) {
		verifyScreenshot(reference, null, null);
	}

	public static void verifyScreenshot(String reference, String controlName) {
		verifyScreenshot(reference, controlName, null);
	}

	public static void verifyScreenshot(String reference, String controlName, String context) {
		Runnable verifyScreenshot = () -> {
			Matcher<View> viewMatcher = controlName != null ? withName(controlName) : withId(android.R.id.content);
			onControl(viewMatcher, controlName, context)
					.perform(takeSnapshot(reference))
					.check(ViewAssertions.Companion.verifySnapshot(reference));
		};
		logAndRun(verifyScreenshot, "verifyScreenshot", reference, controlName, context);
	}

	// Getters
	public static Boolean isControlEnabled(String controlName) {
		return isControlEnabled(controlName, null);
	}

	public static Boolean isControlEnabled(String controlName, String context) {
		ResultRunnable<Boolean> isControlEnabled = () -> new Control(controlName, context).isEnabled();
		return logAndRun(isControlEnabled, "isControlEnabled", controlName, context);
	}

	public static Boolean isControlVisible(String controlName) {
		return isControlVisible(controlName, null);
	}

	public static Boolean isControlVisible(String controlName, String context) {
		ResultRunnable<Boolean> isControlVisible = () -> new Control(controlName, context).isVisible();
		return logAndRun(isControlVisible, "isControlVisible", controlName, context);
	}

	public static String getControlValue(String controlName) {
		return getControlValue(controlName, null);
	}

	public static String getControlValue(String controlName, String context) {
		ResultRunnable<String> getControlValue = () -> new Control(controlName, context).getValue();
		return logAndRun(getControlValue, "getControlValue", controlName, context);
	}

	public static Boolean getCheckboxValue(String controlName) {
		return getCheckboxValue(controlName, null);
	}

	public static Boolean getCheckboxValue(String controlName, String context) {
		ResultRunnable<Boolean> getCheckboxValue = () -> new Control(controlName, context).getCheckboxValue();
		return logAndRun(getCheckboxValue, "getCheckboxValue", controlName, context);
	}

	public static int getGridRowsCount(String controlName) {
		return getGridRowsCount(controlName, null);
	}

	public static int getGridRowsCount(String controlName, String context) {
		ResultRunnable<Integer> getGridRowsCount = () -> new Grid(controlName, context).getRowsCount();
		return logAndRun(getGridRowsCount, "getGridRowsCount", controlName, context);
	}

	public static Boolean isShowingMessage() {
		ResultRunnable<Boolean> isShowingMessage = () -> new Root().isShowingMessage();
		return logAndRun(isShowingMessage, "isShowingMessage");
	}

	public static String getMessageText() {
		ResultRunnable<String> getMessageText = () -> new Root().getMessage();
		return logAndRun(getMessageText, "getMessageText");
	}

	// Condition verification
	public static void verifyCondition(boolean value) {
		verifyCondition(value, Strings.EMPTY);
	}

	public static void verifyCondition(boolean value, String message) {
		Runnable verifyCondition = () -> new Condition(value).verify(message);
		logAndRun(verifyCondition, "verifyCondition", value, message);
	}

	private static void logAndRun(Runnable r, String command, Object... parameters) {
		new TestCommand(command, parameters).logAndRun(r);
	}

	private static <T> T logAndRun(ResultRunnable<T> r, String command, Object... parameters) {
		return new TestCommand(command, parameters).logAndRun(r);
	}
}
