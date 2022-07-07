package com.genexus.android.core.externalobjects;

import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import androidx.annotation.NonNull;

import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.activities.ActivityHelper;
import com.genexus.android.core.activities.ActivityLauncher;
import com.genexus.android.core.activities.IntentFactory;
import com.genexus.android.content.FileProviderUtils;
import com.genexus.android.notification.BadgeManager;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.PhoneHelper;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.externalapi.ExternalApiResult;
import com.genexus.android.core.providers.EntityDataProvider;

public class InteropAPI extends ExternalApi {
	public static final String OBJECT_NAME = "GeneXus.SD.Interop";

	private static final String METHOD_OPEN = "Open";
	private static final String METHOD_OPEN_IN_BROWSER = "OpenInBrowser";
	private static final String METHOD_CAN_OPEN = "CanOpen";

	private static final String METHOD_CLEAR_CACHE = "ClearCache";
	private static final String METHOD_IS_ONLINE = "IsOnline";
	private static final String METHOD_MESSAGE = "Msg";
	private static final String METHOD_CONFIRM = "Confirm";

	private static final String PARAMETER_MESSAGE_TOAST = "nowait";
	private static final String PARAMETER_MESSAGE_LOG = "status";

	private static final String METHOD_SLEEP = "Sleep";

	private static final String METHOD_TO_STRING = "ToString";
	private static final String METHOD_FORMAT = "Format";

	private static final String METHOD_PLACE_CALL = "PlaceCall";
	private static final String METHOD_SEND_EMAIL = "SendEmail";
	private static final String METHOD_SEND_EMAIL_ADVANCED = "SendEmailAdvanced";
	private static final String METHOD_SEND_SMS = "SendSms";

	private static final String METHOD_SET_BADGE_NUMBER = "SetBadgeNumber";

	private static final String METHOD_GET_APPLICATION_STATE = "ApplicationState";
	private static final int APPLICATION_STATE_ACTIVE = 0;
	private static final int APPLICATION_STATE_BACKGROUND = 2;

	private static final String[] IGNORED_METHODS = new String[]
			{"IOSSetBadgeNumber", "IOSSetBadgeTextToTabIndex", "IOSSetSelectedTabIndex"};

	public InteropAPI(ApiAction action) {
		super(action);
	}

