package com.genexus.android.core.ui;

import java.util.ArrayDeque;
import java.util.Queue;

import android.util.Pair;

import com.genexus.android.core.actions.UIContext;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.IViewDefinition;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.fragments.IDataView;

public class FormCoordinator extends CoordinatorBase
{
	private final IDataView mDataView;
	private final Queue<Pair<ActionDefinition, Anchor>> mPendingActions;

	public FormCoordinator(UIContext context, IDataView dataView)
	{
		super(context);
		mDataView = dataView;
		mPendingActions = new ArrayDeque<>();
	}

	@Override
	protected IViewDefinition getContainerDefinition()
	{
		return mDataView.getDefinition();
	}

	@Override
	public void setData(Entity data)
	{
		boolean isFirst = (getData() == null);
		super.setData(data);

		if (data != null)
		{
			if (isFirst)
				runPendingActions();
		}
		else
			mPendingActions.clear();
	}

	@Override
	public boolean runAction(ActionDefinition action, Anchor anchor)
	{
		if (getData() == null)
		{
			// Delay actions (e.g. control events) until the coordinator has data.
			mPendingActions.add(new Pair<>(action, anchor));
		}
		else
			mDataView.runAction(action, anchor);

		return true;
	}

	private void runPendingActions()
	{
		while (!mPendingActions.isEmpty())
		{
			final Pair<ActionDefinition, Anchor> item = mPendingActions.remove();
			Services.Device.postOnUiThread(() -> runAction(item.first, item.second));
		}
	}
}
