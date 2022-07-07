package com.genexus.android.core.base.metadata.theme

import com.genexus.android.core.utils.ThemeUtils

const val APP_CLASS_NAME = "Application"

const val PLACEHOLDER_IMAGE = "placeholder_image"
const val PROMPT_IMAGE = "prompt_image"
const val DATEPICKER_IMAGE = "datepicker_image"

val ThemeClassDefinition.actionTintColorId: Int?
    get() = ThemeUtils.getColorId(optStringProperty("action_tint_color"))

val ThemeClassDefinition.placeholderImage: String
    get() = getImage(PLACEHOLDER_IMAGE)

val ThemeClassDefinition.promptImage: String
    get() = getImage(PROMPT_IMAGE)

val ThemeClassDefinition.getDatePickerImage: String
    get() = getImage(DATEPICKER_IMAGE)

val ThemeClassDefinition.useImageLoadingIndicator: Boolean
    get() = optBooleanProperty("image_loading_indicator")