	@Override
	public @NonNull ExternalApiResult execute(String method, List<Object> parameters) {
		if (Strings.arrayContains(IGNORED_METHODS, method, true))
			return ExternalApiResult.SUCCESS_CONTINUE; // Ignore all these methods and continue.

		List<String> parameterValues = toString(parameters);

		Activity myActivity = getActivity();
		if (method.equalsIgnoreCase("sendmessage")) {
			SDActionsHelper.sendMessageFromParameters(getActivity(), parameterValues);
			return ExternalApiResult.SUCCESS_WAIT; // Picker opened.
		} else if (method.equalsIgnoreCase("playvideo") || method.equalsIgnoreCase("playaudio")) {
			//play video
			if (parameterValues.size() > 0) {
				String data = parameterValues.get(0);
				if (method.equalsIgnoreCase("playvideo"))
					ActivityLauncher.callViewVideo(getContext(), data);
				else
					ActivityLauncher.callViewAudio(getContext(), data);

				return ExternalApiResult.SUCCESS_WAIT;
			}
		} else if (method.equalsIgnoreCase(METHOD_PLACE_CALL)) {
			//call number
			if (parameterValues.size() > 0) {
				String data = parameterValues.get(0);

				if (PhoneHelper.callNumber(getActivity(), data))
					return ExternalApiResult.SUCCESS_WAIT;
				else
					return getInteropActionFailureResult();
			}
		} else if (method.equalsIgnoreCase(METHOD_SEND_EMAIL_ADVANCED)) {
			//send mail
			if (parameterValues.size() > 4) {
				String[] email = convertJsonArrayToArray(parameterValues.get(0));
				String[] ccEmail = convertJsonArrayToArray(parameterValues.get(1));
				String[] bccEmail = convertJsonArrayToArray(parameterValues.get(2));
				String subject = parameterValues.get(3);
				String message = parameterValues.get(4);

				List<String> attachments = null;
				if (parameterValues.size() > 5)
					attachments = convertJsonArrayToList(parameterValues.get(5));

				if (PhoneHelper.sendEmail(getActivity(), email, ccEmail, bccEmail, subject, message, attachments))
					return ExternalApiResult.SUCCESS_WAIT;
				else
					return getInteropActionFailureResult();
			}
		} else if (method.equalsIgnoreCase(METHOD_SEND_EMAIL)) {
			//send mail
			if (parameterValues.size() > 2) {
				String email = parameterValues.get(0);
				String subject = parameterValues.get(1);
				String message = parameterValues.get(2);

				if (PhoneHelper.sendEmail(getActivity(), email, subject, message))
					return ExternalApiResult.SUCCESS_WAIT;
				else
					return getInteropActionFailureResult();
			}
		} else if (method.equalsIgnoreCase(METHOD_SEND_SMS)) {
			//send sms
			if (parameterValues.size() > 1) {
				String phone = parameterValues.get(0);
				String message = parameterValues.get(1);

				if (PhoneHelper.sendSms(getActivity(), phone, message))
					return ExternalApiResult.SUCCESS_WAIT;
				else
					return getInteropActionFailureResult();
			}
		} else if (method.equalsIgnoreCase(METHOD_MESSAGE)) {
			if (parameterValues.size() > 0) {
				String message = parameterValues.get(0);
				boolean isToast = parameterValues.size() >= 2 && PARAMETER_MESSAGE_TOAST.equalsIgnoreCase(parameterValues.get(1));
				boolean isLog = parameterValues.size() >= 2 && PARAMETER_MESSAGE_LOG.equalsIgnoreCase(parameterValues.get(1));
				// read optional parameter
				String okButtonText = (parameterValues.size() >= 2)? parameterValues.get(1): null;

				if (isLog) {
					Services.Log.debug(message);
					return ExternalApiResult.SUCCESS_CONTINUE;
				} else {
					// pass action to showMessage.
					SDActionsHelper.showMessage(getAction(), myActivity, message, isToast, okButtonText);
					return (isToast ? ExternalApiResult.SUCCESS_CONTINUE : ExternalApiResult.SUCCESS_WAIT);
				}
			}
		} else if (method.equalsIgnoreCase(METHOD_CONFIRM)) {
			if (parameterValues.size() > 0) {
				String message = parameterValues.get(0);
				// read optional parameters
				String okButtonText = (parameterValues.size() >= 2)? parameterValues.get(1): null;
				String cancelButtonText = (parameterValues.size() >= 3)? parameterValues.get(2): null;

				// pass action to showConfirmDialog.
				SDActionsHelper.showConfirmDialog(getAction(), myActivity, message, okButtonText, cancelButtonText);
				return ExternalApiResult.SUCCESS_WAIT;
			}
		} else if (method.equalsIgnoreCase(METHOD_OPEN)) {
			if (parameterValues.size() != 0) {
				if (open(parameterValues.get(0)))
					return ExternalApiResult.SUCCESS_WAIT;
				else
					return getInteropActionFailureResult();
			}
		} else if (method.equalsIgnoreCase(METHOD_OPEN_IN_BROWSER) && parameterValues.size() != 0) {
			if (PhoneHelper.openInBrowser(getActivity(), parameterValues.get(0)))
				return ExternalApiResult.SUCCESS_WAIT;
			else
				return getInteropActionFailureResult();
		} else if (method.equalsIgnoreCase(METHOD_CAN_OPEN)) {
			if (parameterValues.size() != 0) {
				boolean canOpen = canOpen(parameterValues.get(0));
				return ExternalApiResult.success(canOpen ? Strings.TRUE : Strings.FALSE);
			}
		} else if (method.equalsIgnoreCase(METHOD_CLEAR_CACHE)) {
			EntityDataProvider.clearAllCaches();
			return ExternalApiResult.SUCCESS_CONTINUE;
		} else if (method.equalsIgnoreCase(METHOD_IS_ONLINE)) {
			String result = Boolean.toString(Services.HttpService.isOnline());
			return ExternalApiResult.success(result);

		} else if (method.equalsIgnoreCase(METHOD_SLEEP)) {
			if (parameterValues.size() != 0) {
				Double seconds = Services.Strings.tryParseDouble(parameterValues.get(0));
				//noinspection EmptyCatchBlock
				try {
					if (seconds != null)
						Thread.sleep((long) (seconds * 1000));
				} catch (InterruptedException e) {
				}

				return ExternalApiResult.SUCCESS_CONTINUE;
			}
		} else if (method.equalsIgnoreCase(METHOD_TO_STRING)) {
			if (parameterValues.size() != 0) {
				// Second and third parameters can be length and decimals.
				Integer length = null;
				if (parameterValues.size() >= 2)
					length = Services.Strings.tryParseInt(parameterValues.get(1));

				Integer decimals = null;
				if (parameterValues.size() >= 3)
					decimals = Services.Strings.tryParseInt(parameterValues.get(2));

				String str = GenexusFunctions.gxToString(getAction(), parameterValues.get(0), length, decimals);
				return ExternalApiResult.success(str);
			}
		} else if (method.equalsIgnoreCase(METHOD_FORMAT)) {
			String str = GenexusFunctions.gxFormat(getAction(), parameterValues);
			return ExternalApiResult.success(str);
		} else if (method.equalsIgnoreCase(METHOD_GET_APPLICATION_STATE)) {
			int state = (ActivityHelper.hasCurrentActivity() ? APPLICATION_STATE_ACTIVE : APPLICATION_STATE_BACKGROUND);
			return ExternalApiResult.success(String.valueOf(state));
		} else if (method.equalsIgnoreCase(METHOD_SET_BADGE_NUMBER)) {
			int number = Integer.parseInt(parameterValues.get(0));
			BadgeManager.INSTANCE.setBadge(getContext(), number);
			return ExternalApiResult.SUCCESS_CONTINUE;
		}

		return ExternalApiResult.failureUnknownMethod(this, method, parameters.size());
	}

