package com.genexus.android.core.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteFullException;
import android.util.Pair;

import com.genexus.android.BuildConfig;
import com.genexus.android.core.base.metadata.StructureDefinition;
import com.genexus.android.core.base.metadata.filter.SearchDefinition;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

class EntityDatabase
{
	private SQLiteDatabase mDatabase;
	private final DatabaseDefinition mDefinition;

	private static final long INSERT_ERROR = -1L;
	private static final long NOT_FOUND = -1L;

	// Schema version. Change whenever the schema tables definition changes, so that the DB is recreated.
	public static final int SCHEMA_VERSION = 8;

	static class QUERY
	{
		static final String TABLE = "_Queries";

		static final String COL_ID = "ROWID";
		static final String COL_BASE_URI = "QueryBaseUri";
		static final String COL_PARAMETERS = "QueryParameters";
		static final String COL_SERVER_TIMESTAMP = "QueryServerTimestamp";
		static final String COL_CLIENT_TIMESTAMP = "QueryClientTimestamp";
		static final String COL_IS_COMPLETE = "QueryIsComplete";
		static final String COL_ROW_COUNT = "QueryRowCount";
		static final String COL_HASH = "QueryHash";
	}

	static class QueryEntity
	{
		static final String TABLE = "_QueryEntities";

		static final String COL_QUERY_ID = "QueryId"; // FK to ROWID of Queries table.
		static final String COL_ENTITY_ID = "EntityId"; // FK to ROWID of corresponding Entity table.
		static final String COL_ORDER = "ROWID"; // ROWID of QueryEntity is insertion order.

		static final String ENTITIES_OF_QUERY =
			String.format("SELECT %s FROM %s WHERE %s = ?", COL_ENTITY_ID, TABLE, COL_QUERY_ID);
	}

	static class ENTITY
	{
		static final String COL_ENTITY_ID = "ROWID";
		static final String COL_EXTRA_KEY_HASH = "__ExtraKeyHash";
		static final String COL_DUMMY = "__Dummy";
		// static final String COL_ENTITY_KEY = "_EntityKey";
	}

	static class OPERATION
	{
		static final String TABLE = "_Operations";

		static final String COL_ID = "ROWID";

		static final String COL_URI = "OperationUri";
		static final String COL_TYPE = "OperationType";
		static final String COL_DATA = "OperationData";
		static final String COL_DATAKEY = "OperationDataKey";
		static final String COL_STATUS = "OperationStatus";

		static final String COL_OPERATION_TIMESTAMP = "OperationStartTimestamp";
		static final String COL_STATUS_TIMESTAMP = "OperationStatusTimestamp";
	}

	public EntityDatabase(Context context, DatabaseDefinition definition)
	{
		mDefinition = definition;
		open(context);
	}

	private void open(Context context)
	{
		EntityDatabaseHelper helper = new EntityDatabaseHelper(context, mDefinition);
		mDatabase = helper.getWritableDatabase();
	}

	public void close()
	{
		mDatabase.close();
	}

	public List<Entity> selectAll(String tableName, QueryData query)
	{
		List<Entity> result = new ArrayList<>();

		TableDefinition table = mDefinition.getTable(tableName);
		if (table != null)
			selectAll(table, query, result);

		return result;
	}

	private void selectAll(TableDefinition table, QueryData query, List<Entity> result)
	{
		long queryId = readQueryId(query);
		if (queryId == NOT_FOUND)
			return;

		String join = String.format("%1$s LEFT JOIN %2$s ON (%1$s.%3$s = %2$s.%4$s)",
				table.getSqlName(), QueryEntity.TABLE, ENTITY.COL_ENTITY_ID, QueryEntity.COL_ENTITY_ID);

		String[] columns = EntityDatabaseHelper.columnNames(table.getSqlName(), table.getColumns());
		String selection = String.format("%s.%s = ?", QueryEntity.TABLE, QueryEntity.COL_QUERY_ID);
		String[] selectionArgs = new String[] { Long.toString(queryId) };
		String orderBy = String.format("%s.%s", QueryEntity.TABLE, QueryEntity.COL_ORDER);

		Cursor cursor = mDatabase.query(join, columns, selection, selectionArgs, null, null, orderBy);
		try
		{
			while (cursor.moveToNext())
			{
				Entity entity = readEntity(table, cursor);
				result.add(entity);
			}
		}
		finally
		{
			cursor.close();
		}
	}

