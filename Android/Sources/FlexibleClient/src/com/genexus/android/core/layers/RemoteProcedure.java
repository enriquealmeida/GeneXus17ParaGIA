package com.genexus.android.core.layers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.genexus.android.json.NodeCollection;
import com.genexus.android.json.NodeObject;
import com.genexus.android.core.base.application.IProcedure;
import com.genexus.android.core.base.application.OutputResult;
import com.genexus.android.core.base.metadata.ObjectParameterDefinition;
import com.genexus.android.core.base.metadata.ProcedureDefinition;
import com.genexus.android.core.base.model.BaseCollection;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.PropertiesObject;
import com.genexus.android.core.base.serialization.INodeCollection;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.ServiceResponse;
import com.genexus.android.core.base.services.Services;

class RemoteProcedure implements IProcedure
{
	private final String mName;
	private final ProcedureDefinition mDefinition;

	public RemoteProcedure(String name, ProcedureDefinition definition)
	{
		mName = name;
		mDefinition = definition;
	}

	@Override
	public OutputResult execute(PropertiesObject parameters)
	{
		if (mDefinition == null)
			return RemoteUtils.outputNoDefinition(mName);

		String url = Services.Application.get().UriMaker.getAllBCUrl(mDefinition.getName());
		try
		{
			// Prepare input (encode into JSON content).
			JSONObject jsonParameters = prepareProcedureInput(parameters);

			// Execute
			ServiceResponse response = Services.HttpService.postJson(url, jsonParameters);

			// Read output parameters from server response.
			readProcedureOutput(response, parameters);

			// Return errors and/or messages, if any.
			return RemoteUtils.translateOutput(response);
		}
		catch (IOException e)
		{
			return OutputResult.error(Services.HttpService.getNetworkErrorMessage(e));
		}
	}

	@Override
	public OutputResult executeMultiple(List<PropertiesObject> parameters)
	{
		if (mDefinition == null)
			return RemoteUtils.outputNoDefinition(mName);

		// The URL is of the form <server>/gxmulticall?<procName>
		String url = Services.Application.get().UriMaker.getMultiCallUrl(mDefinition.getName());

		try
		{
			// Prepare input (encode into JSON content).
			JSONArray jsonArray = prepareMultipleCallInput(parameters);

			// Perform the multicall.
			ServiceResponse response = Services.HttpService.postJson(url, jsonArray);

			// Server call does not support output parameters; just read response for any errors.
			return RemoteUtils.translateOutput(response);
		}
		catch (IOException e)
		{
			return OutputResult.error(Services.HttpService.getNetworkErrorMessage(e));
		}
	}

	private JSONObject prepareProcedureInput(PropertiesObject parameters)
	{
		JSONObject jsonParameters = new JSONObject();
		for (Map.Entry<String, Object> parameter : parameters.getInternalProperties().entrySet())
		{
			try
			{
				//Services.Log.debug("ProcCall", "Input " + parameter.getKey() + " " + toJsonValue(parameter.getValue()) );
				jsonParameters.put(parameter.getKey(), toJsonValue(parameter.getValue()));
			}
			catch (JSONException e)
			{
				Services.Log.error("putParameter", "Exception in JSONObject.put()", e);
			}
		}

		return jsonParameters;
	}

	private JSONArray prepareMultipleCallInput(List<PropertiesObject> parameters)
	{
		// Post body is values, a JSON array comprising one array for each item, e.g.
		// [["Item1Parm1", "Item1Parm2", "Item1Parm3"], ["Item2Parm1", "Item2Parm2", "Item2Parm3"]].
		JSONArray array = new JSONArray();
		for (PropertiesObject itemValues : parameters)
		{
			JSONArray item = new JSONArray();

			// Order them the same as in the procedure parameters.
			for (int i = 0; i < mDefinition.getParameters().size(); i++)
			{
				ObjectParameterDefinition procParameter = mDefinition.getParameter(i);
				if (procParameter.isInput())
				{
					Object itemValue = itemValues.getProperty(procParameter.getName());
					item.put(itemValue);
				}
			}

			array.put(item);
		}

		return array;
	}

	private static Object toJsonValue(Object value)
	{
		if (value == null)
			return JSONObject.NULL;

		// Convert to "real" JSON if it's a structure.
		if (value instanceof Entity)
		{
			INodeObject json = ((Entity)value).serialize(Entity.JSONFORMAT_INTERNAL);
			return ((NodeObject)json).getInner();
		}

		// Convert to "real" JSON if it's a structured collection.
		if (value instanceof BaseCollection<?>)
		{
			INodeCollection json = ((BaseCollection<?>)value).serialize(Entity.JSONFORMAT_INTERNAL);
			return ((NodeCollection)json).getInner();
		}

		// Atomic or unknown type.
		return value;
	}

	private void readProcedureOutput(ServiceResponse response, PropertiesObject parameters)
	{
		// Read output parameters from JSON response and assign them to parameters collection.
		if (response.Data != null)
		{
			for (int i = 0; i < mDefinition.getParameters().size(); i++)
			{
				ObjectParameterDefinition procParameter = mDefinition.getParameter(i);
				if (procParameter.isOutput())
				{
					// Read result parameter from procedure.
					String name = procParameter.getName();
					Object value = response.has(name) ?
						// get the value as returned from the server, as String, it will be deserialized later
						response.get(name) :
						// if output is not in the response, use empty value since that is the convention
						// the type of value is not String in most cases, i.e EntityList for collections of SDT
						// but it doesn't need to be a String, deserialization is done only when needed
						procParameter.getEmptyValue();
					parameters.setProperty(name, value);
				}
			}
		}
	}

	@Override
	public OutputResult executeReplicator(PropertiesObject parameters)
	{
		if (mDefinition == null)
			return RemoteUtils.outputNoDefinition(mName);

		String url = Services.Application.get().UriMaker.getAllBCUrl(mDefinition.getName());
		try
		{
			// Prepare input (encode into JSON content).
			JSONObject jsonParameters = prepareProcedureInput(parameters);

			// Execute
			ServiceResponse response = Services.HttpService.postJsonSyncReplicator(url, jsonParameters);

			// Read output parameters from server response.
			readProcedureOutput(response, parameters);

			// Return errors and/or messages, if any.
			return RemoteUtils.translateOutput(response);
		}
		catch (IOException e)
		{
			return OutputResult.error(Services.HttpService.getNetworkErrorMessage(e));
		}
	}
}
