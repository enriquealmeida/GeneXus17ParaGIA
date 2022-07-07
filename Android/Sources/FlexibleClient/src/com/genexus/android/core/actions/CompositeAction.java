package com.genexus.android.core.actions;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Intent;

import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.expressions.Expression;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

import androidx.annotation.NonNull;

public class CompositeAction extends Action
{
	private final boolean mIsComposite;
	private final ArrayList<Action> mActions;
	private int mCurrentAction = 0;
	private boolean mIsEnded;

	private boolean mCurrentActionFail = false;
	private Action mCurrentActionExecuted = null;
	private IEventListener mEventListener;

	public interface IEventListener
	{
		// Not needed, for now.
		// void onStartEvent(CompositeAction event);
		void onEndEvent(CompositeAction event, boolean successful);
	}

	public CompositeAction(UIContext context, ActionDefinition definition, ActionParameters parameters, boolean isComposite)
	{
		super(context, definition, parameters);
		mIsComposite = isComposite;
		mActions = new ArrayList<>();
	}

	public void addAction(Action action)
	{
		mActions.add(action);
		action.setParentComposite(this);
	}

	Action getCurrentActionExecuted()
	{
		return mCurrentActionExecuted;
	}

	Action getNextActionToExecute()
	{
		try {
			if (mCurrentAction < mActions.size())
				return mActions.get(mCurrentAction);
		} catch (IndexOutOfBoundsException ex) {
			StringBuilder sb = new StringBuilder("mCurrentAction: " + mCurrentAction + ", mActions.size: " + mActions.size());
			if (mActions != null) sb.append(", mActions: ").append(mActions.toString());
			if (mCurrentActionExecuted != null) sb.append(", mCurrentActionExecuted: ").append(mCurrentActionExecuted.getClass().toString());
			IndexOutOfBoundsException exception = new IndexOutOfBoundsException(sb.toString());
			exception.setStackTrace(ex.getStackTrace());
			Services.Log.error(exception);
		}

		return null;
	}

	public boolean getIsComposite() {
		return mIsComposite;
	}

	@Override
	public boolean catchOnActivityResult()
	{
		//noinspection SimplifiableIfStatement
		if (mCurrentActionExecuted != null)
			return mCurrentActionExecuted.catchOnActivityResult();

		return false;
	}

	@Override
	public boolean isActivityEnding()
	{
		//noinspection SimplifiableIfStatement
		if (mCurrentActionExecuted != null)
			return mCurrentActionExecuted.isActivityEnding();
		else
			return false;
	}

	@Override
	public @NonNull ThreadPreference getThreadPreference()
	{
		Action next = getNextActionToExecute();
		if (next != null)
			return next.getThreadPreference();
		else
			return super.getThreadPreference();
	}

	// local lock for action do.
	ReentrantLock lock = new ReentrantLock();

	@Override
	public boolean Do()
	{
		lock.lock();
		try
		{
			// avoid concurrent change of currentAction index
			int currentActionToExecute = mCurrentAction;
			if (currentActionToExecute < mActions.size())
			{
				Action action = mActions.get(currentActionToExecute);
				if (mCurrentActivity != null)
					action.mCurrentActivity = mCurrentActivity;

				// if (action.getDefinition()!=null)
				// 	Services.Log.debug("CompositeAction Do , action: name: " + action.getDefinition().getName() + " object: " + action.getDefinition().getGxObject() + " action: " + action.toString());

				if (mCurrentAction >= mActions.size())
				{
					Services.Log.error("CompositeAction Do , Array index out of range: index: " + mCurrentAction + " old index: " + currentActionToExecute);
					Services.Log.debug("CompositeAction Do , Array index out of range: TotalIndex: " + mActions.size());
					Services.Log.debug("CompositeAction Do , Array index out of range: action: " + action.getDefinition().getName() + action.toString());
				}
				if (mCurrentAction != currentActionToExecute)
				{
					Services.Log.error("CompositeAction Do , Index changed: index: " + mCurrentAction + " old index: " + currentActionToExecute);
					Services.Log.debug("CompositeAction Do , Index changed: action: " + action.getDefinition().getName() + action.toString());
				}

				mCurrentActionExecuted = action;
				if (!action.Do())
				{
					if (mIsComposite) {
						mCurrentActionFail = true;
						//TODO Clean actions pending stack, did should not be necessary if cancel of an
						//action dont need to clean stack.
						//Only clean current action stack leave the rest alone, so a retry could work
						if (!CompositeBlockAction.isAction(getDefinition()) && !isSubRoutine())
							ActionExecution.cleanCurrentPendingAsDone();
						return false;
					} else {
						setErrorVariables(action.getErrorCode(), action.getErrorMessage());
					}
				} else if (!mIsComposite) {
					setErrorVariables(ERROR_CODE_NONE, "");
				}
				mCurrentAction++;
			}
			else
			{
				if (isDone() && getCurrentActionExecuted() != null)
					 ActionExecution.onEndEventAsDone(this, !isCurrentActionFail());
			}
		}
		finally
		{
			lock.unlock();
		}
		return true;
	}

