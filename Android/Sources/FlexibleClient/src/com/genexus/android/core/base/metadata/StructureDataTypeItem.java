package com.genexus.android.core.base.metadata;

import com.genexus.android.core.base.serialization.INodeObject;

public class StructureDataTypeItem extends DataTypeDefinition
{
	private static final long serialVersionUID = 1L;

	private StructureItemCollection mItems;
	private String mName;

	public StructureDataTypeItem(INodeObject node, StructureDataTypeItem parent)
	{
		super(node);
		mItems = new StructureItemCollection(parent);
	}

	public StructureItemCollection getItems() { return mItems; }

	@Override
	public String getName() { return mName; }

	void setName(String name) { mName = name; }
}
