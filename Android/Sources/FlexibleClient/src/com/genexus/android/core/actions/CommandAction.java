package com.genexus.android.core.actions;

import android.content.Intent;

import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.ActionParameter;
import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.metadata.expressions.IMethodCall;
import com.genexus.android.core.base.utils.Strings;

import java.util.Set;

public class CommandAction extends ActionWithOutput {
    private final ActionParameter mExpression;
    private boolean mCatchOnActivityResult;

    static final String COMMAND_EXPRESSION = "commandExpression";
	private static final Set<String> ASSIGNMENT_METHODS = Strings.newSet("FromWKT");

    CommandAction(UIContext context, ActionDefinition definition, ActionParameters parameters) {
        super(context, definition, parameters);
        mExpression = ActionHelper.getParameter(COMMAND_EXPRESSION, definition);
    }

    public static boolean isAction(ActionDefinition definition) {
        return ActionHelper.hasProperties(definition, COMMAND_EXPRESSION);
    }

    private ActionResult eval() {
        // Evaluate expression
        mCatchOnActivityResult = false;
        Expression.Value value = getParameterValue(mExpression);
        if (value.getType() == Expression.Type.WAIT) {
            mCatchOnActivityResult = true;
            return ActionResult.SUCCESS_WAIT;
        } else if (value.getType() == Expression.Type.FAIL) {
            setOutput(value.coerceToOutputResult());
            return ActionResult.FAILURE;
        }

		// Cases where the result should be assigned to the method's target
		if (value.getType() != Expression.Type.UNKNOWN && mExpression.getExpression() instanceof IMethodCall) {
			IMethodCall methodCall = (IMethodCall) mExpression.getExpression();
			if (ASSIGNMENT_METHODS.contains(methodCall.getMethod())) {
				ActionParameter outputParameter = new ActionParameter(null, methodCall.getTarget().toString(), methodCall.getTarget());
				setOutputValue(outputParameter, value);
			}
		}

        return ActionResult.SUCCESS_CONTINUE;
    }

    @Override
    public boolean Do() {
        return eval().isSuccess();
    }

    @Override
    public ActionResult afterActivityResult(int requestCode, int resultCode, Intent result) {
        setActivityResultParameters(requestCode, resultCode, result);
        return eval();
    }

	@Override
	public ActionResult afterRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		setRequestPermissionsResultParameters(requestCode, permissions, grantResults);
		return eval();
	}

    @Override
    public boolean catchOnActivityResult() {
        return mCatchOnActivityResult;
    }
}
