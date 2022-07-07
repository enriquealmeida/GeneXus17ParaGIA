package com.genexus.android.core.base.metadata.layout;

import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;

public class ActionGroupActionDefinition extends ActionGroupItemDefinition implements ILayoutActionDefinition
{
	private static final long serialVersionUID = 1L;

	private final String mActionName;
	private ActionDefinition mAction;

	ActionGroupActionDefinition(ActionGroupDefinition parent, INodeObject json)
	{
		super(parent, json);
		mActionName = json.optString("@actionElement");
	}

	@Override
	public int getType()
	{
		return ActionGroupItem.TYPE_ACTION;
	}

	@Override
	public ActionDefinition getEvent()
	{
		if (mAction == null && Services.Strings.hasValue(mActionName))
			mAction = getParent().getLayout().getParent().getEvent(mActionName);

		return mAction;
	}

	@Override
	public String getEventName()
	{
		return mActionName;
	}

	@Override
	public boolean isVisible()
	{
		return LayoutActionDefinitionHelper.isVisible(this);
	}

	@Override
	public boolean isEnabled()
	{
		return LayoutActionDefinitionHelper.isEnabled(this);
	}

	@Override
	public ThemeClassDefinition getThemeClass()
	{
		return LayoutActionDefinitionHelper.getThemeClass(this);
	}

	@Override
	public String getCaption()
	{
		return LayoutActionDefinitionHelper.getCaption(this);
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

	@Override
	protected String getPriorityValue()
	{
		 return LayoutActionDefinitionHelper.getProperty(this, "@priority");
	}
}
