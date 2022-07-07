package com.genexus.android.core.layers;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.genexus.android.core.base.application.MessageLevel;
import com.genexus.android.core.base.controls.MappedValue;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.providers.GxUri;
import com.genexus.android.core.base.providers.IApplicationServer;
import com.genexus.android.core.base.providers.IDataSourceResult;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.DataRequest;
import com.genexus.android.core.utils.Cast;

class CommonUtils
{
	public static Entity getDataSingle(IApplicationServer server, GxUri uri, int sessionId)
	{
		IDataSourceResult data = server.getData(uri, sessionId, 0, 0, null);
		if (data != null && data.getErrorType() == DataRequest.ERROR_NONE && data.getData().size() >= 1)
			return data.getData().get(0);
		else
			return null;
	}

	private static final short MSG_TYPE_WARNING = 0;
	private static final short MSG_TYPE_ERROR = 1;

	public static MessageLevel translateMessageLevel(String itemType)
	{
		Integer intValue = Services.Strings.tryParseInt(itemType);
		if (intValue != null)
			return translateMessageLevel(intValue);
		else
			return MessageLevel.WARNING;
	}

	public static MessageLevel translateMessageLevel(int itemType)
	{
		switch (itemType)
		{
			case MSG_TYPE_WARNING : return MessageLevel.WARNING;
			case MSG_TYPE_ERROR : return MessageLevel.ERROR;
		}

		Services.Log.warning(String.format("Unknown GX message type: %s", itemType));
		return MessageLevel.ERROR;
	}

	public static List<Pair<String, String>> jsonToMap(JSONArray jsonValues)
	{
		List<Pair<String, String>> result = new ArrayList<>();

		if (jsonValues != null)
		{
			int countValues = jsonValues.length();
			for (int i = 0; i < countValues; i++)
			{
				try
				{
					JSONArray item = Cast.as(JSONArray.class, jsonValues.get(i));
					if (item.length() != 0)
					{
						String value = item.getString(0);
						String description = value;
						if (item.length() > 1)
							description = item.getString(1);

						result.add(new Pair<>(value, description));
					}
				}
				catch (JSONException e)
				{
					Services.Log.error("Invalid JSON for map.", e);
				}
			}
		}

		return result;
	}

	public static List<String> jsonToList(JSONArray array)
	{
		ArrayList<String> result = new ArrayList<>();
		if (array != null)
		{
			for (int i = 0; i < array.length(); i++)
			{
				String item;
				try
				{
					item = array.getString(i);
				}
				catch (JSONException e)
				{
					Services.Log.error("Invalid JSON for array.", e);
					item = Strings.EMPTY;
				}

				result.add(item);
			}
		}

		return result;
	}

	public static MappedValue jsonToMappedValue(JSONArray jsonValues)
	{
		try
		{
			if (jsonValues != null)
			{
				if (jsonValues.length() == 1)
				{
					String value = jsonValues.getString(0);
					return new MappedValue(MappedValue.Type.EXACT, value);
				}
				else if (jsonValues.length() == 2)
				{
					String value = jsonValues.getString(0);
					String code = jsonValues.getString(1);
					if (code.equalsIgnoreCase("ambiguousck"))
						return new MappedValue(MappedValue.Type.AMBIGUOUS, value);
					else if (code.equalsIgnoreCase("101"))
						return new MappedValue(MappedValue.Type.NOT_FOUND, value);
				}
			}
			else
				return new MappedValue(MappedValue.Type.NOT_FOUND, "");
		}
		catch (JSONException e)
		{
			Services.Log.error(e);
		}

		Services.Log.warning(String.format("Unexpected format in JSON for mapped value: '%s'.", jsonValues));
		return new MappedValue(MappedValue.Type.NOT_FOUND, "");
	}
}
