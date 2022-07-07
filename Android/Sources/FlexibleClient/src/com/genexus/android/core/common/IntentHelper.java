package com.genexus.android.core.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import androidx.collection.LruCache;

import com.genexus.android.serialization.DataSourceDefinitionSerializer;
import com.genexus.android.serialization.DataViewDefinitionSerializer;
import com.genexus.android.serialization.GxUriSerializer;
import com.genexus.android.serialization.ISerializer;
import com.genexus.android.core.base.metadata.IDataSourceDefinition;
import com.genexus.android.core.base.metadata.IDataViewDefinition;
import com.genexus.android.core.base.providers.GxUri;
import com.genexus.android.core.base.services.Services;

public final class IntentHelper
{
	private static final HashMap<Class<?>, ISerializer<?>> SERIALIZERS;
	private static final LruCache<String, Object> HASH = new LruCache<>(20);

	static
	{
		SERIALIZERS = new HashMap<>();

		// Initialize known ISerializers. More can be added later.
		registerSerializer(GxUri.class, new GxUriSerializer());
		// registerSerializer(Entity.class, new EntitySerializer());
		registerSerializer(IDataViewDefinition.class, new DataViewDefinitionSerializer());
		registerSerializer(IDataSourceDefinition.class, new DataSourceDefinitionSerializer());
	}

	/**
	 * Puts the object in the intent.
	 * - If the object has an ISerializer registered, then it's converted to JSON and put as string.
	 * - Otherwise, an UUID is generated to identify it, and it's put into the Intent. This doesn't work for intents that may survive the application.
	 * The object can later be retrieved with a call to getObject().
	 * @param <T> Object type.
	 * @param intent Intent to use.
	 * @param name Intent extra key.
	 * @param obj Object to pass.
	 */

	public static <T> void putObject(Intent intent, String name, Class<T> clazz, T obj)
	{
		if (obj == null)
			return;

		ISerializer<T> serializer = tryGetSerializer(clazz);
		if (serializer != null)
		{
			// ISerializer available, put content.
			try
			{
				JSONObject json = serializer.serialize(obj);
				intent.putExtra(name, json.toString());
			}
			catch (JSONException ex)
			{
				Services.Log.error(String.format("IntentHelper: error serializing object of type '%s'.", clazz.getName()), ex);
			}
		}
		else
		{
			// No ISerializer, put reference.
			String hashKey = UUID.randomUUID().toString();
			HASH.put(hashKey, obj);

			intent.putExtra(name, hashKey);
		}
	}

	/**
	 * Gets the object previously put in the Intent using putObject().
	 * - If the object has an ISerializer registered, then it's deserialized from JSON and the new instance is returned.
	 * - Otherwise, the global reference will be returned (and "forgotten", so that subsequent calls to getObject() will fail).
	 * @param <T> Object type.
	 * @param intent Intent with the data.
	 * @param name Intent extra key.
	 * @return The object associated by a previous call to putObject().
	 */
	public static <T> T getObject(Intent intent, String name, Class<T> clazz)
	{
		// About weird syntax, see https://bugs.eclipse.org/bugs/show_bug.cgi?id=98379
		// and http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6302954
		return IntentHelper.getObject(intent, name, clazz, false);
	}

	/**
	 * Gets the object previously put in the Intent using putObject().
	 * - If the object has an ISerializer registered, then it's deserialized from JSON and the new instance is returned.
	 * - Otherwise, the global reference will be returned.
	 * @param <T> Object type.
	 * @param intent Intent with the data.
	 * @param name Intent extra key.
	 * @param keepAvailable Don't remove the object (if it's a global reference), so that it can be obtained again via this method.
	 * @return The object associated by a previous call to putObject().
	 */
	@SuppressWarnings("unchecked")
	private static <T> T getObject(Intent intent, String name, Class<T> clazz, boolean keepAvailable)
	{
		if (intent == null)
			return null;

		ISerializer<T> serializer = tryGetSerializer(clazz);
		if (serializer != null)
		{
			// ISerializer available, get content and recreate instance.
			String jsonString = intent.getStringExtra(name);
			if (!Services.Strings.hasValue(jsonString))
				return null;

			try
			{
				JSONObject json = new JSONObject(jsonString);
				return serializer.deserialize(json);
			}
			catch (JSONException ex)
			{
				Services.Log.error(String.format("IntentHelper: error deserializing object of type '%s'.", clazz.getName()), ex);
				return null;
			}
		}
		else
		{
			// No ISerializer, get reference.
			String hashKey = intent.getStringExtra(name);

			if (hashKey != null)
			{
				T obj = (T) HASH.get(hashKey);

				if (obj != null && !keepAvailable)
					HASH.remove(hashKey);

				return obj;
			}
			else
				return null;
		}
	}

	public static <T> void registerSerializer(Class<T> clazz, ISerializer<T> serializer)
	{
		SERIALIZERS.put(clazz, serializer);
	}

	@SuppressWarnings("unchecked")
	private static <T> ISerializer<T> tryGetSerializer(Class<T> clazz)
	{
		return (ISerializer<T>) SERIALIZERS.get(clazz);
	}

	public static <T> void putList(Intent intent, String name, List<T> value)
	{
		// Important: getExtras() returns a COPY of extras; so don't call putList(intent.getExtras(), name, value).
		Bundle bundle = new Bundle();
		putList(bundle, name, value);
		intent.putExtras(bundle);
	}

	public static <T> void putList(Bundle bundle, String name, List<T> value)
	{
		if (value != null)
		{
			// Skip wrapper creation for most common List classes.
			if (value instanceof ArrayList<?>)
				bundle.putSerializable(name, (ArrayList<?>)value);
			else if (value instanceof Vector<?>)
				bundle.putSerializable(name, (Vector<?>)value);
			else
				bundle.putSerializable(name, new ArrayList<>(value));
		}
	}

	public static <T> List<T> getList(Intent intent, String name)
	{
		if (intent == null)
			return null;

		return getList(intent.getExtras(), name);
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> getList(Bundle bundle, String name)
	{
		if (bundle == null)
			return null;

		try
		{
			// Because of type erasure, this cast isn't 100% safe (we could have a different kind of map inside).
			return (List<T>)bundle.getSerializable(name);
		}
		catch (ClassCastException ex)
		{
			Services.Log.error(ex.getMessage(), ex);
			return null;
		}
	}

	public static <K, V> void putMap(Intent intent, String name, Map<K, V> value)
	{
		if (value != null)
		{
			// Skip wrapper creation for most common Map classes.
			if (value instanceof HashMap<?,?>)
				intent.putExtra(name, (HashMap<?,?>)value);
			else if (value instanceof TreeMap<?, ?>)
				intent.putExtra(name, (TreeMap<?,?>)value);
			else if (value instanceof Hashtable<?, ?>)
				intent.putExtra(name, (Hashtable<?,?>)value);
			else
				intent.putExtra(name, new HashMap<>(value));
		}
	}

	public static <K, V> Map<K, V> getMap(Intent intent, String name)
	{
		if (intent == null)
			return null;

		return getMap(intent.getExtras(), name);
	}
	
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> getMap(Bundle bundle, String name)
	{
		try
		{
			// Because of type erasure, this cast isn't 100% safe (we could have a different kind of map inside).
			return (Map<K, V>)bundle.getSerializable(name);
		}
		catch (ClassCastException ex)
		{
			Services.Log.error(ex.getMessage(), ex);
			return null;
		}
	}
}
