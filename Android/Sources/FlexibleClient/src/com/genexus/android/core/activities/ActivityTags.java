package com.genexus.android.core.activities;

import java.util.WeakHashMap;

import android.app.Activity;

import com.genexus.android.core.base.utils.NameMap;

public class ActivityTags
{
	private static WeakHashMap<Activity, NameMap<Object>> sTags = new WeakHashMap<>();

	public static void put(Activity activity, String key, Object value)
	{
		NameMap<Object> activityValues = sTags.get(activity);
		if (activityValues == null)
		{
			activityValues = new NameMap<>();
			sTags.put(activity, activityValues);
		}

		activityValues.put(key, value);
	}

	public static Object get(Activity activity, String key)
	{
		NameMap<Object> activityValues = sTags.get(activity);
		if (activityValues != null)
			return activityValues.get(key);
		else
			return null;
	}
}
