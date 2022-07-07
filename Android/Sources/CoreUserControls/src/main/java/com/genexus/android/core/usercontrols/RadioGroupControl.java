package com.genexus.android.core.usercontrols;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.genexus.android.core.base.controls.IGxControlRuntime;
import com.genexus.android.core.base.controls.IGxEditThemeable;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.layout.ControlInfo;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.IGxEdit;
import com.genexus.android.core.controls.SpinnerControl;
import com.genexus.android.core.controls.common.StaticValueItems;
import com.genexus.android.core.controls.common.TextViewUtils;
import com.genexus.android.core.controls.common.ValueItem;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.utils.ThemeUtils;

public class RadioGroupControl extends android.widget.RadioGroup implements IGxEdit, IGxEditThemeable, OnCheckedChangeListener, IGxControlRuntime
{
	public static final String NAME = "Radio Button";
	private final Coordinator mCoordinator;
	private int mNextRadioButtonId;
	private boolean mFireControlValueChanged;
	private LayoutItemDefinition mDefinition;
	private ThemeClassDefinition mEditClass;

	public RadioGroupControl(Context context, Coordinator coordinator, LayoutItemDefinition def)
	{
		super(context);
		mCoordinator = coordinator;
		mFireControlValueChanged = true;

		setOnCheckedChangeListener(this);

		setLayoutDefinition(def);
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}

	public RadioGroupControl(Context context)
	{
		super(context);
		throw new UnsupportedOperationException("Unsupported constructor.");
	}

	public LayoutItemDefinition getDefinition() {
		return mDefinition;
	}

	@Override
	public String getGxValue()
	{
		int checkedId = getCheckedRadioButtonId();
		RadioButton button = findViewById(checkedId);
		if (button != null)
			return (String) button.getTag();
		else
			return Strings.EMPTY;
	}

	@Override
	public void setGxValue(String value) {
		RadioButton button = findViewWithTag(value);
		if (button != null) {
			mFireControlValueChanged = false;
			super.check(button.getId());
			mFireControlValueChanged = true;
		}
	}

	@Override
	public String getGxTag()
	{
		return getTag().toString();
	}

	@Override
	public void setGxTag(String tag)
	{
		setTag(tag);
	}

	@Override
	public void setValueFromIntent(Intent data) { }

	private void setOrientationFrom(ControlInfo info)
	{
		setOrientation(HORIZONTAL); // Default value.

		if (info != null) {
			String orientation = info.optStringProperty("@ControlDirection");
			if (Services.Strings.hasValue(orientation)) {
				if (orientation.equalsIgnoreCase("Vertical"))
					setOrientation(VERTICAL);
				else if (orientation.equalsIgnoreCase("Horizontal"))
					setOrientation(HORIZONTAL);
			}
		}
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		List<View> list = new ArrayList<>();
		for (int n = 0; n < getChildCount(); n++) {
			list.add(getChildAt(n));
		}
		for (View radItem : list) {
			removeView(radItem); // Why this?
			radItem.setEnabled(enabled);
			addView(radItem);
		}
	}

	@Override
	public IGxEdit getViewControl()
	{
		setFocusable(false);
		setEnabled(false);
		return this;
	}

	@Override
	public IGxEdit getEditControl()
	{
		return this;
	}

	private void setLayoutDefinition(LayoutItemDefinition definition) {
		mDefinition = definition;
		StaticValueItems items = new StaticValueItems(definition.getDataItem(), definition.getControlInfo());

		super.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		setOrientationFrom(definition.getControlInfo());

		for (int i = 0; i < items.size(); i++)
		{
			ValueItem item = items.get(i);
			RadioButton radioItem = createRadioButton(item.Value, item.Description);
			addView(radioItem);
		}
	}

	private RadioButton createRadioButton(String value, String description) {
		RadioButton radioItem = new AppCompatRadioButton(getContext());
		radioItem.setLayoutParams(new RadioGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		radioItem.setId(mNextRadioButtonId++);
		radioItem.setTag(value);
		TextViewUtils.setText(radioItem, description, mDefinition);
		return radioItem;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId)
	{
		if (mCoordinator != null)
			mCoordinator.onValueChanged(this, mFireControlValueChanged);
	}

	@Override
	public void applyEditClass(@NonNull ThemeClassDefinition themeClass)
	{
		mEditClass = themeClass;
		for (int n = 0; n < getChildCount(); n++) {
			ThemeUtils.setFontProperties((RadioButton)getChildAt(n), themeClass);
		}
	}

	@Override
	public boolean isEditable()
	{
		return isEnabled(); // Editable when enabled.
	}

	// IGxControlRuntime

	@Override
	public Value getPropertyValue(String name) {
		if (SpinnerControl.PROPERTY_COUNT.equalsIgnoreCase(name)) {
			return Value.newInteger(getChildCount());
		}
		return null;
	}

	@Override
	public Value callMethod(String name, List<Value> parameters) {
		if (SpinnerControl.METHOD_ADD_ITEM.equalsIgnoreCase(name) && parameters.size() >= 1 && parameters.size() <= 3) {
			String itemValue = parameters.get(0).coerceToString();
			if (itemValue != null) {
				String itemDescription = parameters.size() == 1 ? itemValue : parameters.get(1).coerceToString();
				for (int i = 0; i < getChildCount(); i++) {
					RadioButton child = (RadioButton) getChildAt(i);
					if (itemValue.equals(child.getTag())) {
						// change description of an existing item
						TextViewUtils.setText(child, itemDescription, mDefinition);
						return null;
					}
				}
				RadioButton radioItem = createRadioButton(itemValue, itemDescription);
				if (mEditClass != null)
					ThemeUtils.setFontProperties(radioItem, mEditClass);
				if (parameters.size() >= 3) {
					int index = parameters.get(2).coerceToInt() - 1;
					if (index < 0)
						addView(radioItem, 0);
					else if (index >= getChildCount())
						addView(radioItem);
					else
						addView(radioItem, index);
				}
				else {
					addView(radioItem);
				}
			}
		}
		else if (SpinnerControl.METHOD_REMOVE_ITEM.equalsIgnoreCase(name) && parameters.size() == 1) {
			int index = parameters.get(0).coerceToInt() - 1;
			if (index >= 0 && index < getChildCount()) {
				removeViewAt(index);
			}
		}
		else if (SpinnerControl.METHOD_CLEAR.equalsIgnoreCase(name) && parameters.size() == 0) {
			removeAllViews();
		}
		else if (SpinnerControl.METHOD_VALUE.equalsIgnoreCase(name) && parameters.size() == 1) {
			int index = parameters.get(0).coerceToInt() - 1;
			if (index >= 0 && index < getChildCount())
				return Value.newString((String)getChildAt(index).getTag());
		}
		else if (SpinnerControl.METHOD_TEXT.equalsIgnoreCase(name) && parameters.size() == 1) {
			int index = parameters.get(0).coerceToInt() - 1;
			if (index >= 0 && index < getChildCount())
				return Value.newString(((RadioButton)getChildAt(index)).getText().toString());
		}
		return null;
	}
}
