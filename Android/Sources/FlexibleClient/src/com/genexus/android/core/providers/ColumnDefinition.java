package com.genexus.android.core.providers;

import androidx.annotation.NonNull;

import java.io.Serializable;

import com.genexus.android.core.base.metadata.DataItem;

public class ColumnDefinition implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private final String mName;
	private final int mType;
	private final boolean mIsKey;
	
	public ColumnDefinition(@NonNull String name, int type)
	{
		this(name, type, false);
	}
	
	public ColumnDefinition(@NonNull String name, int type, boolean isKey)
	{
		mName = name;
		mType = type;
		mIsKey = isKey;
	}
	
	ColumnDefinition(DataItem dataItem)
	{
		this(
			dataItem.getName(),
			dataItem.isCollection() ? TYPE_STRUCTURED : dataItem.getStorageType(),
			dataItem.isKey());
	}

	@Override
	public String toString()
	{
		return mName;
	}

	public @NonNull String getName() { return mName; }
	public String getSqlName() { return EntityDatabaseHelper.sqlName(mName); }
	
	public int getType() { return mType; }
	public boolean isKey() { return mIsKey; }

	public static final int TYPE_CHARACTER = 1; // Character, VarChar, LongVarChar
	public static final int TYPE_INTEGER = 2; // Numeric(X,0)
	public static final int TYPE_FLOAT = 3; // Numeric(X,>0)
	public static final int TYPE_DATETIME = 4; // Date, DateTime
	public static final int TYPE_BOOLEAN = 5; // Boolean
	public static final int TYPE_BLOB = 6; // Blob
	public static final int TYPE_URI = 7; // Image, Video, Audio
	public static final int TYPE_STRUCTURED = 8; // SDT, BC.
}
