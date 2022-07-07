package com.genexus.android.core.controls.common;

import java.util.ArrayList;
import java.util.List;

import com.genexus.android.core.base.utils.Strings;

public abstract class ValueItems<ItemT extends ValueItem>
{
	private final ArrayList<ItemT> mItems;
	private ItemT mEmptyItem;

	public ValueItems()
	{
		mItems = new ArrayList<>();
	}

	protected void add(ItemT item)
	{
		mItems.add(item);
	}

	protected void setEmptyItem(ItemT item)
	{
		mEmptyItem = item;
		mItems.add(0, item);
	}

	public ItemT getEmptyItem() {
		return mEmptyItem;
	}

	protected void clear()
	{
		mItems.clear();
	}

	public int size()
	{
		return mItems.size();
	}

	public int indexOf(String value)
	{
		for (int i = 0; i < mItems.size(); i++)
			if (mItems.get(i).Value.equalsIgnoreCase(value))
				return i;

		return -1;
	}

	public String getValue(int index)
	{
		ValueItem item = get(index);
		return (item != null ? item.Value : Strings.EMPTY);
	}

	public String getDescription(String value)
	{
		int index = indexOf(value);
		if (index != -1)
			return get(index).Description;
		else
			return (mEmptyItem != null ? mEmptyItem.Description : Strings.EMPTY);
	}

	public ItemT get(int index)
	{
		if (index >= 0 && index < mItems.size())
			return mItems.get(index);

		return null;
	}

	public List<ValueItem> getItems()
	{
		return new ArrayList<ValueItem>(mItems);
	}
}
