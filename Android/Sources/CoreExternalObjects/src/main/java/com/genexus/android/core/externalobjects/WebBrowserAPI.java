package com.genexus.android.core.externalobjects;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.actions.ExternalObjectEvent;
import com.genexus.android.core.activities.IntentFactory;
import com.genexus.android.core.base.metadata.enums.RequestCodes;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.externalapi.ExternalApiResult;

import java.util.Arrays;
import java.util.List;

import static com.genexus.android.core.externalobjects.WebBrowserRedirectActivity.KEY_EXTRA_WEB_BROWSER_URL;

public class WebBrowserAPI extends ExternalApi {

	public static final String OBJECT_NAME = "GeneXus.SD.WebBrowser";

	private static final String METHOD_OPEN = "Open";
	private static final String METHOD_CLOSE = "Close";

	/* BeforeNavigate event only applies for Component domain based variables */
	private static final String EVENT_BEFORE_NAVIGATE = "BeforeNavigate";
	private static final String EVENT_ON_CLOSE = "OnClose";

	private static String mLastUrl;
	private static ApiAction mOpenAction;

	public WebBrowserAPI(ApiAction action) {
		super(action);
		addMethodHandler(METHOD_OPEN, 1, mMethodOpen);
		addMethodHandler(METHOD_CLOSE, 0, mMethodClose);
	}

	private final IMethodInvokerWithActivityResult mMethodOpen = new IMethodInvokerWithActivityResult() {

		@NonNull
		@Override
		public ExternalApiResult invoke(List<Object> parameters) {
			String url = (String) parameters.get(0);
			if (!url.isEmpty()) {
				mLastUrl = url;
				Intent browserIntent = IntentFactory.newCustomTabsIntent(getActivity(), Uri.parse(mLastUrl));
				if (getContext().getPackageManager().resolveActivity(browserIntent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
					setCurrentOpenAction();
					getActivity().startActivityForResult(getOpenBrowserIntent(mLastUrl), RequestCodes.ACTION_ALWAYS_SUCCESSFUL);
					return ExternalApiResult.SUCCESS_WAIT;
				} else {
					return ExternalApiResult.failure(R.string.GXM_NoApplicationAvailable);
				}
			}
			return ExternalApiResult.FAILURE;
		}

		@NonNull
		@Override
		public ExternalApiResult handleActivityResult(int requestCode, int resultCode, @Nullable Intent result) {
			return handleBrowserActivityResult(requestCode, resultCode);
		}
	};

	private final IMethodInvokerWithActivityResult mMethodClose = new IMethodInvokerWithActivityResult() {

		@NonNull
		@Override
		public ExternalApiResult invoke(List<Object> parameters) {
			//WebBrowser had to be opened prior to closing it
			if (mOpenAction != null) {
				mOpenAction.afterActivityResult(RequestCodes.ACTION_ALWAYS_SUCCESSFUL, Activity.RESULT_CANCELED, null);
				mOpenAction = null;
				getActivity().startActivityForResult(getCloseBrowserIntent(), RequestCodes.ACTION_ALWAYS_SUCCESSFUL);
			}
			return ExternalApiResult.SUCCESS_WAIT;
		}

		@NonNull
		@Override
		public ExternalApiResult handleActivityResult(int requestCode, int resultCode, @Nullable Intent result) {
			return handleBrowserActivityResult(requestCode, resultCode);
		}
	};

	private ExternalApiResult handleBrowserActivityResult(int requestCode, int resultCode) {
		if (requestCode == RequestCodes.ACTION_ALWAYS_SUCCESSFUL) {
			if (resultCode == Activity.RESULT_OK)
				fireEvent(EVENT_ON_CLOSE, mLastUrl);

			return ExternalApiResult.SUCCESS_CONTINUE;
		}

		return ExternalApiResult.FAILURE;
	}

	private Intent getOpenBrowserIntent(String url) {
		Intent intent = new Intent(getActivity(), getWebBrowserRedirectActivityClass());
		intent.putExtra(KEY_EXTRA_WEB_BROWSER_URL, url);
		return intent;
	}

	private Intent getCloseBrowserIntent() {
		Intent intent = new Intent(getActivity(), getWebBrowserRedirectActivityClass());
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return intent;
	}

	private Class<? extends Activity> getWebBrowserRedirectActivityClass() {
		return WebBrowserRedirectActivity.class;
	}

	private void fireEvent(String eventName, Object... eventArgs) {
		ExternalObjectEvent event = new ExternalObjectEvent(OBJECT_NAME, eventName);
		event.fire(Arrays.asList(eventArgs));
	}

	private void setCurrentOpenAction() {
		ApiAction action = getAction();
		if (action.getMethodName().equalsIgnoreCase(METHOD_OPEN))
			mOpenAction = action;
	}

}
