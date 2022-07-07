package com.genexus.android.core.controllers;

import com.genexus.android.core.base.metadata.IDataSourceDefinition;

public interface IDataSourceController
{
	int getId();
	String getName();
	IDataViewController getParent();
	IDataSourceDefinition getDefinition();
	DataSourceModel getModel();

	void onRequestMoreData();
}
