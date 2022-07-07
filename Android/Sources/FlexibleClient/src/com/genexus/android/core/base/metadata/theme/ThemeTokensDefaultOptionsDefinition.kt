package com.genexus.android.core.base.metadata.theme

import com.genexus.android.core.base.model.PropertiesObject
import com.genexus.android.core.base.serialization.INodeObject

class ThemeTokensDefaultOptionsDefinition(json: INodeObject) : PropertiesObject() {
    init {
        deserialize(json)
    }
}
