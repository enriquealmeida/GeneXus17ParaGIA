package com.genexus.android.core.base.metadata;

import java.util.List;

import com.genexus.android.core.base.metadata.layout.LayoutDefinition;
import com.genexus.android.core.base.metadata.rules.RulesDefinition;

/**
 * Common interface for Detail, List, and Section
 * (elements that have data, layouts, etc).
 */
public interface IDataViewDefinition extends IViewDefinition
{
	WorkWithDefinition getPattern();

	/**
	 * All data sources used in this data view.
	 */
	DataSourceDefinitionList getDataSources();

	/**
	 * Data source associated to the first level of the view (e.g. to individual attributes but not to grids).
	 */
	IDataSourceDefinition getMainDataSource();

	// UI
	List<LayoutDefinition> getLayouts();
	LayoutDefinition getLayout(String type);
	LayoutDefinition getLayoutForMode(short mode);

	RulesDefinition getRules();

	List<ActionDefinition> getActions();

	boolean getShowAds();
	String getAdsPosition();


}
