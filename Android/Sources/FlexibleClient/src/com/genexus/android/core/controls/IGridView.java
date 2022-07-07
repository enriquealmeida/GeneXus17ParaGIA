package com.genexus.android.core.controls;

import com.genexus.android.core.actions.UIContext;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.controllers.ViewData;
import com.genexus.android.core.usercontrols.IGxUserControl;

/**
 * This interface should be implemented for each user control that can be used in a Grid.
 */
public interface IGridView extends IGxUserControl
{
	void addListener(GridEventsListener listener);
	void update(ViewData data);

	interface GridEventsListener
	{
		UIContext getHostUIContext();
		void requestMoreData();
		boolean runAction(UIContext context, ActionDefinition action, Entity entity);
		boolean runDefaultAction(UIContext context, Entity entity, Runnable postAction);
	}
}
