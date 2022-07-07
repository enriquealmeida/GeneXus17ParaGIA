package com.genexus.android.core.base.metadata.expressions;

import com.genexus.android.core.base.serialization.INodeObject;

class VariableExpression extends ValueExpression
{
	static final String TYPE = "variable";

	public VariableExpression(INodeObject node)
	{
		super(node);
	}
}
