package com.genexus.android.core.base.metadata.filter;

import com.genexus.android.core.base.metadata.DataItemHelper;
import com.genexus.android.core.base.metadata.DataSourceMemberDefinition;
import com.genexus.android.core.base.metadata.IDataSourceDefinition;
import com.genexus.android.core.base.metadata.loader.MetadataLoader;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;

public class SearchAttributeDefinition extends DataSourceMemberDefinition
{
	private static final long serialVersionUID = 1L;

	private final String mName;
	private final String mDescription;

	SearchAttributeDefinition(IDataSourceDefinition parent, INodeObject jsonData)
	{
		super(parent);
		String name = MetadataLoader.getAttributeName(jsonData.getString("@attribute"));

		mName = DataItemHelper.getNormalizedName(name);
		mDescription = jsonData.getString("@description");
	}

	@Override
	public String getName() { return mName; }

	public String getDescription() { return Services.Language.getTranslation(mDescription); }
}
