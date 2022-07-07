package com.genexus.android.core.controls.common;

import android.util.Pair;

import java.util.List;

public class DynamicValueItems extends ValueItems<ValueItem>
{
	public DynamicValueItems(List<Pair<String, String>> values)
	{
		super();

		for (Pair<String, String> value : values)
			add(new ValueItem(value.first, value.second));
	}
}
