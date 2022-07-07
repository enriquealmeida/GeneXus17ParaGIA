package com.genexus.android.core.common;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import android.content.Context;
import androidx.test.InstrumentationRegistry;
import androidx.test.filters.SmallTest;

import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.testing.OnDeviceTestingUtils;

import static com.google.common.truth.Truth.assertThat;

@RunWith(Parameterized.class)
@SmallTest
public class StringUtilTest {
    private final Locale mLocale;
    private final String mExpectedTwoDigitsDate;
    private final String mExpectedFourDigitsDate;
    private final String mExpectedHourTime;
    private final String mExpectedHourMinsTime;
    private final String mExpectedHourMinsSecsTime;
    private StringUtil mStringUtil;
    private Date mDate;

    @Parameterized.Parameters
    public static Collection<Object []> locales() {
        return Arrays.asList(new Object[][]{
                        {Locale.US, "1/17/15", "1/17/2015", "8 PM", "8:40 PM", "8:40:53 PM"},
                        {Locale.ITALY, "17/01/15", "17/01/2015", "8 PM", "8:40 PM", "8:40:53 PM"},
                        {Locale.GERMANY, "17.01.15", "17.01.2015", "8 nachm.", "8:40 nachm.", "8:40:53 nachm."},
                        {Locale.JAPAN, "15/01/17", "2015/01/17", "8 午後", "8:40 午後", "8:40:53 午後"}
                }
        );
    }

    public StringUtilTest(Locale locale, String twoDigitDate, String fourDigitsDate, String hourTime, String hourMinTime, String hourMinSecsTime) {
        mLocale = locale;
        mExpectedTwoDigitsDate = twoDigitDate;
        mExpectedFourDigitsDate = fourDigitsDate;
        mExpectedHourTime = hourTime;
        mExpectedHourMinsTime = hourMinTime;
        mExpectedHourMinsSecsTime = hourMinSecsTime;
    }

    @Before
    public void setup() {
        Context appContext = InstrumentationRegistry.getTargetContext().getApplicationContext();
        OnDeviceTestingUtils.setLocale(appContext, mLocale);
        mStringUtil = new StringUtil(appContext);
        mDate = new GregorianCalendar(2015, Calendar.JANUARY, 17, 20, 40, 53).getTime();
    }

    @Test
    public void getDateString() {
        assertThat(mStringUtil.getDateString(mDate, "99/99/99")).isEqualTo(mExpectedTwoDigitsDate);
        assertThat(mStringUtil.getDateString(mDate, "99/99/9999")).isEqualTo(mExpectedFourDigitsDate);
    }

    @Test
    public void getTimeString() {
        assertThat(mStringUtil.getTimeString(mDate, "99")).isEqualTo(mExpectedHourTime);
        assertThat(mStringUtil.getTimeString(mDate, "99:99")).isEqualTo(mExpectedHourMinsTime);
        assertThat(mStringUtil.getTimeString(mDate, "99:99:99")).isEqualTo(mExpectedHourMinsSecsTime);
    }

    @Test
    public void getDateTimeString() {
        assertThat(mStringUtil.getDateTimeString(mDate, "99/99/99 99"))
                .isEqualTo(mExpectedTwoDigitsDate + Strings.SPACE + mExpectedHourTime);
        assertThat(mStringUtil.getDateTimeString(mDate, "99/99/99 99:99"))
                .isEqualTo(mExpectedTwoDigitsDate + Strings.SPACE + mExpectedHourMinsTime);
        assertThat(mStringUtil.getDateTimeString(mDate, "99/99/99 99:99:99"))
                .isEqualTo(mExpectedTwoDigitsDate + Strings.SPACE + mExpectedHourMinsSecsTime);
        assertThat(mStringUtil.getDateTimeString(mDate, "99/99/9999 99"))
                .isEqualTo(mExpectedFourDigitsDate + Strings.SPACE + mExpectedHourTime);
        assertThat(mStringUtil.getDateTimeString(mDate, "99/99/9999 99:99"))
                .isEqualTo(mExpectedFourDigitsDate + Strings.SPACE + mExpectedHourMinsTime);
        assertThat(mStringUtil.getDateTimeString(mDate, "99/99/9999 99:99:99"))
                .isEqualTo(mExpectedFourDigitsDate + Strings.SPACE + mExpectedHourMinsSecsTime);
    }
}