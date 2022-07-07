package com.genexus.android.core.base.model;

import java.util.ArrayList;

import androidx.annotation.NonNull;

import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.serialization.INodeCollection;
import com.genexus.android.core.base.services.Services;

/**
 * Base collection.
 */
public abstract class BaseCollection<T> extends ArrayList<T>
{
	public static final String PROPERTY_COUNT = "Count";
	public static final String PROPERTY_CURRENT_ITEM = "CurrentItem";

	public static final String METHOD_GET_ITEM = "Item";
	public static final String METHOD_ADD = "Add";
	public static final String METHOD_REMOVE = "Remove";
	public static final String METHOD_CLEAR = "Clear";
	public static final String METHOD_INDEXOF = "IndexOf";

	public static final String METHOD_TO_JSON = "ToJson";
	public static final String METHOD_FROM_JSON = "FromJson";

	private T mCurrentItem;

	public void setCurrentItem(T item)
	{
		mCurrentItem = item;
	}

	public T getCurrentItem()
	{
		return mCurrentItem;
	}

	@Override
	public String toString()
	{
		return serialize(Entity.JSONFORMAT_INTERNAL).toString();
	}

	public @NonNull INodeCollection serialize(short jsonFormat)	{
		INodeCollection collection = Services.Serializer.createCollection();
		for (T item : this)
			serializeItem(collection, item, jsonFormat);

		return collection;
	}

	public void deserialize(INodeCollection collection, short jsonFormat) {
		clear();
		for (int n = 0; n < collection.length(); n++) {
			T item = deserializeItem(collection, n, jsonFormat);
			add(item);
		}
	}

	protected abstract void serializeItem(INodeCollection collection, T item, short jsonFormat);
	protected abstract T deserializeItem(INodeCollection collection, int index, short jsonFormat);

	public abstract Expression.Type getItemType();
}
