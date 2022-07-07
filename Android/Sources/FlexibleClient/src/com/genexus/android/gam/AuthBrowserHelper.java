package com.genexus.android.gam;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import androidx.annotation.NonNull;

import com.genexus.android.core.activities.IntentFactory;
import com.genexus.android.core.base.metadata.enums.RequestCodes;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.ResultDetail;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.SecurityHelper;
import com.genexus.android.core.utils.TaskRunner;

/**
 * Helper class for supplying authorization credentials by means of a Web Browser.
 * Used for external logins such as Google, Facebook, or Twitter.
 */
public class AuthBrowserHelper
{
	public static final String EXTRA_EXTERNAL_LOGIN_RESULT = "com.artech.android.gam.AuthBrowserHelper.EXTERNAL_LOGIN_RESULT";
	public static final String EXTRA_IS_EXTERNAL_LOGIN = "com.artech.android.gam.AuthBrowserHelper.IS_EXTERNAL_LOGIN";

	public static void requestAuthorization(@NonNull Activity context, @NonNull Uri uri)
	{
		Intent browserIntent = IntentFactory.newCustomTabsIntent(context, uri);
		if (supportsCustomTabs() && context.getPackageManager().resolveActivity(browserIntent, PackageManager.MATCH_DEFAULT_ONLY) != null)
		{
			// Login with Chrome Custom Tab
			context.startActivityForResult(AuthManagementActivity.createStartIntent(context, browserIntent), RequestCodes.ACTION_ALWAYS_SUCCESSFUL);
		}
		else
		{
			// As fallback, login with a WebView. This won't work for Google (as of Apr 2017)
			// but might be necessary for restricted profiles or other devices without a browser.
			Intent intent = IntentFactory.getIntentForWebApplication(context, uri.toString());
			intent.putExtra(EXTRA_IS_EXTERNAL_LOGIN, true);
			context.startActivityForResult(intent, RequestCodes.ACTION_ALWAYS_SUCCESSFUL);
		}
	}

	public static void finishExternalLogin(final Activity tempActivity, final Uri uri)
	{
		TaskRunner.execute(new TaskRunner.BaseTask<ResultDetail<?>>() {
			@Override
			public ResultDetail<?> doInBackground()
			{
				// Do this in background, since it involves an additional network call.
				return SecurityHelper.afterExternalLogin(uri.toString());
			}

			@Override
			public void onPostExecute(ResultDetail<?> result)
			{
				// Return the login result to the caller.
				Intent resultIntent = new Intent();
				resultIntent.putExtra(EXTRA_EXTERNAL_LOGIN_RESULT, result);
				AuthManagementActivity.resultIntent = resultIntent;
				AuthManagementActivity.resultCode = Activity.RESULT_OK;
				tempActivity.setResult(Activity.RESULT_OK, resultIntent);
				tempActivity.finish();
			}
		});
	}

	public static String getSupportedRedirectUrlScheme()
	{
		if (supportsCustomTabs() && Strings.hasValue(Services.Application.get().getClientId()))
		{
			// This must match the scheme of the intent-filter added to the AuthRedirectActivity.
			return "gxgam" + Services.Application.get().getClientId();
		}
		else
			return null;
	}

	private static boolean supportsCustomTabs()
	{
		final Context context = Services.Application.getAppContext();
		final Uri SAMPLE_URI = Uri.parse("https://www.genexus.com/");
		Intent intent = IntentFactory.newCustomTabsIntent(context, SAMPLE_URI);

		List<ResolveInfo> apps = context.getPackageManager().queryIntentActivities(intent, 0);
		return (apps.size() != 0);
	}
}
