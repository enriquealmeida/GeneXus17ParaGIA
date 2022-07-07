package com.genexus.android.core.base.metadata.expressions;

import androidx.annotation.NonNull;

import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;

class ConstantStringExpression extends ConstantExpression
{
	static final String TYPE = "string";

	public ConstantStringExpression(INodeObject node)
	{
		super(node);
	}

	@Override
	public @NonNull Value eval(IExpressionContext context)
	{
		String str = Services.Language.getExpressionTranslation(getConstant());

		// Remove the quotes to get the value.
		String[] quoteSet = new String[] { "\"", "'" };
		for (String quote : quoteSet)
		{
			if (str.startsWith(quote) && str.endsWith(quote))
			{
				str = str.substring(1, str.length() - 1);
				break;
			}
		}

		return Value.newString(str);
	}
}
