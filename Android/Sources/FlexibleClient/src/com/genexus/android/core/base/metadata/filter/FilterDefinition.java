package com.genexus.android.core.base.metadata.filter;

import java.util.ArrayList;
import java.util.List;

import com.genexus.android.core.base.metadata.DataSourceMemberDefinition;
import com.genexus.android.core.base.metadata.IDataSourceDefinition;
import com.genexus.android.core.base.serialization.INodeCollection;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;

public class FilterDefinition extends DataSourceMemberDefinition
{
	private static final long serialVersionUID = 1L;

	// Search
	private SearchDefinition mSearch;

	// Advanced filters
	private final List<FilterAttributeDefinition> mFilterAttributes;

	private FilterDefinition(IDataSourceDefinition parent)
	{
		super(parent);
		mFilterAttributes = new ArrayList<>();
	}

	public FilterDefinition(IDataSourceDefinition parent, INodeObject jsonData)
	{
		this(parent);

		INodeObject jsonSearch = jsonData.getNode("search");
		if (jsonSearch != null)
		{
			SearchDefinition search = new SearchDefinition(parent, jsonSearch);
			if (search.getAttributes().size() != 0)
				mSearch = search; // A search with no attributes is not a search.
		}

		INodeObject jsonAdvanced = jsonData.getNode("advanced");
		if (jsonAdvanced != null)
		{
			INodeCollection jsonAttributes = jsonAdvanced.optCollection("filterAttribute");
			for (int i = 0; i < jsonAttributes.length(); i++)
			{
				FilterAttributeDefinition filterAtt = new FilterAttributeDefinition(parent, jsonAttributes.getNode(i));
				mFilterAttributes.add(filterAtt);
			}
		}
	}

	public static FilterDefinition empty(IDataSourceDefinition parent)
	{
		return new FilterDefinition(parent);
	}

	@Override
	public String getName()
	{
		return "Filter";
	}

	public SearchDefinition getSearch() { return mSearch; }

	public List<FilterAttributeDefinition> getAttributes() { return mFilterAttributes; }

	public FilterAttributeDefinition getAttribute(int index)
	{
		return mFilterAttributes.get(index);
	}

	public FilterAttributeDefinition getAttribute(String name)
	{
		if (!Services.Strings.hasValue(name))
			return null;

		for (FilterAttributeDefinition filterAtt : mFilterAttributes)
			if (filterAtt.getName().equalsIgnoreCase(name))
				return filterAtt;

		return null;
	}

	public boolean hasAdvancedFilter()
	{
		return (mFilterAttributes.size() != 0);
	}
}