	private static Entity readEntity(TableDefinition table, Cursor cursor)
	{
		StructureDefinition entityStructure = table.getStructure();
		if (entityStructure == null)
			entityStructure = StructureDefinition.EMPTY;

		Entity entity = EntityFactory.newEntity(entityStructure);

		for (ColumnDefinition column : table.getColumns())
			EntityDatabaseHelper.readValue(cursor, column, entity);

		return entity;
	}

	public void insert(String tableName, QueryData query, Collection<Entity> entities) throws EntityStorageException
	{
		try
		{
			TableDefinition table = mDefinition.getTable(tableName);
			if (table != null)
				insert(table, query, entities);
		}
		catch (SQLiteFullException e)
		{
			throw new EntityStorageException("Error inserting data for " + tableName, e);
		}
	}

	private void insert(TableDefinition table, QueryData query, Collection<Entity> entities)
	{
		mDatabase.beginTransaction();
		try
		{
			// Insert in Query table, or get current query id.
			long queryId = writeQueryId(query);

			// Insert records in Entities table and get their ids.
			Collection<Long> entityIds = new ArrayList<>();
			for (Entity entity : entities)
			{
				// Hack: insert even empty values.
				EntityDatabaseHelper.ensureHasKey(entity, table);

				long entityId = insertOrUpdateEntity(table, entity);
				entityIds.add(entityId);
			}

			// Insert references in Query-Entities table.
			ContentValues qeValues = new ContentValues();
			qeValues.put(QueryEntity.COL_QUERY_ID, queryId);
			for (Long entityId : entityIds)
			{
				qeValues.put(QueryEntity.COL_ENTITY_ID, entityId);

				try
				{
					mDatabase.insertOrThrow(QueryEntity.TABLE, null, qeValues);
				}
				catch (SQLiteConstraintException ex)
				{
					// An error due to PK violation is not even logged;
					// it is expected if data changes in the server while the client is paging.
				}
				catch (SQLException ex)
				{
					Services.Log.error("Error inserting " + qeValues, ex);
				}
			}

			mDatabase.setTransactionSuccessful();
		}
		finally
		{
			mDatabase.endTransaction();
		}
	}

	private long insertOrUpdateEntity(TableDefinition table, Entity entity)
	{
		// insertWithOnConflict() is API level 8! So we have to manually select and then insert or update :(
		long entityId = readEntityId(table, entity);

		if (entityId == NOT_FOUND)
		{
			// Insert new entity and return id.
			ContentValues entityValues = EntityDatabaseHelper.toValues(entity, table);
			// entityValues.put(ENTITY.COL_ENTITY_KEY, entity.getKeyString());

			// Send nullColumnHack="__Dummy" for tables with no attributes (see SQLiteDatabase.insert()).
			entityId = mDatabase.insert(table.getSqlName(), ENTITY.COL_DUMMY, entityValues);

			if (BuildConfig.DEBUG && entityId == INSERT_ERROR)
				throw new IllegalStateException("INSERT_ERROR was returned by insert(ENTITY).");
		}
		else
		{
			// Table has a primary key. Otherwise readEntityId() would not have succeeded.
			if (table.getSecondaryColumns().size() != 0)
			{
				// Update entity with possible new data.
				Pair<String, String[]> whereKey = EntityDatabaseHelper.keyToWhereCondition(entity, table);
				ContentValues secondaryValues = EntityDatabaseHelper.toValues(entity, table.getSecondaryColumns());

				int rowsAffected = mDatabase.update(table.getSqlName(), secondaryValues, whereKey.first, whereKey.second);

				if (BuildConfig.DEBUG && rowsAffected != 1)
					throw new IllegalStateException("Unexpected number of rowsAffected returned by updating table data: " + rowsAffected);
			}
		}

		return entityId;
	}

	public void deleteAll()
	{
		mDatabase.beginTransaction();
		try
		{
			for (TableDefinition table : mDefinition.getTables())
				deleteTable(table.getSqlName(), null, null);

			for (String mdTable : EntityDatabaseHelper.metadataTableNames())
				deleteTable(mdTable, null, null);

			mDatabase.setTransactionSuccessful();
		}
		finally
		{
			mDatabase.endTransaction();
		}
	}

