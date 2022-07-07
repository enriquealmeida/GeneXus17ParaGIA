package com.genexus.android.core.base.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.genexus.android.core.utils.Cast;

public class ListUtils
{
	public static <T> int indexOf(List<T> list, T item, Comparator<T> comparator)
	{
		for (int i = 0; i < list.size(); i++)
			if (comparator.compare(list.get(i), item) == 0)
				return i;

		return -1;
	}

	public static <T> boolean contains(List<T> list, T item, Comparator<T> comparator)
	{
		return (indexOf(list, item, comparator) != -1);
	}

	public static <ItemTypeT> List<ItemTypeT> itemsOfType(Iterable<?> items, Class<ItemTypeT> type)
	{
		ArrayList<ItemTypeT> itemsOfType = new ArrayList<>();

		for (Object obj : items)
		{
			ItemTypeT item = Cast.as(type, obj);
			if (item != null)
				itemsOfType.add(item);
		}

		return itemsOfType;
	}

	public static <T> List<T> select(List<T> list, Function<T, Boolean> where)
	{
		ArrayList<T> selected = new ArrayList<>();

		for (T item : list)
		{
			if (where == null || where.run(item))
				selected.add(item);
		}

		return selected;
	}

	/**
	 * Converts a list of objects into a list of Strings, calling toString() on each one.
	 * Null items in the source list are mapped to null strings (not empty ones).
	 */
	public static <T> List<String> toStringList(List<T> srcList)
	{
		ArrayList<String> stringList = new ArrayList<>(srcList.size());
		for (T item : srcList)
		{
			String strItem = (item != null ? item.toString() : null);
			stringList.add(strItem);
		}

		return stringList;
	}

	public static <T> Iterable<T> inReverse(List<T> list)
	{
		return new ReverseIterator<>(list);
	}

	private static class ReverseIterator<T> implements Iterable<T>
	{
	    private ListIterator<T> mListIterator;

	    public ReverseIterator(List<T> list)
	    {
	    	mListIterator = list.listIterator(list.size());
	    }

	    @Override
		public Iterator<T> iterator()
	    {
	        return new Iterator<T>()
       		{
	            @Override
				public boolean hasNext()
	            {
	                return mListIterator.hasPrevious();
	            }

	            @Override
				public T next()
	            {
	                return mListIterator.previous();
	            }

	            @Override
				public void remove()
	            {
	            	mListIterator.remove();
	            }
	        };
	    }
	}
}
