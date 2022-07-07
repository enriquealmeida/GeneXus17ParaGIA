package com.genexus.android.core.layers;

import com.genexus.android.core.base.application.IGxObject;
import com.genexus.android.core.base.application.OutputResult;
import com.genexus.android.core.base.metadata.DataProviderDefinition;
import com.genexus.android.core.base.metadata.ObjectParameterDefinition;
import com.genexus.android.core.base.model.PropertiesObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.common.IServiceDataResult;

class RemoteDataProvider implements IGxObject
{
	private final DataProviderDefinition mDefinition;

	public RemoteDataProvider(DataProviderDefinition definition)
	{
		mDefinition = definition;
	}

	@Override
	public OutputResult execute(PropertiesObject parameters)
	{
		// Call Data Provider via HTTP get.
		String uri = getUri(parameters);
		IServiceDataResult response = Services.HttpService.getDataFromProvider(uri, null, false);

		// Read the SDT output parameter.
		readOutput(response, parameters);

		// Return errors and/or messages, if any.
		return RemoteUtils.translateOutput(response);
	}

	private String getUri(PropertiesObject parameters)
	{
		String qualifiedName = mDefinition.getName();
		int lastIndex = qualifiedName.lastIndexOf('.');
		String serviceName = lastIndex != -1 ? qualifiedName.replace("." , "/") : mDefinition.getName();
		return Services.Application.get().UriMaker.getObjectUrl(serviceName, parameters.getInternalProperties());
	}

	private void readOutput(IServiceDataResult response, PropertiesObject parameters)
	{
		if (mDefinition.getOutParameters().size() == 1)
		{
			ObjectParameterDefinition outParam = mDefinition.getOutParameters().get(0);
			parameters.setProperty(outParam.getName(), response.getData());
		}
	}
}
