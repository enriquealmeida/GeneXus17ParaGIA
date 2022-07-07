package com.genexus.android.core.base.metadata.theme

import com.genexus.android.core.base.metadata.enums.MeasureUnit
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.Strings
import com.genexus.android.core.utils.ThemeUtils

const val TAB_CLASS_NAME = "Tab"

const val TAB_STRIP_POSITION_TOP = 0
const val TAB_STRIP_POSITION_BOTTOM = 1

val ThemeClassDefinition.tabStripPosition: Int
    get() {
        val tabPosition: String = optStringProperty("tabs_position")
        return if (Strings.hasValue(tabPosition) && tabPosition.equals("bottom", ignoreCase = true))
            TAB_STRIP_POSITION_BOTTOM
        else
            TAB_STRIP_POSITION_TOP // Default
    }

val ThemeClassDefinition.tabStripColorId: Int?
    get() = ThemeUtils.getColorId(optStringProperty("tab_strip_background_color"))

val ThemeClassDefinition.tabStripElevation: Int?
    get() {
        val elevation = Services.Strings.tryParseInt(optStringProperty("tab_strip_elevation"))
        return if (elevation != null) Services.Device.dipsToPixels(elevation) else null
    }

val ThemeClassDefinition.indicatorColorId: Int?
    get() = ThemeUtils.getColorId(optStringProperty("tab_strip_indicator_color"))

val ThemeClassDefinition.selectedPageClass: ThemeClassDefinition?
    get() = getRelatedClass("ThemeSelectedTabPageClassReference")

val ThemeClassDefinition.unselectedPageClass: ThemeClassDefinition?
    get() = getRelatedClass("ThemeUnselectedTabPageClassReference")

/**
 * Gets the TabStrip height (in pixels) set in this class.
 * If null, use the control's default height instead.
 */
val ThemeClassDefinition.tabStripHeight: Int
    get() {
        val str = optStringProperty("tab_strip_height")
        val dipValue = Services.Strings.parseMeasureValue(str, MeasureUnit.DIP)
        return if (dipValue != null) Services.Device.dipsToPixels(dipValue) else 0
    }
