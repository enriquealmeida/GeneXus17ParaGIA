package com.genexus.android.core.controls.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.genexus.android.R;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.ListUtils;

public class HistoryHelper
{
	private static final String PREFERENCES_KEY = "InputHistory";
	private static final char LIST_SEPARATOR = ',';

	private final String mKey;
	private List<String> mValues;

	public HistoryHelper(LayoutItemDefinition definition)
	{
		mKey = definition.getLayout().getParent().getName() + "::" + definition.getName();
	}

	private void loadValues()
	{
		if (mValues == null)
		{
			SharedPreferences historyStore = Services.Preferences.getAppSharedPreferences(PREFERENCES_KEY);
			String historyValues = historyStore.getString(mKey, null);
			if (Services.Strings.hasValue(historyValues))
				mValues = Services.Strings.decodeStringList(historyValues, LIST_SEPARATOR);
			else
				mValues = new ArrayList<>();
		}
	}

	public HistoryAdapter buildAdapter(Context context, ThemeClassDefinition themeClass)
	{
		return new HistoryAdapter(context, themeClass, getValues());
	}

	private List<String> getValues()
	{
		loadValues();

		// For the user, return sorted values. The internal list is kept in insertion order.
		ArrayList<String> sortedValues = new ArrayList<>(mValues);
		Collections.sort(sortedValues, String.CASE_INSENSITIVE_ORDER);
		return sortedValues;
	}

	public void store(String value)
	{
		if (!Services.Strings.hasValue(value))
			return;

		loadValues();

		// If present, remove it (so that it's "moved to end" when re-added).
		int prevIndex = ListUtils.indexOf(mValues, value, String.CASE_INSENSITIVE_ORDER);
		if (prevIndex != -1)
			mValues.remove(prevIndex);

		mValues.add(value);
		String historyValues = Services.Strings.encodeStringList(mValues, LIST_SEPARATOR);

		SharedPreferences historyStore = Services.Preferences.getAppSharedPreferences(PREFERENCES_KEY);
		historyStore.edit().putString(mKey, historyValues).apply();
	}

	@SuppressWarnings("unused")
	public static void clearAll()
	{
		SharedPreferences historyStore = Services.Preferences.getAppSharedPreferences(PREFERENCES_KEY);
		historyStore.edit().clear().apply();
	}

	public static class HistoryAdapter extends ArrayAdapter<String>
	{
		private final ThemeClassDefinition mThemeClass;

		public HistoryAdapter(Context context, ThemeClassDefinition themeClass, List<String> objects)
		{
			super(context, R.layout.support_simple_spinner_dropdown_item, objects);
			mThemeClass = themeClass;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			// Create the item view and customize it according to theme.
			View view = super.getView(position, convertView, parent);
			AdapterThemeHelper.applyStyle(view, mThemeClass, true);
			return view;
		}
	}
}
