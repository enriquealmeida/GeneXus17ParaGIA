package com.genexus.android.core.providers;

import java.util.Date;

import com.genexus.android.core.base.providers.GxUri;
import com.genexus.android.core.base.utils.Strings;

final class QueryData
{
	private final String mBaseUri;
	private final String mParameters;

	private Date mServerTimestamp;
	private Date mClientTimestamp;
	private boolean mComplete;
	private String mHash;
	private int mRowCount;

	private boolean mNoCachedData;

	public QueryData(GxUri uri)
	{
		// Parse to get components.
		mBaseUri = uri.getName();
		mParameters = uri.getQuery();
	}

	public static QueryData empty(QueryData from)
	{
		QueryData queryData = new QueryData(from.mBaseUri, from.mParameters, null, null, false, 0, Strings.EMPTY);
		queryData.mNoCachedData = true;
		return queryData;
	}

	public QueryData(String baseUri, String parameters, Date serverTimestamp, Date clientTimestamp, boolean complete, int rowCount, String hash)
	{
		mBaseUri = baseUri;
		mParameters = parameters;
		mServerTimestamp = serverTimestamp;
		mClientTimestamp = clientTimestamp;
		mComplete = complete;
		mRowCount = rowCount;
		mHash = hash;
	}

	public String getUri()
	{
		return mBaseUri + Strings.QUESTION + mParameters;
	}

	public String getBaseUri() { return mBaseUri; }
	public String getParameters() { return mParameters; }

	/**
	 * Timestamp returned by the server (the LastUpdate header field) representing the last
	 * known change to the returned data.
	 */
	public Date getServerTimestamp() { return mServerTimestamp; }

	/**
	 * Timestamp that indicates when the query was done by the client.
	 * Used to determine if a server refresh is needed or not, according to data source properties.
	 */
	public Date getClientTimestamp() { return mClientTimestamp; }

	/**
	 * Returns true if the query is "complete", i.e. it's known that there is no more data in the server.
	 */
	public boolean isComplete() { return mComplete; }

	/**
	 * Returns ture if the query has no data, e.g. there is no entry for it in the cache.
	 */
	public boolean hasNoCachedData() { return mNoCachedData; }

	/**
	 * Number of rows read for this query.
	 */
	public int getRowCount() { return mRowCount; }

	/**
	 * Data hash. Changes whenever server data is read (unless it returns "up to date").
	 */
	public String getHash() { return mHash; }

	/**
	 * Sets the client timestamp associated to the query.
	 */
	void setClientInfo(Date timestamp)
	{
		mClientTimestamp = timestamp;
	}

	/**
	 * Sets the server timestamp (and other data) associated to the query.
	 */
	void setServerInfo(boolean isComplete, Date timestamp, int rowCount, String hash)
	{
		mComplete = isComplete;
		mServerTimestamp = timestamp;
		mRowCount = rowCount;
		mHash = hash;
		mNoCachedData = false;
	}
}
