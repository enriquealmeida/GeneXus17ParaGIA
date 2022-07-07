package com.genexus.android.core.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.genexus.android.core.activities.IntentParameters;
import com.genexus.android.core.base.metadata.DynamicCallDefinition;
import com.genexus.android.core.base.metadata.IDataSourceDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.common.DataRequest;
import com.genexus.android.core.common.IntentHelper;
import com.genexus.android.core.providers.EntityDataProvider;
import com.genexus.android.core.services.EntityService;
import com.genexus.android.core.services.EntityServiceResponse;

public class DataSourceController implements IDataSourceControllerInternal
{
	private final DataViewController mParent;
	private final DataSourceModel mModel;
	private final int mId;

	private IDataSourceBoundView mView;
	private EntityReceiver mReceiver;

	// Status
	private ViewData mLastResponse;
	private ViewData mDelayedCacheResponse;
	private ArrayList<HashMap<String, String>> mServerValues;
	private boolean mRequestPending;
	private boolean mJustAttached;
	private static int sNextDataSourceId = 1;

	public DataSourceController(DataViewController parent, DataSourceModel model)
	{
		mParent = parent;
		mModel = model;
		mId = createDataSourceId();
		mServerValues = new ArrayList<>();
	}
	static synchronized int createDataSourceId()
	{
		return sNextDataSourceId++;
	}

	@Override
	public IDataSourceDefinition getDefinition()
	{
		return mModel.getDefinition();
	}

	@Override
	public int getId()
	{
		return mId;
	}

	@Override
	public String getName()
	{
		return mModel.getDefinition().getName();
	}

	@Override
	public IDataViewController getParent()
	{
		return mParent;
	}

	@Override
	public DataSourceModel getModel()
	{
		return mModel;
	}

	@Override
	public void attach(IDataSourceBoundView view)
	{
		mView = view;
		mJustAttached = true;
	}

	@Override
	public void detach()
	{
		mView = null;
	}

	// -----------------------------------------------------
	// Fragment data management methods.

	@Override
	public void onResume()
	{
		if (mReceiver == null)
		{
			// Prepare receiver for data communication.
			mReceiver = new EntityReceiver();
			IntentFilter filter = new IntentFilter(mReceiver.Id);
			LocalBroadcastManager.getInstance(Services.Application.getAppContext()).registerReceiver(mReceiver, filter);

			if (mView != null && mView.needsMoreData())
			{
				// If this controller is being "resurrected", then the old data may suffice.
				// Otherwise start a new task to load the data.
				if (mJustAttached && mLastResponse != null)
				{
					postResponse(mLastResponse);
					followUpAfterResponse(mLastResponse);
				}
				else
					startLoading(DataRequest.REQUEST_FIRST, DataRequest.COUNT_DEFAULT);
			}
		}

		mJustAttached = false;
	}

	/**
	 * Calls (via Intent) the EntityService to get data.
	 * @param requestType Used to indicate if the request comes at activity start, when scrolling, or via refresh.
	 */
	private synchronized void startLoading(int requestType, int requestCount)
	{
		// Start service to load data in background.
		// Copy receive to local variable because we could be interrupted by another thread.
		EntityReceiver receiver = mReceiver;
		if (receiver != null)
		{
			Context context = Services.Application.getAppContext();

			Intent msgService = new Intent(EntityService.ACTION_LOAD_DATA);
			msgService.setComponent(new ComponentName(context, Services.Application.getEntityProvider().getEntityServiceClass()));

			msgService.putExtra(IntentParameters.Service.DATA_VIEW_SESSION, mParent.getSessionId());
			IntentHelper.putObject(msgService, IntentParameters.Service.DATA_PROVIDER, EntityDataProvider.class, mModel.getProvider());
			msgService.putExtra(IntentParameters.Service.INTENT_FILTER, receiver.Id);
			msgService.putExtra(IntentParameters.Service.REQUEST_TYPE, requestType);
			msgService.putExtra(IntentParameters.Service.REQUEST_COUNT, requestCount);

			mRequestPending = true;
			context.startService(msgService);
		}
	}

	@Override
	public void onRequestMoreData()
	{
		// Controls may send more requests than necessary (for example, scrolling seems to be fired
		// when loading data). Ignore these so that the UC doesn't have so much work to do.
		if (mRequestPending)
			return;

		if (mView != null && mView.needsMoreData())
			startLoading(DataRequest.REQUEST_MORE, DataRequest.COUNT_DEFAULT);
	}

