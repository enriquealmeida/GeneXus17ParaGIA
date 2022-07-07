package com.genexus.android.core.controllers;

import com.genexus.android.core.base.metadata.IDataSourceDefinition;
import com.genexus.android.core.base.providers.GxUri;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.providers.EntityDataProvider;

public class DataSourceModel
{
	private final DataViewModel mParent;
	private final IDataSourceDefinition mDefinition;
	private final EntityDataProvider mProvider;
	private GxUri mUri;
	private String mFilterExtraInfo = Strings.EMPTY;

	public DataSourceModel(DataViewModel parent, IDataSourceDefinition dataSource, GxUri uri)
	{
		mParent = parent;
		mDefinition = dataSource;
		mUri = uri;
		mProvider = Services.Application.getEntityProvider().getProvider();
		if (mProvider != null) {
			mProvider.setDefinition(dataSource, parent.getConnectivity());
			mProvider.setDataUri(uri);
		}
	}

	@Override
	public String toString()
	{
		return getName();
	}

	public String getName() { return mDefinition.getName(); }
	public DataViewModel getParent() { return mParent; }
	public IDataSourceDefinition getDefinition() { return mDefinition; }
	public EntityDataProvider getProvider() { return mProvider; }

	public GxUri getUri()
	{
		return mUri;
	}

	public void setUri(GxUri uri)
	{
		if (mDefinition != uri.getDataSource())
			throw new IllegalArgumentException(String.format("Uri data source does not match model data source (%s =/= %s).", uri.getDataSource(), mDefinition));

		mUri = uri;
		mProvider.setDataUri(uri);
	}

	void setRowsPerPage(int rows)
	{
		mProvider.setRowsPerPage(rows);
	}

	public String getFilterExtraInfo() { return mFilterExtraInfo; }
	public void setFilterExtraInfo(String info) { mFilterExtraInfo = info; }
}
