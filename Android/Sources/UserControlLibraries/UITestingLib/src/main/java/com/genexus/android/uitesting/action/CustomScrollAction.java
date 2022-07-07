package com.genexus.android.uitesting.action;

import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;

import androidx.core.widget.NestedScrollView;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.util.HumanReadables;

import com.genexus.android.layout.CustomScrollView;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controls.GxListView;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static org.hamcrest.Matchers.anyOf;

public class CustomScrollAction<T extends Class> implements ViewAction {

	private Class mClass;

	public CustomScrollAction(Class clazz) {
		this.mClass = clazz;
	}

	@Override
	public Matcher<View> getConstraints() {
		return Matchers.allOf(
			isDescendantOfA(anyOf(
				isAssignableFrom(CustomScrollView.class),
				isAssignableFrom(NestedScrollView.class),
				isAssignableFrom(GxListView.class))),
			withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE));
	}

	@Override
	public String getDescription() {
		return "nested scroll to";
	}

	@Override
	public void perform(UiController uiController, View view) {
		if (isDisplayingAtLeast(90).matches(view)) {
			Services.Log.info("View is already displayed.");
			return;
		}

		View customScrollView = findParentByClass(view, mClass);
		if (customScrollView != null) {
			customScrollView.scrollTo(0, view.getTop());
		} else {
			throw new PerformException.Builder()
				.withActionDescription(this.getDescription())
				.withViewDescription(HumanReadables.describe(view))
				.withCause(new Throwable("Unable to find CustomScrollView parent."))
				.build();
		}
		uiController.loopMainThreadUntilIdle();
	}

	private View findParentByClass(View view, Class<? extends View> parentClass) {
		ViewParent parent = new FrameLayout(view.getContext());
		ViewParent incrementView = null;
		boolean firstTry = true;
		while (parent != null && !(parent.getClass() == parentClass)) {
			if (firstTry) {
				parent = getParent(view);
				firstTry = false;
			} else {
				parent = getParent(incrementView);
			}
			incrementView = parent;
		}
		return (View) parent;
	}

	private ViewParent getParent(View view) {
		return view.getParent();
	}

	private ViewParent getParent(ViewParent view) {
		return view.getParent();
	}

}
