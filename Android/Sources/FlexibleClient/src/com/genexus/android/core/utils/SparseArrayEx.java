package com.genexus.android.core.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;

import android.util.SparseArray;

/**
 * Subclass of Android's SparseArray providing the values() method as present in the Map<Int,E> interface.
 */
public class SparseArrayEx<T> extends SparseArray<T>
{
	private SparseArrayIterable mIterable = new SparseArrayIterable();

	public Iterable<T> values()
	{
		return mIterable;
	}

	private class SparseArrayIterable implements Iterable<T>
	{
		@Override
		public Iterator<T> iterator()
		{
			return new SparseArrayIterator();
		}
	}

	private class SparseArrayIterator implements Iterator<T>
	{
		private int mCurrent = 0;

		@Override
		public boolean hasNext()
		{
			return (mCurrent < size());
		}

		@Override
		public T next()
		{
			if (!hasNext())
				throw new NoSuchElementException();

			return valueAt(mCurrent++);
		}

		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}
}
