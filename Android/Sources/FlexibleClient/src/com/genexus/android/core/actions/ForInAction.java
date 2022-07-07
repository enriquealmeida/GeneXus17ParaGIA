package com.genexus.android.core.actions;

import android.content.Intent;

import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.ActionParameter;
import com.genexus.android.core.base.metadata.expressions.Expression;

import java.util.Iterator;

/**
 * Statments that correspond to for &variable in &collection
 */
class ForInAction extends ActionWithOutput
{
    private final String mVariable;
    private final ActionParameter mCollectionExpression;
    private final CompositeAction mActionBlock;
    private boolean mCatchOnActivityResult;
    private Iterator<?> mIterator;

    protected ForInAction(UIContext context, ActionDefinition definition, ActionParameters parameters, boolean isComposite) {
        super(context, definition, parameters);

        mVariable = definition.optStringProperty("@ForInVariable");
        mCollectionExpression = ActionHelper.getParameter("ForInCollectionExpression", definition);
        mActionBlock = ActionFactory.getInnerActionChildren(context, definition, parameters, isComposite);
    }

    public static boolean isAction(ActionDefinition definition) {
        return "forin".equals(definition.optStringProperty(ActionHelper.STATEMENT_NAME));
    }

    private ActionResult execute() {
        Expression.Value value = getParameterValue(mCollectionExpression);
        if (value.getType() == Expression.Type.WAIT) {
            mCatchOnActivityResult = true;
            return ActionResult.SUCCESS_WAIT;
        }
        else if (value.getType() == Expression.Type.FAIL) {
            setOutput(value.coerceToOutputResult());
            return ActionResult.FAILURE;
        }
        else if (value.getType() == Expression.Type.COLLECTION) {
            mIterator = value.coerceToCollection().iterator();
            return iterate();
        }
        else if (value.getType() == Expression.Type.API) {
            mIterator = value.coerceToApi().iterator();
            return iterate();
        }

        return ActionResult.SUCCESS_CONTINUE;
    }

    private ActionResult iterate() {
        if (mIterator != null) {
            CompositeAction.LoopCondition loopCondition = () -> {
                if (mIterator.hasNext()) {
                    Object item = mIterator.next();
                    setOutputValue(mVariable, Expression.Value.newValue(item));
                    return true;
                }
                return false;
            };
            if (loopCondition.continueLoop()) {
                mActionBlock.setLoopCondition(loopCondition);
                ActionExecution exec = new ActionExecution(mActionBlock);
                exec.executeAction();
                mCatchOnActivityResult = true;
                return ActionResult.SUCCESS_WAIT;
            }
        }
        return ActionResult.SUCCESS_CONTINUE;
    }

    @Override
    public boolean Do()
    {
        return execute().isSuccess();
    }

    @Override
    public ActionResult afterActivityResult(int requestCode, int resultCode, Intent result)
    {
        setActivityResultParameters(requestCode, resultCode, result);
        return execute();
    }

    @Override
	public ActionResult afterRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
	{
		setRequestPermissionsResultParameters(requestCode, permissions, grantResults);
		return execute();
	}

    @Override
    public boolean catchOnActivityResult()
    {
        return mCatchOnActivityResult;
    }
}
