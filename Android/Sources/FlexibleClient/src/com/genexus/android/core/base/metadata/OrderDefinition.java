package com.genexus.android.core.base.metadata;

import java.util.ArrayList;
import java.util.List;

import com.genexus.android.core.base.metadata.loader.MetadataLoader;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

public class OrderDefinition extends DataSourceMemberDefinition
{
	private static final long serialVersionUID = 1L;

	private String mName;
	private final List<OrderAttributeDefinition> mAttributes;

	private boolean mBreakBy = false;
	private String mBreakByUpToAttribute = null;
	private String mBreakByDescriptionAttribute = null;
	private boolean mEnableAlphaIndexer = false;

	public OrderDefinition(IDataSourceDefinition parent, INodeObject jsonOrder)
	{
		super(parent);
		mAttributes = new ArrayList<>();
		deserialize(jsonOrder);
	}

	public int getId()
	{
		return getParent().getOrders().indexOf(this);
	}

	@Override
	public String getName() { return Services.Language.getTranslation(mName); }
	public List<OrderAttributeDefinition> getAttributes() { return mAttributes;	}

	boolean hasBreakBy()	{ return mBreakBy; }

	public boolean getEnableAlphaIndexer() { return mEnableAlphaIndexer; }

	List<DataItem> getBreakByAttributes()
	{
		ArrayList<DataItem> attributes = new ArrayList<>();
		for (OrderAttributeDefinition orderAtt : mAttributes)
		{
			attributes.add(orderAtt.getAttribute());
			if (Services.Strings.hasValue(mBreakByUpToAttribute) && mBreakByUpToAttribute.equalsIgnoreCase(orderAtt.getName()))
				break; // Up to here.
		}

		return attributes;
	}

	List<DataItem> getBreakByDescriptionAttributes()
	{
		DataItem single = getParent().getDataItem(mBreakByDescriptionAttribute);
		if (single != null)
		{
			ArrayList<DataItem> attributes = new ArrayList<>();
			attributes.add(single);
			return attributes;
		}
		else
			return getBreakByAttributes();
	}

	private void deserialize(INodeObject jsonOrder)
	{
		mName = jsonOrder.optString("@name");
		mBreakBy = jsonOrder.optBoolean("@GroupBy");
		mBreakByUpToAttribute = MetadataLoader.getObjectName(jsonOrder.optString("@groupByUpTo", Strings.EMPTY));
		mBreakByDescriptionAttribute = MetadataLoader.getObjectName(jsonOrder.optString("@descriptionAttribute", Strings.EMPTY));
		mEnableAlphaIndexer = jsonOrder.optBoolean("@EnableAlphaIndexer");

		// Read Attributes in Order
		for (INodeObject jsonAttribute : jsonOrder.optCollection("attribute"))
		{
			OrderAttributeDefinition attribute = new OrderAttributeDefinition(this, jsonAttribute);
			mAttributes.add(attribute);
		}
	}
}
