package com.genexus.android.core.actions;

import java.util.ArrayList;
import java.util.Set;

import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.ActionParameter;
import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.expressions.ExpressionFactory;
import com.genexus.android.core.base.metadata.expressions.IMethodCall;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.utils.Strings;

/**
 * Statments that correspond to a method called "in isolation", without assigning its return value,
 * if any, to a variable. For example, &var.FromString("x") or &File.ReadLine().
 */
class MethodCallAction extends Action
{
	private final IMethodCall mMethodCall;

	private static final Set<String> ASSIGNMENT_METHODS = Strings.newSet("FromImage", "FromJson", "FromString", "FromURL", "SetEmpty");

	protected MethodCallAction(UIContext context, ActionDefinition definition, ActionParameters parameters)
	{
		super(context, definition, parameters);

		Expression target = ExpressionFactory.parse(definition.getProperty(INodeObject.class, ActionHelper.METHOD_CALL_TARGET));
		String method = definition.optStringProperty(ActionHelper.METHOD_CALL_METHOD_NAME);
		ArrayList<Expression> methodParameters = new ArrayList<>();
		for (ActionParameter parameter : definition.getParameters())
			methodParameters.add(parameter.getExpression());

		mMethodCall = ExpressionFactory.newMethodCall(target, method, methodParameters);
	}

	public static boolean isAction(ActionDefinition definition)
	{
		return ActionHelper.hasProperties(definition, ActionHelper.METHOD_CALL_TARGET, ActionHelper.METHOD_CALL_METHOD_NAME);
	}

	@Override
	public boolean Do()
	{
		ActionParameter tmpParameter = new ActionParameter(mMethodCall);
		Value methodResult = getParameterValue(tmpParameter);

		// In the case of FromString(), the methodResult should be assigned to the method's target
		// For example, &SDT.Field.FromString(...) is something like &SDT.Field = FromString(...)
		if (methodResult.getType() != Expression.Type.UNKNOWN && ASSIGNMENT_METHODS.contains(mMethodCall.getMethod()))
		{
			ActionParameter outputParameter = new ActionParameter(null, mMethodCall.getTarget().toString(), mMethodCall.getTarget());
			setOutputValue(outputParameter, methodResult);
		}

		return true;
	}

	@Override
	public String getErrorMessage() {
		return ""; // never fails
	}
}
