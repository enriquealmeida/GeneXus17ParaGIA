package com.genexus.android.core.controls.actiongroup;

import java.util.ArrayList;
import java.util.List;

import com.genexus.android.core.base.metadata.layout.ActionGroupItemDefinition;
import com.genexus.android.core.base.metadata.layout.ILayoutActionDefinition;
import com.genexus.android.core.controls.GxControlBase;
import com.genexus.android.core.utils.Cast;

abstract class ActionGroupBaseItemControl<ItemControlT extends ActionGroupBaseItemControl<ItemControlT>> extends GxControlBase
{
	private final ActionGroupItemDefinition mDefinition;
	private final ArrayList<ItemControlT> mSubItems;
	private int mPriority;

	public ActionGroupBaseItemControl(ActionGroupItemDefinition definition)
	{
		mDefinition = definition;
		setEnabled(definition.isEnabled());
		setCaption(definition.getCaption());
		setVisible(definition.isVisible());
		setThemeClass(definition.getThemeClass());
		setPriority(definition.getPriority());

		mSubItems = new ArrayList<>();
	}

	public int getType()
	{
		return mDefinition.getType();
	}

	public ILayoutActionDefinition getAction()
	{
		return Cast.as(ILayoutActionDefinition.class, mDefinition);
	}

	public List<ItemControlT> getSubItems()
	{
		return mSubItems;
	}

	@Override
	public String getName()
	{
		return mDefinition.getControlName();
	}

	public int getPriority()
	{
		return mPriority;
	}

	public void setPriority(int priority)
	{
		mPriority = priority;
	}
}
