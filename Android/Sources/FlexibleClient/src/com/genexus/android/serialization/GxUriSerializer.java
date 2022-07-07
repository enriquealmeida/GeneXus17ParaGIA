package com.genexus.android.serialization;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.genexus.android.core.base.metadata.IDataSourceDefinition;
import com.genexus.android.core.base.metadata.filter.FilterAttributeDefinition;
import com.genexus.android.core.base.providers.GxUri;
import com.genexus.android.core.base.services.Services;

public class GxUriSerializer implements ISerializer<GxUri>
{
	private static final String DATASOURCE = "DataSource";
	
	private static final String PARAMETERS = "Parameters";
	private static final String PARAMETER_NAME = "Name";
	private static final String PARAMETER_VALUE = "Value";
	
	private static final String ORDER = "OrderBy";
	
	private static final String FILTERS = "Filters";
	private static final String FILTER_NAME = "Name";
	private static final String FILTER_VALUES = "Values";
	
	private static final String SEARCH = "Search";
	private static final String SEARCH_TEXT = "Text";
	private static final String SEARCH_FIELD = "Field";
	
	@Override
	public JSONObject serialize(GxUri data) throws JSONException
	{
		JSONObject json = new JSONObject();
		json.put(DATASOURCE, data.getDataSource().getName());
		
		// Parameters.
		if (data.getParameters().size() != 0)
		{
			JSONArray jsonParameters = new JSONArray();
			for (String paramName : data.getParameters().keySet())
			{
				JSONObject jsonParameter = new JSONObject()
					.put(PARAMETER_NAME, paramName)
					.put(PARAMETER_VALUE, data.getParameters().get(paramName));
	
				jsonParameters.put(jsonParameter);
			}
			
			json.put(PARAMETERS, jsonParameters);
		}
		
		// Order
		if (data.getOrder() != null)
			json.put(ORDER, data.getOrder().getId());

		// Filters
		JSONArray jsonFilters = new JSONArray();
		for (FilterAttributeDefinition filterName : data.getDataSource().getFilter().getAttributes())
		{
			Object[] values = data.getFilter(filterName);
			if (values != null && values.length != 0)
			{
				JSONObject jsonFilter = new JSONObject()
					.put(FILTER_NAME, filterName.getName());
				
				// Filter values
				JSONArray jsonFilterValues = new JSONArray();
				for (Object value : values)
					jsonFilterValues.put(value);
				
				jsonFilter.put(FILTER_VALUES, jsonFilterValues);
				jsonFilters.put(jsonFilter);
			}
		}
		
		if (jsonFilters.length() != 0)
			json.put(FILTERS, jsonFilters);
		
		// Search
		if (Services.Strings.hasValue(data.getSearchText()))
		{
			JSONObject jsonSearch = new JSONObject();
			jsonSearch.put(SEARCH_TEXT, data.getSearchText());
			jsonSearch.put(SEARCH_FIELD, data.getSearchField());
			
			json.put(SEARCH, jsonSearch);
		}
		
		return json;
	}

	@Override
	public GxUri deserialize(JSONObject json) throws JSONException
	{
		String dsName = json.optString(DATASOURCE);
		IDataSourceDefinition dataSource = Services.Application.getDefinition().getDataSource(dsName);
		if (dataSource == null)
			return null;
		
		GxUri uri = new GxUri(dataSource);

		// Parameters
		JSONArray jsonParameters = json.optJSONArray(PARAMETERS);
		if (jsonParameters != null)
		{
			for (int i = 0; i < jsonParameters.length(); i++)
			{
				JSONObject jsonParameter = jsonParameters.getJSONObject(i);
				uri.setParameter(
					jsonParameter.getString(PARAMETER_NAME), 
					jsonParameter.get(PARAMETER_VALUE)); 
			}
		}
		
		// Order
		uri.setOrder(json.optInt(ORDER));

		// Filters
		JSONArray jsonFilters = json.optJSONArray(FILTERS);
		if (jsonFilters != null)
		{
			for (int i = 0; i < jsonFilters.length(); i++)
			{
				JSONObject jsonFilter = jsonFilters.getJSONObject(i);
				String filterName = jsonFilter.getString(FILTER_NAME);
				
				JSONArray jsonFilterValues = jsonFilter.getJSONArray(FILTER_VALUES);
				ArrayList<Object> filterValues = new ArrayList<>();
				for (int j = 0; j < jsonFilterValues.length(); j++)
					filterValues.add(jsonFilterValues.getString(j));
				
				uri.setFilter(filterName, filterValues.toArray());
			}
		}

		// Search
		JSONObject jsonSearch = json.optJSONObject(SEARCH);
		if (jsonSearch != null)
		{
			uri.setSearch(
				jsonSearch.optString(SEARCH_TEXT),
				jsonSearch.optInt(SEARCH_FIELD));
		}

		return uri;
	}
}
