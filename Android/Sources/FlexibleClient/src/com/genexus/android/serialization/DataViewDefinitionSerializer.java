package com.genexus.android.serialization;

import org.json.JSONException;
import org.json.JSONObject;

import com.genexus.android.core.base.metadata.IDataViewDefinition;
import com.genexus.android.core.base.services.Services;

public class DataViewDefinitionSerializer implements ISerializer<IDataViewDefinition>
{
	private static final String NAME = "DataView";
	
	@Override
	public JSONObject serialize(IDataViewDefinition data) throws JSONException
	{
		JSONObject json = new JSONObject();
		json.put(NAME, data.getName());
		return json;
	}

	@Override
	public IDataViewDefinition deserialize(JSONObject json)	throws JSONException
	{
		String name = json.getString(NAME);
		return Services.Application.getDefinition().getDataView(name);
	}
}
