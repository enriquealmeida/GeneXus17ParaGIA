package com.genexus.android.uitesting.matchers;

import android.view.View;

import com.genexus.android.layout.CustomScrollView;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controls.GxListView;
import com.genexus.android.uitestinglib.R;

import junit.framework.AssertionFailedError;

import org.hamcrest.Matcher;

import androidx.test.espresso.AmbiguousViewMatcherException;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.ViewInteraction;
import androidx.test.platform.app.InstrumentationRegistry;

import java.util.ArrayList;
import java.util.Locale;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.genexus.android.uitesting.action.CustomActionViews.customScrollTo;
import static com.genexus.android.uitesting.matchers.ControlMatchers.withName;
import static com.genexus.android.uitesting.matchers.DataMatchers.withItemPositionInGrid;
import static com.genexus.android.uitesting.matchers.DataMatchers.withMenuItemTitle;
import static com.genexus.android.uitesting.matchers.DataMatchers.withTextInRow;
import static com.genexus.android.uitesting.matchers.ViewMatchers.withActionTarget;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.either;

public class Selectors {

	public static ViewInteraction onControl(final Matcher<View> viewMatcher, String target, String context) {
		ViewInteraction viewInteraction = onControl(viewMatcher);

		if (context != null && !context.isEmpty()) {
			ContextHierarchy contextHierarchy = new ContextHierarchy(context, viewMatcher);
			if (contextHierarchy.isApplicationBar())
				tryOpeningMenu();
			else {
				ArrayList<Matcher<View>> hierarchyList = contextHierarchy.getContextList();
				viewInteraction = onControl(allOf(hierarchyList.toArray(new Matcher[]{})));
			}
		}

		tryFindingInHierarchyAndScroll(target);
		return viewInteraction;
	}

	private static ViewInteraction onControl(final Matcher<View> viewMatcher) {
		return onView(viewMatcher);
	}

	public static DataInteraction onGrid(Matcher<? extends Object> dataMatcher, String context) {
		return onData(dataMatcher);
	}

	/**
	 * <b>Note:</b> Works ONLY for List and Table menus (not Tabs).
	 */
	public static DataInteraction onMenu(Matcher<? extends Object> dataMatcher) {
		return onData(dataMatcher).inAdapterView(
			either(
				allOf(withId(R.id.DashBoardGridView), isDisplayed())
			).or(
				allOf(withId(R.id.DashBoardListView), isDisplayed())
			));
	}

	private static void tryFindingInHierarchyAndScroll(String target) {
		if (target != null && !target.isEmpty()) {
			try {
				onMenu(withMenuItemTitle(target))
					.perform(scrollTo())
					.check(matches(isDisplayed()));
			} catch (AssertionFailedError | PerformException | NoMatchingViewException menuException) {
				try {
					onGrid(withTextInRow(target), null)
						.perform(customScrollTo(GxListView.class))
						.check(matches(isDisplayed()));
					/*
					AmbiguousViewMatcherException is thrown by perform Action when Menu "Control" property is
					Platform Default but in this case scrolling is not needed because it would have already been
					performed by the first try block, thus execution not reaching this one.
					 */
				} catch (AssertionFailedError | PerformException | AmbiguousViewMatcherException | NoMatchingViewException gridException) {
					try {
						onControl(withActionTarget(target))
							.perform(customScrollTo(CustomScrollView.class))
							.check(matches(isDisplayed()));
					} catch (AssertionFailedError | PerformException | NoMatchingViewException controlException) {
						Services.Log.error("Control was not found on the screen.");
					}
				}
			}
		}
	}

	private static void tryOpeningMenu() {
		try {
			openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
		} catch (NoMatchingViewException e) {
			Services.Log.warning("Context is ApplicationBar but Menu couldn't be expanded", e);
		}
	}

	private static class ContextHierarchy {

		private boolean mIsApplicationBar = false;
		private ArrayList<Matcher<View>> mMatchers = new ArrayList<>();

		public ContextHierarchy(String context, Matcher<View> startingMatcher) {
			if (context.trim().isEmpty())
				throw new IllegalArgumentException("Context cannot be empty");

			parseContext(context);

			if (startingMatcher != null)
				mMatchers.add(startingMatcher);
		}

		public boolean isApplicationBar() {
			return mIsApplicationBar;
		}

		public ArrayList<Matcher<View>> getContextList() {
			return mMatchers;
		}

		private void parseContext(String context) {
			if (context.equalsIgnoreCase(UITestingContext.applicationBar.toString())) {
				mIsApplicationBar = true;
			} else if (!context.contains(".")) {
				mMatchers.add(isDescendantOfA(withName(context)));
			} else {
				String[] contextList = Services.Strings.split(context, ".");
				for (int i = 0; i < contextList.length; i++) {
					String aContext = contextList[i].toLowerCase(Locale.ROOT);
					if (aContext.matches("item[(][1-9]+[)]")) {
						int itemPosition = Integer.parseInt(aContext.replace("item(", "").replace(")", "")) - 1;
						String containingGrid = contextList[i - 1];
						mMatchers.remove(mMatchers.size() - 1); //last added context is removed because it belongs with an Item index
						mMatchers.add(isDescendantOfA(withItemPositionInGrid(containingGrid, itemPosition)));
					} else {
						mMatchers.add(isDescendantOfA(withName(aContext)));
					}
				}
			}
		}

		private enum UITestingContext {
			applicationBar
		}
	}
}
