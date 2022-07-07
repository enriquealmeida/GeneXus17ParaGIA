package com.genexus.android.core.base.metadata.layout;

import java.io.Serializable;

import com.genexus.android.core.base.serialization.INodeObject;

public class ActionBarDefinition extends ActionGroupDefinition implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private boolean mIsVisible;

	ActionBarDefinition(LayoutDefinition parent, INodeObject jsonActionBar)
	{
		super(parent, jsonActionBar);
		mIsVisible = true;

		if (jsonActionBar != null) {
			mThemeClass = jsonActionBar.optString("@applicationBarsClass", "ApplicationBars");
			mIsVisible = jsonActionBar.optBoolean("@showApplicationBars", true);
		}
	}

	public boolean isVisible()
	{
		return mIsVisible;
	}
}
