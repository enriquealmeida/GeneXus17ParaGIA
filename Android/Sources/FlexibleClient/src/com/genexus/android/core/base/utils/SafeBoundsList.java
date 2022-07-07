package com.genexus.android.core.base.utils;

import java.util.ArrayList;

/**
 * Subclass of ArrayList that does not throw an IndexOutOfBoundsException if the index
 * is outside the valid range in the get() method.
 */
public class SafeBoundsList<T> extends ArrayList<T>
{
	private static final long serialVersionUID = 1L;

	@Override
	public T get(int index)
	{
		if (index < 0 || index >= size())
			return null;

		return super.get(index);
	}
}
