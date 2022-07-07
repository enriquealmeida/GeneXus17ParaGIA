package com.genexus.android.core.base.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;

/**
 * Substitute class from Map<K, List<V>>
 */
public class MultiMap<K, V> // almost implements Map<K, V>
{
	private final HashMap<K, ArrayList<V>> mInner;

	public MultiMap()
	{
		mInner = new HashMap<>();
	}

	/**
	 * Adds the specified key-value pair to the map.
	 * Returns all the values associated to the key, including this one.
	 */
	public @NonNull List<V> put(K key, V value)
	{
		ArrayList<V> keyValues = mInner.get(key);
		if (keyValues == null)
		{
			keyValues = new ArrayList<>();
			mInner.put(key, keyValues);
		}

		keyValues.add(value);
		return keyValues;
	}

	/**
	 * Adds the specified key-value pairs to the map.
	 * Returns all the values associated to the key, including these ones.
	 */
	public @NonNull List<V> putAll(K key, Collection<V> values)
	{
		ArrayList<V> keyValues = mInner.get(key);
		if (keyValues == null)
		{
			keyValues = new ArrayList<>();
			mInner.put(key, keyValues);
		}

		keyValues.addAll(values);
		return keyValues;
	}

	public @NonNull Set<K> keySet()
	{
		return mInner.keySet();
	}

	public @NonNull Collection<V> values()
	{
		ArrayList<V> allValues = new ArrayList<>();
		for (ArrayList<V> valuesList : mInner.values())
			allValues.addAll(valuesList);

		return Collections.unmodifiableList(allValues);
	}

	public @NonNull Collection<Collection<V>> valueGroups()
	{
		Collection<Collection<V>> valueGroups = new ArrayList<>();

		for (ArrayList<V> valuesList : mInner.values())
			valueGroups.add(Collections.unmodifiableCollection(valuesList));

		return Collections.unmodifiableCollection(valueGroups);
	}

	public void clear()
	{
		mInner.clear();
	}

	public void clear(K key)
	{
		mInner.remove(key);
	}

	public boolean containsKey(K key)
	{
		return mInner.containsKey(key);
	}

	public @NonNull List<V> get(K key)
	{
		ArrayList<V> values = mInner.get(key);
		if (values != null)
			return values;
		else
			return new ArrayList<>();
	}

	public int size()
	{
		int size = 0;
		for (ArrayList<V> valuesList : mInner.values())
			size += valuesList.size();

		return size;
	}

	public int getCount(K key)
	{
		ArrayList<V> values = mInner.get(key);
		if (values != null)
			return values.size();
		else
			return 0;
	}
}