	public void setErrorVariables(int code, String message) {
		setOutputValue(ERROR_VARIABLE_NAME, Expression.Value.newInteger(code));
		setOutputValue(ERROR_MESSAGE_VARNAME, Expression.Value.newString(message));
	}

	@Override
	public String getErrorMessage() {
		if (getCurrentActionExecuted() != null)
			return getCurrentActionExecuted().getErrorMessage();
		else
			return "";
	}

	public interface LoopCondition {
		boolean continueLoop();
	}

	private LoopCondition mLoopCondition;
	public void setLoopCondition(LoopCondition loopCondition) {
		mLoopCondition = loopCondition;
	}

	public void move(int delta)
	{
		mCurrentAction += delta;
	}

	public boolean isDone()
	{
		if (mCurrentActionFail)
			return true;

		if (mCurrentAction >= mActions.size())
		{
			if (mLoopCondition != null && mLoopCondition.continueLoop())
				mCurrentAction = 0;
			else
				return true;
		}

		return false;
	}

	public void setAsDone()
	{
		mCurrentAction = mActions.size();
	}

	public boolean isEnded() {
		return mIsEnded;
	}

	public void setAsEnded() {
		mIsEnded = true;
	}

	public void setCurrentActionFail(boolean fail)
	{
		mCurrentActionFail = fail;
	}

	public boolean isCurrentActionFail()
	{
		return mCurrentActionFail;
	}

	@Override
	public UIContext getContext()
	{
		if (mCurrentActionExecuted != null)
			return mCurrentActionExecuted.getContext();

		return super.getContext();
	}

	@Override
	public ActionResult afterActivityResult(int requestCode, int resultCode, Intent result)
	{
		if (mCurrentActionExecuted != null)
		{
			//logging.
			// Services.Log.debug("afterActivityResult afterActivityResult mCurrentActionExecuted" + mCurrentActionExecuted.toString());
			//Services.Log.debug("afterActivityResult requestCode is " + requestCode + " resultCode " + resultCode);
			//Services.Log.debug("afterActivityResult intent is null " + (result==null));
			return mCurrentActionExecuted.afterActivityResult(requestCode, resultCode, result);
		}

		return ActionResult.SUCCESS_CONTINUE;
	}

	@Override
	public ActionResult afterRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
	{
		if (mCurrentActionExecuted != null)
			return mCurrentActionExecuted.afterRequestPermissionsResult(requestCode, permissions, grantResults);
		else
			return ActionResult.SUCCESS_CONTINUE;
	}

	@Override
	public String toString()
	{
		String result ="";
		for (Action action : mActions)
			result += " " + action.toString();

		return result;
	}

	public Iterable<Action> getComponents()
	{
		return mActions;
	}

	public boolean isSubRoutine()
	{
		if (getDefinition() != null && Strings.hasValue(getDefinition().getActionType()))
		{
			if (getDefinition().getActionType().equalsIgnoreCase("Subroutine"))
				return true;
		}
		return false;	
	}

	public boolean isNested() {
		if (getDefinition() == null)
			return false;

		if (isSubRoutine())
			return true;

		if (ForInAction.isAction(getDefinition()))
			return true;

		if (ForEachLineAction.isAction(getDefinition()))
			return true;

		if (CompositeBlockAction.isAction(getDefinition()))
			return true;

		return false;
	}

	public boolean isCompositeBlock() {
		if (getDefinition() == null)
			return false;

		return CompositeBlockAction.isAction(getDefinition());
	}

	public void setEventListener(IEventListener listener)
	{
		mEventListener = listener;
	}

	public IEventListener getEventListener()
	{
		return mEventListener;
	}


	//helper methods
	public boolean hasRefresh()
	{
		for (Action action : mActions)
		{
			if (action != null && action instanceof ApiAction)
			{
				ApiAction apiAction = (ApiAction) action;
				if (apiAction.isRefreshAction())
				{
					return true;
				}
			}
		}
		return false;
	}

	public boolean hasControlAction(String controlName, String methodName)
	{
		for (Action action : mActions)
		{
			if (action != null && action instanceof ControlMethodAction)
			{
				ControlMethodAction controlMethodAction = (ControlMethodAction) action;
				if (controlMethodAction.isActionForControlMethod(controlName, methodName))
				{
					return true;
				}
			}
		}
		return false;
	}

	public boolean isEmpty()
	{
		if (mActions.isEmpty())
			return true;
		if (mActions.size()==1 && mActions.get(0) instanceof EmptyAction)
			return true;
		return false;
	}
}
