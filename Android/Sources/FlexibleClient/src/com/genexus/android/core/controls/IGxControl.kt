package com.genexus.android.core.controls

import android.view.View
import com.genexus.android.core.base.controls.IGxControlRuntimeContext
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition

/**
 * Interface for controls in a form.
 */
interface IGxControl : IGxControlRuntimeContext {
    val name: String
    val definition: LayoutItemDefinition?
    var isEnabled: Boolean
    var themeClass: ThemeClassDefinition?
    var isVisible: Boolean
    var caption: String?
    val view: View?
    fun setFocus(showKeyboard: Boolean)
    fun requestLayout()
    fun getInterface(c: Class<*>): Any?
}
