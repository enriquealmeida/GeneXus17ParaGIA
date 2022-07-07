package com.genexus.android.core.base.metadata;

import com.genexus.android.core.base.metadata.loader.MetadataLoader;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;

public class DataSourceAttributeDefinition extends DataItem
{
	private static final long serialVersionUID = 1L;

	private final boolean mIsKey;

	public DataSourceAttributeDefinition(INodeObject json)
	{
		super(getDefinition(json));

		// Although name is in definition, getDefinition() may fail if the
		// attributes file was not loaded, so set it from here.
		setProperty("Name", getAttributeName(json));

		// Read properties from the attribute node proper.
		mIsKey = json.optBoolean("@isKey");
		setStorageType(json.optInt("@internalType"));
	}

	@Override
	public boolean isKey()
	{
		return mIsKey;
	}

	private static ITypeDefinition getDefinition(INodeObject json)
	{
		return Services.Application.getDefinition().getAttribute(getAttributeName(json));
	}

	private static String getAttributeName(INodeObject json)
	{
		 return MetadataLoader.getAttributeName(json.optString("@attribute"));
	}
}
