package com.genexus.android.live_editing.commands

import com.genexus.android.core.base.metadata.theme.ThemeDefinition
import com.genexus.android.core.base.serialization.INodeObject

class CommandDesignStylesChanged(designSystemName: String, newMetadata: INodeObject) :
    CommandDesignSystemChanged(designSystemName, newMetadata) {

    override fun clean(theme: ThemeDefinition) {
        theme.clearImportedThemes()
        theme.clearClasses()
        theme.clearTransformations()
        theme.clearFontFamilies()
    }
}
