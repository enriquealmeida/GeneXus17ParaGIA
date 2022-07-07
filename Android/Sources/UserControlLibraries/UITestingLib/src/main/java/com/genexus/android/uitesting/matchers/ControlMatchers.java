package com.genexus.android.uitesting.matchers;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.test.espresso.matcher.BoundedMatcher;

import com.genexus.android.core.controls.IGxControl;
import com.genexus.android.layout.LayoutTag;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.DataBoundControl;
import com.genexus.android.core.controls.IGxEdit;
import com.genexus.android.core.controls.SpinnerControl;
import com.genexus.android.core.controls.common.ValueItem;
import com.genexus.android.core.usercontrols.RadioGroupControl;
import com.genexus.android.uitesting.utils.GenexusModel;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;

public class ControlMatchers {
    /**
     * Returns a matcher that matches {@link IGxControl} based on its control
     * name. The match for the name is <b>case insensitive</b>.
     * <p>
     * Note: Sugar for withName(equalToIgnoringCase("string")).
     */
    public static Matcher<View> withName(@NonNull final String name) {
        return withName(equalToIgnoringCase(name));
    }

    /**
     * Returns a matcher that matches {@link IGxControl} based on its control
     * name.
     */
    public static Matcher<View> withName(@NonNull final Matcher<String> stringMatcher) {
        return new TypeSafeMatcher<View>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("with control name: ");
                stringMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(View view) {
            	boolean isGridCheckbox = GenexusModel.isGridCheckbox(view);
	            if (!GenexusModel.isGenexusControl(view) && !isGridCheckbox)
		            return false;

	            String controlName;
	            LayoutItemDefinition definition = (LayoutItemDefinition) view.getTag(LayoutTag.CONTROL_DEFINITION);
	            if (definition != null)
		            controlName = definition.getName();
	            else if (isGridCheckbox)
					controlName = (String) view.getTag();
	            else
	            	return false;

                return stringMatcher.matches(controlName);
            }
        };
    }

    /**
     * Returns a matcher that matches {@link IGxControl} based on the theme
     * class name it has assigned. The match for the name is <b>case insensitive</b>.
     * <p>
     * Note: Sugar for withThemeClass(equalTo("string")).
     */
    public static Matcher<View> withThemeClass(@NonNull final String name) {
        return withThemeClass(equalToIgnoringCase(name));
    }

    /**
     * Returns a matcher that matches {@link IGxControl} based on the theme
     * class name it has assigned.
     */
    public static Matcher<View> withThemeClass(@NonNull final Matcher<String> stringMatcher) {
        return new TypeSafeMatcher<View>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("with theme class: ");
                stringMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(View view) {
                if (!GenexusModel.isGenexusControl(view)) {
                    return false;
                }

                Object themeClassTag = view.getTag(LayoutTag.CONTROL_THEME_CLASS);

                if (!(themeClassTag instanceof ThemeClassDefinition)) {
                    return false;
                }

                ThemeClassDefinition themeClassDefinition = (ThemeClassDefinition) themeClassTag;

                return stringMatcher.matches(themeClassDefinition.getName());
            }
        };
    }

    /**
     * Returns a matcher that matches {@link DataBoundControl} based on its caption string value.
     * The match for the name is <b>case sensitive</b>.
     * <p>
     * Note: Sugar for withLabelCaption(equalTo("string")).
     */
    public static Matcher<View> withLabelCaption(@NonNull final String label) {
        return withLabelCaption(equalTo(label));
    }

    /**
     * Returns a matcher that matches {@link DataBoundControl} based on its caption string value.
     */
    public static Matcher<View> withLabelCaption(@NonNull final Matcher<String> stringMatcher) {
        return new BoundedMatcher<View, DataBoundControl>(DataBoundControl.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("with control label: ");
                stringMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(DataBoundControl control) {
                return stringMatcher.matches(control.getLabel().getText().toString());
            }
        };
    }

    /**
     * Returns a matcher that matches {@link IGxEdit} based on its control value.
     * <p>
     * Note: Sugar for withControlValue(equalTo("string")).
     */
    public static Matcher<View> withControlValue(@NonNull final String value) {
        return withControlValue(equalTo(value));
    }

    /**
     * Returns a matcher that matches {@link IGxEdit} based on its control value.
     */
    public static Matcher<View> withControlValue(@NonNull final Matcher<String> stringMatcher) {
        return new BoundedMatcher<View, View>(View.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("with control value: ");
                stringMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(View view) {
                if (!(view instanceof IGxEdit)) {
                    return false;
                }
                IGxEdit control = (IGxEdit) view;
                return stringMatcher.matches(control.getGxValue());
            }
        };
    }

	/**
	 * Returns a matcher that matches {@link ActionMenuItemView} based on its caption string value.
	 * The match for the name is <b>case insensitive</b>.
	 * <p>
	 * Note: Sugar for withMenuItemText(equalToIgnoringCase("string")).
	 */
	public static Matcher<View> withMenuItemText(@NonNull final String caption) {
		return withMenuItemText(equalToIgnoringCase(caption));
	}

	/**
	 * Returns a matcher that matches {@link ActionMenuItemView} based on its caption string value.
	 */
	public static Matcher<View> withMenuItemText(@NonNull final Matcher<String> stringMatcher) {
		return new BoundedMatcher<View, ActionMenuItemView>(ActionMenuItemView.class) {

			@Override
			public void describeTo(Description description) {
				description.appendText("with appbar button text: ");
				stringMatcher.describeTo(description);
			}

			@SuppressLint("RestrictedApi")
			@Override
			protected boolean matchesSafely(ActionMenuItemView control) {
				String desc = Strings.EMPTY;
				String caption = Strings.EMPTY;

				if (control.getContentDescription() != null)
					desc = control.getContentDescription().toString();

				if (control.getItemData() != null && control.getItemData().getTitle() != null)
					caption = control.getItemData().getTitle().toString();

				return (!desc.isEmpty() && stringMatcher.matches(desc)) || (!caption.isEmpty() && stringMatcher.matches(caption));
			}
		};
	}

	/**
	 * Returns a matcher that matches {@link RadioButton} or {@link SpinnerControl} based on its control
	 * name and value.
	 */
	public static Matcher<View> withGroupActionTarget(@NonNull final String controlName, @NonNull final String value) {
		return anyOf(
			allOf(withParent(isAssignableFrom(RadioGroupControl.class)),
				anyOf(withText(equalToIgnoringCase(value)), withTagValue(equalTo(value)))),
			allOf(isAssignableFrom(SpinnerControl.class),
				withGroupActionTarget(equalToIgnoringCase(controlName), equalToIgnoringCase(value)))
		);
	}

	/**
	 * Returns a matcher that matches {@link RadioButton} or {@link SpinnerControl} based on its control
	 * name and value.
	 */
	public static Matcher<View> withGroupActionTarget(@NonNull final Matcher<String> controlMatcher,
													  @NonNull final Matcher<String> valueMatcher) {
		return new BoundedMatcher<View, View>(View.class) {

			@Override
			public void describeTo(Description description) {
				description.appendText("with control name: ");
				controlMatcher.describeTo(description);
				description.appendText(" and control value: ");
				valueMatcher.describeTo(description);
			}

			@Override
			protected boolean matchesSafely(View view) {
				String controlName = Strings.EMPTY;
				String controlValue = Strings.EMPTY;

				if (view instanceof RadioGroupControl) {
					RadioGroupControl radioGroup = (RadioGroupControl) view;
					controlName = radioGroup.getDefinition().getName();
					int count = radioGroup.getChildCount();
					for (int i = 0; i<count; i++) {
						RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
						String radioButtonValue = String.valueOf(radioButton.getText());
						if (valueMatcher.matches(radioButtonValue)) {
							controlValue = radioButtonValue;
							break;
						}
					}
				} else if (view instanceof SpinnerControl) {
					SpinnerControl spinner = (SpinnerControl) view;
					controlName = spinner.getDefinition().getName();
					int count = spinner.getAdapter().getCount();
					for (int i = 0; i<count; i++) {
						ValueItem item = (ValueItem) spinner.getAdapter().getItem(i);
						String itemValue = String.valueOf(item.Description);
						if (valueMatcher.matches(itemValue)) {
							controlValue = itemValue;
							break;
						}
					}
				} else {
					return false;
				}

				return controlMatcher.matches(controlName) && valueMatcher.matches(controlValue);
			}
		};
	}
}
