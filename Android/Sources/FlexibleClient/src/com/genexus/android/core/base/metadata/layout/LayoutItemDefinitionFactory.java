package com.genexus.android.core.base.metadata.layout;

import com.genexus.android.core.base.metadata.enums.LayoutItemsTypes;

public class LayoutItemDefinitionFactory
{
	public static LayoutItemDefinition createDefinition(LayoutDefinition layout, LayoutItemDefinition parent, String attName)
	{
		if (attName.equalsIgnoreCase(LayoutItemsTypes.DATA)	||
			attName.equalsIgnoreCase(LayoutItemsTypes.IMAGE) ||
			attName.equalsIgnoreCase(LayoutItemsTypes.TEXT_BLOCK) ||
			attName.equalsIgnoreCase(LayoutItemsTypes.CONTENT))
			return new LayoutItemDefinition(layout, parent);

		if (attName.equalsIgnoreCase(LayoutItemsTypes.ACTION))
			return new LayoutActionDefinition(layout, parent);

		if (attName.equalsIgnoreCase(LayoutItemsTypes.USER_CONTROL))
			return new LayoutUserControlDefinition(layout, parent);
		
		if (attName.equalsIgnoreCase(LayoutItemsTypes.GROUP))
			return new GroupDefinition(layout, parent);
		
		if (attName.equalsIgnoreCase(LayoutItemsTypes.ROW))
			return new RowDefinition(layout, parent);
		
		if (attName.equalsIgnoreCase(LayoutItemsTypes.CELL))
			return new CellDefinition(layout, parent);
		
		if (attName.equalsIgnoreCase(LayoutItemsTypes.GRID))
			return new GridDefinition(layout, parent); 
			
		if (attName.equalsIgnoreCase(LayoutItemsTypes.TABLE))
			return new TableDefinition(layout, parent);

		if (attName.equalsIgnoreCase(LayoutItemsTypes.TAB))
			return new TabControlDefinition(layout, parent);
		
		if (attName.equalsIgnoreCase(LayoutItemsTypes.TAB_PAGE))
			return new TabItemDefinition(layout, parent);

		if (attName.equalsIgnoreCase(LayoutItemsTypes.ALL_CONTENT))
			return new AllContentDefinition(layout, parent);

		if (attName.equalsIgnoreCase(LayoutItemsTypes.ONE_CONTENT))
			return new ContentDefinition(layout, parent);

		if (attName.equalsIgnoreCase(LayoutItemsTypes.COMPONENT))
			return new ComponentDefinition(layout, parent);

		return null;
	}
}
