package com.genexus.android.controls.relativetimer

import com.genexus.android.core.base.metadata.layout.ControlInfo
import java.util.Locale

class FormatterConfig(controlInfo: ControlInfo? = null) {
    var locale: Locale
    var timerFormat: String
    var timerUnits: String
    var mostRepresentativeUnitOnly: Boolean
    var countingType: String
    var maxSeconds: Int
    var minSeconds: Int
    var maxText: String
    var minText: String
    var prefix: String
    var suffix: String

    init {
        locale = Locale.getDefault()
        if (controlInfo != null) {
            timerFormat = controlInfo.optStringProperty("@SDRelativeTimerFormat") // "Positional"
            timerUnits = controlInfo.optStringProperty("@SDRelativeTimerUnits") // "smhdwMY"
            mostRepresentativeUnitOnly = controlInfo.optBooleanProperty("@SDRelativeTimerMostRepresentativeUnitOnly") // "False"
            countingType = controlInfo.optStringProperty("@SDRelativeTimerCountingType") // "Auto"
            maxSeconds = controlInfo.optIntProperty("@SDRelativeTimerMaxSeconds") // "0"
            minSeconds = controlInfo.optIntProperty("@SDRelativeTimerMinSeconds") // "0"
            maxText = controlInfo.optStringProperty("@SDRelativeTimerMaxText")
            minText = controlInfo.optStringProperty("@SDRelativeTimerMinText")
            prefix = controlInfo.optStringProperty("@SDRelativeTimerPrefixText")
            suffix = controlInfo.optStringProperty("@SDRelativeTimerSuffixText")
        } else {
            // allow null for testing
            timerFormat = "Positional"
            timerUnits = "smhdwMY"
            mostRepresentativeUnitOnly = false
            countingType = "Auto"
            maxSeconds = 0
            minSeconds = 0
            maxText = ""
            minText = ""
            prefix = ""
            suffix = ""
        }
    }
}
