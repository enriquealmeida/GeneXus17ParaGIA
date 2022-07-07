package com.genexus.android.core.layers;

import com.artech.base.services.IGxProcedure;
import com.genexus.android.core.base.application.IGxObject;
import com.genexus.android.core.base.application.OutputResult;
import com.genexus.android.core.base.model.PropertiesObject;

public class LocalDataProvider implements IGxObject
{
	private final String mName;
	private final IGxProcedure mImplementation;

	public LocalDataProvider(String name)
	{
		mName = name;

		// Data providers are actually generated as procedures.
		mImplementation = GxObjectFactory.getProcedure(name);
	}

	@Override
	public OutputResult execute(PropertiesObject parameters)
	{
		if (mImplementation != null)
		{
			LocalUtils.beginTransaction();
			try
			{
				mImplementation.execute(parameters);
			}
			finally {
				LocalUtils.endTransaction();
			}
		
			return OutputResult.ok();
		}
		else
			return LocalUtils.outputNoImplementation(mName);
	}
}
