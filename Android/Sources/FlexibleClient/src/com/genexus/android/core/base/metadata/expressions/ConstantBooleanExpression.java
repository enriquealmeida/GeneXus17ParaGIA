package com.genexus.android.core.base.metadata.expressions;

import androidx.annotation.NonNull;

import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;

class ConstantBooleanExpression extends ConstantExpression
{
	static final String TYPE = "boolean";

	public ConstantBooleanExpression(INodeObject node)
	{
		super(node);
	}

	@Override
	public @NonNull Value eval(IExpressionContext context)
	{
		String strValue = getConstant();
		Boolean value = Services.Strings.tryParseBoolean(strValue);
		if (value != null)
		{
			return Value.newBoolean(value);
		}
		else
		{
			Services.Log.warning(String.format("Unexpected value parsing constant boolean expression '%s'.", strValue));
			return new Value(Type.BOOLEAN, false);
		}
	}

}
