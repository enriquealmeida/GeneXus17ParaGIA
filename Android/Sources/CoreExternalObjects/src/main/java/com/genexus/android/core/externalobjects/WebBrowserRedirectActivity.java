package com.genexus.android.core.externalobjects;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;

import com.genexus.android.core.base.metadata.enums.RequestCodes;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.common.PhoneHelper;

public class WebBrowserRedirectActivity extends Activity {

	public static final String KEY_EXTRA_WEB_BROWSER_URL = "WEBBROWSER_API_URL";

	private static boolean mIsStarted = false;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
		super.onCreate(savedInstanceState, persistentState);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (mIsStarted) {
			finishExecution();
		}

		Bundle extras = getIntent().getExtras();
		if (extras == null || extras.getString(KEY_EXTRA_WEB_BROWSER_URL, "").isEmpty()) {
			Services.Log.debug("Closing WebBrowser");
			cancelExecution();
		} else {
			String url = extras.getString(KEY_EXTRA_WEB_BROWSER_URL);
			if (PhoneHelper.openInBrowser(this, url)) {
				mIsStarted = true;
			} else {
				Services.Log.warning("Cannot open URL in WebBrowser API." +
					"Chrome CustomTabs not supported on this device");
				cancelExecution();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RequestCodes.ACTION_ALWAYS_SUCCESSFUL && resultCode == Activity.RESULT_CANCELED) {
			finishExecution();
		}
	}

	private void finishExecution() {
		setNotStarted();
		setResult(Activity.RESULT_OK);
		finish();
	}

	private void cancelExecution() {
		setNotStarted();
		setResult(Activity.RESULT_CANCELED);
		finish();
	}

	private void setNotStarted() {
		mIsStarted = false;
	}
}
