package com.genexus.android.core.base.metadata.layout;

import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.model.PropertiesObject;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;

public abstract class ActionGroupItemDefinition extends PropertiesObject
{
	private static final long serialVersionUID = 1L;

	private final ActionGroupDefinition mParent;
	private final String mControlName;

	public static final int PRIORITY_LOW = 1;
	public static final int PRIORITY_NORMAL = 2;
	public static final int PRIORITY_HIGH = 3;

	private static final String STR_PRIORITY_LOW = "Low";
	private static final String STR_PRIORITY_NORMAL = "Normal";
	private static final String STR_PRIORITY_HIGH = "High";

	public ActionGroupItemDefinition(ActionGroupDefinition parent, INodeObject json)
	{
		mParent = parent;
		mControlName = json.optString("@controlName");

		deserialize(json);
	}

	public abstract int getType();

	protected ActionGroupDefinition getParent()
	{
		return mParent;
	}

	public String getControlName()
	{
		return mControlName;
	}

	public boolean isEnabled()
	{
		return getBooleanProperty("@enabled", true);
	}

	public boolean isVisible()
	{
		return getBooleanProperty("@visible", true);
	}

	public abstract String getCaption();

	public abstract ThemeClassDefinition getThemeClass();

	public int getPriority()
	{
		String strPriority = getPriorityValue();

		if (STR_PRIORITY_LOW.equalsIgnoreCase(strPriority))
			return PRIORITY_LOW;
		else if (STR_PRIORITY_NORMAL.equalsIgnoreCase(strPriority))
			return PRIORITY_NORMAL;
		else if (STR_PRIORITY_HIGH.equalsIgnoreCase(strPriority))
			return PRIORITY_HIGH;

		Services.Log.warning(String.format("Unknown priority value (%s) in action bar item '%s'.", strPriority, getControlName()));
		return PRIORITY_NORMAL;
	}

	protected String getPriorityValue()
	{
		 return optStringProperty("@priority");
	}
}
