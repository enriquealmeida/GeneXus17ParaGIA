package com.genexus.android.uitesting.matchers;

import androidx.annotation.NonNull;
import androidx.test.espresso.matcher.BoundedMatcher;
import android.view.View;

import com.genexus.android.core.controls.IGxGridControl;
import com.genexus.android.layout.LayoutTag;
import com.genexus.android.core.base.metadata.DashboardItem;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.controls.grids.GridItemLayout;
import com.genexus.android.core.fragments.GridContainer;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;

public class DataMatchers {
    /**
     * Returns a matcher that matches a <b>Grid Row</b> based on its text content.
     * <p>
     * Note: Sugar for withTextInRow(equalTo("string"))
     */
    public static Matcher<Object> withTextInRow(@NonNull final String text) {
        return withTextInRow(equalTo(text));
    }

    /**
     * Returns a matcher that matches a <b>Grid Row</b> based on its text content.
     */
    public static Matcher<Object> withTextInRow(@NonNull final Matcher<String> stringMatcher) {
        return new BoundedMatcher<Object, Entity>(Entity.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("with text in row: ");
                stringMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(Entity item) {
                Iterable<Object> values = item.getPropertyValues();
                for (Object value : values) {
                    if (value instanceof String) {
                        String str = (String) value;
                        if (stringMatcher.matches(str)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        };
    }

    /**
     * Returns a matcher that matches an {@link GridContainer} based on its item count.
     * <p>
     * Note: Sugar for withItemCount(equalTo("integer"))
     */
    public static Matcher<View> withItemCount(@NonNull final Integer count) {
        return withItemCount(equalTo(count));
    }

    /**
     * Returns a matcher that matches an {@link GridContainer} based on its item count.
     */
    public static Matcher<View> withItemCount(@NonNull final Matcher<Integer> intMatcher) {
        return new BoundedMatcher<View, GridContainer>(GridContainer.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("with item count: ");
                intMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(GridContainer item) {
                return intMatcher.matches(((IGxGridControl)item).getData().size());
            }
        };
    }

    /**
     * Returns a matcher that matches a <b>Menu Item</b> based on its title string value.
     * <p>
     * Note: Sugar for withMenuItemTitle(equalTo("string"))
     */
    public static Matcher<Object> withMenuItemTitle(@NonNull final String title) {
        return withMenuItemTitle(equalTo(title));
    }

    /**
     * Returns a matcher that matches a <b>Menu Item</b> based on its title string value.
     */
    public static Matcher<Object> withMenuItemTitle(@NonNull final Matcher<String> stringMatcher) {
        return new BoundedMatcher<Object, DashboardItem>(DashboardItem.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("with menu item title: ");
                stringMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(DashboardItem item) {
                return stringMatcher.matches(item.getTitle());
            }
        };
    }

	/**
	 * Returns a matcher that matches a <b>Grid Item</b> based on its index and containing Grid name.
	 * <p>
	 * Note: Sugar for withItemPositionInGrid(equalToIgnoringCase(gridName), equalTo(position))
	 */
	public static Matcher<View> withItemPositionInGrid(@NonNull final String gridName, @NonNull final int position) {
		return withItemPositionInGrid(equalToIgnoringCase(gridName), equalTo(position));
	}

	/**
	 * Returns a matcher that matches a <b>Grid Item</b> based on its index and containing Grid name.
	 */
	public static Matcher<View> withItemPositionInGrid(@NonNull final Matcher<String> gridMatcher, @NonNull final Matcher<Integer> positionMatcher) {
		return new BoundedMatcher<View, GridItemLayout>(GridItemLayout.class) {

			@Override
			public void describeTo(Description description) {
				description.appendText("with item position: ");
				positionMatcher.describeTo(description);
				description.appendText(" in Grid: ");
				gridMatcher.describeTo(description);
			}

			@Override
			protected boolean matchesSafely(GridItemLayout item) {
				LayoutItemDefinition definition = (LayoutItemDefinition) item.getParentGrid().getTag(LayoutTag.CONTROL_DEFINITION);

				if (definition == null) {
					return false;
				}

				String controlName = definition.getName();
				return positionMatcher.matches(item.getPosition()) && gridMatcher.matches(controlName);
			}
		};
	}
}
