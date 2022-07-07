package com.genexus.android.controls.relativetimer

import android.annotation.SuppressLint
import android.content.Context
import com.genexus.android.core.base.controls.IGxControlRuntime
import com.genexus.android.core.base.metadata.expressions.Expression
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition
import com.genexus.android.core.base.services.IValuesFormatter
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.controls.GxTextView
import com.genexus.android.core.ui.Coordinator
import net.time4j.PlainTimestamp
import net.time4j.SystemClock
import net.time4j.TemporalType
import net.time4j.tz.ZonalOffset
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

@SuppressLint("ViewConstructor")
class RelativeTimerControl(context: Context, val coordinator: Coordinator, definition: LayoutItemDefinition) : GxTextView(context, coordinator, definition, EmptyValueFormatter()), IGxControlRuntime {
    private val formatter: RelativeTimerFormatter = RelativeTimerFormatter(FormatterConfig(definition.controlInfo))
    private val hasMilliseconds: Boolean = definition.dataItem.decimals == 12
    private val hasSeconds: Boolean = definition.dataItem.decimals >= 8
    private var lastState: State = State.Uninitialized
    private var timer: Timer? = null
    private lateinit var gxValue: String
    private lateinit var dateValue: PlainTimestamp

    class EmptyValueFormatter : IValuesFormatter {
        override fun needsAsync(): Boolean {
            return false
        }

        override fun format(value: String?): CharSequence {
            return value!!
        }
    }

    /**
     * Updates the control's text based on {@link #dateValue} and the formatting configuration
     * also updates the {@link #lastState} and trigger the event if it has changed
     */
    private fun updateText() {
        val now = SystemClock.currentMoment().toZonalTimestamp(ZonalOffset.UTC)
        val text = formatter.getText(now, dateValue)
        val state = formatter.getState(now, dateValue)

        super.setGxValue(text)

        // check if state has changed to trigger event
        if (lastState != State.Uninitialized && state != lastState)
            coordinator.runControlEvent(this, "TimerStatusChanged")

        lastState = state
    }

    override fun getGxValue(): String {
        return gxValue
    }

    override fun setGxValue(value: String) {
        gxValue = value
        val date = Services.Strings.getDateTime(value, false, hasSeconds, hasMilliseconds)
        dateValue = TemporalType.JAVA_UTIL_DATE.translate(date).toZonalTimestamp(ZonalOffset.UTC)

        if (timer == null) {
            timer = Timer()
            val monitor = object : TimerTask() {
                override fun run() {
                    post({ updateText() })
                }
            }
            timer?.schedule(monitor, 0, 1000)
        }
    }

    override fun setPropertyValue(name: String, value: Expression.Value) {
        when (name.lowercase(Locale.ROOT)) {
            "maxseconds" -> formatter.config.maxSeconds = value.coerceToInt()
            "minseconds" -> formatter.config.minSeconds = value.coerceToInt()
            "maxtext" -> formatter.config.maxText = value.coerceToString()
            "mintext" -> formatter.config.minText = value.coerceToString()
            "prefixtext" -> formatter.config.prefix = value.coerceToString()
            "suffixtext" -> formatter.config.suffix = value.coerceToString()
        }
        if (lastState != State.Uninitialized)
            post({ updateText() }) // don't wait for next second to show the change
    }

    override fun getPropertyValue(name: String): Expression.Value {
        return when (name.lowercase(Locale.US)) {
            "maxseconds" -> Expression.Value.newInteger(formatter.config.maxSeconds.toLong())
            "minseconds" -> Expression.Value.newInteger(formatter.config.minSeconds.toLong())
            "maxtext" -> Expression.Value.newString(formatter.config.maxText)
            "mintext" -> Expression.Value.newString(formatter.config.minText)
            "prefixtext" -> Expression.Value.newString(formatter.config.prefix)
            "suffixtext" -> Expression.Value.newString(formatter.config.suffix)
            else -> Expression.Value.UNKNOWN
        }
    }

    companion object {
        const val NAME = "SDRelativeTimer"
    }
}
