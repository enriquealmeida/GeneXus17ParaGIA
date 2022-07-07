package com.genexus.android.core.base.metadata;

import java.io.Serializable;

import com.genexus.android.core.base.services.Services;

public class EnumValuesDefinition implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String mValue;
	private String mDescription;
	private String mName;

	public void setValue(String myValue) {
		this.mValue = myValue;
	}
	public String getValue() {
		return mValue;
	}
	public void setDescription(String myDescription) {
		this.mDescription = myDescription;
	}
	public String getDescription() {
		return Services.Language.getTranslation(mDescription);
	}
	public void setName(String string) {
		mName = string;
	}
	public String getName() {
		return mName;
	}

	@Override
	public String toString(){
		return getName();
	}
}
