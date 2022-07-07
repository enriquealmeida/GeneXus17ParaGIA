package com.genexus.android.core.controls;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;

import com.genexus.android.core.base.controls.IGxControlRuntime;
import com.genexus.android.core.base.controls.IGxEditThemeable;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.controls.common.FocusHelper;
import com.genexus.android.core.controls.common.SpinnerItemsAdapter;
import com.genexus.android.core.controls.common.ValueItem;
import com.genexus.android.core.ui.Coordinator;

import java.util.List;

public abstract class SpinnerControl extends AppCompatSpinner implements IGxEditThemeable, IGxControlRuntime
{
	// Known properties and methods that can be used in events
	public static final String PROPERTY_COUNT = "Count";
	public static final String METHOD_ADD_ITEM = "AddItem";
	public static final String METHOD_REMOVE_ITEM = "RemoveItem";
	public static final String METHOD_CLEAR = "Clear";
	public static final String METHOD_VALUE = "Value";
	public static final String METHOD_TEXT = "Text";

	protected final Coordinator mCoordinator;
	protected final LayoutItemDefinition mDefinition;
	protected final ThemeClassDefinition mThemeClass;
	protected SpinnerItemsAdapter mAdapter;

	private SpinnerControl(Context context)
	{
		super(context);
		throw new UnsupportedOperationException("Unsupported constructor.");
	}

	public SpinnerControl(Context context, Coordinator coordinator, LayoutItemDefinition definition)
	{
		super(context);
		mDefinition = definition;
		mCoordinator = coordinator;
		mThemeClass = mDefinition.getThemeClass();

		FocusHelper.removeFocusabilityIfNecessary(this, definition);
	}

	protected int getIndex(String value) {
		int newPosition = -1;
		for (int n = 0; n < mAdapter.getCount(); n++) {
			if (value.equals(mAdapter.getItem(n).Value)) {
				newPosition = n;
				break;
			}
		}
		return newPosition;
	}

	public LayoutItemDefinition getDefinition() {
		return mDefinition;
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

	@Override
	public void applyEditClass(@NonNull ThemeClassDefinition themeClass)
	{
		if (mAdapter != null)
			mAdapter.applyClass(themeClass);
	}

	@Override
	public boolean isEditable()
	{
		return isEnabled(); // Editable when enabled.
	}

	// IGxControlRuntime

	@Override
	public Value getPropertyValue(String name) {
		if (PROPERTY_COUNT.equalsIgnoreCase(name)) {
			return Value.newInteger(mAdapter.getCount());
		}
		return null;
	}

	@Override
	public Value callMethod(String name, List<Value> parameters) {
		if (METHOD_ADD_ITEM.equalsIgnoreCase(name) && parameters.size() >= 1 && parameters.size() <= 3) {
			String itemValue = parameters.get(0).coerceToString();
			if (itemValue != null) {
				String itemDescription = parameters.size() == 1 ? itemValue : parameters.get(1).coerceToString();
				ValueItem item = new ValueItem(itemValue, itemDescription);
				for (int i = 0; i < mAdapter.getCount(); i++) {
					ValueItem child = mAdapter.getItem(i);
					if (itemValue.equals(child.Value)) {
						// change description of an existing item
						mAdapter.remove(child);
						if (i < mAdapter.getCount())
							mAdapter.insert(item, i);
						else
							mAdapter.add(item);
						return null;
					}
				}
				if (parameters.size() >= 3) {
					int index = parameters.get(2).coerceToInt() - 1;
					if (index < 0)
						mAdapter.insert(item, 0);
					if (index >= mAdapter.getCount())
						mAdapter.add(item);
					else
						mAdapter.insert(item, index);
				}
				else {
					mAdapter.add(item);
				}
			}
		}
		else if (METHOD_REMOVE_ITEM.equalsIgnoreCase(name) && parameters.size() == 1) {
			int index = parameters.get(0).coerceToInt() - 1;
			if (index >= 0 && index < mAdapter.getCount())
				mAdapter.remove(mAdapter.getItem(index));
		}
		else if (METHOD_CLEAR.equalsIgnoreCase(name) && parameters.size() == 0) {
			mAdapter.clear();
		}
		else if (METHOD_VALUE.equalsIgnoreCase(name) && parameters.size() == 1) {
			int index = parameters.get(0).coerceToInt() - 1;
			if (index >= 0 && index < mAdapter.getCount())
				return Value.newString(mAdapter.getItem(index).Value);
		}
		else if (METHOD_TEXT.equalsIgnoreCase(name) && parameters.size() == 1) {
			int index = parameters.get(0).coerceToInt() - 1;
			if (index >= 0 && index < mAdapter.getCount())
				return Value.newString(mAdapter.getItem(index).Description);
		}
		return null;
	}
}
