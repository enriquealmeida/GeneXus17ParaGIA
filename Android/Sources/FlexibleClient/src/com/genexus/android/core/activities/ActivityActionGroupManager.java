package com.genexus.android.core.activities;

import java.util.HashMap;

import android.app.Activity;

import com.genexus.android.core.controls.IGxControl;
import com.genexus.android.core.controls.actiongroup.ActionBarMerger;
import com.genexus.android.core.controls.actiongroup.ActionGroupManager;
import com.genexus.android.core.fragments.IDataView;

class ActivityActionGroupManager
{
	private final HashMap<IDataView, ActionGroupManager> mActionGroups;
	private final ActionBarMerger mActionBars;

	public ActivityActionGroupManager(Activity activity)
	{
		mActionGroups = new HashMap<>();
		mActionBars = new ActionBarMerger(activity);
	}

	public void addDataView(IDataView dataView)
	{
		mActionGroups.put(dataView, new ActionGroupManager(dataView));
		mActionBars.add(dataView);
	}

	public void removeDataView(IDataView dataView)
	{
		ActionGroupManager groupManager = mActionGroups.get(dataView);
		if (groupManager != null)
		{
			groupManager.onCloseDataView();
			mActionGroups.remove(dataView);
		}

		mActionBars.remove(dataView);
	}

	public void removeAll()
	{
		for (ActionGroupManager groupManager : mActionGroups.values())
			groupManager.onCloseDataView();

		mActionGroups.clear();
		mActionBars.clear();
	}

	public IGxControl getControl(IDataView dataView, String controlName)
	{
		if (dataView != null)
		{
			ActionGroupManager groupManager = mActionGroups.get(dataView);
			if (groupManager != null)
			{
				IGxControl groupControl = groupManager.getControl(controlName);
				if (groupControl != null)
					return groupControl;
			}

			IGxControl actionBarControl = mActionBars.getControl(dataView, controlName);
			if (actionBarControl != null)
				return actionBarControl;
		}

		return null;
	}

	ActionBarMerger getActionBar()
	{
		return mActionBars;
	}
}
