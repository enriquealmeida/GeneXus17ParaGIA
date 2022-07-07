package com.genexus.android.core.base.metadata.layout;

import com.genexus.android.core.base.model.PropertiesObject;
import com.genexus.android.core.base.services.Services;

public class ControlInfo extends PropertiesObject
{
	private static final long serialVersionUID = 1L;

	private String mControl;

	public String getControl() { return mControl; }
	public void setControl(String value) { mControl = value; }

	public String getTranslatedProperty(String propName)
	{
		String value = optStringProperty(propName);
		return Services.Language.getTranslation(value);
	}
}
