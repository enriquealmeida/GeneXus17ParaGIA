package com.genexus.android.core.providers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.DataRequest;

public class ProviderDataResult
{
	private QueryData mQuery;
	private int mSource;
	private List<Entity> mData;
	private boolean mReturnedUpToDate;

	private int mStatusCode;
	private String mStatusMessage;
	private String mVersion;

	public ProviderDataResult(QueryData query, String version, int source, List<Entity> entities, boolean returnedUpToDate)
	{
		mQuery = query;
		mVersion = version;
		mSource = source;
		mData = (entities != null ? entities : new ArrayList<Entity>());
		mReturnedUpToDate = returnedUpToDate;
	}

	static ProviderDataResult upToDate(QueryData query)
	{
		return new ProviderDataResult(query, query.getHash(), DataRequest.RESULT_SOURCE_SERVER, null, true);
	}

	static ProviderDataResult nothing(QueryData query)
	{
		return new ProviderDataResult(query, Strings.EMPTY, DataRequest.RESULT_SOURCE_NONE, null, false);
	}

	static ProviderDataResult error(QueryData query, int errorType, String errorMessage)
	{
		ProviderDataResult result = new ProviderDataResult(query, UUID.randomUUID().toString(), DataRequest.RESULT_SOURCE_SERVER, null, false);
		result.mStatusCode = errorType;
		result.mStatusMessage = errorMessage;

		return result;
	}

	public QueryData getQuery() { return mQuery; }
	public int getSource() { return mSource; }
	public List<Entity> getData() { return mData; }

	public int getStatusCode() { return mStatusCode; }
	public String getStatusMessage() { return mStatusMessage; }
	public boolean isError() { return mStatusCode != 0; }
	public String getVersion() { return mVersion; }

	public boolean isUpToDate() { return mReturnedUpToDate; }
	public boolean isMoreDataAvailable() { return !mQuery.isComplete() && !isError(); }
}
