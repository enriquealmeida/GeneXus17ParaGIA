package com.genexus.android.gam;


import com.genexus.android.core.base.services.Services;

public class GAMApplication
{
	public GAMApplication()
	{
	}

	// Static API methods.
	public static String getClientId() { return Services.Application.get().getClientId(); }

}