	private ExternalApiResult getInteropActionFailureResult() {
		return ExternalApiResult.failure(R.string.GXM_NoApplicationAvailable);
	}

	private boolean open(String url) {
		Intent intent = getIntent(getContext(), url);
		if (intent == null)
			return false;

		try {
			IntentFactory.setIntentFlagsNewDocument(intent);
			return PhoneHelper.startAction(getActivity(), intent);
		} catch (ActivityNotFoundException e) {
			return false;
		}
	}

	private boolean canOpen(String url) {
		Intent intent = getIntent(getContext(), url);
		if (intent != null) {
			List<ResolveInfo> intentActivities = getContext().getPackageManager().queryIntentActivities(intent, 0);
			return (intentActivities.size() != 0);
		} else
			return false;
	}

	private static Intent getIntent(@NonNull Context context, String url) {
		if (!Strings.hasValue(url))
			return null;

		Intent intent = new Intent(Intent.ACTION_VIEW);

		final String FILE_SCHEME = "file://";
		if (Strings.starsWithIgnoreCase(url, FILE_SCHEME)) {
			// First, try to guess the MIME type so that the proper app is chosen.
			File file = new File(url.substring(FILE_SCHEME.length()));
			String mimeType = URLConnection.guessContentTypeFromName(file.getName());

			// Secondly, we cannot actually share file URIs in Android N (and it was discouraged
			// before) so convert file:// uris inside our own directories to content:// ones.
			if (FileProviderUtils.canShareFile(context, file)) {
				Uri providerUri = FileProviderUtils.getUriForFile(context, file);
				intent.setDataAndType(providerUri, mimeType);

				// This should not be necessary, but is.
				// See http://stackoverflow.com/a/33652695/82788
				FileProviderUtils.grantReadPermissions(context, intent);
			} else
				intent.setDataAndType(Uri.fromFile(file), mimeType);
		} else {
			// Not a file. Add http as scheme, if missing.
			if (!url.contains(":"))
				url = "http://" + url;

			intent.setData(Uri.parse(url));
		}

		return intent;
	}

	/**
	 * Helper function to convert parameters from JSONArray to array of strings
	 */
	private static String[] convertJsonArrayToArray(String arrayValues) {
		List<String> result = convertJsonArrayToList(arrayValues);
		return result.toArray(new String[result.size()]);
	}

	private static List<String> convertJsonArrayToList(String arrayValues) {
		List<String> result = new ArrayList<>();
		try {
			JSONArray array = new JSONArray(arrayValues);
			for (int i = 0; i < array.length(); i++) {
				String value = array.getString(i);
				result.add(value);
			}
		} catch (JSONException e) {
			Services.Log.error("Cannot convert " + arrayValues + " to json array");
		}

		return result;
	}
}
