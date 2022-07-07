package com.genexus.android.core.actions;

import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.model.EntityFactory;

/**
 * Custom action used to include a specific method call (via a Runnable) inside a composite.
 */
public class RunnableAction extends Action
{
	private final Runnable mRunnable;

	public RunnableAction(UIContext context, Runnable runnable)
	{
		super(context, new ActionDefinition(null), new ActionParameters(EntityFactory.newEntity()));
		mRunnable = runnable;
	}

	public RunnableAction(UIContext context, Runnable runnable, ActionDefinition actionDef, ActionParameters parms)
	{
		super(context, actionDef, parms);
		mRunnable = runnable;
	}

	@Override
	public boolean Do()
	{
		mRunnable.run();
		return true;
	}

	@Override
	public String getErrorMessage() {
		return ""; // never fails
	}
}
