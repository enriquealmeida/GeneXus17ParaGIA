package com.genexus.android.core.controls.actiongroup;

import java.util.ArrayList;

import com.genexus.android.core.base.metadata.layout.ActionGroupDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controls.IGxControl;
import com.genexus.android.core.fragments.IDataView;

public class ActionGroupManager
{
	private final IDataView mDataView;
	private ArrayList<ActionGroupBaseControl> mActionGroups;

	private static final String CONTROL_TYPE_SHEET = "Action Sheet";
	private static final String CONTROL_TYPE_BAR = "Bar";
	private static final String CONTROL_TYPE_POPUP_MENU = "Menu";

	public ActionGroupManager(IDataView dataView)
	{
		mDataView = dataView;
		if (dataView.getLayout() != null)
			initActionGroups();
	}

	private void initActionGroups()
	{
		if (mActionGroups == null && mDataView.getLayout() != null)
		{
			mActionGroups = new ArrayList<>();

			for (ActionGroupDefinition group : mDataView.getLayout().getActionGroups())
			{
				String groupType = group.getControlType();
				ActionGroupBaseControl groupControl = null;

				if (CONTROL_TYPE_SHEET.equalsIgnoreCase(groupType))
					groupControl = new ActionGroupSheetControl(mDataView, group);
				else if (CONTROL_TYPE_BAR.equalsIgnoreCase(groupType))
					groupControl = new ActionGroupBarControl(mDataView, group);
				else if (CONTROL_TYPE_POPUP_MENU.equalsIgnoreCase(groupType))
					groupControl = new ActionGroupPopupControl(mDataView, group);
				else
					Services.Log.warning(String.format("Unknown action group control type: '%s'", groupType));

				if (groupControl != null)
					mActionGroups.add(groupControl);
			}
		}
	}

	public IGxControl getControl(String name)
	{
		if (mActionGroups == null)
			initActionGroups();

		// Search for action groups or any controls inside them.
		for (ActionGroupBaseControl group : mActionGroups)
		{
			if (group.getName().equalsIgnoreCase(name))
				return group;

			IGxControl groupItem = group.getControl(name);
			if (groupItem != null)
				return groupItem;
		}

		return null;
	}

	public void onCloseDataView()
	{
		if (mActionGroups != null)
		{
			for (ActionGroupBaseControl group : mActionGroups)
				group.onCloseDataView();
		}
	}
}
