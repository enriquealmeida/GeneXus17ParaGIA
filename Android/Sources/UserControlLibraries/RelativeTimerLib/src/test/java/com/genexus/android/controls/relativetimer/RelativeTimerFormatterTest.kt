package com.genexus.android.controls.relativetimer

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import net.time4j.CalendarUnit.DAYS
import net.time4j.CalendarUnit.MONTHS
import net.time4j.ClockUnit.HOURS
import net.time4j.ClockUnit.MILLIS
import net.time4j.ClockUnit.MINUTES
import net.time4j.ClockUnit.SECONDS
import net.time4j.PlainTimestamp
import net.time4j.android.ApplicationStarter
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.Locale

@RunWith(RobolectricTestRunner::class)
class RelativeTimerFormatterTest {
    private lateinit var formatter: RelativeTimerFormatter
    private lateinit var timestamp: PlainTimestamp

    private fun testTimestamp(): PlainTimestamp {
        return PlainTimestamp.of(2019, 11, 15, 15, 12, 5) // use fixed time to avoid time zone and different month lenghts
    }

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        ApplicationStarter.initialize(context, true) // initialize Time4A library
        formatter = RelativeTimerFormatter()
        timestamp = testTimestamp()
    }

    @Test
    fun `should return properly formatted text when given a non-zero positive interval of time`() {
        val text = formatter.getText(timestamp, timestamp.plus(1, SECONDS))

        assertThat(text).isEqualTo("00:00:01")
    }

    @Test
    fun `should return properly formatted text avoiding double zero when counting down`() {
        val textOneHalfSecond = formatter.getText(timestamp, timestamp.plus(1500, MILLIS))
        val textHalfSecond = formatter.getText(timestamp, timestamp.plus(500, MILLIS))
        val textNegativeHalfSecond = formatter.getText(timestamp, timestamp.plus(-500, MILLIS))
        val textNegativeOneSecond = formatter.getText(timestamp, timestamp.plus(-1, SECONDS))
        val textNegativeTwoSecond = formatter.getText(timestamp, timestamp.plus(-2, SECONDS))

        // counting down it doesn't show zero twice
        assertWithMessage("1500 millis").that(textOneHalfSecond).isEqualTo("00:00:01")
        assertWithMessage("500 millis").that(textHalfSecond).isEqualTo("00:00:00")
        assertWithMessage("-500 millis").that(textNegativeHalfSecond).isEqualTo("00:00:01")

        // round numbers still get the correct value
        assertWithMessage("-1 second").that(textNegativeOneSecond).isEqualTo("00:00:01")
        assertWithMessage("-2 seconds").that(textNegativeTwoSecond).isEqualTo("00:00:02")
    }

    @Test
    fun `should return desired text when given min and max value`() {
        formatter.config.minSeconds = 2
        formatter.config.maxSeconds = 4
        formatter.config.maxText = "Max reached"
        formatter.config.minText = "Min reached"

        val textOneSecond = formatter.getText(timestamp, timestamp.plus(1, SECONDS))
        val textTwoSecond = formatter.getText(timestamp, timestamp.plus(2, SECONDS))
        val textThreeSecond = formatter.getText(timestamp, timestamp.plus(3, SECONDS))
        val textFourSecond = formatter.getText(timestamp, timestamp.plus(4, SECONDS))
        val textFiveSecond = formatter.getText(timestamp, timestamp.plus(5, SECONDS))

        assertWithMessage("excedes min value").that(textOneSecond).isEqualTo("Min reached")
        assertWithMessage("equals min value").that(textTwoSecond).isEqualTo("Min reached")
        assertWithMessage("normal formatted value").that(textThreeSecond).isEqualTo("00:00:03")
        assertWithMessage("equals max value").that(textFourSecond).isEqualTo("Max reached")
        assertWithMessage("excedes max value").that(textFiveSecond).isEqualTo("Max reached")
    }

    @Test
    fun `should return properly formatted text when given prefix and suffix`() {
        formatter.config.prefix = "Before "
        formatter.config.suffix = " After"

        val text = formatter.getText(timestamp, timestamp.plus(1, SECONDS))

        assertThat(text).isEqualTo("Before 00:00:01 After")
    }

    @Test
    fun `should return properly formatted text when given default (Auto) countingType`() {
        val textOneSecond = formatter.getText(timestamp, timestamp.plus(1, SECONDS))
        val textZero = formatter.getText(timestamp, timestamp)
        val textNegativeOneSecond = formatter.getText(timestamp, timestamp.plus(-1, SECONDS))

        assertWithMessage("1 second").that(textOneSecond).isEqualTo("00:00:01")
        assertWithMessage("0 seconds").that(textZero).isEqualTo("00:00:00")
        assertWithMessage("-1 seconds").that(textNegativeOneSecond).isEqualTo("00:00:01")
    }

    @Test
    fun `should return properly formatted text when given countingType Down`() {
        formatter.config.countingType = "Down"

        val textOneSecond = formatter.getText(timestamp, timestamp.plus(1, SECONDS))
        val textZero = formatter.getText(timestamp, timestamp)
        val textNegativeOneSecond = formatter.getText(timestamp, timestamp.plus(-1, SECONDS))

        assertWithMessage("1 second").that(textOneSecond).isEqualTo("00:00:01")
        assertWithMessage("0 seconds").that(textZero).isEqualTo("00:00:00")
        assertWithMessage("-1 seconds").that(textNegativeOneSecond).isEqualTo("00:00:00")
    }

    @Test
    fun `should return properly formatted text when given countingType Up`() {
        formatter.config.countingType = "Up"

        val textOneSecond = formatter.getText(timestamp, timestamp.plus(1, SECONDS))
        val textZero = formatter.getText(timestamp, timestamp)
        val textNegativeOneSecond = formatter.getText(timestamp, timestamp.plus(-1, SECONDS))

        assertWithMessage("1 second").that(textOneSecond).isEqualTo("00:00:00")
        assertWithMessage("0 seconds").that(textZero).isEqualTo("00:00:00")
        assertWithMessage("-1 seconds").that(textNegativeOneSecond).isEqualTo("00:00:01")
    }

    @Test
    fun `should return properly formatted text when given mostRepresentativeUnitOnly in true`() {
        val value = timestamp.plus(1, MINUTES).plus(20, SECONDS)
        val textFalse = formatter.getText(timestamp, value)
        formatter.config.mostRepresentativeUnitOnly = true
        val textTrue = formatter.getText(timestamp, value)

        assertWithMessage("without mostRepresentativeUnitOnly").that(textFalse).isEqualTo("00:01:20")
        assertWithMessage("with mostRepresentativeUnitOnly").that(textTrue).isEqualTo("00:01:00")
    }

    @Test
    fun `should return properly formatted text when given different timerUnits using default (Positional) timerFormat`() {
        val value = timestamp.plus(5, DAYS).plus(3, HOURS).plus(1, MINUTES).plus(20, SECONDS)
        formatter.config.timerUnits = "s"
        val textSeconds = formatter.getText(timestamp, value)
        formatter.config.timerUnits = "m"
        val textMinutes = formatter.getText(timestamp, value)
        formatter.config.timerUnits = "sm"
        val textSecondMinutes = formatter.getText(timestamp, value)
        formatter.config.timerUnits = "sh"
        val textSecondHours = formatter.getText(timestamp, value)
        formatter.config.timerUnits = "smd"
        val textSecondDays = formatter.getText(timestamp, value)
        formatter.config.timerUnits = "md"
        val textMinuteDays = formatter.getText(timestamp, value)

        assertWithMessage("seconds").that(textSeconds).isEqualTo("442880")
        assertWithMessage("minutes").that(textMinutes).isEqualTo("7381")
        assertWithMessage("minutes and seconds").that(textSecondMinutes).isEqualTo("7381:20")
        assertWithMessage("hours and seconds").that(textSecondHours).isEqualTo("123:00:80")
        assertWithMessage("days, minutes and seconds").that(textSecondDays).isEqualTo("5d 181:20")
        assertWithMessage("days and minutes").that(textMinuteDays).isEqualTo("5d 181")
    }

    @Test
    fun `should return properly formatted text when given different timerUnits using timerFormat Full`() {
        formatter.config.timerFormat = "Full"

        val value = timestamp.plus(5, DAYS).plus(3, HOURS).plus(1, MINUTES).plus(20, SECONDS)
        formatter.config.timerUnits = "s"
        val textSeconds = formatter.getText(timestamp, value)
        formatter.config.timerUnits = "m"
        val textMinutes = formatter.getText(timestamp, value)
        formatter.config.timerUnits = "sm"
        val textSecondMinutes = formatter.getText(timestamp, value)
        formatter.config.timerUnits = "sh"
        val textSecondHours = formatter.getText(timestamp, value)
        formatter.config.timerUnits = "smd"
        val textSecondDays = formatter.getText(timestamp, value)
        formatter.config.timerUnits = "md"
        val textMinuteDays = formatter.getText(timestamp, value)

        assertWithMessage("seconds").that(textSeconds).isEqualTo("442880 seconds")
        assertWithMessage("minutes").that(textMinutes).isEqualTo("7381 minutes")
        assertWithMessage("minutes and seconds").that(textSecondMinutes).isEqualTo("7381 minutes and 20 seconds")
        assertWithMessage("hours and seconds").that(textSecondHours).isEqualTo("123 hours and 80 seconds")
        assertWithMessage("days, minutes and seconds").that(textSecondDays).isEqualTo("5 days, 181 minutes, and 20 seconds")
        assertWithMessage("days and minutes").that(textMinuteDays).isEqualTo("5 days and 181 minutes")
    }

    @Test
    fun `should return properly formatted text when given five months using default (Positional) timerFormat`() {
        val text = formatter.getText(timestamp, timestamp.plus(5, MONTHS).plus(1, MINUTES).plus(20, SECONDS))

        assertThat(text).isEqualTo("152d 00:01:20")
    }

    @Test
    fun `should return properly formatted text when given zero seconds timerFormat Full`() {
        formatter.config.timerFormat = "Full"

        val text = formatter.getText(timestamp, timestamp)

        assertThat(text).isEqualTo("0 seconds")
    }

    @Test
    fun `should return properly formatted text when given a date using default timerFormat Abbreviated`() {
        formatter.config.timerFormat = "Abbreviated"

        val text = formatter.getText(timestamp, timestamp.plus(5, MONTHS).plus(1, MINUTES).plus(20, SECONDS))

        assertThat(text).isEqualTo("5m 1m 20s")
    }

    @Test
    fun `should return properly formatted text when given a date using default timerFormat Short`() {
        formatter.config.timerFormat = "Short"

        val text = formatter.getText(timestamp, timestamp.plus(5, MONTHS).plus(1, MINUTES).plus(20, SECONDS))

        assertThat(text).isEqualTo("5 mths, 1 min, 20 sec")
    }

    @Test
    fun `should return properly formatted text when given a date using default timerFormat Full`() {
        formatter.config.timerFormat = "Full"

        val text = formatter.getText(timestamp, timestamp.plus(5, MONTHS).plus(1, MINUTES).plus(20, SECONDS))

        assertThat(text).isEqualTo("5 months, 1 minute, and 20 seconds")
    }

    @Test
    fun `should return properly formatted text when given a date using default timerFormat SpelledOut`() {
        formatter.config.timerFormat = "SpelledOut"

        val text = formatter.getText(timestamp, timestamp.plus(5, MONTHS).plus(1, MINUTES).plus(20, SECONDS))

        assertThat(text).isEqualTo("5 months, 1 minute, and 20 seconds")
    }

    @Test
    fun `should return zero when given the same timestamp`() {
        formatter.config.timerFormat = "SpelledOut"
        formatter.config.timerUnits = "m"

        val textZeroMinute = formatter.getText(timestamp, timestamp)

        assertThat(textZeroMinute).isEqualTo("0 minutes")
    }

    @Test
    fun `should return properly formatted text when given a date using default timerFormat Full on Polish locale`() {
        formatter.config.timerFormat = "SpelledOut"
        formatter.config.locale = Locale("pl", "PL")

        val textZeroSecond = formatter.getText(timestamp, timestamp.plus(0, SECONDS))
        val textOneSecond = formatter.getText(timestamp, timestamp.plus(1, SECONDS))
        val textTwoSecond = formatter.getText(timestamp, timestamp.plus(2, SECONDS))

        assertWithMessage("0 seconds").that(textZeroSecond).isEqualTo("0 sekund")
        assertWithMessage("1 second").that(textOneSecond).isEqualTo("1 sekunda")
        assertWithMessage("2 seconds").that(textTwoSecond).isEqualTo("2 sekundy")
    }

    @Test
    fun `should return MinValue when given values bellow or equal the minimum`() {
        formatter.config.minSeconds = 2

        val stateOneSecond = formatter.getState(timestamp, timestamp.plus(1, SECONDS))
        val stateTwoSecond = formatter.getState(timestamp, timestamp.plus(2, SECONDS))
        val stateNegativeOneSecond = formatter.getState(timestamp, timestamp.plus(-1, SECONDS))
        val stateNegativeTwoSecond = formatter.getState(timestamp, timestamp.plus(-2, SECONDS))

        assertWithMessage("excedes min value after").that(stateOneSecond).isEqualTo(State.MinText)
        assertWithMessage("equals min value after").that(stateTwoSecond).isEqualTo(State.MinText)
        assertWithMessage("excedes min value before").that(stateNegativeOneSecond).isEqualTo(State.MinText)
        assertWithMessage("equals min value before").that(stateNegativeTwoSecond).isEqualTo(State.MinText)
    }

    @Test
    fun `should return MaxValue when given values above or equal the maximum`() {
        formatter.config.maxSeconds = 2

        val stateThreeSecond = formatter.getState(timestamp, timestamp.plus(3, SECONDS))
        val stateTwoSecond = formatter.getState(timestamp, timestamp.plus(2, SECONDS))
        val stateNegativeThreeSecond = formatter.getState(timestamp, timestamp.plus(-3, SECONDS))
        val stateNegativeTwoSecond = formatter.getState(timestamp, timestamp.plus(-2, SECONDS))

        assertWithMessage("excedes max value after").that(stateThreeSecond).isEqualTo(State.MaxText)
        assertWithMessage("equals max value after").that(stateTwoSecond).isEqualTo(State.MaxText)
        assertWithMessage("excedes max value before").that(stateNegativeThreeSecond).isEqualTo(State.MaxText)
        assertWithMessage("equals max value before").that(stateNegativeTwoSecond).isEqualTo(State.MaxText)
    }

    @Test
    fun `should return CountUp when given values between max and min after now`() {
        formatter.config.minSeconds = 2
        formatter.config.maxSeconds = 4

        val stateThreeSecond = formatter.getState(timestamp, timestamp.plus(3, SECONDS))

        assertThat(stateThreeSecond).isEqualTo(State.CountDown)
    }

    @Test
    fun `should return CountDown when given values between max and min before now`() {
        formatter.config.minSeconds = 2
        formatter.config.maxSeconds = 4

        val stateThreeSecond = formatter.getState(timestamp, timestamp.plus(-3, SECONDS))

        assertThat(stateThreeSecond).isEqualTo(State.CountUp)
    }
}
