package com.genexus.android.uitesting.matchers

import com.genexus.android.core.base.metadata.DashboardItem
import com.genexus.android.core.base.model.EntityFactory
import com.genexus.android.core.base.model.EntityList
import com.genexus.android.core.fragments.GridContainer
import com.genexus.android.testing.MyTestApplication
import com.google.common.truth.Truth.assertThat
import org.hamcrest.Matchers
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Unit tests for [DataMatchers].
 */
@RunWith(RobolectricTestRunner::class)
@Config(application = MyTestApplication::class)
class DataMatchersTest {
    @Test
    fun withTextInRowString() {
        val text1 = "SampleText1"
        val text2 = "SampleText2"
        val item = EntityFactory.newEntity()
        item.setProperty("PropName1", text1)
        assertThat(DataMatchers.withTextInRow(text1).matches(item)).isTrue()
        assertThat(DataMatchers.withTextInRow(text2).matches(item)).isFalse()
    }

    @Test
    fun withMenuItemTitleString() {
        val text1 = "SampleText1"
        val text2 = "SampleText2"
        val item = DashboardItem(null)
        item.title = text1
        assertThat(DataMatchers.withMenuItemTitle(text1).matches(item)).isTrue()
        assertThat(DataMatchers.withMenuItemTitle(text2).matches(item)).isFalse()
    }

    @Test
    fun withItemCountInt() {
        val data = mock<EntityList>()
        whenever(data.size).thenReturn(10)
        val view = mock<GridContainer>()
        whenever(view.getData()).thenReturn(data)
        assertThat(DataMatchers.withItemCount(10).matches(view)).isTrue()
        assertThat(DataMatchers.withItemCount(Matchers.lessThan(11)).matches(view)).isTrue()
        assertThat(DataMatchers.withItemCount(Matchers.greaterThan(9)).matches(view)).isTrue()
    }
}
