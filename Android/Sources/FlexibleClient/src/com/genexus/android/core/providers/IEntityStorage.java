package com.genexus.android.core.providers;

import java.util.List;

import com.genexus.android.core.base.model.Entity;

interface IEntityStorage
{
	/**
	 * Inserts the entities in the local storage.
	 * Use with data returned from server.
	 * @param query Server query used.
	 * @param entities Entity list.
	 */
	void insert(QueryData query, List<Entity> entities) throws EntityStorageException;

	/**
	 * Returns information in local storage about the query, if available.
	 * @param query Query info.
	 * @return The stored query data, or an instance with empty values. Never null.
	 */
	QueryData getCacheState(QueryData query);

	/**
	 * Updates information in local storage about the query.
	 * @param query Query info.
	 */
	void setCacheState(QueryData query);

	/**
	 * Returns the entities in local storage associated to the specified query, if any.
	 * @param query Query info.
	 * @return Entity list.
	 */
	List<Entity> read(QueryData query);

	/**
	 * Clears all data in the storage.
	 */
	void clear();

	/**
	 * Removes from the storage all data associated to the specified query.
	 * @param query Query
	 */
	void clear(QueryData query);

	// Utility methods.
	Iterable<String> getStrings(QueryData query, List<String> fields, String filter, int operator);

	/**
	 * Signals the storage that it will no longer be used (and can use this opportunity to clean up used resources).
	 */
	void dispose();
}
