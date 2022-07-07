package com.genexus.android.core.providers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import androidx.annotation.NonNull;

import com.genexus.android.core.activities.SearchHelper;
import com.genexus.android.core.base.metadata.IDataSourceDefinition;
import com.genexus.android.core.base.metadata.StructureDefinition;
import com.genexus.android.core.base.metadata.enums.Connectivity;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.base.providers.GxUri;
import com.genexus.android.core.base.providers.IApplicationServer;
import com.genexus.android.core.base.providers.IDataSourceResult;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.DataRequest;

public abstract class EntityDataProvider extends ContentProvider
{
	private IDataSourceDefinition mDataSource;
	private IApplicationServer mApplicationServer;
	private StructureDefinition mStructure;
	private int mRowsPerPage = 10;

	private GxUri mDataUri;
	private String mDataUriString;
	private EntityList mEntityList;
	private QueryData mLastNonCachedQuery;
	private Connectivity mCallerConnectivity;

	private static IEntityStorage sStorage;
	private static boolean sDisableStorage;

	public EntityDataProvider() { }

	static void setStorage(IEntityStorage storage)
	{
		if (sStorage != null)
			sStorage.dispose();

		sStorage = storage;
	}

    // MIME types used for searching words or looking up a single definition
    private static final String ALLROWS_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/com.artech.data.entities";
    private static final String ONEROW_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/com.artech.data.entity";

    // UriMatcher stuff
    private static final int ALL_ROWS = 0;
    private static final int ONE_ROW = 1;
    private static final int SEARCH_SUGGEST = 2;

	// Note that the value of these two variables are modified by the main's generated code,
	// thus why they are not final.
	@SuppressWarnings("checkstyle:StaticVariableName") // TODO: Would need to change templates
	public static String AUTHORITY = "com.artech.providers.entityprovider";
	@SuppressWarnings("checkstyle:StaticVariableName") // TODO: Would need to change templates
    protected static UriMatcher URI_MATCHER = buildUriMatcher();

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/entity");

    /**
     * Builds up a UriMatcher for search suggestion and shortcut refresh queries.
     */
    protected static UriMatcher buildUriMatcher()
    {
        UriMatcher matcher =  new UriMatcher(UriMatcher.NO_MATCH);
        // to get definitions...
        matcher.addURI(AUTHORITY, "entity", ALL_ROWS);
        matcher.addURI(AUTHORITY, "entity/#", ONE_ROW);

        // to get suggestions...
        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGEST);

