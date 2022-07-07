package com.genexus.android.core.controllers;

/**
 * Extends the public interface IDataSourceController with methods that
 * should only be called from DataViewController, not from final client classes.
 */
public interface IDataSourceControllerInternal extends IDataSourceController
{
	void onResume();
	void onRefresh(RefreshParameters params);
	void onPause();

	void attach(IDataSourceBoundView view);
	void detach();
	IDataSourceBoundView getBoundView();
}
