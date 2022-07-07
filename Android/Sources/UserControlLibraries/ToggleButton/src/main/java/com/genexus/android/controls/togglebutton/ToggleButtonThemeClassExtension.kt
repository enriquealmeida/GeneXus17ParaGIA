package com.genexus.android.controls.togglebutton

import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition

/**
 * Theme extension class for the Gx Toggle Button control.
 */

val ThemeClassDefinition.toggleSelectedForeColor: String
    get() = optStringProperty("ToggleButtonGroupSelectedForeColor")

val ThemeClassDefinition.toggleSelectedBackColor: String
    get() = optStringProperty("ToggleButtonGroupSelectedBackColor")

val ThemeClassDefinition.toggleUnSelectedForeColor: String
    get() = optStringProperty("ToggleButtonGroupUnselectedForeColor")

val ThemeClassDefinition.toggleUnSelectedBackColor: String
    get() = optStringProperty("ToggleButtonGroupUnselectedBackColor")
