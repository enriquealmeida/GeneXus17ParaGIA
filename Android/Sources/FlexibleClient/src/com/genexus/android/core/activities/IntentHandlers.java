package com.genexus.android.core.activities;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import android.content.Intent;

import com.genexus.android.core.actions.UIContext;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.services.Services;

public class IntentHandlers
{
	private static final ArrayList<IIntentHandler> HANDLERS = new ArrayList<>();

    public static void addHandler(IIntentHandler handler)
    {
    	HANDLERS.add(handler);
    }

	public static void addHandler(String className)
	{
		try
		{
			// Create an instance via Reflection, if possible.
			Class<?> clazz = Class.forName(className);
			Constructor<?> constructor = clazz.getConstructor();
			HANDLERS.add((IIntentHandler)constructor.newInstance());
		}
		catch (InstantiationException | InvocationTargetException | NoSuchMethodException
				| IllegalAccessException | ClassNotFoundException e)
		{
          	Services.Log.warning(String.format("Intent Handler with class name '%s' was not found.", className), e);
		}
	}

	public static boolean tryHandleIntent(UIContext context, Intent intent, Entity data)
	{
		for (IIntentHandler intentHandler : HANDLERS)
		{
			if (intentHandler.tryHandleIntent(context, intent, data))
				return true;
		}

		return false;
	}
}
