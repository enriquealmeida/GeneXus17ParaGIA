package com.genexus.android.core.activities;

import android.app.Activity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.genexus.android.core.base.metadata.IViewDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.fragments.IDataView;
import com.genexus.android.core.fragments.IInspectableComponent;
import com.genexus.android.core.fragments.LayoutFragment;
import com.genexus.android.core.ui.navigation.INavigationActivity;

public class ActivityFlowControl
{
	private static String sObjectToReturn;
	private static Integer sObjectToReturnResult;

	// different result for different return modes.
	static final int RETURN_TO_FIRST = 1235234;
	static final int RETURN_TO_OTHERS = 1235235;
	static final int RETURN_TO_CANCEL = 1235236;

	public static void returnTo(final Activity fromActivity, final IDataView fromComponent, final String toObject)
	{
		if (returnToFromDialog(fromActivity, fromComponent, toObject)) return; // Done

		// Store the name of the object we want to return to, and finish the current activity.
		sObjectToReturn = toObject;
		// keep retuning ignoring events.
		sObjectToReturnResult = RETURN_TO_OTHERS;
		// return another code to continue action in the first return like return does (if pending is from caller activity)
		// only if pending activity is the same as ActionAPI activity.
		// and another code in the rest of the return activities? to do nothing.
		fromActivity.setResult(RETURN_TO_FIRST);
		fromActivity.finish();
	}

	public static void cancelTo(final Activity fromActivity, final IDataView fromComponent, final String toObject)
	{
		if (returnToFromDialog(fromActivity, fromComponent, toObject)) return; // Done

		// Store the name of the object we want to return to, and finish the current activity.
		sObjectToReturn = toObject;
		// return code fail, like now to cancel action being executed.
		fromActivity.setResult(RETURN_TO_CANCEL);
		fromActivity.finish();
	}

	public static boolean returnToFromDialog(Activity fromActivity, IDataView fromComponent, String toObject) {
		if (fromComponent instanceof DialogFragment && isShownInDialog((DialogFragment) fromComponent)) {
			// We're running "ReturnTo" from a Dialog.
			// As a special case, see if we're actually returning to the containing activity.
			IViewDefinition activityObject = getActivityDefinition(fromActivity);
			if (isObject(activityObject, toObject)) {
				Services.Device.invokeOnUiThread(new Runnable() {
					@Override
					public void run() {
						dismissDialog((DialogFragment) fromComponent);
					}
				});

				return true;
			}
		}
		return false;
	}


	public static void finishWithReturn(Activity activity)
	{
		if (activity.getParent() != null)
		{
			Activity parentActivity = activity.getParent();
			setReturnResult(parentActivity);
		}

		setReturnResult(activity);
		activity.finish();
	}

	private static void setReturnResult(Activity activity)
	{
		if (activity instanceof IGxActivity)
			((IGxActivity)activity).setReturnResult();
		else
			activity.setResult(Activity.RESULT_OK);
	}

	public static void finishWithCancel(Activity activity)
	{
		// No output to be returned, just set result and finish.
		activity.setResult(Activity.RESULT_CANCELED);
		activity.finish();
	}

