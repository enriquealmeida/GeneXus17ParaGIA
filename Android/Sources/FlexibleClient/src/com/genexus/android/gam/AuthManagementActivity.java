package com.genexus.android.gam;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.genexus.android.core.base.services.Services;

/**
 * Manager activity for OAuth "web" external authorization.
 * Heavily inspired by (actually, mostly copied from) https://github.com/openid/AppAuth-Android/
 *
 * Call stack is:
 * GenexusActivity (login panel) -> AuthManagementActivity -> Browser -> AuthRedirectActivity
 *
 * AuthRedirectActivity will call AuthManagementActivity with CLEAR_TOP, thus removing the
 * browser from the stack and allowing AuthManagementActivity to process the resulting Uri
 * and returning the result to the original activity.
 *
 * AuthRedirectActivity needs to have an intent-filter with the appropriate GAM scheme. This
 * must be set in the application's AndroidManifest.xml file.
 */
public class AuthManagementActivity extends Activity
{
	private static final String LOG_TAG = "Auth";

	private static final String KEY_AUTH_INTENT = "authIntent";
	private static final String KEY_AUTHORIZATION_STARTED = "authStarted";

	private boolean mAuthorizationStarted = false;
	private Intent mAuthIntent;

	public static boolean ignoreActivityResult = false;
	public static Intent resultIntent = null;
	public static int resultCode = Activity.RESULT_CANCELED;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ignoreActivityResult = false;
		resultIntent = null;
		resultCode = Activity.RESULT_CANCELED;

		Services.Log.debug(LOG_TAG, "AuthManagementActivity oncreate");

		if (savedInstanceState == null)
			extractState(getIntent().getExtras());
		else
			extractState(savedInstanceState);
	}

	private void extractState(Bundle state)
	{
		if (state == null)
		{
			Services.Log.warning(LOG_TAG, "No stored state - unable to handle response");
			finish();
			return;
		}

		mAuthIntent = state.getParcelable(KEY_AUTH_INTENT);
		mAuthorizationStarted = state.getBoolean(KEY_AUTHORIZATION_STARTED, false);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		ignoreActivityResult = false;
		resultIntent = null;
		resultCode = Activity.RESULT_CANCELED;

		Services.Log.debug(LOG_TAG, "AuthManagementActivity onResume");

		// If this is the first run of the activity, start the authorization intent.
		// Note that we do not finish the activity at this point, in order to remain on the back
		// stack underneath the authorization activity.
		if (!mAuthorizationStarted)
		{
			startActivity(mAuthIntent);
			mAuthorizationStarted = true;
			return;
		}

		// On a subsequent run, it must be determined whether we have returned to this activity
		// due to an OAuth2 redirect, or the user canceling the authorization flow. This can
		// be done by checking whether a response URI is available, which would be provided by
		// RedirectUriReceiverActivity. If it is not, we have returned here due to the user
		// pressing the back button, or the authorization activity finishing without
		// RedirectUriReceiverActivity having been invoked - this can occur when the user presses
		// the back button, or closes the browser tab.
		if (getIntent().getData() != null)
			handleAuthorizationComplete();
		else
			handleAuthorizationCanceled();
	}

	private void handleAuthorizationComplete()
	{
		Uri responseUri = getIntent().getData();
		Services.Log.debug("Authorization complete - processing response Uri: " + responseUri);

		AuthBrowserHelper.finishExternalLogin(this, responseUri);
	}

	private void handleAuthorizationCanceled()
	{
		Services.Log.debug(LOG_TAG, "Authorization canceled by user - returning to caller activity");
		ignoreActivityResult = false;
		resultIntent = new Intent();
		resultCode = Activity.RESULT_CANCELED;
		finish();
	}

	@Override
	protected void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
		Services.Log.debug(LOG_TAG, "AuthManagementActivity onNewIntent");
		setIntent(intent);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putBoolean(KEY_AUTHORIZATION_STARTED, mAuthorizationStarted);
		outState.putParcelable(KEY_AUTH_INTENT, mAuthIntent);
	}

	/**
	 * Creates an intent to start an authorization flow.
	 * @param context the package context for the app.
	 * @param authIntent the intent to be used to get authorization from the user.
	 */
	static Intent createStartIntent(Context context, Intent authIntent)
	{
		Intent intent = createBaseIntent(context);
		intent.putExtra(KEY_AUTH_INTENT, authIntent);
		ignoreActivityResult = true;
		resultIntent = null;
		resultCode = Activity.RESULT_CANCELED;

		return intent;
	}

	/**
	 * Creates an intent to handle the completion of an authorization flow. This restores
	 * the original AuthManagementActivity that was created at the start of the flow.
	 * @param context the package context for the app.
	 * @param responseUri the response URI, which carries the parameters describing the response.
	 */
	static Intent createResponseHandlingIntent(Context context, Uri responseUri)
	{
		Intent intent = createBaseIntent(context);
		intent.setData(responseUri);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		return intent;
	}

	private static Intent createBaseIntent(Context context)
	{
		return new Intent(context, AuthManagementActivity.class);
	}
}
