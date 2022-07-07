package com.genexus.android.core.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.genexus.android.core.app.ComponentParameters;
import com.genexus.android.core.base.metadata.DashboardMetadata;
import com.genexus.android.core.base.metadata.DataItem;
import com.genexus.android.core.base.metadata.IDataSourceDefinition;
import com.genexus.android.core.base.metadata.IDataViewDefinition;
import com.genexus.android.core.base.metadata.VariableDefinition;
import com.genexus.android.core.base.metadata.enums.Connectivity;
import com.genexus.android.core.base.providers.GxUri;
import com.genexus.android.core.utils.Cast;

public class DataViewModel
{
	private final ComponentParameters mParams;
	private final HashMap<IDataSourceDefinition, DataSourceModel> mDataSources;
	private final Connectivity mConnectivity;

	public DataViewModel(ComponentParameters params, Connectivity connectivity)
	{
		mParams = params;
		mDataSources = new HashMap<>();
		mConnectivity = connectivity;

		if (params.Object instanceof IDataViewDefinition)
		{
			// Fill with default URIs.
			for (IDataSourceDefinition dataSource : ((IDataViewDefinition)params.Object).getDataSources())
			{
				GxUri uri = new GxUri(dataSource).setParameters(mParams.Parameters);
				setUri(dataSource, uri);
			}
		}
	}
	
	public ComponentParameters getParams()
	{
		return mParams;
	}
	
	public IDataViewDefinition getDefinition()
	{
		//mParams.Object can be instance of DashboardMetadata instead of IDataViewDefinition
		if (mParams.Object instanceof IDataViewDefinition)
			return (IDataViewDefinition) mParams.Object;

		return null;
	}

	public DashboardMetadata getDashboardDefinition() {
		if (mParams.Object instanceof DashboardMetadata)
			return (DashboardMetadata) mParams.Object;

		return null;
	}

	Connectivity getConnectivity()
	{
		return mConnectivity;
	}

	Iterable<DataSourceModel> getDataSources()
	{
		return mDataSources.values();
	}

	DataSourceModel getDataSource(IDataSourceDefinition dataSource)
	{
		return mDataSources.get(dataSource);
	}

	public List<VariableDefinition> getVariables() {
		ArrayList<VariableDefinition> variables = new ArrayList<>(getDefinition().getVariables());
		for (DataSourceModel model : getDataSources()) { // add all variables in dataproviders
			for (DataItem dataItem : model.getDefinition().getDataItems()) {
				VariableDefinition variableDefinition = Cast.as(VariableDefinition.class, dataItem);
				if (variableDefinition != null) {
					variables.add(variableDefinition);
				}
			}
		}
		return variables;
	}

	private void setUri(IDataSourceDefinition dataSource, GxUri uri)
	{
		if (dataSource.getParent() != mParams.Object)
			throw new IllegalArgumentException(String.format("Data source '%s' does not belong to this data view (%s)", dataSource.getName(), mParams.Object.getName()));

		if (dataSource != uri.getDataSource())
			throw new IllegalArgumentException(String.format("Uri data source does not match model data source (%s =/= %s).", uri.getDataSource(), dataSource));

		DataSourceModel model = getDataSource(dataSource);
		if (model == null)
		{
			// Initialize new model.
			model = new DataSourceModel(this, dataSource, uri);
			mDataSources.put(dataSource, model);
		}
		else
			model.setUri(uri);
	}
}
