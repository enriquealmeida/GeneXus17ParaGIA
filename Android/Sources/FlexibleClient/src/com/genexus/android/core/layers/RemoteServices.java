package com.genexus.android.core.layers;

import android.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;

import com.genexus.android.core.base.controls.MappedValue;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

class RemoteServices
{
	public static List<Pair<String, String>> getDynamicComboValues(String service, Map<String, String> inputValues)
	{
		JSONArray jsonValues = getJsonValues(service, inputValues);
		return CommonUtils.jsonToMap(jsonValues);
	}

	public static List<String> getDependentValues(String service, Map<String, String> input)
	{
		JSONArray jsonValues = getJsonValues(service, input);
		return CommonUtils.jsonToList(jsonValues);
	}

	public static List<String> getSuggestions(String serviceName, Map<String, String> input)
	{
		JSONArray jsonValues = getJsonValues(serviceName, input);
		return CommonUtils.jsonToList(jsonValues);
	}

	public static MappedValue getMappedValue(String serviceName, Map<String, String> input)
	{
		JSONArray jsonValues = getJsonValues(serviceName, input);
		return CommonUtils.jsonToMappedValue(jsonValues);
	}

	static class CachedResult
	{
		long timestampMs; // ms
		JSONArray value;
	}

	private static ConcurrentHashMap<String, CachedResult> sCachedResults = new ConcurrentHashMap<>();
	private static final long VALID_DURATION = 5 * 1000; // ms

	private static JSONArray getJsonValues(String service, Map<String, String> parameters)
	{
		if (Strings.hasValue(service))
		{
			String uri = Services.Application.get().UriMaker.getObjectUrl(service, parameters);

			CachedResult cachedResult = sCachedResults.get(uri);
			if (cachedResult != null && cachedResult.timestampMs + VALID_DURATION >= System.currentTimeMillis())
				return cachedResult.value;

			JSONArray json = Services.HttpService.getJSONArrayFromUrl(uri);
			if (json != null)
			{
				cachedResult = new CachedResult();
				cachedResult.value = json;
				cachedResult.timestampMs = System.currentTimeMillis();

				sCachedResults.put(uri, cachedResult);
			}

			return json;
		}
		else
			return new JSONArray();
	}
}
