package com.genexus.android.core.actions;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.genexus.android.core.activities.ActivityHelper;
import com.genexus.android.layout.OrientationLock;
import com.genexus.android.core.base.application.MessageLevel;
import com.genexus.android.core.base.application.OutputMessage;
import com.genexus.android.core.base.application.OutputResult;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.enums.RequestCodes;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.common.SecurityHelper;
import com.genexus.android.core.controls.ProgressDialogFactory;
import com.genexus.android.core.utils.Cast;
import com.genexus.android.core.utils.TaskRunner;

public class ActionExecution
{
	private final Action mAction;
	private ActionResult mContinuationResult = null;

	private static ActionExecution sCurrent;
	private static final ArrayList<CompositeAction> STACK_PENDING = new ArrayList<>();

	public ActionExecution(Action action)
	{
		mAction = action;

		CompositeAction previous = getCurrentCompositeAction();

		// When is the previous action pushed into the stack?
		// 1) when there are more actions afterwards (e.g. msg(), call(), msg()), or.
		// 2) when the last executed action expects result data (e.g. call() with output parameters).
		if (previous != null && !previous.equals(mAction) && (!previous.isDone() || previous.catchOnActivityResult()))
		{
			if (!STACK_PENDING.contains(previous))
			{
				//logging.
				// Services.Log.debug("ActionExecution", "create add previous to pending " + previous.toString());
				STACK_PENDING.add(previous);
			}
		}

		//logging.
		// Services.Log.debug("ActionExecution", "create current action " + action.toString());
		sCurrent = this;
	}

	public static ActionResult continueCurrent(Activity currentActivity, boolean continueFromPendings, Action actionToContinue)
	{
		return continueCurrent(RequestCodes.ACTION, Activity.RESULT_OK, null, currentActivity, continueFromPendings, actionToContinue);
	}

	private static ActionResult continueCurrent(int requestCode, int resultCode, Intent resultData, Activity currentActivity, boolean continueFromPendings, Action actionToContinue)
	{
		fixCurrentExecution(continueFromPendings, actionToContinue);

		if (sCurrent != null)
			return sCurrent.continueAction(requestCode, resultCode, resultData, currentActivity, continueFromPendings);
		else
			return ActionResult.SUCCESS_CONTINUE;
	}

	private static void fixCurrentExecution(boolean continueFromPendings, Action actionToContinue)
	{
		if (sCurrent == null && continueFromPendings)
		{
			Services.Log.error("ActionExecution , sCurrent = null, continueCurrent not called and continueFromPendings = true");
			// should create a sCurrent with last pending and run with this?
		}
		if (sCurrent == null && !continueFromPendings)
		{
			Services.Log.warning("ActionExecution , sCurrent = null, continueCurrent not called and continueFromPendings = false");
		}
		if (sCurrent == null && actionToContinue != null)
		{
			Services.Log.info("ActionExecution , sCurrent = null, and has actionToContinue. Put actionToContinue as sCurrent");
			// continue this composite action if its in the stack
			CompositeAction compositeToContinue = actionToContinue.getParentComposite();
			if (STACK_PENDING.contains(compositeToContinue))
			{
				Services.Log.info("ActionExecution , find actionToCancel in StackPending. Set as sCurrent");
				//Services.Log.info("ActionExecution fixCurrentExecution REMOVE, " + compositeToContinue.getDefinition().getName());
				STACK_PENDING.remove(compositeToContinue);
				// Revert sCurrent to the correct action.
				ActionExecution exec = new ActionExecution(compositeToContinue);
				setCurrentActionExecution(exec);
			}
		}
	}

	public static ActionResult continueCurrentFromActivityResult(Activity activity, int requestCode, int resultCode, Intent resultData)
	{
		return continueCurrent(requestCode, resultCode, resultData, activity, true, null);
	}

