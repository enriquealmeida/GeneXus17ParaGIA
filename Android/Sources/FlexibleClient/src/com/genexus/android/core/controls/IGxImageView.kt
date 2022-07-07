package com.genexus.android.core.controls

import com.genexus.android.core.base.metadata.enums.ImageScaleType
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition

interface IGxImageView {
    fun setImagePropertiesFromThemeClass(themeClass: ThemeClassDefinition)
    fun setImageScaleType(type: ImageScaleType)
    fun setImageSize(width: Int, height: Int)
    fun hasImageDrawable(): Boolean
}
