package com.genexus.android.twitter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.genexus.android.core.actions.ActionExecution;
import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.base.metadata.enums.RequestCodes;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.externalapi.ExternalApiResult;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import twitter4j.StatusUpdate;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class TwitterApi extends ExternalApi {
	public static final String OBJECT_NAME = "GeneXus.Social.Twitter";

	// API Property Names
	private static final String PROPERTY_AUTHORIZE_ON_DEMAND	= "AuthorizeOnDemand";
	// API Method Names
	private static final String METHOD_TWEET 					= "Tweet"; 				// Deprecated name for TweetDialog.
	private static final String METHOD_TWEET_DIALOG				= "TweetDialog";
	private static final String METHOD_SEND_TWEET 				= "SendTweet";			// Requires authorization
	private static final String METHOD_FOLLOW 					= "Follow";				// Requires authorization
	private static final String METHOD_OPEN_TWEET 				= "OpenTweet";
	private static final String METHOD_OPEN_PROFILE				= "OpenUserProfile";
	private static final String METHOD_REQUEST_AUTHORIZATION	= "RequestAuthorization";
	private static final String METHOD_IS_AUTHORIZED 			= "IsAuthorized";
	private static final String METHOD_REVOKE_AUTHORIZATION		= "RevokeAuthorization";
	private static final String METHOD_LOGIN					= "Login";
	
	// API methods that require authorization
	private static final List<String> REQUIRES_AUTHORIZATION = Arrays.asList(METHOD_SEND_TWEET, METHOD_FOLLOW, METHOD_LOGIN);
	
	// Internal stuff
	private static boolean sAuthorizeOnDemand = true;
	private RequestToken mRequestToken = null;
	private Pair<String, List<Object>> mPendingOperation = null;

	public TwitterApi(ApiAction action) {
		super(action);
		addPropertyHandler(PROPERTY_AUTHORIZE_ON_DEMAND, mPropertyAuthorizeOnDemandGetter, mPropertyAuthorizeOnDemandSetter);
		addMethodHandler(METHOD_TWEET, 1, mTweetDialogMethod);
		addMethodHandler(METHOD_TWEET_DIALOG, 1, mTweetDialogMethod);
		addMethodHandler(METHOD_TWEET, 2, mTweetDialogMethod);
		addMethodHandler(METHOD_TWEET_DIALOG, 2, mTweetDialogMethod);
		addSimpleMethodHandler(METHOD_SEND_TWEET, 1, mSendTweetMethod);
		addSimpleMethodHandler(METHOD_SEND_TWEET, 2, mSendTweetMethod);
		addSimpleMethodHandler(METHOD_FOLLOW, 1, mFollowMethod);
		addMethodHandler(METHOD_OPEN_TWEET, 2, mOpenTweetMethod);
		addMethodHandler(METHOD_OPEN_PROFILE, 1, mOpenProfileMethod);
		addMethodHandler(METHOD_REQUEST_AUTHORIZATION, 0, mRequestAuthorizationMethod);
		addSimpleMethodHandler(METHOD_IS_AUTHORIZED, 0, mIsAuthorizedMethod);
		addSimpleMethodHandler(METHOD_REVOKE_AUTHORIZATION, 0, mRevokeAuthorizationMethod);
		addSimpleMethodHandler(METHOD_LOGIN, 0, mLoginMethod);
	}

	@Override
	public @NonNull ExternalApiResult execute(String method, List<Object> parameters)
	{
		// Clear pending operations. (Necessary for catchOnActivityResult to work correctly
		// and not get delayed indefinitely if onActivityResult is never called for some reason.)
		mPendingOperation = null;

		if (REQUIRES_AUTHORIZATION.contains(method) && !isAuthorized())
		{
			if (sAuthorizeOnDemand)
			{
				// Set this method as pendingMethod so that once the user authorizes our app, we execute the pending method.
				mPendingOperation = new Pair<String, List<Object>>(method, parameters);
				return invokeMethod(METHOD_REQUEST_AUTHORIZATION, Collections.emptyList());
			}
			else
			{
				// These operations fail if AuthorizeOnDemand is not enabled and it's not authorized.
				return ExternalApiResult.success(String.valueOf(false));
			}
		}
		
		return invokeMethod(method, parameters);
	}

	private final IMethodInvoker mPropertyAuthorizeOnDemandGetter = new IMethodInvoker() {
		@Override
		public @NonNull ExternalApiResult invoke(List<Object> parameters) {
			return ExternalApiResult.success(String.valueOf(sAuthorizeOnDemand));
		}
	};

	private final IMethodInvoker mPropertyAuthorizeOnDemandSetter = new IMethodInvoker() {
		@Override
		public @NonNull ExternalApiResult invoke(List<Object> parameters) {
			String value = (String) parameters.get(0);
			sAuthorizeOnDemand = Boolean.valueOf(value);
			return ExternalApiResult.SUCCESS_CONTINUE;
		}
	};

	@SuppressWarnings("FieldCanBeLocal")
	private final IMethodInvoker mTweetDialogMethod = new IMethodInvoker() {

		@Override
		public @NonNull ExternalApiResult invoke(List<Object> parameters) {
			String message = (String) parameters.get(0);
			String imagePath = (parameters.size() > 1) ? ((String) parameters.get(1)) : "";

			Uri imageUri = Services.Images.getSharedImage(getContext(), imagePath);

			if (TextUtils.isEmpty(message) && imageUri == null)
				return ExternalApiResult.SUCCESS_CONTINUE;

			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.putExtra(Intent.EXTRA_TEXT, message);

			if (imageUri == null) {
				intent.setType("text/plain");
			} else {
				intent.setType("image/*");
				intent.putExtra(Intent.EXTRA_STREAM, imageUri);
			}

			// Find twitter apps that handle this intent
			List<ResolveInfo> activities = getActivity().getPackageManager().queryIntentActivities(intent, 0);

			List<Intent> intentList = new ArrayList<Intent>();

			boolean appsFound = false;
			for (String twitterPackage : TwitterApiConstants.TWITTER_PACKAGES) {
				for (ResolveInfo activity : activities) {
					if (twitterPackage.equalsIgnoreCase(activity.activityInfo.packageName)) {
						intent.setPackage(activity.activityInfo.packageName);
						intentList.add((Intent)intent.clone());
						appsFound = true;
					}
				}
			}

			// Fall back to browser if no apps available
			if (appsFound) {
				intent = Intent.createChooser(intentList.remove(0), Services.Strings.getResource(R.string.abc_activitychooserview_choose_application));
				intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[intentList.size()]));
			} else {
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse(TwitterApiConstants.WEB_TWEET_URL + Uri.encode(message)));
			}

			getActivity().startActivity(intent);
			return ExternalApiResult.SUCCESS_WAIT;
		}
	};

	@SuppressWarnings("FieldCanBeLocal")
	private final ISimpleMethodInvoker mSendTweetMethod = new ISimpleMethodInvoker() {

		@Override
		public Object invoke(List<Object> parameters) {
			String message = (String) parameters.get(0);
			String imagePath = (parameters.size() > 1) ? ((String) parameters.get(1)) : "";

			boolean hasImage = TwitterUtils.isValidImagePath(imagePath);

			if (TextUtils.isEmpty(message) && !hasImage) {
				return String.valueOf(false);
			}

			StatusUpdate status = new StatusUpdate(message);
			if (hasImage) {
				status.setMedia(new File(imagePath));
			}

			try {
				TwitterSingleton.getInstance().updateStatus(status);
				return String.valueOf(true);
			} catch (TwitterException e) {
				TwitterUtils.showErrorMessageOnApp(getActivity(), e);
				return String.valueOf(false);
			}
		}
	};

	@SuppressWarnings("FieldCanBeLocal")
	private final ISimpleMethodInvoker mFollowMethod = new ISimpleMethodInvoker() {

		@Override
		public Object invoke(List<Object> parameters) {
			String userName = (String) parameters.get(0);

			if (!TwitterUtils.isValidTwitterUsername(userName)) {
				Services.Messages.showMessage(getActivity(), R.string.GXM_TwitterInvalidUsername);
				return String.valueOf(false);
			}

			try {
				TwitterSingleton.getInstance().createFriendship(userName);
				return String.valueOf(true);
			} catch (TwitterException e) {
				TwitterUtils.showErrorMessageOnApp(getActivity(), e);
				return String.valueOf(false);
			}
		}
	};

	@SuppressWarnings("FieldCanBeLocal")
	private final IMethodInvoker mOpenTweetMethod = new IMethodInvoker() {

		@Override
		public @NonNull ExternalApiResult invoke(List<Object> parameters) {
			String userName = (String) parameters.get(0);
			String tweetId = (String) parameters.get(1);

			if (!TwitterUtils.isValidTwitterUsername(userName)) {
				Services.Messages.showMessage(getActivity(), R.string.GXM_TwitterInvalidUsername);
				return ExternalApiResult.SUCCESS_CONTINUE;
			}

			if (!TwitterUtils.isValidTwitterStatusId(tweetId)) {
				return ExternalApiResult.SUCCESS_CONTINUE;
			}

			// View in Twitter app
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(TwitterApiConstants.SHOW_STATUS_URI + tweetId));

			// If there are no apps that can handle the intent, fall back to using a browser.
			if (getActivity().getPackageManager().queryIntentActivities(intent, 0).isEmpty())
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse(TwitterApiConstants.MOBILE_URL + userName + "/status/" + tweetId));

			getActivity().startActivity(intent);
			return ExternalApiResult.SUCCESS_WAIT;
		}
	};

	@SuppressWarnings("FieldCanBeLocal")
	private final IMethodInvoker mOpenProfileMethod = new IMethodInvoker() {

		@Override
		public @NonNull ExternalApiResult invoke(List<Object> parameters) {
			String userName = (String) parameters.get(0);

			if (!TwitterUtils.isValidTwitterUsername(userName)) {
				Services.Messages.showMessage(getActivity(), R.string.GXM_TwitterInvalidUsername);
				return ExternalApiResult.SUCCESS_CONTINUE;
			}

			// View in Twitter app
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(TwitterApiConstants.SHOW_USER_PROFILE_URI + userName));

			// If there are no apps that can handle the intent, fall back to using a browser.
			if (getActivity().getPackageManager().queryIntentActivities(intent, 0).isEmpty()) {
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse(TwitterApiConstants.MOBILE_URL + userName));
			}

			getActivity().startActivity(intent);
			return ExternalApiResult.SUCCESS_WAIT;
		}
	};

	@SuppressWarnings("FieldCanBeLocal")
	private final IMethodInvoker mRequestAuthorizationMethod = new IMethodInvoker() {

		@Override
		public @NonNull ExternalApiResult invoke(List<Object> parameters) {
			if (!isAuthorized())
			{
				// Clear any previous Twitter instance.
				TwitterSingleton.destroyInstance();

				try {
					mRequestToken = TwitterSingleton.getInstance().getOAuthRequestToken(TwitterApiConstants.CALLBACK_URI);
				} catch (TwitterException e) {
					TwitterUtils.showErrorMessageOnApp(getActivity(), e);
				}

				Activity activity = getActivity();
				Intent intent = new Intent(activity, TwitterAuthWebView.class);

				if (mRequestToken != null)
					intent.putExtra(TwitterApiConstants.AUTH_URL, mRequestToken.getAuthenticationURL());

				// ACTION_ALWAYS_SUCCESSFUL RequestCode makes sure onAfterActivityResult is called no matter if its RESULT_OK or RESULT_CANCEL.
				// The real result value for this method will be returned by the method afterActivityResult.
				activity.startActivityForResult(intent, RequestCodes.ACTION_ALWAYS_SUCCESSFUL);
				return ExternalApiResult.SUCCESS_WAIT;
			}
			else
				return ExternalApiResult.SUCCESS_CONTINUE;
		}
	};

	@SuppressWarnings("FieldCanBeLocal")
	private final ISimpleMethodInvoker mIsAuthorizedMethod = new ISimpleMethodInvoker() {

		@Override
		public Object invoke(List<Object> parameters) {
			return String.valueOf(isAuthorized());
		}
	};

	@SuppressWarnings("FieldCanBeLocal")
	private final ISimpleMethodInvoker mRevokeAuthorizationMethod = new ISimpleMethodInvoker() {

		@Override
		public Object invoke(List<Object> parameters) {
			Editor editor = Services.Preferences.getAppSharedPreferences(TwitterApiConstants.PREFERENCES_KEY).edit();
			editor.remove(TwitterApiConstants.PREF_KEY_TOKEN);
			editor.remove(TwitterApiConstants.PREF_KEY_TOKEN_SECRET);
			editor.commit();
			TwitterSingleton.destroyInstance();
			return null;
		}
	};

	@SuppressWarnings("FieldCanBeLocal")
	private final ISimpleMethodInvoker mLoginMethod = new ISimpleMethodInvoker() {

		@Override
		public Object invoke(List<Object> parameters) {
			SharedPreferences sharedPrefs = Services.Preferences.getAppSharedPreferences(TwitterApiConstants.PREFERENCES_KEY);
			HashMap<String, String> tokens = new HashMap<>();
			tokens.put("accessToken", sharedPrefs.getString(TwitterApiConstants.PREF_KEY_TOKEN, ""));
			tokens.put("accessTokenSecret", sharedPrefs.getString(TwitterApiConstants.PREF_KEY_TOKEN_SECRET, ""));
			return new Gson().toJson(tokens);
		}
	};

	@Override
	public ExternalApiResult afterActivityResult(int requestCode, int resultCode, Intent resultIntent, String method, List<Object> methodParameters) {
		boolean getAccessToken = resultIntent != null && mRequestToken != null
				&& resultIntent.getStringExtra(TwitterApiConstants.AUTHORIZATION_RESULT) != null;

		if (!getAccessToken) {
			return ExternalApiResult.SUCCESS_CONTINUE;
		}

		String oAuthVerifier = resultIntent.getStringExtra(TwitterApiConstants.AUTHORIZATION_RESULT);
		new Thread(new GetAccessToken(oAuthVerifier)).start();

		return ExternalApiResult.SUCCESS_WAIT;
	}
	
	private class GetAccessToken implements Runnable {
		private final String mOAuthVerifier;

		public GetAccessToken(String oAuthVerifier) {
			mOAuthVerifier = oAuthVerifier;
		}
		
		@Override
		public void run() {
			android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
			AccessToken accessToken = getAccessToken(mOAuthVerifier);
			boolean authSuccessful = accessToken != null;
			mRequestToken = null;
			
			Value result;
			if (authSuccessful) {
				if (mPendingOperation != null) {
					String method = mPendingOperation.first;
					List<Object> parms = mPendingOperation.second;
					result = Value.newValue(execute(method, parms));
				} else {
					result = Value.newBoolean(true);
				}
			} else {
				result = Value.newBoolean(false);
			}
			
			getAction().setOutputValue(result);
			ActionExecution.continueCurrent(getActivity(), true, null);
		}
	}
	
	private boolean isAuthorized() {
		return Services.Preferences.getAppSharedPreferences(TwitterApiConstants.PREFERENCES_KEY).getString(TwitterApiConstants.PREF_KEY_TOKEN, null) != null;
	}
	
	private AccessToken getAccessToken(String oauthVerifier) {
		AccessToken accessToken = null;
		try {
			accessToken = TwitterSingleton.getInstance().getOAuthAccessToken(mRequestToken, oauthVerifier);
		} catch (TwitterException e) {
			TwitterUtils.showErrorMessageOnApp(getActivity(), e);
		}
		
		if (accessToken != null) {
			Editor e = Services.Preferences.getAppSharedPreferences(TwitterApiConstants.PREFERENCES_KEY).edit();
			e.putString(TwitterApiConstants.PREF_KEY_TOKEN, accessToken.getToken());
			e.putString(TwitterApiConstants.PREF_KEY_TOKEN_SECRET, accessToken.getTokenSecret());
			e.commit();
		}
		
		return accessToken;
	}
}
