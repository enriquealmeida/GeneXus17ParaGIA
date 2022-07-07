package com.genexus.android.core.controls;

import android.content.Context;

import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.AttributeDefinition;
import com.genexus.android.core.base.metadata.DataItem;
import com.genexus.android.core.base.metadata.DataTypeName;
import com.genexus.android.core.base.metadata.Properties;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.controls.GxEditTextNumeric;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.testing.MyTestApplication;

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
public class GxEditTextNumericTest {
	private Coordinator mCoordinator;
	private GxEditTextNumeric mControl;

	/**
	 * This test creates a {@link GxEditTextNumeric} instance which has the following events defined:
	 * - ControlValueChanged
	 * - ControlValueChanging
	 */
	@Before
	public void setupControl() {
		Context context = ApplicationProvider.getApplicationContext();

		mCoordinator = mock(Coordinator.class);
		LayoutItemDefinition definition = mock(LayoutItemDefinition.class);

		DataItem dataItem = new DataItem(new AttributeDefinition(null));
		dataItem.setProperty("Length", "8");
		dataItem.setProperty("Decimals", "2");
		dataItem.setProperty("Signed", false);
		dataItem.setProperty("InputPicture", "ZZZZ9.99");

		when(definition.getDataItem())
				.thenReturn(dataItem);

		when(definition.getDataTypeName())
				.thenReturn(new DataTypeName("numeric"));

		when(definition.getLabelPosition())
				.thenReturn(Properties.LabelPositionType.TOP);

		when(definition.getEventHandler("ControlValueChanged"))
				.thenReturn(new ActionDefinition(null));

		when(definition.getEventHandler("ControlValueChanging"))
				.thenReturn(new ActionDefinition(null));

		mControl = new GxEditTextNumeric(context, mCoordinator, definition);
	}


	@Test
	public void controlValueChangedEventShouldNotBeCalledWhenValueIsSetProgrammatically() {
		mControl.setGxValue("1.1");

		verify(mCoordinator, never()).onValueChanged(eq(mControl), anyBoolean());
	}

	@Test
	public void controlValueChangedShouldBeCalledWhenValueHasChangedAfterLosingFocus() {
		mControl.requestFocus();
		mControl.setText("2.1");
		mControl.clearFocus();

		verify(mCoordinator, times(1)).onValueChanged(eq(mControl), eq(true));
	}

	@Test
	public void controlValueChangingShouldNotBeCalledWhenValueIsChangedProgrammatically() {
		String newValue = "3.1";

		mControl.setGxValue(newValue);

		verify(mCoordinator, never()).onValueChanging(eq(mControl), any());
	}

	@Test
	public void controlValueChangingShouldBeCalledWhenValueIsChangedByTheUser() {
		String newValue = "4.1";
		mControl.setText(newValue);

		verify(mCoordinator, times(1)).onValueChanging(eq(mControl), eq(newValue));
	}

	@Test
	public void controlValueChangedShouldNoTBeCalledWhenValueDoesNotChange() {
		mControl.requestFocus();
		mControl.setText("5.1");
		mControl.clearFocus();

		verify(mCoordinator, times(1)).onValueChanged(eq(mControl), eq(true));

		mControl.requestFocus();
		mControl.setText("5.1");
		mControl.clearFocus();

		verify(mCoordinator, times(1)).onValueChanged(eq(mControl), eq(true));
	}
}
