package com.genexus.android.core.base.metadata.expressions;

import com.genexus.android.core.base.serialization.INodeObject;

class AttributeExpression extends ValueExpression
{
	static final String TYPE = "attribute";

	public AttributeExpression(INodeObject node)
	{
		super(node);
	}
}
