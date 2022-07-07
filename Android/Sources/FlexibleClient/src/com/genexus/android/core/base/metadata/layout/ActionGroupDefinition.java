package com.genexus.android.core.base.metadata.layout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;

public class ActionGroupDefinition implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private final LayoutDefinition mLayout;
	private final List<ActionGroupItemDefinition> mItems;
	private String mControlName;
	private String mControlType;
	protected String mThemeClass;
	private String mCaption;

	ActionGroupDefinition(LayoutDefinition layout, INodeObject json)
	{
		mLayout = layout;
		mItems = new ArrayList<>();
		mThemeClass = null;

		deserialize(json);
	}

	private void deserialize(INodeObject json)
	{
		if (json != null)
		{
			mControlName = json.optString("@controlName");
			mControlType = json.optString("@controlType");
			mThemeClass = json.optString("@class");
			mCaption = json.optString("@caption");

			for (INodeObject jsonActionBarItem : json.optCollection("action"))
			{
				ActionGroupItemDefinition item = ActionGroupItem.create(this, jsonActionBarItem);
				if (item != null)
					mItems.add(item);
			}
		}
	}

	public LayoutDefinition getLayout()
	{
		return mLayout;
	}

	public String getName()
	{
		return mControlName;
	}

	public String getControlType()
	{
		return mControlType;
	}

	public List<ActionGroupItemDefinition> getItems()
	{
		return mItems;
	}

	public ThemeClassDefinition getThemeClass() {
		return Services.Themes.getThemeClass(mThemeClass);
	}

	public String getCaption()
	{
		return mCaption;
	}

	public Iterable<ActionGroupActionDefinition> getActions()
	{
		ArrayList<ActionGroupActionDefinition> actions = new ArrayList<>();
		for (ActionGroupItemDefinition item : getItems())
		{
			if (item.getType() == ActionGroupItem.TYPE_ACTION)
				actions.add((ActionGroupActionDefinition)item);
		}

		return actions;
	}
}
