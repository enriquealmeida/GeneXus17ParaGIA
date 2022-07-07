package com.genexus.android.core.providers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.genexus.android.core.base.metadata.DataItem;
import com.genexus.android.core.base.metadata.IDataSourceDefinition;
import com.genexus.android.core.base.metadata.LevelDefinition;
import com.genexus.android.core.base.metadata.StructureDefinition;

class TableDefinition implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private final String mName;
	private final StructureDefinition mStructure;
	private final List<ColumnDefinition> mColumns;
	private final List<ColumnDefinition> mExtraKeyHashColumns;
	private final int mVersion;

	public TableDefinition(IDataSourceDefinition dataSource)
	{
		mName = dataSource.getName();
		mStructure = dataSource.getStructure();

		mColumns = new ArrayList<>();
		mExtraKeyHashColumns = new ArrayList<>();

		for (DataItem dataItem : dataSource.getDataItems()) {
			ColumnDefinition column = new ColumnDefinition(dataItem);
			mColumns.add(column);

			if (dataItem.isVariable())
				mExtraKeyHashColumns.add(column);
		}

		for (LevelDefinition level : dataSource.getStructure().Root.Levels) {
			if (level.getNoParentLevel()) {
				ColumnDefinition column = new ColumnDefinition(level);
				mColumns.add(column);
			}
		}

		// If a table has no key then it shouldn't have an "extra key" either.
		if (getKey().size() == 0)
			mExtraKeyHashColumns.clear();

		mVersion = dataSource.getVersion();
	}

	@Override
	public String toString()
	{
		return mName;
	}

	public String getName() { return mName; }
	public String getSqlName() { return EntityDatabaseHelper.sqlName(mName); }

	public StructureDefinition getStructure() { return mStructure; }
	int getVersion() { return mVersion; }

	public List<ColumnDefinition> getColumns() { return mColumns; }
	public List<ColumnDefinition> getExtraKeyHashColumns() { return mExtraKeyHashColumns; }

	public List<ColumnDefinition> getKey()
	{
		return getColumnsKeyOrNot(true);
	}

	public List<ColumnDefinition> getSecondaryColumns()
	{
		return getColumnsKeyOrNot(false);
	}

	private List<ColumnDefinition> getColumnsKeyOrNot(boolean key)
	{
		List<ColumnDefinition> cols = new ArrayList<>();
		for (ColumnDefinition column : getColumns())
			if (column.isKey() == key)
				cols.add(column);

		return cols;
	}
}
