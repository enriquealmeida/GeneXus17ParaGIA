package com.genexus.android.core.controllers;

import java.util.Map;

import com.genexus.android.core.base.metadata.IDataSourceDefinition;
import com.genexus.android.core.base.metadata.StructureDefinition;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.common.DataRequest;

/**
 * Controller that is not bound to an actual data source (either because
 * the view doesn't have one, or because it is marked as "only definition").
 */
class DataSourceControllerStub implements IDataSourceControllerInternal
{
	private final DataViewController mParent;
	private final DataSourceModel mModel;
	private final String mDataSourceName;
	private final int mId;
	private final ViewData mResponse;

	private IDataSourceBoundView mView;

	/**
	 * Constructor for Data Sources that exist, but are not actually called (i.e. have the "OnlyDefinition" tag).
	 */
	public DataSourceControllerStub(DataViewController parent, DataSourceModel model)
	{
		this(parent, model.getDefinition().getName(), model, model.getDefinition().getStructure());
	}

	/**
	 * Constructor for "bound views" that are not actually bound to a data source.
	 */
	public DataSourceControllerStub(DataViewController parent, IDataSourceBoundView boundView)
	{
		this(parent, boundView.getDataSourceId(), null, StructureDefinition.EMPTY);
		attach(boundView);
	}

	private DataSourceControllerStub(DataViewController parent, String dataSourceName, DataSourceModel model, StructureDefinition dataSourceStructure)
	{
		mId = DataSourceController.createDataSourceId();
		mDataSourceName = dataSourceName;
		mParent = parent;
		mModel = model;

		Entity data = EntityFactory.newEntity(dataSourceStructure);
		data.initialize();

		// Initialize fields that are "received" in parameters.
		if (model != null)
		{
			for (Map.Entry<String, Object> parameter : model.getUri().getParameters().entrySet())
				data.setProperty(parameter.getKey(), parameter.getValue());
		}

		mResponse = ViewData.customData(data, DataRequest.RESULT_SOURCE_SERVER);
	}

	@Override
	public int getId() { return mId; }

	@Override
	public DataSourceModel getModel() { return mModel; }

	@Override
	public String getName() { return (mModel != null ? mModel.getName() : mDataSourceName); }

	@Override
	public IDataSourceDefinition getDefinition() { return (mModel != null ? mModel.getDefinition() : null); }

	@Override
	public IDataViewController getParent() { return mParent; }

	@Override
	public void attach(IDataSourceBoundView view)
	{
		mView = view;
	}

	@Override
	public void detach()
	{
		mView = null;
	}

	@Override
	public void onResume()
	{
		if (mView != null && mView.needsMoreData())
		{
			// Since there is no actual data to load, respond immediately.
			mParent.onReceive(this, null, mResponse, null);
			mView.update(mResponse);
		}
	}

	@Override
	public void onRequestMoreData() { }

	@Override
	public void onRefresh(RefreshParameters params) { }

	@Override
	public void onPause() { }

	@Override
	public IDataSourceBoundView getBoundView()
	{
		return mView;
	}
}
