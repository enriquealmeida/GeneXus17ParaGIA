package com.genexus.android.api;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.services.Services;
import com.genexus.xml.GXXMLSerializable;

/**
 * This class allow access to device information.
 */
public class GlobalEvents
{
	//execute an event with paramters from offline code.
	public static void fireEvent(String externalObjectFullName, String eventName, Object[] parameters)
	{
		// Convert Object[] to List<Object>
		List<Value> myParameters = new ArrayList<Value>();
		for (Object obj : parameters)
		{
			// in/out parameters are array.
			if (obj.getClass().isArray())
			{
				Object objInner = Array.get(obj, 0);
				putObject(myParameters, objInner);
			}
			else
			{
				putObject(myParameters, obj);
			}
		}

		EventDispatcher.fireEvent(Services.Application.getAppContext(), externalObjectFullName, eventName, myParameters);
		return;
	}

	private static void putObject(List<Value> myParameters, Object objInner)
	{
		if (objInner instanceof GXXMLSerializable)
		{
			GXXMLSerializable serializable = (GXXMLSerializable) objInner;
			myParameters.add(Value.newString(serializable.toJSonString()));
		}
		else
			myParameters.add(Value.newValue(objInner));
	}

}
