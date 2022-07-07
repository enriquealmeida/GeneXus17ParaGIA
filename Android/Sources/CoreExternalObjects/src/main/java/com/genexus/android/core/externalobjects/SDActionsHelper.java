package com.genexus.android.core.externalobjects;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;

import androidx.appcompat.app.AlertDialog;

import com.genexus.android.core.actions.Action;
import com.genexus.android.core.actions.ActionExecution;
import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.activities.ActivityFlowControl;
import com.genexus.android.core.activities.ActivityHelper;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.base.utils.ThreadUtils;
import com.genexus.android.core.common.PhoneHelper;
import com.genexus.android.core.utils.UITestingUtils;

public class SDActionsHelper
{

	//return action
	public static void returnAction(Activity activity)
	{
		ActivityFlowControl.finishWithReturn(activity);
	}

	//add contact action.
	public static boolean addContactFromParameters(Activity fromActivity, List<String> values)
	{
		String contactName = Strings.EMPTY;
		String secondName = Strings.EMPTY;
		String email = Strings.EMPTY;
		String phone = Strings.EMPTY;
		String companyName = Strings.EMPTY;
		String photo = Strings.EMPTY;

		if (values.size() > 0)
			contactName = values.get(0);
		if (values.size() > 1)
			secondName = values.get(1);
		if (values.size() > 2)
			email = values.get(2);
		if (values.size() > 3)
			phone = values.get(3);
		if (values.size() > 4)
			companyName = values.get(4);
		if (values.size() > 5)
			photo = values.get(5);

		byte[] photoByteArray = null;
		if (!photo.isEmpty()) {
			Bitmap bitmap = Services.Images.getBitmap(fromActivity, photo);
			if (bitmap != null) {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				photoByteArray = stream.toByteArray();
			}
		}

		return PhoneHelper.addContact(fromActivity, contactName, secondName, phone, email, photoByteArray, companyName);
	}

	public static boolean viewContactFromParameters(Activity fromActivity, List<String> values) {
		String firstName = values.get(0);
		String lastName = values.get(1);
		String fullName = firstName + " " + lastName;
		String phone = values.get(3);

		return PhoneHelper.viewContact(fromActivity, fullName, phone);
	}

	//send message action.
	public static void sendMessageFromParameters(Activity fromActivity, List<String> values)
	{
		String data = Strings.EMPTY;
		if (values.size() > 0)
			data = values.get(0);

		String toMessage = Strings.EMPTY;
		if (values.size() > 1)
			toMessage = values.get(1);

		PhoneHelper.share(fromActivity, data, toMessage);
	}

	//add appointment action
	public static boolean addAppointmentFromParameters(Activity fromActivity, List<String> values)
	{
		String title = Strings.EMPTY;
		Date startDate = null;
		Date endDate = null;
		Date startDateTime = null;
		Date endDateTime = null;
		String place = Strings.EMPTY;

		if (values.size() > 0)
			title = values.get(0);
		if (values.size() > 1)
			startDate = Services.Strings.getDate(values.get(1));
		if (values.size() > 2)
			endDate = Services.Strings.getDate(values.get(2));
		if (values.size() > 3)
		{
			startDateTime = Services.Strings.getDateTime(values.get(3));
			if (startDateTime==null)
				startDateTime = startDate;
			else
				cloneDate(startDate, startDateTime);
		}
		if (values.size() > 4)
		{
			endDateTime = Services.Strings.getDateTime(values.get(4));
			if (endDateTime==null)
				endDateTime = endDate;
			else
				cloneDate(endDate, endDateTime);
		}
		if (values.size() > 5)
			place = values.get(5);
		if (startDateTime == null)
			startDateTime = startDate;
		if (endDateTime == null)
			endDateTime = endDate;

		return PhoneHelper.addAppointment(fromActivity, title, startDateTime, endDateTime, place);
	}

	private static void cloneDate(Date originalDate, Date toReplaceDate)
	{
		if (originalDate!=null)
		{
			Calendar calOriginal = Calendar.getInstance();
			calOriginal.setTime(originalDate);

			Calendar calReplace = Calendar.getInstance();
			calReplace.setTime(toReplaceDate);

			calReplace.set(Calendar.YEAR, calOriginal.get(Calendar.YEAR));
			calReplace.set(Calendar.MONTH, calOriginal.get(Calendar.MONTH));
			calReplace.set(Calendar.DAY_OF_MONTH, calOriginal.get(Calendar.DAY_OF_MONTH));
			calReplace.set(Calendar.SECOND, 0);
			toReplaceDate.setTime(calReplace.getTime().getTime());
		}
	}

	// Dialogs

