package com.genexus.android.core.base.metadata.theme

import com.genexus.android.core.base.metadata.DimensionValue

const val FORM_CLASS_NAME = "Form"

val ThemeClassDefinition.callType: String
    get() = optStringProperty("call_type")

val ThemeClassDefinition.enterEffect: String
    get() = optStringProperty("enter_effect")

val ThemeClassDefinition.exitEffect: String
    get() = optStringProperty("close_effect")

val ThemeClassDefinition.targetName: String
    get() = optStringProperty("target_name")

val ThemeClassDefinition.targetSize: TargetSize?
    get() {
        val size = optStringProperty("target_size")
        val width = optStringProperty("target_width")
        val height = optStringProperty("target_height")
        return TargetSize(size, DimensionValue.parse(width), DimensionValue.parse(height))
    }

/**
 * Form size (for Popup and Callout types).
 */
class TargetSize(val name: String, val customWidth: DimensionValue?, val customHeight: DimensionValue?) {
    override fun toString(): String {
        return String.format("%s (%s * %s)", name, customWidth, customHeight)
    }

    companion object {
        const val SIZE_DEFAULT = "gx_default"
        const val SIZE_SMALL = "small"
        const val SIZE_MEDIUM = "medium"
        const val SIZE_LARGE = "large"
        const val SIZE_CUSTOM = "custom"
    }
}
