package com.genexus.android.core.base.metadata.theme

import com.genexus.android.core.utils.ThemeUtils

const val ATT_CLASS_NAME = "Attribute"

val ThemeClassDefinition.inviteMessageColor: Int?
    get() = ThemeUtils.getColorId(optStringProperty("invitemessage_color"))
