package com.genexus.android.core.actions;

import androidx.annotation.Nullable;

import com.genexus.android.core.base.metadata.ActionDefinition;

public class ActionDefinitionWithHandlers extends ActionDefinition
{
	private static final long serialVersionUID = 1L;

	private final ActionDefinition mDefinition;
	private final Runnable mPreHandler;
	private final Runnable mPostHandler;

	public ActionDefinitionWithHandlers(ActionDefinition def, @Nullable Runnable preHandler, @Nullable Runnable postHandler)
	{
		super(null);
		mDefinition = def;
		mPreHandler = preHandler;
		mPostHandler = postHandler;
	}

	public ActionDefinition getDefinition()
	{
		return mDefinition;
	}

	public @Nullable Runnable getPreHandler()
	{
		return mPreHandler;
	}

	public @Nullable Runnable getPostHandler()
	{
		return mPostHandler;
	}

	@Override
	public String getName() {
		return mDefinition.getName();
	}

	@Override
	public boolean getIsComposite() {
		return mDefinition.getIsComposite();
	}
}
