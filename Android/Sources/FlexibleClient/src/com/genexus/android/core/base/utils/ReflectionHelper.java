package com.genexus.android.core.base.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.artech.base.services.IEntity;
import com.artech.base.services.IPropertiesObject;
import com.genexus.android.core.base.services.Services;

public class ReflectionHelper
{
	public static <T> Class<? extends T> getClass(Class<T> base, String className)
	{
		try
		{
			Class<?> clazz = null;

			// try to get the class by reflection
			clazz = Class.forName(className);
			return clazz.asSubclass(base);
		}
		catch (ClassNotFoundException e)
		{
			Services.Log.error(String.format("Class '%s' could not be loaded via reflection.", className));
			return null;
		}
	}

	public static <T> T createDefaultInstance(Class<T> clazz, boolean emptyParameters)
	{
		try
		{
			if (clazz == null)
			{
				Services.Log.error("Class not provided to getDefaultInstance().");
				return null;
			}

			Constructor<?> constructor;
			if (emptyParameters)
				constructor = clazz.getConstructor();
			else
				constructor = clazz.getConstructor(int.class);

			if (constructor == null)
			{
				Services.Log.error(String.format("Class '%s' does not have a default constructor.", clazz.getName()));
				return null;
			}

			Object instance;
			if (emptyParameters)
				instance = constructor.newInstance();
			else
				instance = constructor.newInstance(Services.Application.get().getRemoteHandle());

			return clazz.cast(instance);
		}
		catch (InstantiationException | InvocationTargetException | NoSuchMethodException
				| IllegalAccessException ex)
		{
		  	Services.Log.error(String.format("Exception creating instance of class '%s' by reflection.", clazz), ex);
		  	return null;
		}
	}


	/**
	 * Same as java.lang.Class.getConstructor(), but returns null instead of throwing an exception when there is no match.
	 */
	public static Constructor<?> getConstructor(@NonNull Class<?> clazz, @Nullable Class<?>... parameterTypes)
	{
		try
		{
			return clazz.getConstructor(parameterTypes);
		}
		catch (NoSuchMethodException noConstructor)
		{
			return null;
		}
	}

	public static Method getMethod(Class<?> clazz, String name)
	{
		try
		{
			if (clazz == null)
			{
				Services.Log.error("Class not provided to getMethod().");
				return null;
			}

			return clazz.getMethod(name, IPropertiesObject.class);
		}
		catch (NoSuchMethodException ex)
		{
		  	Services.Log.error(String.format("Exception creating instance of method '%s' by reflection.", name), ex);
		  	return null;
		}
	}

	public static Method getMethodEntity(Class<?> clazz, String name)
	{
		try
		{
			if (clazz == null)
			{
				Services.Log.error("Class not provided to getMethod().");
				return null;
			}

			return clazz.getMethod(name, IEntity.class);
		}
		catch (NoSuchMethodException ex)
		{
		  	Services.Log.error(String.format("Exception creating instance of method '%s' by reflection.", name), ex);
		  	return null;
		}
	}

	public static Object getStaticField(String className, String fieldName) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, NoSuchFieldException
	{
		Class<?> clz = Class.forName(className);
		Field field = clz.getField(fieldName);
		return field.get(null);
	}

	public static Object getField(Object object, String fieldName) throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException
	{
		Field field = reflectField(object.getClass(), fieldName);
		return field.get(object);
	}

	public static void setField(Object object, String fieldName, Object value) throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException
	{
		Field field = reflectField(object.getClass(), fieldName);
		field.set(object, value);
	}

	/**
	 * Gets a (non-static, possibly private) field declaration from the object. Never returns null.
	 */
	private static Field reflectField(Class<?> clazz, String fieldName) throws IllegalArgumentException, NoSuchFieldException
	{
		Field field = null;
		while (clazz != null)
		{
			try
			{
			    field = clazz.getDeclaredField(fieldName);
			    if (field != null)
			    	break;
			}
			catch (NoSuchFieldException e)
			{
				// Try with superclass.
				// getDeclaredField() only searches in the exact class, while getField() doesn't return public fields.
				clazz = clazz.getSuperclass();
			}
		}

		if (field != null)
		{
			field.setAccessible(true);
			return field;
		}
		else
			throw new NoSuchFieldException(fieldName);
	}

	public static void callMethod(Object object, String methodName, String[] parameterClassNames, Object[] parameterValues) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException
	{
	    Class<?>[] parameterClasses = new Class<?>[parameterClassNames.length];
	    for (int i = 0; i < parameterClassNames.length; i++)
	        parameterClasses[i] = Class.forName(parameterClassNames[i]);

	    Method method = object.getClass().getDeclaredMethod(methodName, parameterClasses);
	    method.invoke(object, parameterValues);
	}
}
