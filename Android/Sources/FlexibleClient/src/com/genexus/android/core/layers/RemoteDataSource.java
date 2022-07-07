package com.genexus.android.core.layers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.genexus.android.json.NodeObject;
import com.genexus.android.core.base.metadata.StructureDefinition;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.providers.GxUri;
import com.genexus.android.core.base.providers.IDataSourceResult;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.common.IServiceDataResult;

class RemoteDataSource
{
	public IDataSourceResult execute(GxUri uri, int sessionId, int start, int count, Date ifModifiedSince)
	{
		String fullUri = uri.toString(sessionId, start, count);
		boolean isCollection = uri.getDataSource().isCollection();
		StructureDefinition dataStructure = uri.getDataSource().getStructure();

		IServiceDataResult serverResult = Services.HttpService.getDataFromProvider(fullUri, ifModifiedSince, isCollection);
		return new DataProviderResult(serverResult, dataStructure);
	}

	private static class DataProviderResult implements IDataSourceResult
	{
		private final IServiceDataResult mResult;
		private final StructureDefinition mDataStructure;
		private List<Entity> mData;

		private DataProviderResult(IServiceDataResult serviceResult, StructureDefinition dataStructure)
		{
			mResult = serviceResult;
			mDataStructure = dataStructure;
		}

		@Override
		public boolean isOk() { return mResult.isOk(); }

		@Override
		public boolean isUpToDate() { return mResult.isUpToDate(); }

		@Override
		public Date getLastModified() { return mResult.getLastModified(); }

		@Override
		public int getErrorType() { return mResult.getErrorType(); }

		@Override
		public String getErrorMessage() { return mResult.getErrorMessage(); }

		@Override
		public List<Entity> getData()
		{
			if (mData == null)
			{
				List<Entity> data = new ArrayList<>();
				for (JSONObject obj : mResult.getDataObjects())
				{
					Entity entity = EntityFactory.newEntity(mDataStructure);
					entity.deserialize(new NodeObject(obj));
					data.add(entity);
				}

				mData = data;
			}

			return mData;
		}
	}
}
