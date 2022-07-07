package com.genexus.android.core.controls.common;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.utils.Cast;

public class SpinnerItemsAdapter extends ArrayAdapter<ValueItem>
{
	private ThemeClassDefinition mThemeClass;
	private LayoutItemDefinition mDefinition;

	public SpinnerItemsAdapter(Context context, ValueItems<? extends ValueItem> items, ThemeClassDefinition themeClass, LayoutItemDefinition definition)
	{
		super(context, android.R.layout.simple_spinner_item, items.getItems());
		setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mDefinition = definition;
		mThemeClass = themeClass;
	}

	public void applyClass(ThemeClassDefinition themeClass)
	{
		mThemeClass = themeClass;
		notifyDataSetChanged();
	}

	@Override
	public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent)
	{
		// Create the view and customize it according to theme class.
		View view = super.getView(position, convertView, parent);
		applyStyle(view, false);
		updateText(view, position);
		return view;
	}

	@Override
	public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent)
	{
		// Create the item view and customize it according to theme.
		View view = super.getDropDownView(position, convertView, parent);
		applyStyle(view, true);
		updateText(view, position);
		return view;
	}

	private void applyStyle(@NonNull View view, boolean setBackground)
	{
		if (view instanceof TextView)
		{
			int gravity = mDefinition.getCellGravity();
			if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == 0)
				gravity |= Gravity.CENTER_VERTICAL;

			((TextView)view).setGravity(gravity);
		}

		AdapterThemeHelper.applyStyle(view, mThemeClass, setBackground);
	}

	private void updateText(@NonNull View view, int position)
	{
		TextView textView = Cast.as(TextView.class, view);
		if (textView != null)
		{
			ValueItem item = this.getItem(position);
			if (item == null)
				throw new IllegalStateException(String.format("Null item in SpinnerItemsAdapter (position = %s)", position));

			TextViewUtils.setText(textView, item.Description, mDefinition);
		}
	}
}
