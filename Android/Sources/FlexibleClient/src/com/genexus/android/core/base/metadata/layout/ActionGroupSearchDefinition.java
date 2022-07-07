package com.genexus.android.core.base.metadata.layout;

import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;

public class ActionGroupSearchDefinition extends ActionGroupItemDefinition
{
	private static final long serialVersionUID = 1L;

	private final String mInviteMessage;

	public ActionGroupSearchDefinition(ActionGroupDefinition parent, INodeObject json)
	{
		super(parent, json);
		mInviteMessage = json.optString("@inviteMessage");
	}

	@Override
	public int getType()
	{
		return ActionGroupItem.TYPE_SEARCH;
	}

	@Override
	public String getCaption()
	{
		return Services.Language.getTranslation(mInviteMessage);
	}

	@Override
	public ThemeClassDefinition getThemeClass()
	{
		return null;
	}
}
