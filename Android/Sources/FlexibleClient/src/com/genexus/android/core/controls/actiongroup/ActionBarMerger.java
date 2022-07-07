package com.genexus.android.core.controls.actiongroup;

import java.util.ArrayList;
import java.util.BitSet;

import android.app.Activity;
import android.view.Menu;

import com.genexus.android.core.actions.ICustomMenuManager;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.layout.GridDefinition;
import com.genexus.android.core.controls.IGxControl;
import com.genexus.android.core.controls.IGxGridControl;
import com.genexus.android.core.fragments.IDataView;
import com.genexus.android.core.fragments.LayoutFragment;
import com.genexus.android.core.ui.Anchor;
import com.genexus.android.core.utils.Cast;

public class ActionBarMerger
{
	private final Activity mActivity;
	private final ArrayList<DataViewActionBar> mActionBars;
	private final BitSet mActionBarIds;

	public ActionBarMerger(Activity activity)
	{
		mActionBars = new ArrayList<>();
		mActionBarIds = new BitSet();
		mActivity = activity;
	}

	public void add(IDataView dataView)
	{
		// Remove if present before.
		remove(dataView);
		addActionBarFor(dataView);
	}

	private DataViewActionBar addActionBarFor(IDataView dataView)
	{
		int barId = mActionBarIds.nextClearBit(0);
		DataViewActionBar actionBar = new DataViewActionBar(dataView, barId);

		mActionBars.add(actionBar);
		mActionBarIds.set(barId);

		return actionBar;
	}

	public void remove(IDataView dataView)
	{
		for (DataViewActionBar actionBar : mActionBars)
		{
			if (actionBar.mDataView == dataView)
			{
				mActionBars.remove(actionBar);
				mActionBarIds.clear(actionBar.mId);
				break;
			}
		}
	}

	public void clear()
	{
		mActionBars.clear();
		mActionBarIds.clear();
	}

	public IGxControl getControl(IDataView dataView, String controlName)
	{
		DataViewActionBar actionBar = getActionBar(dataView);
		if (actionBar != null)
			return actionBar.getControl(controlName);
		else
			return null;
	}

	public void initializeMenu(Menu menu, Iterable<IDataView> dataViews)
	{
		for (IDataView dataView : dataViews)
		{
			DataViewActionBar actionBar = getActionBar(dataView);
			if (actionBar == null)
				actionBar = addActionBarFor(dataView);

			if (actionBar != null)
				actionBar.initializeMenu(menu);

			if (dataView instanceof ICustomMenuManager)
				((ICustomMenuManager)dataView).onCustomCreateOptionsMenu(menu);
		}
	}

	public boolean onOptionsItemSelected(int itemId)
	{
		int barId = (itemId & ActionBarMenuItemManager.GROUP_ID_MASK) / ActionBarMenuItemManager.GROUP_ID_FACTOR;
		DataViewActionBar actionBar = getActionBar(barId);
		if (actionBar != null)
		{
			ActionDefinition event = actionBar.getItemEvent(itemId);
			if (event != null) {
				IGxGridControl gridControl = null;
				String gridName = GridDefinition.getMultipleSelectionAction(event);
				if (gridName != null) {
					LayoutFragment fragment = Cast.as(LayoutFragment.class, actionBar.mDataView);
					if (fragment != null)
						gridControl = Cast.as(IGxGridControl.class, fragment.getFormCoordinator().getControl(gridName));
				}

				if (gridControl != null && gridControl.getDefinition().getShowSelector() == GridDefinition.ShowSelector.OnAction) {
					gridControl.setSelectionMode(true, event);
				} else {
					// The menu item that was pressed acts as anchor for the action.
					Anchor anchor = Anchor.fromViewId(mActivity, itemId);
					actionBar.mDataView.runAction(event, anchor);
				}
				return true;
			}
		}

		return false;
	}

	private class DataViewActionBar
	{
		private final IDataView mDataView;
		private final int mId;
		private ActionBarMenuItemManager mItemManager;

		public DataViewActionBar(IDataView dataView, int barId)
		{
			mDataView = dataView;
			mId = barId;
			initializeItemManager();
		}

		private void initializeItemManager()
		{
			if (mItemManager == null && mDataView.getLayout() != null)
				mItemManager = new ActionBarMenuItemManager(mActivity, mDataView, mId);
		}

		public ActionDefinition getItemEvent(int itemId)
		{
			initializeItemManager();
			if (mItemManager != null)
				return mItemManager.getItemEvent(itemId);
			else
				return null;
		}

		public void initializeMenu(Menu menu)
		{
			initializeItemManager();
			if (mItemManager != null)
				mItemManager.initializeMenu(menu);
		}

		public IGxControl getControl(String controlName)
		{
			initializeItemManager();
			if (mItemManager != null)
				return mItemManager.getControl(controlName);
			else
				return null;
		}
	}

	private DataViewActionBar getActionBar(IDataView dataView)
	{
		for (DataViewActionBar actionBar : mActionBars)
		{
			if (actionBar.mDataView == dataView)
				return actionBar;
		}

		return null;
	}

	private DataViewActionBar getActionBar(int barId)
	{
		for (DataViewActionBar actionBar : mActionBars)
		{
			if (actionBar.mId == barId)
				return actionBar;
		}

		return null;
	}
}