        return matcher;
    }

	@Override
	public String getType(@NonNull Uri uri)
	{
		switch (URI_MATCHER.match(uri))
		{
			case ALL_ROWS:
				return ALLROWS_MIME_TYPE;
			case ONE_ROW:
				return ONEROW_MIME_TYPE;
			case SEARCH_SUGGEST:
				return SearchManager.SUGGEST_MIME_TYPE;
			default:
				throw new IllegalArgumentException("Unknown URL " + uri);
		}
	}

	@Override
	public boolean onCreate()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		String query = null;
		if (uri.getPathSegments().size() > 2)
            query = Strings.toLowerCase(uri.getLastPathSegment());

		switch (URI_MATCHER.match(uri))
		{
			case ONE_ROW:
				return null;

			case ALL_ROWS:
				return getSuggestions(SearchHelper.getCurrentSearchDefinition(), query);

			case SEARCH_SUGGEST:
				return getSuggestions(SearchHelper.getCurrentSearchDefinition(), selectionArgs[0]);
		}

		return null;
	}

	public int getRowsPerPage()
	{
		return mRowsPerPage;
	}

	public void setRowsPerPage(int rows)
	{
		mRowsPerPage = rows;
	}

	/**
     * The columns we'll include in our search suggestions.  There are others that could be used
     * to further customize the suggestions, see the docs in {@link SearchManager} for the details
     * on additional columns that are supported.
     */
	private static final String[] SUGGESTION_COLUMNS =
	{
		BaseColumns._ID, // must include this column
		SearchManager.SUGGEST_COLUMN_TEXT_1,
		SearchManager.SUGGEST_COLUMN_QUERY,
		// SearchManager.SUGGEST_COLUMN_TEXT_2, Only one string per option.
		// SearchManager.SUGGEST_COLUMN_ICON_1, No icon.
		// SearchManager.SUGGEST_COLUMN_INTENT_DATA, Not used.
	};

	private static Cursor getSuggestions(IDataSourceDefinition dataSource, String searchText)
	{
        MatrixCursor cursor = new MatrixCursor(SUGGESTION_COLUMNS);

		if (Services.Strings.hasValue(searchText) && dataSource != null && dataSource.getFilter().getSearch() != null)
		{
			QueryData queryData = new QueryData(new GxUri(dataSource));
			int operator = dataSource.getFilter().getSearch().getOperator();

			// Not all of the search fields may be in the cache table, so take only those.
			List<String> suggestColumns = new ArrayList<>();
			for (String searchColumn : dataSource.getFilter().getSearch().getAttributeNames())
				if (dataSource.getDataItem(searchColumn) != null)
					suggestColumns.add(searchColumn);

			if (suggestColumns.size() != 0)
			{
				Integer index = 0;
				Iterable<String> values = sStorage.getStrings(queryData, suggestColumns, searchText, operator);
				for (String value : values)
				{
					String[] row = new String[] { index.toString(), value, value };
					cursor.addRow(row);
					index++;
				}
			}
		}

		return cursor;
    }

	public ProviderDataResult getData(int sessionId, int requestType, int count)
	{
		QueryData query = getCacheState(mDataUri);

		switch (requestType)
		{
			case DataRequest.REQUEST_CACHED :
				// Read only cached data, if any.
				return getDataCached(query);

			case DataRequest.REQUEST_FIRST :
				// Go to server to know if current data is up-to-date, or to get data if we have nothing.
				return getDataFirstPage(sessionId, query, false, count);

			case DataRequest.REQUEST_REFRESH :
				// Go to server to know if current data is up-to-date, or to get data if we have nothing.
				return getDataFirstPage(sessionId, query, true, count);

			case DataRequest.REQUEST_MORE :
				// Go to server to get next page of data (unless we already have all).
				if (query.isComplete())
					return ProviderDataResult.upToDate(query);
				else
					return getDataNextPage(sessionId, query, count);

			default :
				throw new IllegalArgumentException(String.format("Invalid requestType (%s)", requestType));
		}
	}

	private QueryData getCacheState(GxUri dataUri)
	{
		QueryData base = new QueryData(dataUri);

		if (isCacheEnabled())
			return sStorage.getCacheState(base);
		else
			return (mLastNonCachedQuery != null ? mLastNonCachedQuery : QueryData.empty(base));
	}

	private ProviderDataResult getDataCached(QueryData query)
	{
		if (isCacheEnabled() && !query.hasNoCachedData())
		{
			readFromCache(query);
			return new ProviderDataResult(query, query.getHash(), DataRequest.RESULT_SOURCE_LOCAL, null, false);
		}
		else
			return ProviderDataResult.nothing(query);
	}

	private ProviderDataResult getDataFirstPage(int sessionId, QueryData query, boolean isRefresh, int count)
	{
		// If cache is enabled but we are in the 'don't check yet' period then do nothing.
		if (isCacheEnabled() &&
			!isRefresh &&
			mDataSource != null &&
			mDataSource.getCacheCheckDataLapse() != 0 &&
			query.getClientTimestamp() != null)
		{
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(query.getClientTimestamp());
			calendar.add(Calendar.SECOND, mDataSource.getCacheCheckDataLapse());
			Date checkAfter = calendar.getTime();

			Date now = new Date();
			if (now.before(checkAfter))
			{
				// Return previously cached data, and do nothing else.
				readFromCache(query);
				return ProviderDataResult.upToDate(query);
			}
		}

		ProviderDataResult serverResult = getDataPageFromServer(sessionId, query, 0, count, query.getServerTimestamp());
		if (serverResult.isUpToDate())
		{
			// Return previously cached data (possibly not complete) since it's OK.
			readFromCache(query);

			// Also update query status in cache, because server was queried.
			if (isCacheEnabled())
				sStorage.setCacheState(query);
		}
		else
			afterProviderResult(serverResult, true);

		return serverResult;
	}

	private ProviderDataResult getDataNextPage(int sessionId, QueryData query, int count)
	{
		// Read currently cached data if first time.
		readFromCache(query);

		// Ask for the next page of data and store results in local cache.
		// Start from the last record read (stored in query.rowcount).
		ProviderDataResult result = getDataPageFromServer(sessionId, query, query.getRowCount(), count, null);
		afterProviderResult(result, false);

		return result;
	}

	private ProviderDataResult getDataPageFromServer(int sessionId, QueryData query, int start, int count, Date ifModifiedSince)
	{
		// If caching is disabled, never send the If-Modified-Since header.
		if (!isCacheEnabled())
			ifModifiedSince = null;

		boolean isCollection = (mDataSource != null ? mDataSource.isCollection() : true);
		if (isCollection)
		{
			// Set start & count in URI according to request.
			if (count == DataRequest.COUNT_DEFAULT)
				count = mRowsPerPage;

			if (count == DataRequest.COUNT_ALL)
				count = 0; // "Server" code for ALL.
		}

		IDataSourceResult serverResult = mApplicationServer.getData(mDataUri, sessionId, start, count, ifModifiedSince);

		if (serverResult.isOk())
		{
			// Since the query went to the server, update its client timestamp.
			// Maybe we should do this only when the server actually returned data?
			query.setClientInfo(new Date());

			if (serverResult.isUpToDate())
				return ProviderDataResult.upToDate(query);

			// Else: Data was returned.
			List<Entity> data = new ArrayList<>(serverResult.getData());

			 // Only update "last modified" if reading from first page (otherwise first pages could contain old data).
			Date lastModified = (start == 0 ? serverResult.getLastModified() : query.getServerTimestamp());

			// The query is complete if:
			// * It is not a collection (one or zero records is all), OR
			// * We requested everything (count = 0), OR
			// * We received less than we asked for (source exhausted means there is nothing left), OR
			// * We received MORE than we asked for (a tricky one, but we assume that the server
			//   ignored paging and returned everything). This happens with grids loaded via variables.
			boolean isComplete = (!isCollection || count <= 0 || data.size() != count);

			int rowCount = start + data.size();
			query.setServerInfo(isComplete, lastModified, rowCount, UUID.randomUUID().toString());

			return new ProviderDataResult(query, query.getHash(), DataRequest.RESULT_SOURCE_SERVER, data, false);
		}
		else
			return ProviderDataResult.error(query, serverResult.getErrorType(), serverResult.getErrorMessage());
	}

	private void afterProviderResult(ProviderDataResult result, boolean isFirstPage)
	{
		// By default, don't update cache on error (e.g. keep old data if server is unreachable).
		boolean updateCache = !result.isError();

		// Clear cache if first page (replace old results, if any, with new ones).
		boolean clearCache = isFirstPage;

		// Clear cache on AUTHORIZATION error (we don't have permission to see this data anymore).
		if (result.isError() && result.getStatusCode() == DataRequest.ERROR_SECURITY_AUTHORIZATION)
		{
			updateCache = true;
			clearCache = true;
		}

		if (updateCache)
			storeInCache(result, clearCache);
	}

	private int readFromCache(QueryData query)
	{
		if (mEntityList == null)
		{
			EntityList list = new EntityList();

			// Read from cache only if caching is actually enabled.
			if (isCacheEnabled())
			{
				List<Entity> cachedEntities = sStorage.read(query);
				for (Entity entity : cachedEntities)
					list.addEntity(entity);
			}

			mEntityList = list;
		}

		return mEntityList.size();
	}

	private void storeInCache(ProviderDataResult data, boolean clearPrevious)
	{
		if (clearPrevious)
			mEntityList = new EntityList();

		EntityList list = mEntityList;
		if (list == null)
			return; // EntityDataProvider was reset in preparation for a different query. Discard this one.

		for (Entity entity : data.getData())
			list.addEntity(entity);

		// Write to cache only if caching is actually enabled.
		if (isCacheEnabled())
		{
			try
			{
				if (clearPrevious)
					sStorage.clear(data.getQuery());

				if (data.getData().size() != 0)
					sStorage.insert(data.getQuery(), data.getData());
			}
			catch (EntityStorageException e)
			{
				// Probably disk is full. Disable further use of cache.
				// App behavior will be worse, but at least it shouldn't crash.
				Services.Log.warning("EntityStorage error", e);

				sDisableStorage = true;
				mLastNonCachedQuery = data.getQuery();
			}
		}
		else
		{
			// Save some data that normally goes into cache, for next query.
			mLastNonCachedQuery = data.getQuery();
		}
	}

	private boolean isCacheEnabled()
	{
		if (sDisableStorage)
			return false;

		if (!mApplicationServer.supportsCaching())
			return false;

		return (mDataSource == null || mDataSource.isCacheEnabled());
	}

	public void setDefinition(IDataSourceDefinition dataSource, Connectivity callerConnectivity)
	{
		mDataSource = dataSource;
		mCallerConnectivity = callerConnectivity;
		setDefinition(mDataSource.getStructure());
	}

	private void setDefinition(StructureDefinition definition)
	{
		mStructure = definition;
		mApplicationServer = Services.Application.getApplicationServer(Connectivity.getConnectivitySupport(mCallerConnectivity, mDataSource.getParent().getConnectivitySupport()));
	}

	public StructureDefinition getDefinition()
	{
		return mStructure;
	}

	public void setDataUri(GxUri dataUri)
	{
		// When comparing if URI has changed, don't use "mDataUri.toString()" as old value.
		// Otherwise, if the same GxUri object is modified and re-passed to this method (e.g. after a search),
		// it would never detect a changed URI. Save value in a string and compare with that.
		if (mDataUri == null || !mDataUriString.equalsIgnoreCase(dataUri.toString()))
		{
			mDataUri = dataUri;
			mDataUriString = dataUri.toString();
			mEntityList = null; // Invalidate
			mLastNonCachedQuery = null;
		}
	}

	public GxUri getDataUri() { return mDataUri; }

	public EntityList getEntities()
	{
		return (mEntityList != null ? new EntityList(mEntityList) : new EntityList());
	}

	@Override
	public Uri insert(@NonNull Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(@NonNull Uri uri, ContentValues values, String selection,
					  String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static void clearAllCaches()
	{
		if (sStorage != null)
			sStorage.clear();

		Services.Images.clearCache();
	}
}
