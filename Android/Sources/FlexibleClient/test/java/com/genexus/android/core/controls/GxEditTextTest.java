package com.genexus.android.core.controls;

import android.content.Context;

import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.controls.GxEditText;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.testing.MyTestApplication;
import static com.google.common.truth.Truth.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;


import androidx.test.core.app.ApplicationProvider;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(application = MyTestApplication.class)
public class GxEditTextTest {
	private Coordinator mCoordinator;
	private GxEditText mControl;

	/**
	 * This test creates a {@link GxEditText} instance which has the following events defined:
	 * - ControlValueChanged
	 * - ControlValueChanging
	 */
	@Before
	public void setupControl() {
		Context context = ApplicationProvider.getApplicationContext();
		mCoordinator = mock(Coordinator.class);
		LayoutItemDefinition definition = mock(LayoutItemDefinition.class);

		when(definition.getEventHandler("ControlValueChanged"))
				.thenReturn(new ActionDefinition(null));

		when(definition.getEventHandler("ControlValueChanging"))
				.thenReturn(new ActionDefinition(null));

		mControl = new GxEditText(context, mCoordinator, definition);
	}

	@Test
	public void controlValueChangedEventShouldNotBeCalledWhenTextIsSetProgrammatically() {
		mControl.setGxValue("Text01");

		verify(mCoordinator, never()).onValueChanged(eq(mControl), anyBoolean());
	}

	@Test
	public void controlValueChangedShouldBeCalledWhenTextHasChangedAfterLosingFocus() {
		mControl.requestFocus();
		mControl.setText("Text02");
		mControl.clearFocus();

		verify(mCoordinator, times(1)).onValueChanged(eq(mControl), eq(true));
	}

	@Test
	public void controlValueChangingShouldNotBeCalledWhenInputTextIsChangedProgrammatically() {
		String newValue = "Text03";

		mControl.setGxValue(newValue);

		verify(mCoordinator, never()).onValueChanging(eq(mControl), any());
	}

	@Test
	public void controlValueChangingShouldBeCalledWhenInputTextIsChangedByTheUser() {
		String newValue = "Text04";

		mControl.setText(newValue);

		verify(mCoordinator, times(1)).onValueChanging(eq(mControl), eq(newValue));
	}

	@Test
	public void controlValueChangedShouldNoTBeCalledWhenValueDoesNotChange() {
		mControl.requestFocus();
		mControl.setText("Text05");
		mControl.clearFocus();

		verify(mCoordinator, times(1)).onValueChanged(eq(mControl), eq(true));

		mControl.requestFocus();
		mControl.setText("Text05");
		mControl.clearFocus();

		verify(mCoordinator, times(1)).onValueChanged(eq(mControl), eq(true));
	}

	@Test
	public void controlValueSetShouldPreserveSelectionAtTheEndWhenValueIsChanged() {
		String textBefore = "Text06";
		String textAfter = "Text06.";

		mControl.setText(textBefore);
		mControl.setSelection(textBefore.length()); // Sets the selection right after the '6'

		assertThat(mControl.getSelectionStart()).isEqualTo(textBefore.length());
		assertThat(mControl.getSelectionEnd()).isEqualTo(textBefore.length());

		mControl.setGxValue(textAfter);

		assertThat(mControl.getSelectionStart()).isEqualTo(textAfter.length());
		assertThat(mControl.getSelectionEnd()).isEqualTo(textAfter.length());
	}

	@Test
	public void controlValueSetShouldPreserveSelectionPositionWhenValueIsChanged() {
		String textBefore = "Text06";
		String textAfter = "Text06.";

		mControl.setText(textBefore);
		mControl.setSelection(2); // Sets the selection between the 'e' and the 'x'

		assertThat(mControl.getSelectionStart()).isEqualTo(2);
		assertThat(mControl.getSelectionEnd()).isEqualTo(2);

		mControl.setGxValue(textAfter);

		assertThat(mControl.getSelectionStart()).isEqualTo(2);
		assertThat(mControl.getSelectionEnd()).isEqualTo(2);
	}

	@Test
	public void controlValueSetShouldPreserveSelectionWhenValueIsChangedRemovingCharacters() {
		String textBefore = "Text06";
		String textAfter = "Text";

		mControl.setText(textBefore);
		mControl.setSelection(2, 5); // Sets the selection from the 'x' to the '0'

		assertThat(mControl.getSelectionStart()).isEqualTo(2);
		assertThat(mControl.getSelectionEnd()).isEqualTo(5);

		mControl.setGxValue(textAfter);

		assertThat(mControl.getSelectionStart()).isEqualTo(2);
		assertThat(mControl.getSelectionEnd()).isEqualTo(textAfter.length()); // 4
	}

	@Test
	public void shouldPreserveSelectionWhenSetLengthExceedsItsDefinedLength() {
		mControl.setMaximumLength(3);

		mControl.setGxValue("123456");

		assertThat(mControl.getSelectionStart()).isEqualTo(3);
		assertThat(mControl.getSelectionEnd()).isEqualTo(3);
	}
}
