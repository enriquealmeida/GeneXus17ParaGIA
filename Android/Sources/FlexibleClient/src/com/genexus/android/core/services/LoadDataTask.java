package com.genexus.android.core.services;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

import com.genexus.android.core.common.DataRequest;
import com.genexus.android.core.providers.EntityDataProvider;
import com.genexus.android.core.providers.ProviderDataResult;
import com.genexus.android.core.utils.TaskRunner;

class LoadDataTask extends TaskRunner.BaseTask<EntityServiceResponse>
{
	static final Comparator<LoadDataTask> PRIORITY_COMPARATOR = new PriorityComparator();
	private static final AtomicInteger TASK_COUNTER = new AtomicInteger(1);

	private final int mId;
	private final EntityService mService;
	private final int mSessionId;
	private final EntityDataProvider mProvider;
	private final String mIntentFilter;

	private final int mRequestType;
	private final int mRequestCount;

	LoadDataTask(EntityService service, int sessionId, EntityDataProvider provider, String intentFilter, int requestType, int requestCount)
	{
		mId = TASK_COUNTER.getAndIncrement();

		mService = service;
		mSessionId = sessionId;
		mProvider = provider;
		mIntentFilter = intentFilter;

		mRequestType = requestType;
		mRequestCount = requestCount;
	}

	@Override
	public String toString()
	{
		return String.format("[TASK #%s <Session=%s Type=%s URI=%s>]", mId, mSessionId, mRequestType, mProvider.getDataUri().toDebugString());
	}

	public int getSessionId()
	{
		return mSessionId;
	}

	public String getDataUri()
	{
		return mProvider.getDataUri().toString();
	}

	public int getRequestType()
	{
		return mRequestType;
	}

	@Override
	public EntityServiceResponse doInBackground()
	{
		EntityService.debug("Task BACKGROUND_START -- " + toString());
		try
		{
			// Call data provider (local or remote) and publish result.
			ProviderDataResult providerData = mProvider.getData(mSessionId, mRequestType, mRequestCount);

			if (providerData.getSource() != DataRequest.RESULT_SOURCE_NONE)
				return new EntityServiceResponse(providerData.getVersion(), providerData);
			else
				return null;
		}
		finally
		{
			EntityService.debug("Task BACKGROUND_FINISHED -- " + toString());
		}
	}

	@Override
	public void onCancelled()
	{
		EntityService.debug("Task CANCELLED -- " + toString());
		mService.onTaskFinished(this, mIntentFilter, null);
	}

	@Override
	public void onPostExecute(EntityServiceResponse result)
	{
		EntityService.debug("Task COMPLETED -- " + toString());
		mService.onTaskFinished(this, mIntentFilter, result);
	}

	private static class PriorityComparator implements Comparator<LoadDataTask>
	{
		@Override
		public int compare(LoadDataTask lhs, LoadDataTask rhs)
		{
			// CACHED tasks go first.
			int lhsPriority = (lhs.mRequestType == DataRequest.REQUEST_CACHED ? 1 : 2);
			int rhsPriority = (rhs.mRequestType == DataRequest.REQUEST_CACHED ? 1 : 2);

			return lhsPriority < rhsPriority ? -1 : (lhsPriority == rhsPriority ? 0 : 1);
		}
	}
}
