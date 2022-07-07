package com.genexus.android.core.base.metadata;

import java.io.Serializable;

import com.genexus.android.core.base.metadata.loader.MetadataLoader;
import com.genexus.android.core.base.serialization.INodeObject;

public class OrderAttributeDefinition implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final OrderDefinition mOrder;
	private String mName;
	private boolean mIsAscending;

	OrderAttributeDefinition(OrderDefinition order, INodeObject jsonOrderAttribute)
	{
		mOrder = order;
		deserialize(jsonOrderAttribute);
	}

	public OrderDefinition getOrder() { return mOrder; }
	public String getName() { return mName; }
	public boolean isAscending() { return mIsAscending; }

	public DataItem getAttribute()
	{
		// May be a data source item, or a variable in the object.
		return DataItemHelper.find(mOrder.getParent(), mName, true);
	}

	private void deserialize(INodeObject jsonOrderAttribute)
	{
		mName = MetadataLoader.getAttributeName(jsonOrderAttribute.getString("@attribute"));
		mIsAscending = jsonOrderAttribute.optBoolean("@ascending", true);
	}
}