	private void deleteTable(String sqlName, String whereClause, String[] whereArgs) {
		try {
			mDatabase.delete(sqlName, whereClause, whereArgs);
		}
		catch (android.database.sqlite.SQLiteException exception) // probably no such table, log it
		{
			Services.Log.error("EntityDatabase deleteTable " + exception.toString());
		}
	}

	public void deleteAll(String tableName, QueryData query)
	{
		TableDefinition table = mDefinition.getTable(tableName);
		if (table != null)
			deleteAll(table, query);
	}

	private void deleteAll(TableDefinition table, QueryData query)
	{
		mDatabase.beginTransaction();
		try
		{
			long queryId = readQueryId(query);
			if (queryId == NOT_FOUND)
				return; // Nothing to delete.

			String[] whereQueryIdArgs = new String[] { Long.toString(queryId) };

			// Delete from the <Entity> table, UNLESS they are referenced by other queries.
			String entityWhereClause = String.format("%s IN (%s)", ENTITY.COL_ENTITY_ID, QueryEntity.ENTITIES_OF_QUERY);
			entityWhereClause += String.format(" AND NOT EXISTS (SELECT * FROM %s QE WHERE QE.%s = %s.%s and QE.%s <> ?)", QueryEntity.TABLE, QueryEntity.COL_ENTITY_ID, table.getSqlName(), ENTITY.COL_ENTITY_ID, QueryEntity.COL_QUERY_ID);
			mDatabase.delete(table.getSqlName(), entityWhereClause, new String[] { whereQueryIdArgs[0], whereQueryIdArgs[0] });

			// Delete from the Query-<Entity> table.
			String eqWhereClause = String.format("%s = ?", QueryEntity.COL_QUERY_ID);
			mDatabase.delete(QueryEntity.TABLE, eqWhereClause, whereQueryIdArgs);

			// Delete from the Query table.
			String qWhereClause = String.format("%s = ?", QUERY.COL_ID);
			mDatabase.delete(QUERY.TABLE, qWhereClause, whereQueryIdArgs);

			mDatabase.setTransactionSuccessful();
		}
		finally
		{
			mDatabase.endTransaction();
		}
	}

