package com.genexus.android.core.layers;

import com.genexus.android.core.base.application.IGxObject;
import com.genexus.android.core.base.application.OutputResult;
import com.genexus.android.core.base.model.PropertiesObject;

class DummyObject implements IGxObject
{
	private final String mName;

	public DummyObject(String name)
	{
		mName = name;
	}

	@Override
	public OutputResult execute(PropertiesObject parameters)
	{
		return RemoteUtils.outputNoDefinition(mName);
	}
}
