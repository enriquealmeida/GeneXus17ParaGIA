package com.genexus.android.core.base.metadata.expressions;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import com.genexus.android.core.base.metadata.DetailDefinition;
import com.genexus.android.core.base.metadata.WWLevelDefinition;
import com.genexus.android.core.base.metadata.WWListDefinition;
import com.genexus.android.core.base.metadata.WorkWithDefinition;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.utils.Cast;
import com.genexus.GXutil;

import static com.genexus.android.core.base.metadata.expressions.ExpressionFormatHelper.toUrlParameterString;

class FunctionExpression implements Expression
{
	static final String TYPE = "function";

	private final String mFunction;
	private final List<Expression> mParameters;

	private static final String FUNCTION_IIF = "iif";
	private static final String FUNCTION_MOD = "mod";
	private static final String FUNCTION_GET_LANGUAGE = "GetLanguage";
	private static final String FUNCTION_GET_MESSAGE_TEXT = "GetMessageText";
	private static final String FUNCTION_FORMAT = "Format";
	private static final String FUNCTION_TO_FORMATTED_STRING = "ToFormattedString";
	private static final String FUNCTION_CREATE = "Create";
	private static final String FUNCTION_LINK = "Link";
	private static final String FUNCTION_COMPARE = "compare";

	public FunctionExpression(INodeObject node)
	{
		mFunction = node.getString("@funcName");
		mParameters = ExpressionFactory.parseParameters(node);
	}

	@Override
	public String toString()
	{
		return String.format("%s(%s)", mFunction, mParameters);
	}

	@Override
	public @NonNull Value eval(IExpressionContext context)
	{
		// Special cases that are not mapped to actual functions.
		if (FUNCTION_IIF.equalsIgnoreCase(mFunction))
		{
			Value condition = context.eval(mParameters.get(0));
			if (condition.mustReturn())
				return condition;
			if (condition.coerceToBoolean())
				return context.eval(mParameters.get(1));
			else
				return context.eval(mParameters.get(2));
		}

		List<Value> vParameters = ExpressionHelper.evalExpressions(mParameters, context);
		if (!vParameters.isEmpty()) {
			Value last = vParameters.get(vParameters.size() - 1);
			if (last.mustReturn())
				return last;
		}

		// Special cases that are not mapped to actual functions.
		if (FUNCTION_MOD.equalsIgnoreCase(mFunction))
		{
			Value dividend = vParameters.get(0);
			Value divisor = vParameters.get(1);
			BigDecimal mod = dividend.coerceToNumber().remainder(divisor.coerceToNumber());
			return new Value(dividend.getType(), mod);
		}
		else if (FUNCTION_GET_LANGUAGE.equalsIgnoreCase(mFunction))
		{
			return new Value(Type.STRING, String.valueOf(Services.Language.getCurrentLanguage()));
		}
		else if (FUNCTION_GET_MESSAGE_TEXT.equalsIgnoreCase(mFunction))
		{
			// This function is a bit useless...
			String messageKey = vParameters.get(0).coerceToString();
			if (vParameters.size() == 1)
				return new Value(Type.STRING, Services.Language.getTranslation(messageKey));
			else if (vParameters.size() == 2)
				return new Value(Type.STRING, Services.Language.getTranslation(messageKey, vParameters.get(1).coerceToString()));
		}
		else if (FUNCTION_FORMAT.equalsIgnoreCase(mFunction) && vParameters.size() != 0)
		{
			// First parameter is format string, rest are values to embed in it.
			String formatString = vParameters.get(0).coerceToString();
			vParameters.remove(0);

			return Value.newString(ExpressionFormatHelper.format(formatString, vParameters));
		}
		else if (FUNCTION_TO_FORMATTED_STRING.equalsIgnoreCase(mFunction) && mParameters.size() == 1)
		{
			return Value.newString(ExpressionFormatHelper.toFormattedString(vParameters.get(0)));
		}
		else if ((FUNCTION_CREATE.equalsIgnoreCase(mFunction) || FUNCTION_LINK.equalsIgnoreCase(mFunction)) && vParameters.size() > 0)
		{
			StringBuilder builder = new StringBuilder();
			builder.append("sd:");
			if (vParameters.get(0).getType() == Type.PANEL) {
				WorkWithDefinition wwd = Cast.as(WorkWithDefinition.class, vParameters.get(0).getValue());
				WWLevelDefinition level = Cast.as(WWLevelDefinition.class, vParameters.get(0).getValue());
				WWListDefinition list = Cast.as(WWListDefinition.class, vParameters.get(0).getValue());
				DetailDefinition detail = Cast.as(DetailDefinition.class, vParameters.get(0).getValue());
				if (wwd != null)
					builder.append(wwd.getName());
				else if (level != null)
					builder.append(level.getName());
				else if (list != null)
					builder.append(list.getName());
				else if (detail != null)
					builder.append(detail.getName());
			}
			else if (vParameters.get(0).getType() == Type.STRING) {
				builder.append(vParameters.get(0).getValue());
			}
			builder.append(Strings.QUESTION);
			int count = vParameters.size();
			for (int n = 1; n < count; n++) {
				builder.append(Services.HttpService.uriEncode(toUrlParameterString(vParameters.get(n))) );
				if (n+1 < count)
					builder.append(',');
			}
			return Value.newString(builder.toString());
		}
		else if (FUNCTION_COMPARE.equalsIgnoreCase(mFunction) && vParameters.size() != 0)
		{
			// Second parameter is compare operator.
			String operatorString = vParameters.get(1).coerceToString();

			Object result = GXutil.compare((Comparable) vParameters.get(0).getValue(), operatorString, (Comparable) vParameters.get(2).getValue());
			return ExpressionHelper.javaObjectToValue(Type.BOOLEAN, result);
		}

		// Generic functions.
		return FunctionHelper.call(mFunction, vParameters);
	}

	@Override
	public void values(@NonNull HashMap<String, DataType> nameTypes) {
		for (Expression e : mParameters)
			e.values(nameTypes);
	}

	@Override
	public boolean needsBackgroundThread() {
		for (Expression e : mParameters) {
			if (e.needsBackgroundThread())
				return true;
		}
		return false;
	}
}
