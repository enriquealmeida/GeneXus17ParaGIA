package com.genexus.android.core.common;

import android.content.ContextWrapper;

import com.genexus.android.core.actions.Action;
import com.genexus.android.core.actions.UIContext;
import com.genexus.android.core.base.model.Entity;

public class ExecutionContext extends ContextWrapper
{
	private final UIContext mContext;
	private Action mAction;

	private ExecutionContext(UIContext context)
	{
		super(context);
		mContext = context;
	}

	public static ExecutionContext base(UIContext context)
	{
		return new ExecutionContext(context);
	}

	public static ExecutionContext inAction(Action action)
	{
		ExecutionContext ec = new ExecutionContext(action.getContext());
		ec.mAction = action;
		return ec;
	}

	public UIContext getUIContext()
	{
		return mContext;
	}

	public Entity getData()
	{
		if (mAction != null)
			return mAction.getParameterEntity();
		else
			return null;
	}
}
