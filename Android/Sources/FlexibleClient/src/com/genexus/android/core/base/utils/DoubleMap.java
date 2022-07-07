package com.genexus.android.core.base.utils;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Substitute class from Map<K1, Map<K2, V>> (K1 and K2 determine V).
 */
public class DoubleMap<K1, K2, V> // almost implements Map<>
{
	private final Map<K1, Map<K2, V>> mInner;
	private final Comparator<K2> mComparator2;

	public DoubleMap(Comparator<K1> comparator1, Comparator<K2> comparator2)
	{
		 mInner = new TreeMap<>(comparator1);
		 mComparator2 = comparator2;
	}

	public static <V> DoubleMap<String, String, V> newStringMap()
	{
		return new DoubleMap<>(String.CASE_INSENSITIVE_ORDER, String.CASE_INSENSITIVE_ORDER);
	}

	@Override
	public String toString()
	{
		return mInner.toString();
	}

	/**
	 * Maps the specified pair of keys to the specified value.
	 * @return the value of any previous mapping with the specified keys or null if there was no such mapping.
	 */
	public V put(K1 key1, K2 key2, V value)
	{
		Map<K2, V> second = mInner.get(key1);
		if (second == null)
		{
			second = new TreeMap<>(mComparator2);
			mInner.put(key1, second);
		}

		return second.put(key2, value);
	}

	/**
	 * Copies every mapping in the specified DoubleMap to this DoubleMap.
	 */
	public void putAll(DoubleMap<K1, K2, V> map)
	{
		for (Map.Entry<K1, Map<K2, V>> item1 : map.entrySet())
			for (Map.Entry<K2, V> item2 : item1.getValue().entrySet())
				put(item1.getKey(), item2.getKey(), item2.getValue());
	}

	public V get(K1 key1, K2 key2)
	{
		Map<K2, V> second = mInner.get(key1);
		if (second != null)
			return second.get(key2);
		else
			return null;
	}

	/**
	 * Returns a set containing all the mappings in this map.
	 * @return
	 */
	public Set<Map.Entry<K1, Map<K2, V>>> entrySet()
	{
		return mInner.entrySet();
	}

	/**
	 * Gets the set of mappings associated to the first key.
	 */
	public Map<K2, V> get(K1 key1)
	{
		Map<K2, V> second = mInner.get(key1);
		if (second != null)
			return second;
		else
			return new TreeMap<>(mComparator2);
	}

	/**
	 * Gets the number of values (K1, K2, V) in the DoubleMap.
	 */
	public int size()
	{
		int size = 0;
		for (Map.Entry<K1, Map<K2, V>> item1 : mInner.entrySet())
			size += item1.getValue().size();

		return size;
	}
}
