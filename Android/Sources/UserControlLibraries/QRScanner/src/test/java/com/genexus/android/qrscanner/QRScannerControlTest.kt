package com.genexus.android.qrscanner

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition
import com.genexus.android.core.ui.Coordinator
import com.genexus.android.testing.MyTestApplication
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = MyTestApplication::class)
class QRScannerControlTest {
    private lateinit var mCoordinator: Coordinator
    private lateinit var mControl: QRScannerControl

    @Before
    fun setupControl() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        mCoordinator = mock(Coordinator::class.java)
        val definition = mock(LayoutItemDefinition::class.java)
        mControl = QRScannerControl(context, mCoordinator, definition)
    }

    @Test
    fun verifyThatControlValueChangedEventIsNotTriggeredWhenSettingControlValueProgrammatically() {
        mControl.gxValue = "QRCodeText"
        verify(mCoordinator, never()).onValueChanged(eq(mControl), anyBoolean())
    }

    @Test
    fun verifyThatControlValueChangedEventIsTriggeredWhenUserChangesControlValue() {
        val resultIntent = Intent()
        resultIntent.putExtra(QRScannerActivity.EXTRA_RESULT, "QRCodeText")
        mControl.handleOnActivityResult(QRScannerControl.REQUEST_CODE, Activity.RESULT_OK, resultIntent)
        verify(mCoordinator, times(1)).onValueChanged(eq(mControl), eq(true))
    }

    @Test
    fun verifyThatControlValueChangedEventIsNotTriggeredWhenControlValueHasNotChanged() {
        mControl.gxValue = "QRCodeText"
        val resultIntent = Intent()
        resultIntent.putExtra(QRScannerActivity.EXTRA_RESULT, "QRCodeText")
        mControl.handleOnActivityResult(QRScannerControl.REQUEST_CODE, Activity.RESULT_OK, resultIntent)
        verify(mCoordinator, never()).onValueChanged(eq(mControl), anyBoolean())
    }
}
