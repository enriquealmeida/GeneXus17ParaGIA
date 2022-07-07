package com.genexus.android.core.utils;

import java.util.ArrayList;

public class Cast
{
	public static <T> T as(Class<T> t, Object o)
	{
		return (t.isInstance(o) ? t.cast(o) : null);
	}

	/**
	 * Iterates over a generic collection, returning all items that are instances of ItemT.
	 */
	public static <ItemT> Iterable<ItemT> iterateAs(Class<ItemT> itemClass, Iterable<?> collection)
	{
		ArrayList<ItemT> result = new ArrayList<>();
		for (Object item : collection)
		{
			if (itemClass.isInstance(item))
				result.add(itemClass.cast(item));
		}

		return result;
	}
}
