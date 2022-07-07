package com.genexus.android.layout;

import android.content.Context;

import com.genexus.android.core.base.metadata.layout.TableDefinition;
import com.genexus.android.core.ui.Coordinator;

public class GxLayoutInTab extends GxLayout
{
	private boolean mHasScroll;
	private boolean mIsActiveTab;

	private boolean mIsVisibleTab = true;

	public GxLayoutInTab(Context context, TableDefinition layout, Coordinator coordinator)
    {
		super(context, layout, coordinator);
	}

	public void setHasScroll(boolean hasScroll)
	{
		mHasScroll = hasScroll;
	}

	public boolean getHasScroll()
	{
		return mHasScroll;
	}

	public boolean isActiveTab()
	{
		return mIsActiveTab;
	}

	public void setIsActiveTab(boolean value)
	{
		mIsActiveTab = value;
	}

	public boolean isVisibleTab()
	{
		return mIsVisibleTab;
	}

	public void setIsVisibleTab(boolean value)
	{
		mIsVisibleTab = value;
	}

}
