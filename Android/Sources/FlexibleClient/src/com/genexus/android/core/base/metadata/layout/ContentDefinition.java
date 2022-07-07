package com.genexus.android.core.base.metadata.layout;

import com.genexus.android.core.base.metadata.DetailDefinition;
import com.genexus.android.core.base.metadata.IDataViewDefinition;
import com.genexus.android.core.base.metadata.IViewDefinition;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.utils.Strings;

public class ContentDefinition extends ComponentDefinition
{
	private String mContentId;
	private String mDisplayType;

	public ContentDefinition(LayoutDefinition layout, LayoutItemDefinition parent)
	{
		super(layout, parent);
	}

	@Override
	public void readData(INodeObject node)
	{
		super.readData(node);
		mContentId = node.optString("@content").replace("Section:", Strings.EMPTY).trim();
		mDisplayType = node.optString("@display");
	}

	public String getDisplayType() { return mDisplayType; }

	/**
	 * Returns the data view that will be shown inside this content (or linked to when display=link).
	 * Should be a section belonging to the current detail.
	 */
	@Override
	public IViewDefinition getObject()
	{
		IDataViewDefinition dataView = getLayout().getParent();
		if (dataView instanceof DetailDefinition)
		{
			DetailDefinition detail = (DetailDefinition)dataView;
			return detail.getSection(mContentId);
		}

		return null;
	}
}
