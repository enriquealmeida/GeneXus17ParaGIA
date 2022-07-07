package com.genexus.android.core.controls

import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition

/*
 * This interface must be implemented for each control that wants to be "themeable"
 */
interface IGxThemeable {
    /*
	 * Return the actual theme class for the associated control
	 *//*
	 * Apply the given themeClass to associated control
	 */
    var themeClass: ThemeClassDefinition?

    /*
	 * Apply the given class to the control but it doesn't change the class for it.
	 */
    fun applyClass(themeClass: ThemeClassDefinition?)
}
