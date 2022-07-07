package com.genexus.android.core.controls;

import android.content.Context;
import android.net.Uri;

import com.genexus.android.core.base.metadata.layout.ControlInfo;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.controls.media.GxMediaEditControl;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.testing.MyTestApplication;
import com.genexus.android.testing.TestingUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import androidx.test.core.app.ApplicationProvider;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(application = MyTestApplication.class)
public class GxMediaEditControlTest {
    private Coordinator mCoordinator;
    private GxMediaEditControl mControl;

    @Before
    public void setupControl() {
        Context context = ApplicationProvider.getApplicationContext();
        mCoordinator = mock(Coordinator.class);

        ControlInfo controlInfo = new ControlInfo();
        LayoutItemDefinition definition = TestingUtils.createControlDefinition(controlInfo);

        mControl = new GxMediaEditControl(context, mCoordinator, definition);
    }

    @Test
    public void verifyThatControlValueChangedEventIsNotTriggeredWhenControlValueIsSetByTheFramework() {
        mControl.setGxValue("file:///blablabla.png");

        verify(mCoordinator, never()).onValueChanged(eq(mControl), eq(true));
    }

    @Test
    public void verifyThatControlValueChangedEventIsTriggeredWhenControlValueIsChangedByTheUserToADifferentOne() {
        mControl.onMediaChanged(Uri.parse("file:///blablabla.png"), true, true);

        verify(mCoordinator, times(1)).onValueChanged(eq(mControl), eq(true));
    }
}
