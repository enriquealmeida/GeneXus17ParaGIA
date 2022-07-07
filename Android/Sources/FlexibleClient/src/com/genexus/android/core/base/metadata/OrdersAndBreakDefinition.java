package com.genexus.android.core.base.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrdersAndBreakDefinition extends ArrayList<OrderDefinition> implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final IDataSourceDefinition mParent;
	private final List<DataItem> mBreakByAttributes;
	private String mBreakByDescriptionAttribute;

	public OrdersAndBreakDefinition(IDataSourceDefinition parent)
	{
		mParent = parent;
		mBreakByAttributes = new ArrayList<>();
	}

	void addBreakBy(DataItem item)
	{
		mBreakByAttributes.add(item);
	}

	void setBreakByDescriptionAttribute(String name)
	{
		mBreakByDescriptionAttribute = name;
	}

	public boolean hasBreakBy(OrderDefinition order)
	{
		if (order != null && order.hasBreakBy())
			return true;

		return (mBreakByAttributes.size() != 0);
	}

	public List<DataItem> getBreakByAttributes(OrderDefinition order)
	{
		if (order != null && order.hasBreakBy())
			return order.getBreakByAttributes();

		return mBreakByAttributes;
	}

	public List<DataItem> getBreakByDescriptionAttributes(OrderDefinition order)
	{
		if (order != null && order.hasBreakBy())
			return order.getBreakByDescriptionAttributes();

		DataItem descriptionAttribute = mParent.getDataItem(mBreakByDescriptionAttribute);
		if (descriptionAttribute != null)
		{
			ArrayList<DataItem> attributes = new ArrayList<>();
			attributes.add(descriptionAttribute);
			return attributes;
		}
		else
			return mBreakByAttributes;
	}

	public boolean hasAnyWithAlphaIndexer()
	{
		for (OrderDefinition order : this)
		{
			if (order.getEnableAlphaIndexer())
				return true;
		}

		return false;
	}

	public boolean hasAnyWithBreakBy()
	{
		for (OrderDefinition order : this)
		{
			if (order.hasBreakBy())
				return true;
		}

		return mBreakByAttributes.size() != 0;

	}
}
