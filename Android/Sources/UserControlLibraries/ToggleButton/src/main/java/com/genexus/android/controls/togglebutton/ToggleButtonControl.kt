package com.genexus.android.controls.togglebutton

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import com.genexus.android.core.base.controls.IGxControlRuntime
import com.genexus.android.core.base.controls.IGxEditThemeable
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition
import com.genexus.android.core.base.utils.Strings
import com.genexus.android.core.controls.IGxEdit
import com.genexus.android.core.controls.common.StaticValueItems
import com.genexus.android.core.ui.Coordinator
import com.genexus.android.core.utils.ThemeUtils
import org.honorato.multistatetogglebutton.ToggleButton.OnValueChangedListener

// TODO: use Material component , com.google.android.material.button.MaterialButton
// This component requires that you specify a valid TextAppearance attribute. Update your app theme to inherit from Theme.MaterialComponents (or a descendant).
//
// need to change app main theme to Material theme to use it at least

@SuppressLint("ViewConstructor")
class ToggleButtonControl(context: Context, private val coordinator: Coordinator, definition: LayoutItemDefinition) : org.honorato.multistatetogglebutton.MultiStateToggleButton(context), IGxEdit, IGxControlRuntime, IGxEditThemeable, OnValueChangedListener {

    private var fireControlValueChanged = true
    private var editClass: ThemeClassDefinition? = null
    private var valuesList: ArrayList<Pair<String, String>> = ArrayList() // List of Values. Id and Desc
    private var lastSelectedIndex = UNSELECTEDINDEX
    private var tag: String = ""

    init {
        // set ValueChanged listener
        this.setOnValueChangedListener(this)

        // apply definition
        setLayoutDefinition(definition)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }

    private fun setLayoutDefinition(definition: LayoutItemDefinition) {
        val items = StaticValueItems(definition.dataItem, definition.controlInfo, "@ToggleButtonGroupValueRangeID")
        for (i in 0 until items.size()) {
            val item = items[i]
            valuesList.add(Pair(item.Value, item.Description))
        }
        setTextsFromValues()
    }

    private fun setTextsFromValues() {
        val texts = ArrayList<CharSequence>()
        for (i in 0 until valuesList.size) {
            val item = valuesList[i]
            texts.add(item.second)
        }
        this.setElements(texts)
    }

    override fun getGxValue(): String {
        return getValueFromSelected(this.getValue())
    }

    private fun getValueFromSelected(selectedValue: Int): String {
        if (selectedValue >= 0 && selectedValue < valuesList.size) {
            return valuesList[selectedValue].first
        }
        return Strings.EMPTY
    }

    override fun setGxValue(newValue: String) {
        fireControlValueChanged = false
        // select the id of value if exist.
        var index = getIdFromValueText(newValue)
        super.setValue(index)
        fireControlValueChanged = true
    }

    private fun getIdFromValueText(valueText: String): Int {
        for (i in 0 until valuesList.size) {
            val item = valuesList[i]
            if (valueText.equals(item.first, ignoreCase = true))
                return i
        }
        return UNSELECTEDINDEX
    }

    override fun getGxTag(): String {
        return tag
    }

    override fun setGxTag(data: String?) {
        tag = data ?: ""
    }

    override fun setValueFromIntent(data: Intent?) {}

    override fun isEditable(): Boolean {
        return isEnabled // Editable when enabled.
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
    }

    override fun getViewControl(): IGxEdit {
        isFocusable = false
        isEnabled = false
        return this
    }

    override fun getEditControl(): IGxEdit {
        return this
    }

    override fun applyEditClass(themeClass: ThemeClassDefinition) {
        editClass = themeClass

        // apply colors to Toggle Buttons, colors from Theme
        val colorSelectedForeId = ThemeUtils.getColorId(themeClass.toggleSelectedForeColor)
        val colorSelectedBackId = ThemeUtils.getColorId(themeClass.toggleSelectedBackColor)
        val colorUnSelectedForeId = ThemeUtils.getColorId(themeClass.toggleUnSelectedForeColor)
        val colorUnSelectedBackId = ThemeUtils.getColorId(themeClass.toggleUnSelectedBackColor)

        var customColors = false
        if (colorSelectedForeId != null && colorSelectedBackId != null) {
            this.setPressedColors(colorSelectedForeId, colorSelectedBackId)
            customColors = true
        }
        if (colorUnSelectedForeId != null && colorUnSelectedBackId != null) {
            this.setNotPressedColors(colorUnSelectedForeId, colorUnSelectedBackId)
            customColors = true
        }
        if (customColors) {
            // force refresh control colors
            this.setColors(0, 0)
        }
    }

    override fun onValueChanged(value: Int) {
        if (fireControlValueChanged && lastSelectedIndex != UNSELECTEDINDEX && lastSelectedIndex == value) {
            // if user select the same value again. deselect it
            // Services.Log.debug("onValueChanged same index value " + value + " Deselect it")
            lastSelectedIndex = UNSELECTEDINDEX
            super.setValue(UNSELECTEDINDEX)
            return
        } else {
            // normal value change event
            // Services.Log.debug("onValueChanged fire onValueChanged index " + value)
            coordinator.onValueChanged(this, fireControlValueChanged)
        }
        lastSelectedIndex = value
    }

    companion object {
        const val NAME = "ToggleButtonGroup"
        const val UNSELECTEDINDEX = -1
    }
}
