package com.genexus.android.core.base.utils;

import java.util.Map;
import java.util.TreeMap;

/**
 * Shortcut to define maps in which the key is a name (with a case-insensitive comparison).
 * @param <V> Value type.
 */
public class NameMap<V> extends TreeMap<String, V>
{
	private static final long serialVersionUID = 1L;

	public NameMap()
	{
		super(String.CASE_INSENSITIVE_ORDER);
	}

	public NameMap(Map<? extends String, ? extends V> copyFrom)
	{
		this();
		if (copyFrom != null)
			putAll(copyFrom);
	}
}
