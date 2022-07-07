package com.genexus.android.core.base.metadata;

import java.util.List;

import com.genexus.android.core.base.metadata.filter.FilterDefinition;

/**
 * Interface for GeneXus Data Sources (collection or single).
 * Defines attributes, search/advanced search and orders.
 */
public interface IDataSourceDefinition
{
	WorkWithDefinition getPattern();
	IDataViewDefinition getParent();
	// only use for live editing purpose
	void setParent(IDataViewDefinition parent);
	String getName();
	boolean hasDataProvider();
	int getVersion();

	// Data
	List<DataItem> getDataItems();
	DataItem getDataItem(String name);
	boolean ignoreOutput(String name);
	StructureDefinition getStructure();
	boolean isCollection();

	// Caching / data access information
	boolean isCacheEnabled();
	int getCacheCheckDataLapse();
	int getAutoRefreshTime();

	// TODO Remove this, or at least change it to return a structure.
	String getAssociatedBCName();

	// Query details
	List<ObjectParameterDefinition> getParameters();
	FilterDefinition getFilter();
	OrdersAndBreakDefinition getOrders();
}
