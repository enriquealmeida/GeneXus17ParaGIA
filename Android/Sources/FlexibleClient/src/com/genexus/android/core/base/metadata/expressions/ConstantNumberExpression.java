package com.genexus.android.core.base.metadata.expressions;

import androidx.annotation.NonNull;

import java.math.BigDecimal;

import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;

class ConstantNumberExpression extends ConstantExpression
{
	static final String TYPE = "number";

	public ConstantNumberExpression(INodeObject node)
	{
		super(node);
	}

	@Override
	public @NonNull Value eval(IExpressionContext context)
	{
		String strValue = getConstant();
		try
		{
			BigDecimal value = new BigDecimal(strValue);
			return new Value(Type.DECIMAL, value);
		}
		catch (NumberFormatException e)
		{
			Services.Log.warning(String.format("Error parsing constant number expression '%s'.", strValue));
			return new Value(Type.DECIMAL, BigDecimal.ZERO);
		}
	}
}
