package com.genexus.android.core.usercontrols

import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition

/**
 * Theme extension class for the Gx Switch controls.
 */

val ThemeClassDefinition.isDefaultSwitchClass: Boolean
    get() = switchOnColor.isEmpty() && switchOnTextColor.isEmpty() &&
        switchOffColor.isEmpty() && switchOffTextColor.isEmpty()

val ThemeClassDefinition.switchOnColor: String
    get() = optStringProperty("SwitchOnColor")

val ThemeClassDefinition.switchOnTextColor: String
    get() = optStringProperty("SwitchOnTextColor")

val ThemeClassDefinition.switchOffColor: String
    get() = optStringProperty("SwitchOffColor")

val ThemeClassDefinition.switchOffTextColor: String
    get() = optStringProperty("SwitchOffTextColor")
