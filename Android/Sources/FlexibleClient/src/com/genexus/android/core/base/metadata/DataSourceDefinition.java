package com.genexus.android.core.base.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.genexus.android.core.base.metadata.filter.FilterDefinition;
import com.genexus.android.core.base.metadata.loader.MetadataLoader;
import com.genexus.android.core.base.metadata.loader.WorkWithMetadataLoader;
import com.genexus.android.core.base.serialization.INodeCollection;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;

/**
 * Information about a particular Data Provider.
 * Panels may have more than one data source.
 */
public class DataSourceDefinition implements IDataSourceDefinition, Serializable
{
	private static final long serialVersionUID = 1L;

	private IDataViewDefinition mComponent;
	private boolean mHasDataProvider;
	private int mVersion;

	private final List<DataItem> mData;
	private final List<String> mIgnoreOutput;
	private StructureDefinition mStructure;
	private boolean mIsCollection;
	private List<ObjectParameterDefinition> mParameters;

	private boolean mCacheEnabled;
	private int mAutoRefreshTime;
	private int mCacheCheckDataLapse;

	private FilterDefinition mFilter;
	private final OrdersAndBreakDefinition mOrders;

	private String mAssociatedBCName;
	@SuppressWarnings("unused")
	private String mAssociatedBCLevelName;

	public DataSourceDefinition(IDataViewDefinition component, INodeObject jsonData)
	{
		mComponent = component;
		mHasDataProvider = true;
		mData = new ArrayList<>();
		mIgnoreOutput = new ArrayList<>();
		mOrders = new OrdersAndBreakDefinition(this);

		deserialize(jsonData);
	}

	@Override
	public WorkWithDefinition getPattern() { return mComponent.getPattern(); }

	@Override
	public IDataViewDefinition getParent() { return mComponent; }

	@Override
	public void setParent(IDataViewDefinition parent)
	{
		mComponent = parent;
	}

	@Override
	public String getName() { return mStructure.getName(); }

	@Override
	public boolean hasDataProvider() { return mHasDataProvider; }

	@Override
	public int getVersion() { return mVersion; }

	@Override
	public List<DataItem> getDataItems() { return mData; }

	@Override
	public StructureDefinition getStructure() { return mStructure; }

	@Override
	public boolean isCollection() { return mIsCollection; }

	@Override
	public boolean isCacheEnabled() { return mCacheEnabled; }

	@Override
	public int getAutoRefreshTime() { return mAutoRefreshTime; }

	@Override
	public int getCacheCheckDataLapse() { return mCacheCheckDataLapse; }

	@Override
	public FilterDefinition getFilter() { return mFilter; }

	@Override
	public OrdersAndBreakDefinition getOrders() { return mOrders; }

	@Override
	public List<ObjectParameterDefinition> getParameters()
	{
		if (mParameters != null)
			return mParameters;
		else
			return mComponent.getParameters();
	}

	@Override
	public String getAssociatedBCName() { return mAssociatedBCName; }

	@Override
	public DataItem getDataItem(String name)
	{
		if (!Services.Strings.hasValue(name))
			return null;

		// TODO: Remove this, variables should have the '&' at the start.
		name = DataItemHelper.getNormalizedName(name);

		return mStructure.getAttribute(name);
	}

	@Override
	public boolean ignoreOutput(String name) {
		return mIgnoreOutput.contains(name);
	}

	/* ----- Deserialization */

	private void deserialize(INodeObject jsonData)
	{
		deserializeDataStructure(jsonData);
		deserializeFilter(jsonData);
		deserializeOrdersAndBreak(jsonData);
	}

