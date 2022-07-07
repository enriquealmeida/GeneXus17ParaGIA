package com.genexus.android.core.base.metadata.expressions;

import androidx.annotation.NonNull;

import com.genexus.android.core.base.serialization.INodeObject;

class KeywordExpression extends ConstantExpression
{
	static final String TYPE = "keyword";

	public KeywordExpression(INodeObject node)
	{
		super(node);
	}

	@Override
	public @NonNull Value eval(IExpressionContext context)
	{
		return Value.newString(getConstant());
	}
}
