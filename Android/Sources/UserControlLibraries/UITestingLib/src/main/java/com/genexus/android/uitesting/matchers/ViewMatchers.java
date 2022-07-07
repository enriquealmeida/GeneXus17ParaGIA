package com.genexus.android.uitesting.matchers;

import android.view.View;

import androidx.annotation.NonNull;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.genexus.android.uitesting.matchers.ControlMatchers.withMenuItemText;
import static com.genexus.android.uitesting.matchers.ControlMatchers.withName;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalToIgnoringCase;

public class ViewMatchers {
	/**
	 * Returns a matcher that matches the first of either case:
	 * <ul>
	 *     <li>A Genexus control whose control name matches the value provided</li>
	 *     <li>A {@link View} that has a text that matches the value provided</li>
	 * </ul>
	 * The match for the text is <b>case insensitive</b>.
	 * <p>
	 * Note: Sugar for withActionTarget(equalToIgnoringCase("string")).
	 */
	public static Matcher<View> withActionTarget(@NonNull final String value) {
		return withActionTarget(equalToIgnoringCase(value));
	}

	/**
	 * Returns a matcher that matches the first of either case:
	 * <ul>
	 *     <li>A Genexus control whose control name matches the value provided</li>
	 *     <li>A {@link View} that has a text or description that matches the value provided</li>
	 * </ul>
	 * The match for the text is <b>case insensitive</b>.
	 * Description is needed for when an ActionMenuItem displays just an image
	 * <p>
	 */
	public static Matcher<View> withActionTarget(@NonNull final Matcher<String> stringMatcher) {
		return first(anyOf(withName(stringMatcher), withText(stringMatcher),
			withContentDescription(stringMatcher), withMenuItemText(stringMatcher)));
	}

	private static <T> Matcher<T> first(final Matcher<T> stringMatcher) {
		return new BaseMatcher<T>() {
			boolean isFirst = true;

			@Override
			public boolean matches(final Object item) {
				if (isFirst && stringMatcher.matches(item)) {
					isFirst = false;
					return true;
				}

				return false;
			}

			@Override
			public void describeTo(final Description description) {
				description.appendText("first of: ");
				stringMatcher.describeTo(description);
			}
		};
	}
}
