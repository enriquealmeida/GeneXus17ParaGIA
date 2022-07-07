package com.genexus.android.core.base.metadata;

import java.util.ArrayList;

public class StructureItemCollection extends ArrayList<StructureDataTypeItem>
{
	private static final long serialVersionUID = 1L;

	private final StructureDataTypeItem mParent;

	public StructureItemCollection(StructureDataTypeItem item)
	{
		mParent = item;
	}

	public StructureDataTypeItem getParent()
	{
		return mParent;
	}
}
