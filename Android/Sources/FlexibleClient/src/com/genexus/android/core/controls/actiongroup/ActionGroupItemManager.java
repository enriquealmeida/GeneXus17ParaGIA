package com.genexus.android.core.controls.actiongroup;

import java.util.ArrayList;
import java.util.List;

import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.layout.ActionGroupDefinition;
import com.genexus.android.core.base.metadata.layout.ActionGroupItem;
import com.genexus.android.core.base.metadata.layout.ActionGroupItemDefinition;
import com.genexus.android.core.base.metadata.layout.ActionGroupSubgroupDefinition;
import com.genexus.android.core.controls.IGxControl;

abstract class ActionGroupItemManager<ItemControlT extends ActionGroupBaseItemControl<ItemControlT>>
{
	private final ArrayList<ItemControlT> mAllItems;
	private final ArrayList<ItemControlT> mMainItems;

	public ActionGroupItemManager()
	{
		// MainItems are those directly in the group. AllItems includes those in subgroups.
		mAllItems = new ArrayList<>();
		mMainItems = new ArrayList<>();
	}

	public void setDefinition(ActionGroupDefinition definition)
	{
		mAllItems.clear();
		mMainItems.clear();
		initializeControls(definition, mMainItems);
	}

	private void initializeControls(ActionGroupDefinition group, List<ItemControlT> parentChildren)
	{
		// For now only actions and subgroups are supported.
		for (ActionGroupItemDefinition groupItem : group.getItems())
		{
			if (groupItem.getType() == ActionGroupItem.TYPE_ACTION)
			{
				ItemControlT groupItemControl = newItemControl(groupItem, mAllItems.size());

				mAllItems.add(groupItemControl);
				parentChildren.add(groupItemControl);
			}
			else if (groupItem.getType() == ActionGroupItem.TYPE_GROUP)
			{
				// Add the submenu itself.
				ActionGroupSubgroupDefinition subgroup = (ActionGroupSubgroupDefinition)groupItem;
				ItemControlT subgroupControl = newItemControl(subgroup, mAllItems.size());

				mAllItems.add(subgroupControl);
				parentChildren.add(subgroupControl);

				// Read subitems.
				initializeControls(subgroup.getGroup(), subgroupControl.getSubItems());
			}
		}
	}

	protected abstract ItemControlT newItemControl(ActionGroupItemDefinition definition, int id);

	protected List<ItemControlT> getMainItems()
	{
		return mMainItems;
	}

	protected List<ItemControlT> getAllItems()
	{
		return mAllItems;
	}

	public ActionDefinition getItemEvent(int id)
	{
		ItemControlT itemControl = getItem(id);
		if (itemControl != null && itemControl.getAction() != null)
			return itemControl.getAction().getEvent();
		else
			return null;
	}

	protected ItemControlT getItem(int id)
	{
		if (id < mAllItems.size())
			return mAllItems.get(id);
		else
			return null;
	}

	public IGxControl getControl(String name)
	{
		for (ItemControlT control : mAllItems)
		{
			if (control.getName().equalsIgnoreCase(name))
				return control;
		}

		return null;
	}
}
