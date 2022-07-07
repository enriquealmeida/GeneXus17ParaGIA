package com.genexus.android.serialization;

import org.json.JSONException;
import org.json.JSONObject;

import com.genexus.android.core.base.metadata.IDataSourceDefinition;
import com.genexus.android.core.base.services.Services;

public class DataSourceDefinitionSerializer implements ISerializer<IDataSourceDefinition>
{
	private static final String NAME = "DataSource";
	
	@Override
	public JSONObject serialize(IDataSourceDefinition data) throws JSONException
	{
		JSONObject json = new JSONObject();
		json.put(NAME, data.getName());
		return json;
	}

	@Override
	public IDataSourceDefinition deserialize(JSONObject json) throws JSONException
	{
		String name = json.getString(NAME);
		return Services.Application.getDefinition().getDataSource(name);
	}
}
