package com.genexus.android.core.base.metadata;

import java.util.List;

import com.genexus.android.core.base.services.Services;

public class DataItemHelper
{
	/**
	 * "Normalizes" data item names. This basically means unifying variables and attributes.
	 */
	public static String getNormalizedName(String name)
	{
		if (name != null)
		{
			name = name.trim();
			if (name.startsWith("&"))
				name = name.substring(1);
		}

		return name;
	}

	/**
	 * Get a data item definition from the context of a data source. In order, it checks for the data item:
	 * * As part of the data source.
	 * * As a variable in the containing data view.
	 * * As an attribute in the KB.
	 */
	public static DataItem find(IDataSourceDefinition dataSource, String itemName, boolean searchGlobally)
	{
		itemName = getNormalizedName(itemName);
		DataItem dataItem = dataSource.getDataItem(itemName);

		if (dataItem == null)
			dataItem = internalFind(dataSource.getParent().getVariables(), itemName);

		if (dataItem == null && searchGlobally)
		{
			AttributeDefinition attDefinition = Services.Application.getDefinition().getAttribute(itemName);
			if (attDefinition != null)
				dataItem = new DataItem(attDefinition);
		}

		return dataItem;
	}

	public static DataItem find(List<? extends DataItem> items, String itemName)
	{
		itemName = getNormalizedName(itemName);
		return internalFind(items, itemName);
	}

	private static DataItem internalFind(List<? extends DataItem> items, String itemName)
	{
		for (DataItem item : items)
		{
			if (item.getName().equalsIgnoreCase(itemName))
				return item;
		}

		return null;
	}
}
