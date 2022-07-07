package com.genexus.android.controls.relativetimer

import net.time4j.CalendarUnit.DAYS
import net.time4j.CalendarUnit.MONTHS
import net.time4j.CalendarUnit.WEEKS
import net.time4j.CalendarUnit.YEARS
import net.time4j.ClockUnit
import net.time4j.ClockUnit.HOURS
import net.time4j.ClockUnit.MINUTES
import net.time4j.ClockUnit.NANOS
import net.time4j.ClockUnit.SECONDS
import net.time4j.Duration
import net.time4j.IsoUnit
import net.time4j.PlainTimestamp
import net.time4j.PrettyTime
import net.time4j.engine.TimeMetric
import net.time4j.format.TextWidth

class RelativeTimerFormatter(config: FormatterConfig = FormatterConfig()) {
    val config: FormatterConfig

    init {
        this.config = config
    }

    private fun metric(): TimeMetric<IsoUnit, Duration<IsoUnit>> {
        val units = mutableListOf<IsoUnit>()
        if (config.timerFormat != "Positional") {
            if (config.timerUnits.contains('Y'))
                units.add(YEARS)
            if (config.timerUnits.contains('M'))
                units.add(MONTHS)
            if (config.timerUnits.contains('w'))
                units.add(WEEKS)
        }
        if (config.timerUnits.contains('d'))
            units.add(DAYS)
        if (config.timerUnits.contains('h'))
            units.add(HOURS)
        if (config.timerUnits.contains('m'))
            units.add(MINUTES)
        if (config.timerUnits.contains('s'))
            units.add(SECONDS)
        return Duration.`in`(*units.toTypedArray())
    }

    private fun lowestUnit(): IsoUnit {
        return when {
            config.timerUnits.contains('s') -> SECONDS
            config.timerUnits.contains('m') -> MINUTES
            config.timerUnits.contains('h') -> HOURS
            config.timerUnits.contains('w') -> WEEKS
            config.timerUnits.contains('d') -> DAYS
            config.timerUnits.contains('M') -> MONTHS
            config.timerUnits.contains('Y') -> YEARS
            else -> SECONDS
        }
    }

    private fun clockPattern(): String? {
        return when {
            config.timerUnits.contains('s') -> when {
                config.timerUnits.contains('h') -> "hh:mm:ss"
                config.timerUnits.contains('m') -> "mm:ss"
                else -> "ss"
            }
            config.timerUnits.contains('m') -> when {
                config.timerUnits.contains('h') -> "hh:mm"
                else -> "mm"
            }
            config.timerUnits.contains('h') -> "hh"
            else -> null
        }
    }

    /**
     * Returns two types of durations
     * @param dateValue This value is compared with now to get the durations
     * @return first a duration with all the metrics, second a duration of only seconds, third if dateValue has passed
     */
    private fun getDurations(now: PlainTimestamp, dateValue: PlainTimestamp): Triple<Duration<IsoUnit>, Duration<IsoUnit>, Boolean> {
        var duration = now.until(dateValue, metric()).abs()

        // get the durantion in seconds to compare with minSeconds and maxSeconds
        val durationSeconds: Duration<IsoUnit>
        if (lowestUnit() == SECONDS) { // it has seconds, avoid double zero is important
            // add 1 second when a partial second has passed after it reached the time
            val durationMillis = now.until(dateValue, Duration.`in`(SECONDS, NANOS))
            if (now.isAfter(dateValue) && durationMillis.getPartialAmount(NANOS).toInt() != 0)
                duration = duration.plus(1, SECONDS)

            // duration has seconds, so we use that
            durationSeconds = duration
        } else {
            // duration doesn't have seconds, get a new duration
            durationSeconds = now.until(dateValue, Duration.`in`(SECONDS as IsoUnit)).abs()
        }

        return Triple(duration, durationSeconds, now.isAfter(dateValue))
    }

    private fun isAboveMaxSeconds(durationSeconds: Duration<IsoUnit>): Boolean {
        return config.maxSeconds > 0 && durationToSeconds(durationSeconds.toClockPeriod()) >= config.maxSeconds
    }

    private fun isBellowMinSeconds(durationSeconds: Duration<IsoUnit>): Boolean {
        return config.minSeconds > 0 && durationToSeconds(durationSeconds.toClockPeriod()) <= config.minSeconds
    }

    private fun durationToSeconds(durationSeconds: Duration<ClockUnit>): Long {
        return durationSeconds.with(SECONDS.only()).getPartialAmount(SECONDS)
    }