	public static void showMessage(ApiAction action, Activity activity, String message, boolean isToast, String okButtonText)
	{
		int type = (isToast ? ShowMessageRunnable.TYPE_TOAST : ShowMessageRunnable.TYPE_MESSAGE);
		Services.Device.runOnUiThread(new ShowMessageRunnable(action, activity, type, message, okButtonText, null));

		// Wait a short delay for toast to appear (not necessary, for better visual effect only).
		if (isToast)
			ThreadUtils.sleep(150);
	}

	public static void showConfirmDialog(ApiAction action, Activity activity, String message, String okButtonText, String cancelButtonText)
	{
		activity.runOnUiThread(new ConfirmRunnable(action, activity, message, okButtonText, cancelButtonText));
	}

	private static class ShowMessageRunnable implements Runnable
	{
		static final int TYPE_MESSAGE = 1;
		static final int TYPE_TOAST = 2;
		static final int TYPE_CONFIRM = 3;

		private final Activity mActivity;
		private final int mType;
		private final CharSequence mText;

		private final ApiAction mShowMessageAction;
		private final String mOkButtonText;
		private final String mCancelButtonText;

		public ShowMessageRunnable(ApiAction action, Activity activity, int type, String text, String okButtonText, String cancelButtonText)
		{
			mActivity = activity;
			mType = type;
			mText = Services.Strings.attemptFromHtml(text);
			mShowMessageAction = action;
			mOkButtonText = okButtonText;
			mCancelButtonText = cancelButtonText;
		}

		@Override
		public void run()
		{
			if (mType == TYPE_CONFIRM || mType == TYPE_MESSAGE)
			{
				Activity currentActivity = ActivityHelper.getCurrentActivity();
				if (currentActivity == null || currentActivity.isFinishing()) {
					return;
				}

				AlertDialog.Builder builder = new AlertDialog.Builder(ActivityHelper.getCurrentActivity());
				builder.setCancelable(false);
				builder.setMessage(mText);
				if (Strings.hasValue(mOkButtonText))
					builder.setPositiveButton(mOkButtonText, mDialogContinue);
				else
					builder.setPositiveButton(R.string.GXM_button_ok, mDialogContinue);
				if (mType == TYPE_CONFIRM)
				{
					if (Strings.hasValue(mCancelButtonText))
						builder.setNegativeButton(mCancelButtonText, mDialogCancel);
					else
						builder.setNegativeButton(R.string.GXM_cancel, mDialogCancel);
				}
				builder.show();
				UITestingUtils.Companion.setDialogPresent(true);
			}
			else
			{
				// Show toast. It's not necessary to call continue, because this particular action
				// does not wait for completion (catchOnActivityResult = false).
				Context contextForToast = mActivity;
				if (contextForToast==null)
					contextForToast = Services.Application.getAppContext();
				if (contextForToast!=null)
					Services.Messages.showMessage(contextForToast, mText);
				else
					Services.Log.warning("Cannot show toast message, context is null");
			}
		}

		private final DialogInterface.OnClickListener mDialogContinue = new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				setDialogDismissed();
				onClickOk();
			}
		};

		private final DialogInterface.OnClickListener mDialogCancel = new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				setDialogDismissed();
				onClickCancel();
			}
		};

		protected void onClickOk()
		{
			// By default, continue execution.
			continueEventExecution(mShowMessageAction);
		}

		protected void onClickCancel()
		{
			// By default, cancel execution.
			cancelEventExecution(mShowMessageAction);
		}

		protected final void continueEventExecution(Action action)
		{
			ActionExecution.continueCurrent(mActivity, false, action);
		}

		protected final void cancelEventExecution(Action action)
		{
			ActionExecution.cancelCurrent(action);
		}

		protected void setDialogDismissed() {
			UITestingUtils.Companion.setDialogPresent(false);
		}
	}

	private static class ConfirmRunnable extends ShowMessageRunnable
	{
		private final ApiAction mConfirmAction;

		public ConfirmRunnable(ApiAction action, Activity activity, String text, String okButtonText, String cancelButtonText)
		{
			super(action, activity, TYPE_CONFIRM, text, okButtonText, cancelButtonText);
			mConfirmAction = action;
		}

		@Override
		protected void onClickOk()
		{
			// Set result to True.
			if (mConfirmAction != null && mConfirmAction.hasOutput())
				mConfirmAction.setOutputValue(Value.newBoolean(true));

			// Continue execution.
			continueEventExecution( mConfirmAction);
		}

		@Override
		protected void onClickCancel()
		{
			if (mConfirmAction != null && mConfirmAction.hasOutput())
			{
				// Set result to False and continue (new behavior).
				mConfirmAction.setOutputValue(Value.newBoolean(false));
				continueEventExecution(mConfirmAction);
			}
			else
			{
				// Cancel event (old behavior)
				cancelEventExecution( mConfirmAction);
			}
		}
	}
}
