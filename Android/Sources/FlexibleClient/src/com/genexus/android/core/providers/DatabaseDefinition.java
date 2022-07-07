package com.genexus.android.core.providers;

import java.io.Serializable;
import java.util.Collection;
import java.util.TreeMap;

import com.genexus.android.core.base.metadata.IDataSourceDefinition;
import com.genexus.android.core.base.services.Services;

public class DatabaseDefinition implements Serializable
{
	// Version id, for serialization. Important: change this number if metadata structure changes!
	private static final long serialVersionUID = 1L;

	private final String mName;
	private final TreeMap<String, TableDefinition> mTables;
	private int mVersion = 0;

	public DatabaseDefinition(String name)
	{
		mTables = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

		// Load database definition from Data Sources in application.
		mName = name;
	}

	public void addTableFor(IDataSourceDefinition dataSource)
	{
		TableDefinition table = new TableDefinition(dataSource);
		mTables.put(table.getName(), table);
	}

	int getVersion()
	{
		if (mVersion == 0)
		{
			// Calculate version hash of database by combining schema version, table versions, and URI.
			// (URI is necessary in case the same application is switched from one server to another)
			int version = EntityDatabase.SCHEMA_VERSION;
			version ^= getUriHash();

			for (TableDefinition table : getTables())
				version ^= table.getVersion();

			mVersion = Math.abs(version);
		}

		return mVersion;
	}

	private static int getUriHash()
	{
		if (Services.Application.get() != null)
		{
			String uri = Services.Application.get().getAPIUri();
			if (uri != null)
			{
				 uri += Services.Application.get().getAppEntry();
				 return uri.hashCode();
			}
		}

		return 0; // No URI?
	}

	public String getName() { return mName; }
	public Collection<TableDefinition> getTables() { return mTables.values(); }

	public TableDefinition getTable(String name)
	{
		return mTables.get(name);
	}
}
