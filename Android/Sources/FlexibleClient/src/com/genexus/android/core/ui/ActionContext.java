package com.genexus.android.core.ui;

import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.IViewDefinition;
import com.genexus.android.core.base.model.Entity;

/**
 * Interface for context that can be used to execute actions.
 */
public interface ActionContext
{
	IViewDefinition getDefinition();
	Entity getContextEntity();

	void runAction(ActionDefinition action, Anchor anchor);
	// run actions allow duplicates from fireEvent
	void runAction(ActionDefinition action, Anchor anchor, boolean allowDuplicate);
}
