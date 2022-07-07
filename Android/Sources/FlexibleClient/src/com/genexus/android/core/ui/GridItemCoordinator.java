package com.genexus.android.core.ui;

import com.genexus.android.core.actions.UIContext;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.IViewDefinition;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.controls.grids.GridHelper;

public class GridItemCoordinator extends CoordinatorBase
{
	private final GridHelper mHelper;

	public GridItemCoordinator(UIContext context, GridHelper helper, Entity itemData)
	{
		super(context);
		mHelper = helper;
		setData(itemData);
	}

	public void setItemContext(UIContext context)
	{
		setUIContext(context);
	}

	@Override
	protected IViewDefinition getContainerDefinition()
	{
		return mHelper.getDefinition().getLayout().getParent();
	}

	@Override
	public boolean runAction(ActionDefinition action, Anchor anchor)
	{
		return mHelper.runAction(action, getData(), anchor);
	}
}