	public static ActionResult continueCurrentFromActivityResultOnlyLeavingActivityOrCleanCurrent(Activity activity, int requestCode, int resultCode, Intent resultData)
	{
		if (sCurrent !=null)
		{
			Action action = ((CompositeAction) sCurrent.getAction()).getCurrentActionExecuted();
			Activity leavingActivity = null;
			// ReturnTo Action.
			if (action instanceof ApiAction) {
				ApiAction apiAction = (ApiAction) action;
				leavingActivity = apiAction.getActivity();
			}

			CompositeAction compositeToContinue = getLastPendingAction();

			if (compositeToContinue.getActivity().equals(leavingActivity)) {
				// continue current action from caller activity
				return continueCurrent(requestCode, resultCode, resultData, activity, true, null);
			}
		}
		// default case cancel with not current.
		ActionExecution.cleanCurrentOrLastPendingActionFromActivityResult(requestCode, resultCode, resultData, activity);
		//ActionExecution.cleanLastPendingActionFromActivityResult(requestCode, resultCode, resultData, activity);
		return ActionResult.SUCCESS_CONTINUE;

	}

	public static void continueCurrentFromRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults, Action actionToContinue)
	{
		fixCurrentExecution(true, actionToContinue);

		if (sCurrent != null)
			sCurrent.continueFromRequestPermissionsResult(activity, requestCode, permissions, grantResults);
		else
			Services.Log.warning("ActionExecution Received onRequestPermissionsResult (" + Arrays.toString(permissions) + "), but we didn't know what to continue!");
	}

	private void continueFromRequestPermissionsResult(final Activity activity, final int requestCode, final String[] permissions, final int[] grantResults)
	{
		TaskRunner.execute(new TaskRunner.BaseTask<ActionResult>()
		{
			@Override
			public ActionResult doInBackground() {
				return mAction.afterRequestPermissionsResult(requestCode, permissions, grantResults);
			}

			@Override
			public void onPostExecute(ActionResult result) {
				// If the permission was enough to complete the action, continue, otherwise wait (or abort).
				if (result != ActionResult.SUCCESS_WAIT)
				{
					// After the permission, we are good to go. Continue.
					continueActionLocal(requestCode, Activity.RESULT_OK, null, activity, true);
				}
			}
		});
	}

	public static void cancelCurrent(Action actionToCancel)
	{
		if (sCurrent == null && actionToCancel != null)
		{
			Services.Log.info("ActionExecution , sCurrent = null, and has actionToCancel. Put actionToCancel as sCurrent");
			// continue this composite action if its in the stack
			CompositeAction compositeToContinue = actionToCancel.getParentComposite();
			if (STACK_PENDING.contains(compositeToContinue))
			{
				Services.Log.info("ActionExecution , find actionToCancel in StackPending. Set as sCurrent");
				//Services.Log.info("ActionExecution cancelCurrent REMOVE, " + compositeToContinue.getDefinition().getName());
				STACK_PENDING.remove(compositeToContinue);
				// Revert sCurrent to the correct action.
				ActionExecution exec = new ActionExecution(compositeToContinue);
				setCurrentActionExecution(exec);
			}
		}
		if (sCurrent != null)
			cleanCurrentPendingAsDone();
	}

	public static boolean currentCatchOnActivityResult()
	{
		CompositeAction action = getCurrentCompositeAction();
		if (action != null)
			return action.catchOnActivityResult();
		return false;
	}

	public static CompositeAction getCurrentCompositeAction()
	{
		if (sCurrent != null)
			return Cast.as(CompositeAction.class, sCurrent.getAction());
		else
			return null;
	}

	private static void setCurrentActionExecution(ActionExecution myCurrent)
	{
		//if (myCurrent==null)
		//	Services.Log.debug("ActionExecution, setCurrentActionExecution null");
		//else
		//	Services.Log.debug("ActionExecution, setCurrentActionExecution " + myCurrent.toString());

		sCurrent = myCurrent;
	}

	public static void cleanCurrentOrLastPendingActionFromActivityResult(int requestCode, int resultCode, Intent resultData, Activity currentActivity)
	{
		// The unique action that continue on activity result not OK is scan barcode loop
		CompositeAction compositeInner = getCurrentCompositeAction();
		ApiAction apiAction = null;
		if (compositeInner != null)
		{
			Action action = compositeInner.getCurrentActionExecuted();

			//Return action also continue on activity not ok, special case, for example on activity redirect (dynamic call).
			if (action instanceof ApiAction)
			{
				apiAction = (ApiAction)action;
				if (apiAction.isReturnAction())
				{
					// if Return Action, finish current event
					// Services.Log.info("ActionExecution", "onPostExecute finishCurrentEvent for return action " + compositeInner.toString());
					if (ActionExecution.sCurrent != null)
					{
						ActionExecution.sCurrent.finishCurrentEventAndSetPreviousAsCurrent(action, compositeInner, apiAction.finishReturnRequestCode, apiAction.finishReturnResultCode, apiAction.finishReturnResult, apiAction.finishReturnCurrentActivity);
					}
				}
			}
		}

		// call Activity result with resultCode fail and then fail if necessary
		// Services.Log.debug("ActionExecution cleanCurrentOrLastPendingActionFromActivityResult afterActivityResult " );
		ActionResult result = ActionResult.SUCCESS_CONTINUE;
		if (sCurrent != null)
		{
			result = sCurrent.getAction().afterActivityResult(requestCode, resultCode, resultData);
			// if sCurrent is Cancel action should notify last pending executed action of activity result
			if ((apiAction != null && apiAction.isCancelAction()))
			{
				CompositeAction pendingAction = getLastPendingAction();
				if (pendingAction != null)
				{
					result = pendingAction.afterActivityResult(requestCode, resultCode, resultData);
				}
			}
		}
		else
		{
			Services.Log.warning("ActionExecution , sCurrent = null, action afterActivityResult not called");
			// should create a sCurrent with last pending and run afterActivityResult with this, just to notify.
			CompositeAction pendingAction = getLastPendingAction();
			if (pendingAction != null)
			{
				result = pendingAction.afterActivityResult(requestCode, resultCode, resultData);
			}
		}

		// Only don't fail if ActionResult is REPEAT, same as OnAction Continuation
		if (result != ActionResult.SUCCESS_WAIT)
		{
			//finish this action unsuccessfully
			cleanCurrentPendingAsDone();

			//	Clean last pending action or if cancel set fail called event.
			if (compositeInner == null || (apiAction != null && apiAction.isCancelAction()))
			{
				removeLastPendingEventFromPendings();
			}
		}

	}

	public static void cleanLastPendingActionFromActivityResult(int requestCode, int resultCode, Intent resultData, Activity currentActivity)
	{
		Services.Log.debug("cleanLastPendingActionFromActivityResult");
		ActionResult result = ActionResult.SUCCESS_CONTINUE;
		// create a sCurrent with last pending and run afterActivityResult with this, just to notify.
		CompositeAction pendingAction = getLastPendingAction();
		if (pendingAction != null)
		{
			result = pendingAction.afterActivityResult(requestCode, resultCode, resultData);
		}

		// Only don't fail if ActionResult is REPEAT, same as OnAction Continuation
		if (result != ActionResult.SUCCESS_WAIT)
		{
			//do not modify current event !

			//	Clean last pending action or if cancel set fail called event.
			removeLastPendingEventFromPendings();
		}
	}

	private static CompositeAction getLastPendingAction()
	{
		if (STACK_PENDING.size() > 0)
		{
			CompositeAction pendingAction = STACK_PENDING.get(STACK_PENDING.size() - 1);
			return pendingAction;
		}
		return null;
	}

	private static void removeLastPendingEventFromPendings()
	{
		//	TODO: should only clean one level of Pending actions on stack?
		//	STACK_PENDING.clear();
		if (STACK_PENDING.size() > 0)
		{
			CompositeAction pendingAction = STACK_PENDING.get(STACK_PENDING.size() - 1);
			//Services.Log.info("ActionExecution removeLastPendingEventFromPendings REMOVE, " + pendingAction.getDefinition().getName());
			STACK_PENDING.remove(pendingAction);

			//logging.
			//Services.Log.debug("ActionExecution", "cleanCurrentOrLastPendingAction cancel action remove from pending " + pendingAction.toString());
			Services.Log.debug("ActionExecution", "cleanCurrentOrLastPendingAction cancel action remove from pending ");

			//finish this action unsuccessfully
			pendingAction.setCurrentActionFail(true);

			// Event ended unsuccessfully. , set sCurrent to null
			onEndEvent(pendingAction, false);
		}
	}

	public static void cleanCurrentPendingAsDone()
	{
		//set current as done.
		CompositeAction compositeInner = getCurrentCompositeAction();
		if (compositeInner != null)
		{
			// finish this action unsuccessfully
			compositeInner.setCurrentActionFail(true);

			// Event ended unsuccessfully. , set sCurrent to null
			onEndEvent(compositeInner, false);
		}
	}

	public void executeAction()
	{
		Action.ThreadPreference threadPreference = mAction.getThreadPreference();
		if (threadPreference == Action.ThreadPreference.MAIN_THREAD)
		{
			beforeExecuteAction();
			mAction.Do();
			afterExecuteAction();
		}
		else
		{
			TaskRunner.execute(new TaskRunner.BaseTask<Void>() {
				@Override
				public void onPreExecute()
				{
					beforeExecuteAction();
				}

				@Override
				public Void doInBackground()
				{
					// Services.Log.debug("ActionExecution executeAction Do " + mAction.toString());
					mAction.Do();
					return null;
				}

				@Override
				public void onPostExecute(Void result)
				{
					// Services.Log.debug("ActionExecution executeAction onPostExecute " + mAction.toString());
					afterExecuteAction();
				}
			});
		}
	}

	// Call when composite action is done.
	public static void onEndEventAsDone(CompositeAction composite, boolean success)
	{
		// if a sub ending, do continue, not end event.
		// check if its a sub and if should continue. Is current action composite Do?
		boolean isNestedComposite = composite.isNested();
		// check if should continue after nested?
		if (isNestedComposite && sCurrent !=null)
		{
			Action action = composite.getCurrentActionExecuted();
			if (action != null && action instanceof ApiAction)
			{
				ApiAction apiAction = (ApiAction) action;
				if (apiAction.isSubDoAction())
				{
					// could be : continueCurrent(Activity ?, boolean true, Action composite)
					// end in the call from subrutine to continueExecNextActionFromPendings
					sCurrent.continueExecNextAction(composite, RequestCodes.ACTION, Activity.RESULT_OK, null, null, false);
					return;
				}
			}
		}
		// occurs in spacial case, for sub when sCurrent in null (parallel execution)
		onEndEvent(composite, success);
	}

	/**
	 * Called whenever an user event ends.
	 *
	 * @param success True if composite ended successfully; false if it was interrupted.
	 */
	private static void onEndEvent(CompositeAction event, boolean success)
	{
		//logging.
		if (event.getDefinition() != null)
			Services.Log.debug("ActionExecution", "onEndEvent " + " , "+ event.getDefinition().getName());
		else
			Services.Log.debug("ActionExecution", "onEndEvent"); // DynamicCallAction, call of panel in start event

		event.setAsEnded();

		// Notify ProgressIndicator so it can be removed if not done so already.
		ProgressDialogFactory.onEndEvent(event, success);

		// Activity orientation was locked to prevent rotation during action, can be unlocked now.
		OrientationLock.unlock(event.getActivity(), OrientationLock.REASON_RUN_EVENT);

		// sCurrent event is finished, remove its reference
		setCurrentActionExecution(null);

		if (event.getEventListener() != null)
			event.getEventListener().onEndEvent(event, success);
	}

	private void afterExecuteAction()
	{
		// Handle composite action
		// Services.Log.debug("ActionExecution afterExecuteAction " + mAction.toString());
		if (!mAction.catchOnActivityResult() && mAction instanceof CompositeAction)
		{
			CompositeAction composite = (CompositeAction)mAction;
			boolean continueAction = true;
			if (composite.getCurrentActionExecuted() != null)
			{
				// Services.Log.debug("onPostExecute", "handlePostExecutedSingleAction " + composite.getCurrentActionExecuted().toString());
				continueAction = handlePostExecutedSingleAction(composite.getCurrentActionExecuted());
			}

			if (continueAction)
			{
				// Services.Log.info("onPostExecute", "Continue exec onpost action " + composite.toString());
				continueExecNextAction(composite, RequestCodes.ACTION, Activity.RESULT_OK, null, null, false);
			}
			else
			{

				Action action = composite.getCurrentActionExecuted();
				if (action instanceof ApiAction)
				{
					ApiAction apiAction = (ApiAction)action;
					if (apiAction.isReturnAction())
					{
						// if Return Action, finish current event
						// Services.Log.info("onPostExecute", "finishCurrentEvent for return action " + composite.toString());
						finishCurrentEventAndSetPreviousAsCurrent(action, composite, apiAction.finishReturnRequestCode, apiAction.finishReturnResultCode, apiAction.finishReturnResult, apiAction.finishReturnCurrentActivity);
					}
				}
			}
			return;
		}

		handlePostExecutedSingleAction(mAction);
	}

	private void finishCurrentEventAndSetPreviousAsCurrent(Action action, CompositeAction composite, int requestCode, int resultCode, Intent result, Activity currentActivity)
	{
		// This method is called twice, one when return is finished , one when return to caller activity
		// The order is not always the same, so finish the event the second time.
		if (action instanceof ApiAction)
		{
			ApiAction apiAction = (ApiAction)action;
			if (apiAction.isReturnAction() && apiAction.finishReturn)
			{
				finishCurrentEventAndStartPreviousAsCurrent(composite, requestCode, resultCode, result, currentActivity);
			}
			else if (apiAction.isReturnAction())
			{
				// Services.Log.info("finishCurrentEventAndSetPreviousAsCurrent", "set return as done " + composite.toString());
				apiAction.finishReturn = true;
				apiAction.finishReturnRequestCode = requestCode;
				apiAction.finishReturnResultCode = resultCode;
				apiAction.finishReturnResult = result;
				apiAction.finishReturnCurrentActivity = currentActivity;
			}
		}
	}

	private void finishCurrentEventAndStartPreviousAsCurrent(CompositeAction composite, int requestCode, int resultCode, Intent result, Activity currentActivity) {
		Services.Log.info("finishCurrentEventAndSetPreviousAsCurrent", "call next event after return ");
		composite.setAsDone();

		// check if it's nested
		boolean isNestedComposite = composite.isNested();

		if (composite.isDone() && !isNestedComposite)
			onEndEvent(composite, !composite.isCurrentActionFail());

		// return in a nested action, should cancel this and previous event (caller).
		if (isNestedComposite)
		{
			// sCurrent event is finished, remove its reference
			setCurrentActionExecution(null);
			removeLastPendingEventFromPendings();

			if (composite.isSubRoutine())
				Services.Log.debug("finishing2 sub " + composite.getDefinition().getName());
		}

		if (!composite.isCurrentActionFail())
		{
			// if sub rutine pass composite as current?.
			continueExecNextActionFromPendings(requestCode, resultCode, result, currentActivity);
		}
	}

	private boolean handlePostExecutedSingleAction(Action action)
	{
		if (!action.catchOnActivityResult())
		{
			// Handle action with output (procedure, multicall, BC).
			if (action instanceof IActionWithOutput)
				handleActionOutput((IActionWithOutput)action);

			// Handle Login action
			if (action instanceof CallLoginAction)
				handleLoginOutputMessage(action.getContext(), ((CallLoginAction)action).getErrorMessage());

			//handle Login External action
			if (action instanceof CallLoginExternalAction)
				handleLoginOutputMessage(action.getContext(), ((CallLoginExternalAction)action).getErrorMessage());

			// Handle return continuation, refresh
			if (action instanceof ApiAction)
			{
				ApiAction apiAction = (ApiAction)action;
				if (apiAction.isReturnAction())
				{
					if (mAction instanceof CompositeAction)
					{
						return false;
						// if return action not continue event.
					}
				}
			}
		}

		// If the action has finished the activity, then the current event is done too.
		if (action.isActivityEnding())
			setCurrentActionExecution(null);

		return true;
	}

	/**
	 * Handles action output (shows messages if necessary).
	 *
	 * @param action Action just executed.
	 * @return True if action execution should stop after this; otherwise false.
	 */
	private static boolean handleActionOutput(IActionWithOutput action)
	{
		OutputResult output = action.getOutput();
		if (output != null)
		{
			// We cannot show messages without the app UI (actually, we COULD show toasts,
			// but from the user's perspective it makes no sense to do so from background).
			if (ActivityHelper.getCurrentActivity() == null)
				return !output.isOk();

			// If error message is "token expired", redirect to login.
			if (SecurityHelper.handleSecurityError(action.getContext(), output.getStatusCode(), output.getErrorText(), null) != SecurityHelper.Handled.NOT_HANDLED)
				return true;

			if (((Action) action).getParentComposite().getIsComposite() &&
				!((Action) action).getParentComposite().isCompositeBlock()) { // only show messages if it is the root composite
				// No special messages, just show them.
				int index = 0;
				for (OutputMessage message : output.getMessages()) {
					if (message.getLevel() == MessageLevel.ERROR && action.getActivity() != null)
						Services.Messages.showErrorDialog(action.getActivity(), message.getText());
					else
						Services.Messages.showMessage(action.getActivity(), message.getText());
					index++;
					// if already show 50 message, ignore the rest. has no sense and could crash the app.
					if (index > 50)
						break;
				}
			}
		}

		return false;
	}

	private static void handleLoginOutputMessage(Context actionContext, String errorMessage)
	{
		if (actionContext != null && Services.Strings.hasValue(errorMessage))
		{
			//Show login output messages
			Services.Messages.showErrorDialog(actionContext, errorMessage);
		}
	}

	private void beforeExecuteAction()
	{
		//handle composite action
		if (mAction instanceof CompositeAction)
		{
			CompositeAction composite = (CompositeAction) mAction;
			Action action = composite == null ? null : composite.getNextActionToExecute();
			if (action != null && composite.mCurrentActivity != null)
			{
				action.mCurrentActivity = composite.mCurrentActivity;
			}
		}
	}

	private Action getAction()
	{
		return mAction;
	}

	private ActionResult continueAction(int requestCode, int resultCode, Intent resultData, Activity currentActivity, boolean continueFromPendings)
	{
		// Continue action from onActivityResult().
		// Services.Log.debug("ActionExecution continueAction afterActivityResult mAction" + mAction.toString());
		ActionResult result = mAction.afterActivityResult(requestCode, resultCode, resultData);

		if (result != ActionResult.SUCCESS_WAIT)
		{
			// Go ahead with next action, if any.
			continueActionLocal(requestCode, resultCode, resultData, currentActivity, continueFromPendings);

			// Hack. The mAction above is not the correct one when returning to an activity.
			if (mContinuationResult == ActionResult.SUCCESS_CONTINUE_NO_REFRESH)
				result = ActionResult.SUCCESS_CONTINUE_NO_REFRESH;
		}

		mContinuationResult = null;
		return result;
	}

	private void continueActionLocal(int requestCode, int resultCode, Intent result, Activity currentActivity, boolean continueFromPendings)
	{
		if (mAction instanceof CompositeAction)
		{
			CompositeAction composite = (CompositeAction)mAction;
			if (composite.catchOnActivityResult())
			{
				// Services.Log.info("continueAction", "Continue exec action "+ composite.catchOnActivityResult() + composite.toString());
				continueExecNextAction(composite, requestCode, resultCode, result, currentActivity, continueFromPendings);
			}
			else
			{
				mAction.mCurrentActivity = currentActivity;
				// Services.Log.warning("continueAction", "Not Continue exec action not catch on activity result"+ composite.toString());


				if (composite.getCurrentActionExecuted() != null)
				{
					// if Return Action, finish current event
					// Services.Log.info("continueAction", "finishCurrentEventAndSetPreviousAsCurrent "+ composite.catchOnActivityResult() + composite.toString());
					finishCurrentEventAndSetPreviousAsCurrent(composite.getCurrentActionExecuted(), composite, requestCode, resultCode, result, currentActivity);
				}
			}
		}
		else
		{
			if (STACK_PENDING.size() > 0 && continueFromPendings)
				continueExecNextActionFromPendings(requestCode, resultCode, result, currentActivity);
		}
	}

	private void continueExecNextAction(CompositeAction composite, int requestCode, int resultCode, Intent result, Activity currentActivity, boolean continueFromPendings)
	{
		if (!composite.isDone())
		{
			// composite is equals to mAction. (from its callers)
			ActionExecution exec = new ActionExecution(mAction);
			setCurrentActionExecution(exec);
			if (currentActivity != null)
				mAction.mCurrentActivity = currentActivity;
			//exec.setAction(mAction);
			// Services.Log.info("continueExecNextAction", "exec action " + composite.toString());
			exec.executeAction();
		}
		else
		{
			boolean isNestedComposite = composite.isNested();

			// if current action fail, already called "OnEndEvent", so not call it again.
			if (!composite.isCurrentActionFail()) {
				if (isNestedComposite)
					// sCurrent event is finished, remove its reference
					setCurrentActionExecution(null);
				else {
					onEndEvent(composite, !composite.isCurrentActionFail());
				}
			}

			if (isNestedComposite) {
				continueFromPendings = true;
				if (composite.isSubRoutine())
					Services.Log.debug("finishing sub " + composite.getDefinition().getName());
			}

			// Not continue action from pendings in an end of a event.
			// Only when return to caller activity

			// if it is nested, continue the caller event
			// find the caller events in pendings to use it. Could not be the sCurrent.
			// only if it is nested.
			if (!composite.isCurrentActionFail() && continueFromPendings) {
				if (isNestedComposite) {
					// should do something like fixCurrentExecution, to find current to execute
					// pass composite and use it for now
					continueExecNextActionFromPendings(requestCode, resultCode, result, currentActivity, composite);
				} else {
					continueExecNextActionFromPendings(requestCode, resultCode, result, currentActivity);
				}
			}
			else if (composite.isCurrentActionFail() && isNestedComposite) {
				if (CompositeBlockAction.isAction(composite.getDefinition()) || composite.isSubRoutine()) {
					if (STACK_PENDING.size() > 0) {
						CompositeAction pendingAction = STACK_PENDING.get(STACK_PENDING.size() - 1);
						STACK_PENDING.remove(pendingAction);
						if (pendingAction.getIsComposite()) {
							pendingAction.setCurrentActionFail(true);
							continueExecNextAction(pendingAction, requestCode, resultCode, result, currentActivity, true);
						} else {
							ActionExecution exec = new ActionExecution(pendingAction);
							setCurrentActionExecution(exec);
							if (currentActivity != null)
								pendingAction.mCurrentActivity = currentActivity;
							exec.executeAction();
						}
					}
				} else {
					removeLastPendingEventFromPendings();
				}
			}
			else if (STACK_PENDING.contains(composite) && composite.isDone() && !isNestedComposite
					&& !composite.isCurrentActionFail() && !composite.catchOnActivityResult())
			{
				// Sanity, cleanup Done Composite Actions from pendings if already executed complete.
				//Services.Log.info("ActionExecution continueExecNextAction REMOVE, " + composite.getDefinition().getName());
				STACK_PENDING.remove(composite);
			}
		}
	}

	private void continueExecNextActionFromPendings(int requestCode, int resultCode, Intent result, Activity currentActivity, CompositeAction subToContinue)
	{
		CompositeAction compositeToContinue = subToContinue.getCallerEventComposite();
		if (compositeToContinue==null)
		{
			// Coult be null now.
			//Services.Log.Error("parent sub composite null");
		}
		if (compositeToContinue!=null && STACK_PENDING.contains(compositeToContinue))
		{
			Services.Log.info("ActionExecution, find actionToContinue in StackPending. " + compositeToContinue.getDefinition().getName());
			//Services.Log.debug("ActionExecution2 continue from pendings size " + STACK_PENDING.size());

			if (STACK_PENDING.size() > 0)
			{
				CompositeAction pendingAction = STACK_PENDING.get(STACK_PENDING.size() - 1);
				if (!pendingAction.getDefinition().getName().equalsIgnoreCase(compositeToContinue.getDefinition().getName()))
				{
					Services.Log.warning("wrong event to continue from sub : " + subToContinue.getDefinition().getName() + " to  " +
							pendingAction.getDefinition().getName() + " should be " + compositeToContinue.getDefinition().getName());
					//Services.Log.Error("wrong event to continue from: " + subToContinue.getDefinition().getName() + " to  " +
					//	pendingAction.isDone() + " should be " + compositeToContinue.isDone());
				}
			}

			// Call the correct event to continue
			STACK_PENDING.remove(compositeToContinue);
			continuePendingAction(requestCode, resultCode, result, currentActivity, compositeToContinue);
		}
		else
		{
			continueExecNextActionFromPendings(requestCode, resultCode, result, currentActivity);
		}
	}

	private void continueExecNextActionFromPendings(int requestCode, int resultCode, Intent result, Activity currentActivity)
	{
		// If current run if done, check for pending ones
		if (STACK_PENDING.size() > 0)
		{
			CompositeAction pendingAction = STACK_PENDING.get(STACK_PENDING.size() - 1);
			STACK_PENDING.remove(pendingAction);

			//Services.Log.info("ActionExecution , continue action STANDARD way " + pendingAction.getDefinition().getName());
			continuePendingAction(requestCode, resultCode, result, currentActivity, pendingAction);
		}
	}

	private void continuePendingAction(int requestCode, int resultCode, Intent result, Activity currentActivity, CompositeAction pendingAction)
	{
		//logging.
		//Services.Log.debug("continueExecNextActionFromPendings", "continue from pendings size " + STACK_PENDING.size());
		Services.Log.debug("continueExecNextActionFromPendings", "continue from pendings action " + pendingAction.getDefinition().getName());

		// Notify last pending executed action of activity result, notify on return ok.
		// Services.Log.debug("continueExecNextActionFromPendings pendingAction" + pendingAction.toString() );
		// Services.Log.debug("intent is null " + (result==null));
		if (result != null && pendingAction.catchOnActivityResult())
		{
			Services.Log.debug("continueExecNextActionFromPendings result" + result.toString() );
			mContinuationResult = pendingAction.afterActivityResult(requestCode, resultCode, result);
		}

		// Run pending action.
		pendingAction.mCurrentActivity = currentActivity;
		ActionExecution exec = new ActionExecution(pendingAction);
		setCurrentActionExecution(exec);
		exec.executeAction();
	}

	public static boolean isEventRunning(String eventName)
	{
		CompositeAction current = getCurrentCompositeAction();
		Services.Log.debug("isEventRunning by name" + eventName);
		if (current != null)
		{
			Services.Log.debug("isEventRunning by name current " + current.getDefinition().getName() );
			if (current.getDefinition().getName().equalsIgnoreCase(eventName)
				&& !current.isEnded())
				return true;
		}
		return false;
	}

	// check if this event is already running
	public static boolean isEventRunning(final ActionDefinition event, UIContext context, ActionParameters parameters)
	{
		CompositeAction current = getCurrentCompositeAction();
		// not current event.
		if (current == null) {
			Services.Log.debug("isEventRunning current=null");
			return false;
		}

		// is subroutine , this is not the event, should find the parent
		if (current.isSubRoutine())
		{
			Services.Log.debug("isEventRunning. Event running is sub: " + current.getDefinition().getName() );
			// find parent
			current = getEventFromQueued(event);
			if (current==null){
				Services.Log.debug("isEventRunning sub parent not found null");
				return false;
			}
			Services.Log.debug("isEventRunning. Event running sub parent: " + current.getDefinition().getName() );
		}

		// is the same event
		if (current.getDefinition() == event)
		{
			Services.Log.debug("isEventRunning. Event is already running: " + event.getName() + " ." );
			// event already finished
			// use isEnded() instead of isDone() because if there is a Panel call at the end of the event,
			// the Panel is launched and isDone() is true but there is a second when the user can still press the button
			// isEnded() is only true when the Panel returns
			if (current.isEnded()) {
				Services.Log.debug("isEventRunning. isEventRunning isEnded");
				return false;
			}

			// not the same activity
			if (current.getContext().getActivity() != context.getActivity()) {
				Services.Log.debug("isEventRunning. different Activity");
				return false;
			}

			// not the same entity parameter
			if (current.getParameterEntity() != parameters.getEntity())
			{
				// root entity could be change for a refresh, check that
				if (current.getParameterEntity()==null || parameters.getEntity()==null)
				{
					Services.Log.debug("isEventRunning. different Entity and null");
					return false;
				}
				// if entity has change, but is root one, allow this difference
				if (current.getParameterEntity().getParentInfo()!=null
						&& current.getParameterEntity().getParentInfo().getParent()==null)
				{
					Services.Log.debug("isEventRunning.  different Entity but parent null");
					return true;
				}
				Services.Log.debug("isEventRunning. different grid/line Entity");
				return false;
			}

			// if all above test pass , is the same event/context/activity
			return true;
		}
		Services.Log.debug(String.format("isEventRunning. different Action %s %s", current.getDefinition().getName(), event.getName()));
		return false;
	}

	private static CompositeAction getEventFromQueued(final ActionDefinition event)
	{
		int pendingSize = STACK_PENDING.size();
		if (pendingSize > 0)
		{
			for (int index = pendingSize - 1; index>=0; index--)
			{
				CompositeAction pendingAction = STACK_PENDING.get(index);
				if (pendingAction.getDefinition() == event)
					return pendingAction;
			}
		}
		return null;
	}
}
