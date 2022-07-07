package com.genexus.android.core.controls;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;

import com.genexus.android.core.base.metadata.EnumValuesDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.ui.Coordinator;

public class GxEnumComboSpinner extends AppCompatSpinner implements IGxComboEdit, OnItemSelectedListener
{
	private Coordinator mCoordinator;
	private LayoutItemDefinition mDefinition;
	private boolean mFireControlValueChanged;
	private List<? extends EnumValuesDefinition> mEnumValues = null;

	public GxEnumComboSpinner(Context context, Coordinator coordinator, LayoutItemDefinition def)
	{
		super(context);
		mCoordinator = coordinator;
		mDefinition = def;
		mFireControlValueChanged = false;
		setOnItemSelectedListener(this);
	}

	public GxEnumComboSpinner(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public GxEnumComboSpinner(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public String getGxValue()
	{
		String description = (String) getSelectedItem();
		return (description != null) ? findValueFromDescription(description) : Strings.EMPTY;
	}

	@Override
	public void setGxValue(String value)
	{
		@SuppressWarnings("unchecked")
		ArrayAdapter<String> adapter = (ArrayAdapter<String>) getAdapter();
		if (adapter == null)
			return;

		// Only make a selection if there are items in the adapter.
		if (getCount() > 0)
		{
			int currentPosition = getSelectedItemPosition();
			int newPosition = findValuePosition(value);

			// Reset to the first item if the value was invalid.
			if (newPosition == -1)
				newPosition = 0;

			// Do nothing if there's no change of item position.
			if (newPosition == currentPosition)
				return;

			mFireControlValueChanged = false;
			setSelection(newPosition);
		}
	}

	@Override
	public String getGxTag()
	{
		return (String) this.getTag();
	}

	@Override
	public void setComboValues(List<? extends EnumValuesDefinition> values)
	{
		//Do something with enumvalues.
		mEnumValues = values;

		List<String> enumStrings = new ArrayList<>();

		for (EnumValuesDefinition item : values)
			enumStrings.add(item.getDescription());

		//Combo Source.
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, enumStrings);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setAdapter(spinnerAdapter);
	}

	private int findValuePosition(String value)
	{
		int result = 0;
		if (mEnumValues != null)
		{
			for (EnumValuesDefinition item : mEnumValues)
			{
				if (value.equalsIgnoreCase(item.getValue()))
					return result;
				result++;
			}
		}

		return 0;
	}

	private String findValueFromDescription(String description)
	{
		if (mEnumValues != null)
		{
			for (EnumValuesDefinition item : mEnumValues)
			{
				if (description.equalsIgnoreCase(item.getDescription()))
					return item.getValue();
			}
		}

		return Strings.EMPTY;
	}

	@Override
	public void setGxTag(String data)
	{
		this.setTag(data);
	}

	@Override
	public LayoutItemDefinition getDefinition()
	{
		return mDefinition;
	}

	@Override
	public void setValueFromIntent(Intent data) { }

	@Override
	public IGxEdit getViewControl()
	{
		return new GxTextView(getContext(), mDefinition);
	}

	@Override
	public IGxEdit getEditControl()
	{
		return this;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
	{
		if (mCoordinator != null)
			mCoordinator.onValueChanged(this, mFireControlValueChanged);

		mFireControlValueChanged = true;
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) { }

	@Override
	public boolean isEditable()
	{
		return isEnabled(); // Editable when enabled.
	}
}