	private void deserializeDataStructure(INodeObject jsonData)
	{
		String dpName = jsonData.optString("@DataProvider");
		mIsCollection = jsonData.optBoolean("@isCollection");
		mVersion = jsonData.optInt("@hash");

		if (jsonData.optBoolean("@onlyDefinition"))
			mHasDataProvider = false;

		mCacheEnabled = jsonData.optBoolean("@CacheEnabled", true);
		mCacheCheckDataLapse = jsonData.optInt("@CacheCheckForNewDataLapse") * 60; // minutes to seconds
		mAutoRefreshTime = jsonData.optInt("@autoRefreshTime");

		StructureDefinition structure = new StructureDefinition(dpName);
		structure.Root.setName(dpName);

		// Read associated BC and Level.
		mAssociatedBCName = jsonData.optString("@bc");
		mAssociatedBCLevelName = jsonData.optString("@level");

		// Read parameters (optional, may be different from DataView's).
		INodeObject jsonParameters = jsonData.getNode("parameters");
		if (jsonParameters != null)
		{
			mParameters = new ArrayList<>();
			WorkWithMetadataLoader.readObjectParameterList(mParameters, jsonParameters);
		}

		// Read Attributes
		for (INodeObject jsonAttribute : jsonData.optCollection("attribute"))
		{
			DataItem dataItem = deserializeDataAttribute(jsonAttribute);
			if (dataItem != null)
				mData.add(dataItem);
		}

		// Read Variables
		for (INodeObject jsonVariable : jsonData.optCollection("variable"))
		{
			DataItem dataItem = deserializeDataVariable(jsonVariable);
			if (dataItem != null)
				mData.add(dataItem);
		}

		// Read Ignore Output
		for (String ignoreOutput : jsonData.optStringArray("IgnoreOutput"))
		{
			mIgnoreOutput.add(ignoreOutput);
		}

		// Look for the Base BusinessComponent for this Data Source and merge properties.
		structure.Root.Items.addAll(mData);
		if (Services.Strings.hasValue(mAssociatedBCName))
		{
			// TODO: Should use mAssociatedBCLevelName too!
			StructureDefinition bc = Services.Application.getDefinition().getBusinessComponent(mAssociatedBCName);
			if (bc != null)
				structure.merge(bc);
			else
				Services.Log.error("readMetadataError", "BC not found: " + mAssociatedBCName);
		}

		mStructure = structure;

		deserializeChildren(jsonData, structure.Root); // after assigning mStructure
	}

	private static DataItem deserializeDataAttribute(INodeObject attributeJson)
	{
		return new DataSourceAttributeDefinition(attributeJson);
	}

	private static DataSourceVariableDefinition deserializeDataVariable(INodeObject varNode)
	{
		return new DataSourceVariableDefinition(varNode);
	}

	private void deserializeFilter(INodeObject jsonData)
	{
		FilterDefinition filter = FilterDefinition.empty(this);
		INodeObject filterJson = jsonData.getNode("filter");
		if (filterJson != null)
			filter = new FilterDefinition(this, filterJson);

		mFilter = filter;
	}

	private void deserializeOrdersAndBreak(INodeObject jsonData)
	{
		INodeObject jsonOrders = jsonData.getNode("orders");
		if (jsonOrders != null)
		{
			for (INodeObject jsonOrder : jsonOrders.optCollection("order"))
			{
				OrderDefinition order = new OrderDefinition(this, jsonOrder);
				mOrders.add(order);
			}
		}

		INodeObject jsonBreakBy = jsonData.getNode("breakBy");
		if (jsonBreakBy != null)
		{
			for (INodeObject jsonBreakByAtt : jsonBreakBy.optCollection("attribute"))
			{
				String attributeName = MetadataLoader.getAttributeName(jsonBreakByAtt.optString("@attribute"));
				DataItem dataItem = getDataItem(attributeName);
				if (dataItem != null)
					mOrders.addBreakBy(dataItem);
			}

			mOrders.setBreakByDescriptionAttribute(MetadataLoader.getAttributeName(jsonBreakBy.optString("@descriptionAttribute")));
		}
	}

	private void deserializeChildren(INodeObject jsonData, LevelDefinition level) {
		INodeCollection jsonDatas = jsonData.optCollection("data");
		for (int i = 0; i < jsonDatas.length(); i++) {
			INodeObject jsonChildData = jsonDatas.getNode(i);
			String name = jsonChildData.optString("@level");
			if (!Services.Strings.hasValue(name))
				continue;

			LevelDefinition childLevel = new LevelDefinition(null);
			childLevel.setName(name);
			childLevel.setIsCollection(true);
			childLevel.setNoParentLevel(true);
			childLevel.setStructure(mStructure);

			// Read Attributes
			for (INodeObject jsonAttribute : jsonChildData.optCollection("attribute"))
			{
				DataItem dataItem = deserializeDataAttribute(jsonAttribute);
				if (dataItem != null)
					childLevel.Items.add(dataItem);
			}

			// Read Variables
			for (INodeObject jsonVariable : jsonChildData.optCollection("variable"))
			{
				DataItem dataItem = deserializeDataVariable(jsonVariable);
				if (dataItem != null)
					childLevel.Items.add(dataItem);
			}

			deserializeChildren(jsonChildData, childLevel);
			level.Levels.add(childLevel);
		}
	}
}