    private fun replaceClockPattern(duration: Duration<IsoUnit>, clockPattern: String, pattern: String, truncate: IsoUnit, ammount: ClockUnit): String {
        if (clockPattern.contains(pattern)) {
            val durationSeconds = duration.plus(duration.truncatedTo(truncate))
            val fullSeconds = durationToSeconds(durationSeconds.toClockPeriod())
            var replaceText = durationSeconds.getPartialAmount(ammount).toString()
            if (truncate == MINUTES && ammount == SECONDS && fullSeconds.mod(60L) == 0L)
                replaceText = "0"
            if (truncate == HOURS && ammount == MINUTES && fullSeconds.mod(60L) == 0L)
                replaceText = (fullSeconds / 60).toString()
            if (replaceText.length == 1)
                replaceText = "0" + replaceText
            return clockPattern.replace(pattern, replaceText)
        } else {
            return clockPattern
        }
    }

    private fun getPositionalClock(duration: Duration<IsoUnit>): String? {
        var clockPattern = clockPattern()
        if (clockPattern == null)
            return null
        clockPattern = replaceClockPattern(duration, clockPattern, "ss", MINUTES, SECONDS)
        clockPattern = replaceClockPattern(duration, clockPattern, "mm", HOURS, MINUTES)
        clockPattern = replaceClockPattern(duration, clockPattern, "hh", DAYS, HOURS)
        return clockPattern
    }

    fun getState(now: PlainTimestamp, dateValue: PlainTimestamp): State {
        val (_, durationSeconds, isAfter) = getDurations(now, dateValue)
        if (isAboveMaxSeconds(durationSeconds))
            return State.MaxText
        else if (isBellowMinSeconds(durationSeconds))
            return State.MinText
        else
            return if (isAfter) State.CountUp else State.CountDown
    }

    fun getText(now: PlainTimestamp, dateValue: PlainTimestamp): String {
        val (durationFull, durationSeconds, isAfter) = getDurations(now, dateValue)

        if (isAboveMaxSeconds(durationSeconds))
            return config.maxText
        if (isBellowMinSeconds(durationSeconds))
            return config.minText

        // otherwise a value has to be shown
        var duration = durationFull

        // if countingType is only one direction and it is reached, show 0
        if (config.countingType == if (isAfter) "Down" else "Up") {
            duration = Duration.of(0, ClockUnit.SECONDS)
        } else if (config.mostRepresentativeUnitOnly) {
            // show only the most representative unit
            for (unit in arrayOf<IsoUnit>(YEARS, MONTHS, DAYS, HOURS, MINUTES)) {
                if (duration.getPartialAmount(unit) > 0) { // if there is 1 or more of that unit truncate to it
                    duration = duration.truncatedTo(unit)
                    break
                }
            }
        }

        // get format
        var positionalClock: String? = null
        val textWidth = when (config.timerFormat) {
            "Positional" -> {
                positionalClock = getPositionalClock(duration) // hh:mm:ss or parts of it as required, null if nothing has to be shown
                duration = duration.truncatedTo(DAYS) // beside hh:mm:ss only days are shown
                TextWidth.NARROW
            }
            "Abbreviated" -> {
                TextWidth.NARROW // symbol form (typically only one single letter)
            }
            "Short" -> {
                TextWidth.SHORT // abbreviation
            }
            else -> { // "Full", "SpelledOut"
                TextWidth.WIDE // full text
            }
        }

        // get the text of the duration using PrettyTime class to format it
        var text: String
        val unit = if (positionalClock == null) lowestUnit() else DAYS
        if (unit != SECONDS && duration.isEmpty) {
            // workaround to get the correct string instead of "0 seconds"
            val durationNoZero = duration.plus(5, unit) // use 5 to get the string for 0, use 5 instead of 2 because of Polish language
            text = PrettyTime.of(config.locale).print(durationNoZero, textWidth).replace('5', '0')
        } else {
            text = PrettyTime.of(config.locale).print(duration, textWidth)
        }

        // special case, use calculed text for days and positionalClock for time
        if (config.timerFormat == "Positional") {
            val clock = positionalClock
            val calendar = if (duration.isEmpty) null else text
            // combine the two based on null
            text = if (clock == null) if (calendar == null) "0" else calendar else if (calendar == null) clock else calendar + " " + clock
        }

        // add preffix and suffix
        return config.prefix + text + config.suffix
    }
}