	public QueryData readQueryData(QueryData query)
	{
		String table = QUERY.TABLE;
		String[] columns = { "*" };

		String queryWhere = QUERY.COL_BASE_URI + " = ? AND " + QUERY.COL_PARAMETERS + " = ?";
		String[] queryWhereParameters = new String[] { query.getBaseUri(), query.getParameters() };

		Cursor cursor = mDatabase.query(table, columns, queryWhere, queryWhereParameters, null, null, null);
		try
		{
			if (cursor.moveToFirst())
			{
				Date serverTimestamp = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(QUERY.COL_SERVER_TIMESTAMP)));
				Date clientTimestamp = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(QUERY.COL_CLIENT_TIMESTAMP)));
				boolean isComplete = (cursor.getInt(cursor.getColumnIndexOrThrow(QUERY.COL_IS_COMPLETE)) == 1);
				int rowCount = cursor.getInt(cursor.getColumnIndexOrThrow(QUERY.COL_ROW_COUNT));
				String hash = cursor.getString(cursor.getColumnIndexOrThrow(QUERY.COL_HASH));

				return new QueryData(query.getBaseUri(), query.getParameters(), serverTimestamp, clientTimestamp, isComplete, rowCount, hash);
			}
			else
				return QueryData.empty(query);
		}
		finally
		{
			cursor.close();
		}
	}

	public void writeQueryData(QueryData query)
	{
		writeQueryId(query);
	}

	public Iterable<String> getFieldValues(String tableName, List<String> fields, String filter, int operator)
	{
		TableDefinition table = mDefinition.getTable(tableName);
		if (table != null && fields.size() > 0)
			return getFieldValues(table, fields, filter, operator);
		else
			return new ArrayList<>();
	}

	private Iterable<String> getFieldValues(TableDefinition table, List<String> fields, String filter, int operator)
	{
		List<String> result = new ArrayList<>();
		final String STR_FIELD = "_Str";

		String subSelect = Strings.EMPTY;
		for (String field : fields)
		{
			if (subSelect.length() != 0)
				subSelect += " union ";

			subSelect += String.format("select %s as %s from %s", field, STR_FIELD, table.getSqlName());
		}

		if (Services.Strings.hasValue(filter))
		{
			if (operator == SearchDefinition.OPERATOR_BEGINS_WITH)
				filter = Strings.toLowerCase(filter) + "%";
			else
				filter = "%" + Strings.toLowerCase(filter) + "%";
		}
		else
			filter = "%";

		Cursor cursor = mDatabase.query(true, "(" + subSelect + ")", new String[] { STR_FIELD },
				STR_FIELD + " like ?", new String[] { filter }, null, null, STR_FIELD, null);
		try
		{
			while (cursor.moveToNext())
				result.add(cursor.getString(0));
		}
		finally
		{
			cursor.close();
		}

		return result;
	}

	/**
	 * Gets the id of a query if it's already stored.
	 * @param query Query URI.
	 * @return The id of the query, or -1 if it does not exist.
	 */
	private long readQueryId(QueryData query)
	{
		// See if the query is already stored.
		String[] queryColumns = new String[] { QUERY.COL_ID };
		String queryWhere = QUERY.COL_BASE_URI + " = ? AND " + QUERY.COL_PARAMETERS + " = ?";
		String[] queryWhereParameters = new String[] { query.getBaseUri(), query.getParameters() };

		Cursor current = mDatabase.query(QUERY.TABLE, queryColumns, queryWhere, queryWhereParameters, null, null, null);
		try
		{
			if (current.moveToFirst())
				return current.getInt(0);
		}
		finally
		{
			current.close();
		}

		return NOT_FOUND;
	}

	/**
	 * Gets the id of the query if it's already stored, otherwise stores it and returns the new id.
	 * @param query Query URI.
	 * @return The id of the query.
	 */
	private long writeQueryId(QueryData query)
	{
		// See if the query already has an id.
		long queryId = readQueryId(query);

		if (queryId == NOT_FOUND)
		{
			// A new query, register it.
			ContentValues insertValues = new ContentValues();
			insertValues.put(QUERY.COL_BASE_URI, query.getBaseUri());
			insertValues.put(QUERY.COL_PARAMETERS, query.getParameters());
			putQueryInfo(insertValues, query);

			// Insert and return rowId.
			queryId = mDatabase.insertOrThrow(QUERY.TABLE, null, insertValues);
		}
		else
		{
			// Update extra data.
			ContentValues updateValues = new ContentValues();
			putQueryInfo(updateValues, query);

			String uWhereClause = String.format("%s = ?", QUERY.COL_ID);
			String[] uWhereArgs = new String[] { Long.toString(queryId) };

			int rowsAffected = mDatabase.update(QUERY.TABLE, updateValues, uWhereClause, uWhereArgs);

			if (BuildConfig.DEBUG && rowsAffected != 1)
				throw new IllegalStateException("Unexpected number of rowsAffected returned in writeQueryId(): " + rowsAffected);
		}

		return queryId;
	}

	private static void putQueryInfo(ContentValues values, QueryData query)
	{
		values.put(QUERY.COL_SERVER_TIMESTAMP, (query.getServerTimestamp() != null ? query.getServerTimestamp().getTime() : 0));
		values.put(QUERY.COL_CLIENT_TIMESTAMP, (query.getClientTimestamp() != null ? query.getClientTimestamp().getTime() : 0));
		values.put(QUERY.COL_IS_COMPLETE, query.isComplete());
		values.put(QUERY.COL_ROW_COUNT, query.getRowCount());
		values.put(QUERY.COL_HASH, query.getHash());
	}

	/**
	 * Gets the id (rowid) of the entity, searching by its primary key.
	 * @param table Table associated to the entity type.
	 * @param entity Entity to look for.
	 * @return The entity id value, or NOT_FOUND if the entity is not present in the database.
	 */
	private long readEntityId(TableDefinition table, Entity entity)
	{
		long entityId = NOT_FOUND;

		// In a table with no key (e.g. all variables) the record cannot be found.
		// Return id = 0 so that a new one is created if needed.
		if (table.getKey().size() == 0)
			return NOT_FOUND;

		String[] idColumns = new String[] { ENTITY.COL_ENTITY_ID };
		Pair<String, String[]> whereKey = EntityDatabaseHelper.keyToWhereCondition(entity, table);

		Cursor current = mDatabase.query(table.getSqlName(), idColumns, whereKey.first, whereKey.second, null, null, null);
		try
		{
			if (current.moveToFirst())
				entityId = current.getLong(0);
		}
		finally
		{
			current.close();
		}

		return entityId;
	}
}
