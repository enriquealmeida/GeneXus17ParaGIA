package com.genexus.android.core.base.metadata.theme

import com.genexus.android.core.base.model.PropertiesObject
import com.genexus.android.core.base.serialization.INodeObject

class ThemeTokensDefinition(jsonTokens: INodeObject) : PropertiesObject() {
    val options = ThemeOptions()

    fun getResourceStyleNameSuffix(): String {
        var suffix = ""
        for (name in options.propertyNames.sorted()) {
            suffix += "." + options.getStringProperty(name, "")
        }
        return suffix
    }

    init {
        options.deserialize(jsonTokens.getNode("Options"))
        deserialize(jsonTokens.getNode("Values"))
    }
}
