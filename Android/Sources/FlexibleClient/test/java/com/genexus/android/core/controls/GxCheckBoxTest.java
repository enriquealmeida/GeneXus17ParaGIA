package com.genexus.android.core.controls;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.content.Context;

import com.genexus.android.core.base.metadata.layout.ControlInfo;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.testing.MyTestApplication;
import com.genexus.android.testing.TestingUtils;

import androidx.test.core.app.ApplicationProvider;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(application = MyTestApplication.class)
public class GxCheckBoxTest {
	private static final String TITLE = "Title1";
	private static final String CHECK_VALUE = "Hello";
	private static final String UNCHECK_VALUE = "Goodbye";

	private Coordinator mCoordinator;
	private GxCheckBox mControl;

	@Before
	public void setupControl() {
		Context context = ApplicationProvider.getApplicationContext();
		mCoordinator = mock(Coordinator.class);

		ControlInfo controlInfo = new ControlInfo();
		controlInfo.setProperty(GxCheckBox.PROPERTY_TITLE, TITLE);
		controlInfo.setProperty(GxCheckBox.PROPERTY_CHECK_VALUE, CHECK_VALUE);
		controlInfo.setProperty(GxCheckBox.PROPERTY_UNCHECK_VALUE, UNCHECK_VALUE);
		LayoutItemDefinition definition = TestingUtils.createControlDefinition(controlInfo);

		mControl = new GxCheckBox(context, mCoordinator, definition);
	}

	@Test
	public void verifyControlTitle() {
		assertThat(mControl.getTitle().toString()).isEqualTo(TITLE);
	}

	@Test
	public void verifySettingCheckValue() {
		mControl.setGxValue(CHECK_VALUE);
		assertThat(mControl.getGxValue()).isEqualTo(CHECK_VALUE);
	}

	@Test
	public void verifySettingUncheckValue() {
		mControl.setGxValue(UNCHECK_VALUE);
		assertThat(mControl.getGxValue()).isEqualTo(UNCHECK_VALUE);
	}

	@Test
	public void verifyThatControlValueChangedEventIsNotTriggeredWhenSettingControlValueProgrammatically() {
		mControl.setGxValue(UNCHECK_VALUE);
        verify(mCoordinator, never()).onValueChanged(eq(mControl), anyBoolean());
	}

	@Test
	public void verifyThatControlValueChangedEventIsTriggeredWhenUserChangesControlValue() {
		mControl.setChecked(!mControl.isChecked()); // Simulate a user changing the control's value to a different one.
        verify(mCoordinator, times(1)).onValueChanged(mControl, true);
	}
}
