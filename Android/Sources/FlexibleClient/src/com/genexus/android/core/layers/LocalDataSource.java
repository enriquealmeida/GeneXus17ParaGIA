package com.genexus.android.core.layers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.artech.base.services.IGxProcedure;
import com.genexus.android.core.base.metadata.IDataSourceDefinition;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.PropertiesObject;
import com.genexus.android.core.base.providers.GxUri;
import com.genexus.android.core.base.providers.IDataSourceResult;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.DataRequest;

class LocalDataSource
{
	private final IDataSourceDefinition mDefinition;
	private final IGxProcedure mImplementation;

	private static final String OUTPUT_PARAMETER = "Gx_Output";

	public LocalDataSource(IDataSourceDefinition definition)
	{
		mDefinition = definition;
		mImplementation = GxObjectFactory.getProcedure(definition.getName());
	}

	public IDataSourceResult execute(GxUri uri, int sessionId, int start, int count)
	{
		if (mImplementation != null)
		{
			// Convert DP parameters to procedure parameters (converted into strings).
			PropertiesObject parameters = new PropertiesObject();
			Map<String, Object> parameterValues = uri.getQueryValues(sessionId, start, count);

			for (Map.Entry<String, Object> entry : parameterValues.entrySet())
				parameters.setProperty(entry.getKey(), LocalUtils.toParameter(entry.getValue()));

			LocalUtils.beginTransaction();
			try
			{
				// Execute the DP's procedure.
				mImplementation.execute(parameters);
			}
			finally
			{
				LocalUtils.endTransaction();
			}
			
			// Read output.
			return deserializeResult(parameters);
		}
		else
			return new DataProviderResult(LocalUtils.messageNoImplementation(mDefinition.getName()));
	}

	private IDataSourceResult deserializeResult(PropertiesObject procParameters)
	{
		// DP output can be a single Entity, or a List<Entity>.
		Object output = procParameters.getProperty(OUTPUT_PARAMETER);
		List<Entity> resultData = new ArrayList<>();

		if (output instanceof Entity)
		{
			resultData.add((Entity)output);
		}
		else if (output instanceof List<?>)
		{
			List<?> outputList = (List<?>)output;
			for (Object outputItem : outputList)
			{
				if (outputItem instanceof Entity)
					resultData.add((Entity)outputItem);
			}
		}

		return new DataProviderResult(resultData);
	}

	private static class DataProviderResult implements IDataSourceResult
	{
		private List<Entity> mData;
		private final int mErrorType;
		private final String mErrorMessage;

		public DataProviderResult(List<Entity> data)
		{
			mData = data;
			mErrorType = DataRequest.ERROR_NONE;
			mErrorMessage = Strings.EMPTY;
		}

		public DataProviderResult(String errorMessage)
		{
			mData = new ArrayList<>();
			mErrorType = DataRequest.ERROR_SERVER;
			mErrorMessage = errorMessage;
		}

		@Override
		public boolean isUpToDate()
		{
			// Caching is not used, so results are never "up to date".
			return false;
		}

		@Override
		public List<Entity> getData() { return mData; }

		@Override
		public boolean isOk() { return (mErrorType == DataRequest.ERROR_NONE); }

		@Override
		public Date getLastModified() { return null; }

		@Override
		public int getErrorType() { return mErrorType; }

		@Override
		public String getErrorMessage() { return mErrorMessage; }
	}
}
