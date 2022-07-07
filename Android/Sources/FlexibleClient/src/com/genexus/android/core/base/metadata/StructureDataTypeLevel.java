package com.genexus.android.core.base.metadata;

import com.genexus.android.core.base.serialization.INodeCollection;
import com.genexus.android.core.base.serialization.INodeObject;

public class StructureDataTypeLevel extends StructureDataTypeItem
{
	private static final long serialVersionUID = 1L;

	public StructureDataTypeLevel(INodeObject obj, StructureDataTypeItem parent)
	{
		super(obj, parent);
	}

	@Override
	public void deserialize(INodeObject obj)
	{
		String isColl = obj.optString("IsCollection");
		if (isColl.length() > 0)
			Boolean.parseBoolean(isColl);

		String name = obj.getString("Name");
		setName(name);

		INodeCollection items = obj.optCollection("Items");
		for (int i = 0; i < items.length() ; i++)
		{
			INodeObject item = items.getNode(i);
			INodeCollection subItems = item.getCollection("Items");

			StructureDataTypeItem subItem;
			if (subItems != null)
				subItem = new StructureDataTypeLevel(item, this);
			else
				subItem = new StructureDataTypeItem(item, this);

			subItem.deserialize(item);
			getItems().add(subItem);
		}
	}
}
