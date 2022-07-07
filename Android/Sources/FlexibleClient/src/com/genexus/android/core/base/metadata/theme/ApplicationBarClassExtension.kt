package com.genexus.android.core.base.metadata.theme

import com.genexus.android.core.utils.ThemeUtils

const val APPBAR_CLASS_NAME = "ApplicationBars"

val ThemeClassDefinition.titleImage: String
    get() = getImage("title_image")

val ThemeClassDefinition.icon: String
    get() = getImage("application_bar_icon")

val ThemeClassDefinition.statusBarColor: Int?
    get() = ThemeUtils.getColorId(optStringProperty("status_bar_color"))

val ThemeClassDefinition.backButtonImage: String
    get() = getImage("application_bar_back_button_image")

val ThemeClassDefinition.backButtonClass: ThemeClassDefinition?
    get() = getRelatedClass("application_bar_back_button_class")
