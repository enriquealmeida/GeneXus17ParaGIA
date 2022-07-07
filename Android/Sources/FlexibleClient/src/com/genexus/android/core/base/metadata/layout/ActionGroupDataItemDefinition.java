package com.genexus.android.core.base.metadata.layout;

import com.genexus.android.core.base.metadata.enums.LayoutItemsTypes;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.serialization.INodeObject;

public class ActionGroupDataItemDefinition extends ActionGroupItemDefinition
{
	private static final long serialVersionUID = 1L;

	private final LayoutItemDefinition mDataItem;

	public ActionGroupDataItemDefinition(ActionGroupDefinition parent, INodeObject json)
	{
		super(parent, json);
		mDataItem = new LayoutItemDefinition(parent.getLayout(), null);
		mDataItem.setType(LayoutItemsTypes.DATA);
		mDataItem.readData(json);
	}

	public LayoutItemDefinition getDataItem()
	{
		return mDataItem;
	}

	@Override
	public int getType()
	{
		return ActionGroupItem.TYPE_DATA;
	}

	@Override
	public String getCaption()
	{
		return mDataItem.getCaption();
	}

	@Override
	public ThemeClassDefinition getThemeClass()
	{
		return mDataItem.getThemeClass();
	}
}
