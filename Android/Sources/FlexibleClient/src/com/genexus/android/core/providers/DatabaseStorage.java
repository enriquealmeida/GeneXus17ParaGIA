package com.genexus.android.core.providers;

import java.util.List;

import android.content.Context;

import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.services.Services;

public class DatabaseStorage implements IEntityStorage
{
	private EntityDatabase mDatabase;

	/**
	 * Must be called in startup of application to enable cache (and supply DB definition).
	 * @param definition Database definition.
	 */
	public static void initialize(Context context, DatabaseDefinition definition)
	{
		DatabaseStorage dbStorage = new DatabaseStorage();
		dbStorage.mDatabase = new EntityDatabase(context, definition);
		EntityDataProvider.setStorage(dbStorage);
	}

	@Override
	public synchronized void insert(QueryData query, List<Entity> entities) throws EntityStorageException
	{
		mDatabase.insert(tableOf(query), query, entities);
	}

	@Override
	public synchronized QueryData getCacheState(QueryData query)
	{
		return mDatabase.readQueryData(query);
	}

	@Override
	public synchronized void setCacheState(QueryData query)
	{
		mDatabase.writeQueryData(query);
	}

	@Override
	public synchronized List<Entity> read(QueryData query)
	{
		return mDatabase.selectAll(tableOf(query), query);
	}

	@Override
	public synchronized void clear()
	{
		Services.Log.debug("Clearing cache database...");
		mDatabase.deleteAll();
	}

	@Override
	public synchronized void clear(QueryData query)
	{
		mDatabase.deleteAll(tableOf(query), query);
	}

	@Override
	public synchronized Iterable<String> getStrings(QueryData query, List<String> fields, String filter, int operator)
	{
		return mDatabase.getFieldValues(tableOf(query), fields, filter, operator);
	}

	@Override
	public synchronized void dispose()
	{
		if (mDatabase != null)
			mDatabase.close();
	}

	private static String tableOf(QueryData query)
	{
		return query.getBaseUri();
	}
}
