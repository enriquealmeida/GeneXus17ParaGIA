package com.genexus.android.core.controls;

import com.genexus.android.core.base.metadata.layout.GridDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

public class ControlPropertiesDefinition
{
	private final LayoutItemDefinition mItem;

	public ControlPropertiesDefinition(LayoutItemDefinition item)
	{
		if (item == null)
			throw new IllegalArgumentException("Null item definition.");

		mItem = item;
	}

	public LayoutItemDefinition getItem() { return mItem; }

	@Override
	public String toString()
	{
		return mItem.getName();
	}

	/**
	 * Helper method to read a "data item" property from a control. Normally, this can
	 * be an attribute or an SDT variable/SDT field combination; this method unifies them
	 * and returns an expression that can be used to get the value.
	 * @param dataProperty Name of the property with the "Data Attribute".
	 * @param selectorProperty Name of the property with the "Data Field Selector"
	 * @return The expression used to get the value from the data entity.
	 */
	public String readDataExpression(String dataProperty, String selectorProperty)
	{
		String data;
		String selector;

		// Try to read from layout item first, in case they are "common" to different control definitions.
		data = mItem.optStringProperty(dataProperty);
		selector = mItem.optStringProperty(selectorProperty);

		if (mItem.getControlInfo() != null)
		{
			if (!Services.Strings.hasValue(data))
				data = mItem.getControlInfo().optStringProperty(dataProperty);

			if (!Services.Strings.hasValue(selector))
				selector = mItem.getControlInfo().optStringProperty(selectorProperty);
		}

		return buildDataExpression(data, selector);
	}

	private String getDataSourceMember()
	{
		if (mItem instanceof GridDefinition)
			return ((GridDefinition)mItem).getDataSourceMember();
		else
			return null;
	}

	protected String buildDataExpression(String dataItem, String fieldSelector)
	{
		// Possible cases:
		// * No data item -> do nothing.
		if (!Services.Strings.hasValue(dataItem) || dataItem.equalsIgnoreCase("(none)"))
			return null;

		if (dataItem.equalsIgnoreCase(getDataSourceMember()))
		{
			// * DataItem is the same variable as the SDT collection of the grid -> strip
			// the "Item(x)" part from the field selector and return that.
			if (Strings.hasValue(fieldSelector))
			{
				final String INDEXER = "Item(0).";
				if (Strings.starsWithIgnoreCase(fieldSelector, INDEXER))
					fieldSelector = fieldSelector.substring(INDEXER.length());

				return fieldSelector;
			}
			else
			{
				// If the fieldSelector is empty then the expression is empty too.
				// This is correct -- when grids are bound to an SDT, the dataItem is the same as
				// the collection, and can't be changed. So the only actual way for the user to NOT
				// set this property is to have an empty fieldSelector.
				return null;
			}
		}
		else
		{
			// * DataItem is an attribute -> return it.
			// * DataItem is a "simple" variable in the DS (no field selector) -> return it.
			if (!Services.Strings.hasValue(fieldSelector))
				return dataItem;

			// * DataItem is a "complex" variable in the DS -> concatenate with field selector and return it.
			return dataItem + Strings.DOT + fieldSelector;
		}
	}
}
