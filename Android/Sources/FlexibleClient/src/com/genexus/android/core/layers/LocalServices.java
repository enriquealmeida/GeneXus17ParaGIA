package com.genexus.android.core.layers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Pair;

import com.genexus.android.core.base.controls.MappedValue;
import com.genexus.android.core.base.model.PropertiesObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.ReflectionHelper;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.GXProcedure;

class LocalServices
{
	public static List<Pair<String, String>> getDynamicComboValues(String serviceName, Map<String, String> input)
	{
		JSONArray jsonResult = getServiceJson(serviceName, input, "dyn_entity_");
		return CommonUtils.jsonToMap(jsonResult);
	}

	public static List<String> getDependentValues(String serviceName, Map<String, String> input)
	{
		JSONArray jsonResult = getServiceJson(serviceName, input, "entity_");
		return CommonUtils.jsonToList(jsonResult);
	}

	public static List<String> getSuggestions(String serviceName, Map<String, String> input)
	{
		JSONArray jsonResult = getServiceJson(serviceName, input, "dyn_entity_");
		return CommonUtils.jsonToList(jsonResult);
	}

	public static MappedValue getMappedValue(String serviceName, Map<String, String> input)
	{
		JSONArray jsonValues = getServiceJson(serviceName, input, "dyn_entity_");
		return CommonUtils.jsonToMappedValue(jsonValues);
	}

	private static JSONArray getServiceJson(String serviceName, Map<String, String> input, String methodPrefix)
	{
		Pair<String, String> serviceClassAndEntryPoint = parseServiceName(serviceName);
		String serviceClass = serviceClassAndEntryPoint.first;
		String serviceEntryPoint = serviceClassAndEntryPoint.second;

		if (Strings.hasValue(serviceClass) && Strings.hasValue(serviceEntryPoint))
		{
			// Call the service via reflection.
			return getServiceJson(serviceClass, serviceEntryPoint, input, methodPrefix);
		}
		else
		{
			Services.Log.warning(String.format("Unable to parse service name '%s'.", serviceName));
			return new JSONArray();
		}
	}

	private static Pair<String, String> parseServiceName(String serviceName)
	{
		Pair<String, String> result = new Pair<>(Strings.EMPTY, Strings.EMPTY);

		if (Strings.hasValue(serviceName))
		{
			String[] parts = serviceName.split("/", -1);
			if (parts.length > 1)
			{
				int count = parts.length;
				String className = parts[0];
				for (int i = 2; i<count; i++)
				{
					className += "." + parts[i-1];
				}
				result = new Pair<>(className, parts[count-1]);
			}
		}

		return result;
	}

	private static JSONArray getServiceJson(String serviceClass, String serviceEntryPoint, Map<String, String> input, String methodPrefix)
	{
		GXProcedure implementation = GxObjectFactory.getComboValuesClass(Strings.toLowerCase(serviceClass));

		if (implementation != null)
		{
			PropertiesObject propObj = new PropertiesObject();
			for (Map.Entry<String, String> inputItem : input.entrySet())
				propObj.setProperty(inputItem.getKey().replaceFirst("&", Strings.EMPTY), inputItem.getValue());

			Method method = ReflectionHelper.getMethod(implementation.getClass(), methodPrefix + Strings.toLowerCase(serviceEntryPoint));
			if (method != null)
			{
				LocalUtils.beginTransaction();
				try
				{
					String result = (String)method.invoke(implementation, propObj);
					return new JSONArray(result);
				} catch (IllegalAccessException | JSONException | InvocationTargetException e) {
				} finally
				{
					LocalUtils.endTransaction();
				}
			}
		}

		return new JSONArray();
	}
}
