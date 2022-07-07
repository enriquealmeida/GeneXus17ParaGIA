package com.genexus.android.core.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Activity used as the destination of a search.
 * Will simply store the search data and finish, so that the caller activity can update itself with the search results.
 */
public class SearchStubActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();

		if (!Intent.ACTION_SEARCH.equals(intent.getAction()))
			throw new IllegalArgumentException(String.format("'%s' should only be called as a result of a search. Action '%s' was not expected.", getClass().getName(), intent.getAction()));

		// Store search data and return.
		SearchHelper.onSearch(intent);
		finish();
	}
}
