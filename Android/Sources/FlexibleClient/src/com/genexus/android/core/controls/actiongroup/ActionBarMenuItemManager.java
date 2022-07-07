package com.genexus.android.core.controls.actiongroup;

import android.app.Activity;

import com.genexus.android.R;
import com.genexus.android.core.activities.ActivityHelper;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.enums.DisplayModes;
import com.genexus.android.core.base.metadata.layout.ActionGroupItemDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.compatibility.SherlockHelper;
import com.genexus.android.core.fragments.IDataView;

class ActionBarMenuItemManager extends MenuItemManager
{
	private final Activity mActivity;
	private final IDataView mDataView;
	private final int mActionBarId;

	static final int GROUP_ID_FACTOR = 0x00010000;
	static final int GROUP_ID_MASK   = 0x7fff0000;
	static final int ITEM_ID_MASK    = 0x0000ffff;

	public ActionBarMenuItemManager(Activity activity, IDataView dataView, int barId)
	{
		super(activity);

		mActivity = activity;
		mDataView = dataView;
		mActionBarId = barId;

		if (dataView.getLayout() != null)
			setDefinition(dataView.getLayout().getActionBar());
	}

	@Override
	protected MenuItemControl newItemControl(ActionGroupItemDefinition definition, int id)
	{
		int compositeId = (GROUP_ID_FACTOR * mActionBarId) + id;
		MenuItemControl control = super.newItemControl(definition, compositeId);

		// If the activity has no action bar, force priority to LOW
		// (so that it's available when using the menu button if the device has one).
		if (!ActivityHelper.hasActionBar(mActivity))
			control.setPriority(ActionGroupItemDefinition.PRIORITY_LOW);

		// Change standard "Save" to "Delete" when in Delete mode.
		if (mDataView.getMode() == DisplayModes.DELETE)
		{
			if (control.getAction() != null && ActionDefinition.StandardAction.SAVE.equalsIgnoreCase(control.getAction().getEventName()))
				control.setCaption(Services.Strings.getResource(R.string.GXM_mnudelete));
		}

		// Set event handler for onRequestLayout() to invalidate the menu.
		if (ActivityHelper.hasActionBar(mActivity))
			control.setOnRequestLayoutListener(mOnMenuItemRequestLayout);

		return control;
	}

	@Override
	protected MenuItemControl getItem(int id)
	{
		return super.getItem(id & ITEM_ID_MASK);
	}

	private final MenuItemControl.OnRequestLayoutListener mOnMenuItemRequestLayout = new MenuItemControl.OnRequestLayoutListener()
	{
		@Override
		public void onRequestLayout(MenuItemControl item)
		{
			Services.Device.runOnUiThread(new Runnable()
			{
				@Override
				public void run() { SherlockHelper.invalidateOptionsMenu(mActivity); }
			});
		}
	};
}