	@Override
	public void onRefresh(RefreshParameters params)
	{
		mParent.updateDataSourceParameters(this);
		
		int count = DataRequest.COUNT_DEFAULT;
		if (params.keepPosition && mLastResponse != null)
		{
			if (mLastResponse.isMoreAvailable())
			{
				// If we had more than one page of data, ask for as many pages in this request.
				// That way the list won't "scroll upwards".
				if (mLastResponse.getCount() > mModel.getProvider().getRowsPerPage())
					count = mLastResponse.getCount();
			}
			else
				count = DataRequest.COUNT_ALL;
		}

		if (mView != null)
			mView.onBeforeRefresh(params);

		startLoading(DataRequest.REQUEST_REFRESH, count);
	}

	// Inner Class For Receiving Intents
	@SuppressWarnings("checkstyle:MemberName")
	public class EntityReceiver extends BroadcastReceiver
	{
		public final String Id = UUID.randomUUID().toString();

		@Override
		public void onReceive(Context context, Intent intent)
		{
			final EntityServiceResponse response = EntityServiceResponse.get(intent);
			if (response == null)
				return;

			// Server response means request is completely done (could be an error or "up to date", doesn't matter).
			if (response.getSource() == DataRequest.RESULT_SOURCE_SERVER)
				mRequestPending = false;

			ViewData viewData = null;

			if (response.getSource() == DataRequest.RESULT_SOURCE_SERVER && mDelayedCacheResponse != null)
			{
				// We delayed the cache response to wait for the server (probably because
				// there was a cached dynamic call) so use it if the server didn't respond.
				if (response.getStatusCode() == DataRequest.ERROR_NETWORK)
					viewData = mDelayedCacheResponse;

				mDelayedCacheResponse = null;
			}

			// If this response is the same as the one last passed to the UI, don't update the view.
			if (mLastResponse == null || !mLastResponse.getResponseId().equals(response.getResponseId()))
			{
				if (viewData == null)
				{
					viewData = new ViewData(
						mModel.getUri(),
						response.getResponseId(),
						response.getSource(),
						mModel.getProvider().getEntities(), // copy to new collection (TODO: in background thread).
						response.hasMoreData(),
						response.getStatusCode(),
						response.getStatusMessage(),
						false);
				}

				// Don't post a response with dynamic calls from cache, wait for the real response from server.
				if (response.getSource() == DataRequest.RESULT_SOURCE_LOCAL &&
					viewData.getSingleEntity() != null &&
					DynamicCallDefinition.from(viewData.getSingleEntity()).size() != 0)
				{
					mDelayedCacheResponse = viewData;
					return;
				}

				postResponse(viewData);
			}
			else
			{
				// raise "on refresh not change" notification
				if (viewData == null)
				{
					viewData = new ViewData(
							mModel.getUri(),
							response.getResponseId(),
							response.getSource(),
							null, // data not needed.
							response.hasMoreData(),
							response.getStatusCode(),
							response.getStatusMessage(),
							true);
				}

				postResponse(viewData);
			}

			// In case the view *still* needs more data after updating...
			followUpAfterResponse(mLastResponse);
		}
	}

	private void postResponse(ViewData viewData)
	{
		if (!viewData.getDataUnchanged() &&
			viewData != mLastResponse) // if it is mLastResponse then the initialization from onReceive is already done, skip it else mServerValues gets the wrong values
		{
			// Notify the parent controller before updating UI.
			mParent.onReceive(this, mLastResponse, viewData, mServerValues);
			mLastResponse = viewData;
		}

		Services.Log.debug(String.format("Updating UI: %s (%s).", getName(), EntityServiceResponse.getSourceName(viewData.getResponseSource())));

		// Update the corresponding UI control.
		if (mView != null)
			mView.update(viewData);

		// not invalidate option menu on refresh, seems not necessary and breaks search view at list.
		//SherlockHelper.invalidateOptionsMenu(mParent.getParent().getActivity());
	}

	private void followUpAfterResponse(final ViewData response)
	{
		if (mView != null &&
			mView.needsMoreData() &&
			response.isMoreAvailable() &&
			response.getResponseSource() == DataRequest.RESULT_SOURCE_SERVER &&
			response.getStatusCode() == DataRequest.ERROR_NONE)
		{
			// ... launch a follow-up request.
			startLoading(DataRequest.REQUEST_MORE, DataRequest.COUNT_DEFAULT);
		}
	}

	@Override
	public void onPause()
	{
		if (mReceiver != null)
		{
			LocalBroadcastManager.getInstance(Services.Application.getAppContext()).unregisterReceiver(mReceiver);
			mReceiver = null;
		}
	}

	@Override
	public IDataSourceBoundView getBoundView()
	{
		return mView;
	}
}
