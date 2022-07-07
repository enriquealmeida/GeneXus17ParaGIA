package com.genexus.android.core.controls

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import com.genexus.android.core.base.metadata.layout.CellDefinition
import com.genexus.android.core.base.metadata.layout.GroupDefinition
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition
import com.genexus.android.core.base.metadata.layout.TableDefinition
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition
import com.genexus.android.core.base.metadata.theme.ThemeOverrideProperties
import com.genexus.android.core.ui.Coordinator
import com.genexus.android.layout.GxLayout
import com.genexus.android.layout.IGxLayout

class GxGroup(context: Context, coordinator: Coordinator, definition: GroupDefinition) : LinearLayout(context), IGxLayout {
    private val textView: GxTextView
    private val table: GxLayout
    private val themedOverrideViewHelper: ThemedOverrideViewHelper

    init {
        orientation = VERTICAL
        textView = GxTextView(context, coordinator, definition)
        textView.layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        textView.background = ColorDrawable(Color.rgb(221, 221, 221)) // #DDD
        textView.gxValue = definition.caption
        table = GxLayout(context, definition.content, coordinator)
        table.layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        addView(textView)
        addView(table)
        themedOverrideViewHelper = ThemedOverrideViewHelper(this, definition)
    }

    override fun getThemeOverrideProperties(): ThemeOverrideProperties? {
        return themedOverrideViewHelper.themeOverrideProperties
    }

    override fun applyClass(themeClass: ThemeClassDefinition?) {
        textView.applyClass(themeClass?.themeGroupSeparatorClass)
    }

    override var themeClass: ThemeClassDefinition?
        get() = themedOverrideViewHelper.themeClass
        set(value) {
            themedOverrideViewHelper.themeClass = value
            applyClass(value)
        }

    override fun addChild(view: View) {
        table.addChild(view)
    }

    override fun setLayoutParams(params: ViewGroup.LayoutParams?) {
        super.setLayoutParams(params)
        themedOverrideViewHelper.setLayoutParams(params)
    }

    override fun updateHorizontalSeparators(separator: GxHorizontalSeparator?) {
        table.updateHorizontalSeparators(separator)
    }

    override fun updateSelfLayoutParams() {
        table.updateSelfLayoutParams()
    }

    override fun setLayout(coordinator: Coordinator?, layout: TableDefinition?) {
        table.setLayout(coordinator, layout)
    }

    override fun requestAlignFieldLabels() {
        table.requestAlignFieldLabels()
    }

    override fun setOverride(propertyName: String?, propertyValue: String?) {
        table.setOverride(propertyName, propertyValue)
    }

    override fun setChildLayoutParams(cell: CellDefinition?, item: LayoutItemDefinition?, view: View?) {
        table.setChildLayoutParams(cell, item, view)
    }

    override fun updateChildLayoutParams(cell: CellDefinition?, item: LayoutItemDefinition?, view: View?) {
        table.updateChildLayoutParams(cell, item, view)
    }
}
