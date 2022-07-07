package com.genexus.android.core.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.util.Pair;

import com.genexus.android.core.base.metadata.IDataSourceDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controllers.IDataSourceController;

/**
 * Internal class to manage SearchStubActivity and its callers.
 */
public class SearchHelper
{
	private static IDataSourceDefinition sCurrentSearchDefinition;
	private static int sSearchedDataSource;
	private static String sSearchText;

	static void prepare(IDataSourceController dataSource)
	{
		// Store search definition for suggestion provider
		sCurrentSearchDefinition = dataSource.getDefinition();
		sSearchedDataSource = dataSource.getId();
	}

	static void onSearch(Intent intent)
	{
		// Get search string.
		sSearchText = intent.getStringExtra(SearchManager.QUERY);
	}

	public static IDataSourceDefinition getCurrentSearchDefinition()
	{
		return sCurrentSearchDefinition;
	}

	/**
	 * Returns and <b>clears</b> the last performed search.
	 */
	static Pair<IDataSourceController, String> getCurrentSearch(ActivityController activityController)
	{
		Pair<IDataSourceController, String> value = null;
		if (sSearchedDataSource != 0 && Services.Strings.hasValue(sSearchText))
		{
			IDataSourceController dataSource = activityController.getDataSource(sSearchedDataSource);
			if (dataSource != null)
				value = new Pair<>(dataSource, sSearchText);
		}

		sSearchText = null;
		return value;
	}
}
