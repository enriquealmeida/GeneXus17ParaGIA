package com.genexus.android.core.base.metadata.layout;

import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.serialization.INodeObject;

public class ActionGroupSubgroupDefinition extends ActionGroupItemDefinition implements ILayoutActionDefinition
{
	private static final long serialVersionUID = 1L;

	private final ActionGroupDefinition mGroup;

	public ActionGroupSubgroupDefinition(ActionGroupDefinition parent, INodeObject json)
	{
		super(parent, json);
		mGroup = new ActionGroupDefinition(parent.getLayout(), json);
	}

	@Override
	public int getType()
	{
		return ActionGroupItem.TYPE_GROUP;
	}

	public ActionGroupDefinition getGroup()
	{
		return mGroup;
	}

	@Override
	public String getCaption()
	{
		String caption = LayoutActionDefinitionHelper.getCaption(this);
		if (caption == null)
			caption = mGroup.getName();

		return caption;
	}

	@Override
	public ThemeClassDefinition getThemeClass()
	{
		return LayoutActionDefinitionHelper.getThemeClass(this);
	}

	@Override
	public String getEventName()
	{
		// Subgroups do not have an associated event, only the visual properties of an action.
		return null;
	}

	@Override
	public ActionDefinition getEvent()
	{
		// Subgroups do not have an associated event, only the visual properties of an action.
		return null;
	}

	@Override
	public String getImage()
	{
		return LayoutActionDefinitionHelper.getImage(this);
	}

	@Override
	public String getDisabledImage()
	{
		return LayoutActionDefinitionHelper.getDisabledImage(this);
	}

	@Override
	public String getHighlightedImage()
	{
		return LayoutActionDefinitionHelper.getHighlightedImage(this);
	}
}
