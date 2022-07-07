package com.genexus.android.core.controls

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.genexus.android.core.base.metadata.AttributeDefinition
import com.genexus.android.core.base.metadata.DataItem
import com.genexus.android.core.base.metadata.DataTypeName
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition
import com.genexus.android.core.ui.Coordinator
import com.genexus.android.testing.MyTestApplication
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = MyTestApplication::class)
public class GxDateTimeEditTest {

    private lateinit var mControlDateTime: GxDateTimeEdit
    private lateinit var mControlDateTimeSeconds: GxDateTimeEdit
    private lateinit var mControlDate: GxDateTimeEdit
    private lateinit var mControlTime: GxDateTimeEdit

    /**
     * This test creates a [GxDateTimeEdit] instance
     */
    @Before
    fun setupControl() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val mockCoordinator = Mockito.mock(Coordinator::class.java)

        val definitionDateTime = Mockito.mock(LayoutItemDefinition::class.java)
        val dataItemDateTime = DataItem(AttributeDefinition(null))
        dataItemDateTime.setProperty("Length", "8")
        dataItemDateTime.setProperty("Decimals", "5")
        dataItemDateTime.setProperty("InputPicture", "99/99/99 99:99:99")
        Mockito.`when`(definitionDateTime.dataItem)
            .thenReturn(dataItemDateTime)
        Mockito.`when`(definitionDateTime.dataTypeName)
            .thenReturn(DataTypeName("datetime"))
        mControlDateTime = GxDateTimeEdit(context, mockCoordinator, definitionDateTime)
        val definitionDateTimeSeconds = Mockito.mock(
            LayoutItemDefinition::class.java
        )
        val dataItemDateTimeSeconds = DataItem(AttributeDefinition(null))
        dataItemDateTimeSeconds.setProperty("Length", "8")
        dataItemDateTimeSeconds.setProperty("Decimals", "8")
        dataItemDateTimeSeconds.setProperty("InputPicture", "99/99/99 99:99:99")
        Mockito.`when`(definitionDateTimeSeconds.dataItem)
            .thenReturn(dataItemDateTimeSeconds)
        Mockito.`when`(definitionDateTimeSeconds.dataTypeName)
            .thenReturn(DataTypeName("datetime"))
        mControlDateTimeSeconds = GxDateTimeEdit(context, mockCoordinator, definitionDateTimeSeconds)
        val definitionDate = Mockito.mock(
            LayoutItemDefinition::class.java
        )
        val dataItemDate = DataItem(AttributeDefinition(null))
        dataItemDate.setProperty("Length", "8")
        dataItemDate.setProperty("Decimals", "0")
        dataItemDate.setProperty("InputPicture", "99/99/99")
        Mockito.`when`(definitionDate.dataItem)
            .thenReturn(dataItemDate)
        Mockito.`when`(definitionDate.dataTypeName)
            .thenReturn(DataTypeName("date"))
        mControlDate = GxDateTimeEdit(context, mockCoordinator, definitionDate)
        val definitionTime = Mockito.mock(
            LayoutItemDefinition::class.java
        )
        val dataItemTime = DataItem(AttributeDefinition(null))
        dataItemTime.setProperty("Length", "0")
        dataItemTime.setProperty("Decimals", "5")
        dataItemTime.setProperty("InputPicture", "99:99")
        Mockito.`when`(definitionTime.dataItem)
            .thenReturn(dataItemTime)
        Mockito.`when`(definitionTime.dataTypeName)
            .thenReturn(DataTypeName("datetime"))
        mControlTime = GxDateTimeEdit(context, mockCoordinator, definitionTime)
    }

    // gx value datetime format is "yyyy-MM-dd HH:mm:ss"
    @Test
    fun shouldPreserveSetValueDateTime() {
        // set value
        mControlDateTime.gxValue = "2018-10-12 19:05:11"
        // check value, not preserver seconds for picture
        Truth.assertThat(mControlDateTime.gxValue).isEqualTo("2018-10-12 19:05:00")
    }

    @Test
    fun invalidValueDateTime() {
        // set invalid value
        mControlDateTime.gxValue = "20181012190511"
        // check value
        Truth.assertThat(mControlDateTime.gxValue).isEqualTo("0000-00-00T00:00:00")
    }

    @Test
    fun shouldPreserveSetValueDateTimeSeconds() {
        // set value with seconds
        mControlDateTimeSeconds.gxValue = "2018-10-12 19:05:11"
        // check value, preserver seconds
        Truth.assertThat(mControlDateTimeSeconds.gxValue).isEqualTo("2018-10-12 19:05:11")
    }

    @Test
    fun invalidValueDateTimeSeconds() {
        // set invalid value
        mControlDateTimeSeconds.gxValue = "20181012190511"
        // check value
        Truth.assertThat(mControlDateTimeSeconds.gxValue).isEqualTo("0000-00-00T00:00:00")
    }

    @Test
    fun shouldPreserveSetValueDate() {
        // set value
        mControlDate.gxValue = "2018-10-12"
        // check value
        Truth.assertThat(mControlDate.gxValue).isEqualTo("2018-10-12")
    }

    @Test
    fun invalidValueDate() {
        // set invalid value
        mControlDate.gxValue = "20181012"
        // check value
        Truth.assertThat(mControlDate.gxValue).isEqualTo("0000-00-00")
    }

    @Test
    fun shouldPreserveSetValueTime() {
        // set value, not sure why null date is 0001 for time in server.
        mControlTime.gxValue = "0001-01-01 19:15"
        // check value,
        Truth.assertThat(mControlTime.gxValue).isEqualTo("0001-01-01 19:15:00")
    }

    @Test
    fun invalidValueTime() {
        // set invalid value
        mControlTime.gxValue = "201810121915"
        // check value, this crash before fix in this commit
        Truth.assertThat(mControlTime.gxValue).isEqualTo("0000-00-00T00:00:00")
    }
}