	/**
	 * Called after an activity resumes to handle flow, if applicable (e.g. closing it because
	 * we were asked to return to a previous point).
	 * @return True if the activity will continue; false if it is finished.
	 */
	static boolean onResume(Activity activity)
	{
		if (sObjectToReturn == null)
			return true; // Nothing special to do.

		// We have a pending "ReturnTo" operation.
		// Either we're returning to this activity, or it should be closed. See below.

		if (activity.getParent() != null)
			return true; // Don't do anything with children activities.

		// Also consider as "topmost" the currently displayed popup/callout, if any.
		IViewDefinition activityObject = getActivityDefinition(activity);
		DialogFragment currentDialog = findDialog(activity);
		IViewDefinition popupObject = getFragmentDefinition(currentDialog);
		IViewDefinition activityObjectSlideContent = getActivityDefinitionSlide(activity);

		// Possible cases:
		if (isObject(popupObject, sObjectToReturn))
		{
			// 1) Returning to the popup itself -> we're done.
			clearReturnToState();
			return true;
		}
		else if (isObject(activityObject, sObjectToReturn))
		{
			// 2) Returning to this activity. We're basically done.
			// But if it has a popup, then close it first.
			if (currentDialog != null)
				dismissDialog(currentDialog);

			clearReturnToState();
			return true;
		}
		//
		else if (activityObjectSlideContent!=null && isObject(activityObjectSlideContent, sObjectToReturn))
		{
			// return to panel child in content part of it.
			// 2.1) return to this activity in slide content. We're basically done.

			// But if it has a popup, then close it first.
			if (currentDialog != null)
				dismissDialog(currentDialog);

			clearReturnToState();
			return true;
		}

		else if (activityObject == null)
		{
			// 3) We've gotten lost (e.g. activity with no definition?) -> stop here.
			clearReturnToState();
			return true;
		}
		else if (activity.isTaskRoot())
		{
			//other case where in the root , stop here
			// 4) Cannot return beyond main. Stop here.
			clearReturnToState();
			return true;
		}
		else
		{
			// 5) We're not returning here -> close the activity and keep returning.
			if (sObjectToReturnResult!=null)
				activity.setResult(sObjectToReturnResult);
			activity.finish();
			return false;
		}

	}

	private static void clearReturnToState()
	{
		sObjectToReturn = null;
		sObjectToReturnResult = null;
	}

	private static DialogFragment findDialog(Activity activity)
	{
		if (activity instanceof FragmentActivity)
		{
			FragmentActivity fragmentActivity = (FragmentActivity)activity;
			if (fragmentActivity.getSupportFragmentManager() != null &&
				fragmentActivity.getSupportFragmentManager().getFragments() != null)
			{
				for (Fragment fragment : fragmentActivity.getSupportFragmentManager().getFragments())
				{
					if (fragment instanceof DialogFragment && isShownInDialog(fragment))
						return (DialogFragment)fragment;
				}
			}
		}

		return null;
	}

	private static boolean isShownInDialog(Fragment fragment)
	{
		if (fragment instanceof DialogFragment)
		{
			DialogFragment dialogFragment = (DialogFragment) fragment;
			if (dialogFragment.getDialog() != null && dialogFragment.getDialog().isShowing())
				return true;
		}

		return false;
	}

	private static void dismissDialog(DialogFragment dialogFragment)
	{
		if (dialogFragment instanceof LayoutFragment)
			((LayoutFragment)dialogFragment).returnCancel();
		else
			dialogFragment.dismissAllowingStateLoss();
	}

	private static IViewDefinition getActivityDefinition(Activity activity)
	{
		if (activity instanceof IGxDashboardActivity)
		{
			return ((IGxDashboardActivity)activity).getDashboardDefinition();
		}
		if (activity instanceof IGxActivity)
		{
			return ((IGxActivity)activity).getMainDefinition();
		}
		return null;
	}

	private static IViewDefinition getActivityDefinitionSlide(Activity activity)
	{
		if (activity instanceof INavigationActivity)
		{
			INavigationActivity gxActivity = ((INavigationActivity)activity);
			for ( IInspectableComponent component : gxActivity.getNavigationController().getActiveComponents())
			{
				if (component instanceof IDataView)
				{
					IDataView gxComponent = (IDataView)component;
					return gxComponent.getDefinition();
				}
			}
		}
		return null;
	}

	private static IViewDefinition getFragmentDefinition(Fragment fragment)
	{
		if (fragment instanceof IDataView)
			return ((IDataView)fragment).getDefinition();
		else
			return null;
	}

	private static boolean isObject(IViewDefinition definition, String objectName)
	{
		if (definition == null)
			return false;

		if (definition.getName().equalsIgnoreCase(objectName))
			return true; // Exact match (e.g. 'WWSDCustomer.Customer.List').

		return definition.getObjectName().equalsIgnoreCase(objectName); // Object name match (e.g. 'WWSDCustomer').
	}

	static void onPause(Activity activity)
	{
	}
}
