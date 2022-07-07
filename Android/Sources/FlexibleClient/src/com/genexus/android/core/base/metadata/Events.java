package com.genexus.android.core.base.metadata;

import com.genexus.android.core.base.utils.Strings;

public class Events
{
	public static final String CLIENT_START = "ClientStart";
	public static final String BACK = "Back";
	public static final String UP = "GxUp";

	public static final String SAVE = "Save";

	static ActionDefinition find(Iterable<ActionDefinition> list, String name)
	{
		if (list != null && Strings.hasValue(name))
		{
			for (ActionDefinition action : list)
			{
				if (action.getName().equalsIgnoreCase(name))
					return action;
			}
		}

		return null;
	}
}
