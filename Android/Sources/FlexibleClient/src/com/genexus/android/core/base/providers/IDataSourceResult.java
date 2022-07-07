package com.genexus.android.core.base.providers;

import java.util.Date;
import java.util.List;

import com.genexus.android.core.base.model.Entity;

public interface IDataSourceResult
{
	boolean isOk();
	boolean isUpToDate();

	Date getLastModified();
	int getErrorType();
	String getErrorMessage();

	List<Entity> getData();
}
