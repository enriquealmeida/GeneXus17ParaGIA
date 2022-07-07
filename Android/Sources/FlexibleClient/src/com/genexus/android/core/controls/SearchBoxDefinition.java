package com.genexus.android.core.controls;

import java.io.Serializable;

import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.loader.MetadataLoader;

public class SearchBoxDefinition implements Serializable
{


	// Search Box definition
	private boolean mSearchDynamic = false;
	private boolean mSearchAutoComplete = false;
	private String mSearchAutoCompleteProvider = "";
	private String mSearchResultPanel = "";
	//private boolean mSearchInResultPanel = false;
	private String mSearchResultPanelParameters = "";

	private String mSearchCurrentValue = "";
	private String mThemeClassName = "";


	public SearchBoxDefinition(LayoutItemDefinition definition, String currentValue, String currentClassName)
	{
		// copy definition to fields
		//read definition values
		mSearchDynamic = definition.getControlInfo().optStringProperty("@SearchBoxSearchType").equalsIgnoreCase("Dynamic");
		mSearchAutoComplete = definition.getControlInfo().getBooleanProperty("@SearchBoxSearchAutocomplete", false);
		mSearchResultPanel = MetadataLoader.getAttributeName(definition.getControlInfo().optStringProperty("@SearchBoxResultsPanel"));

		mThemeClassName = currentClassName;
		mSearchCurrentValue = currentValue;
	}

	public boolean isSearchBoxDynamic()
	{
		return mSearchDynamic;
	}

	public String getSearchBoxCurrentValue()
	{
		return mSearchCurrentValue;
	}

	public String getThemeClassName()
	{
		return mThemeClassName;
	}

	public String getSearchResultPanel()
	{
		return mSearchResultPanel;
	}

	/*
	public boolean isSearchBoxInResultPanel()
	{
		return true;
	}
	*/



}
