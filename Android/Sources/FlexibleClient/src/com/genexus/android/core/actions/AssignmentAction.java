package com.genexus.android.core.actions;

import android.content.Intent;

import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.ActionParameter;
import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;

class AssignmentAction extends ActionWithOutput
{
	private final ActionParameter mAssignTarget;
	private final ActionParameter mAssignExpression;
	private boolean mCatchOnActivityResult;

	AssignmentAction(UIContext context, ActionDefinition definition, ActionParameters parameters)
	{
		super(context, definition, parameters);
		mAssignTarget = ActionHelper.getAssignmentLeft(definition);
		mAssignExpression = ActionHelper.getAssignmentRight(definition);
	}

	public static boolean isAction(ActionDefinition definition)
	{
		return ActionHelper.hasProperties(definition, ActionHelper.ASSIGN_LEFT_VARIABLE, ActionHelper.ASSIGN_RIGHT_VALUE);
	}

	private ActionResult assign()
	{
		// Evaluate expression and perform assignment.
		mCatchOnActivityResult = false;
		Value value = getParameterValue(mAssignExpression);
		if (value.getType() == Expression.Type.WAIT) {
			mCatchOnActivityResult = true;
			return ActionResult.SUCCESS_WAIT;
		}
		else if (value.getType() == Expression.Type.FAIL) {
			setOutput(value.coerceToOutputResult());
			return ActionResult.FAILURE;
		}

		setOutputValue(mAssignTarget, value);
		return ActionResult.SUCCESS_CONTINUE;
	}

	@Override
	public boolean Do()
	{
		return assign().isSuccess();
	}

	@Override
	public ActionResult afterActivityResult(int requestCode, int resultCode, Intent result)
	{
		setActivityResultParameters(requestCode, resultCode, result);
		return assign();
	}

	@Override
	public ActionResult afterRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
	{
		setRequestPermissionsResultParameters(requestCode, permissions, grantResults);
		return assign();
	}

	@Override
	public boolean catchOnActivityResult()
	{
		return mCatchOnActivityResult;
	}
}
