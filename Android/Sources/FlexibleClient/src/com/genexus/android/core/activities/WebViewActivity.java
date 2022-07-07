package com.genexus.android.core.activities;

import java.net.HttpCookie;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.*;

import com.genexus.android.R;
import com.genexus.android.gam.AuthBrowserHelper;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.ResultDetail;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.PhoneHelper;
import com.genexus.android.core.compatibility.SherlockHelper;
import com.genexus.android.core.utils.TaskRunner;

public class WebViewActivity extends GxBaseActivity
{
	private String mUrl = null;
	private String mHtml = null;
	private boolean mShareSession = false;
	private boolean mLoginExternalCall = false;

	private WebView mWebview = null;

	/** Called when the activity is first created. */
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		SherlockHelper.requestWindowFeature(this, Window.FEATURE_PROGRESS);
		ActivityHelper.setActionBarVisibility(this, false);

		Intent intent = getIntent();
		mUrl = intent.getStringExtra(ActivityLauncher.INTENT_EXTRA_LINK_ACTION);
		mHtml = intent.getStringExtra("Html");
		mShareSession = intent.getBooleanExtra("ShareSession", false);
		mLoginExternalCall = intent.getBooleanExtra(AuthBrowserHelper.EXTRA_IS_EXTERNAL_LOGIN, false);

		mWebview = new WebView(this);
		setContentView(mWebview);

		webviewInit();

		mWebview.getSettings().setBuiltInZoomControls(true);
		mWebview.setWebViewClient(new MyWebViewClient());
		mWebview.setWebChromeClient(new MyWebChromeClient());

		if ((mUrl!=null) && (mUrl.endsWith(".pdf") || mUrl.endsWith(".apk") || mUrl.contains("www.youtube.com")))
		{
			viewInBrowser();
			finish();
			return;
		}

		if (mUrl!=null)
		{
			if (!mUrl.startsWith("http://") && !mUrl.startsWith("https://"))
				mUrl = "http://" + mUrl;
			if (mShareSession)
				TaskRunner.execute(new WebViewTask());
			else
				mWebview.loadUrl(mUrl);

		}
		else if (mHtml!=null)
		{
			//change for the load data that works with utf-8
			//webview.loadData(html,"text/html", "utf-8");
			mWebview.loadDataWithBaseURL(null, mHtml, "text/html", "utf-8", "about:blank");
		}

		this.setTitle("");
	}


	@SuppressLint("SetJavaScriptEnabled")
	private void webviewInit() {
		if (Services.Application.get().getAllowWebViewExecuteJavascripts())
		{
			mWebview.getSettings().setJavaScriptEnabled(true);
			mWebview.getSettings().setDomStorageEnabled(true);
		}
		if (!Services.Application.get().getAllowWebViewExecuteLocalFiles())
		{
			mWebview.getSettings().setAllowFileAccess(false);
			mWebview.getSettings().setAllowContentAccess(false);

		}

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_web, menu);

		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.viewinBrowser) {
			if (mUrl != null)
			{
				viewInBrowser();
			}
			//finish();
			return true;
		} else {
			return false;
		}
	}


	private void viewInBrowser()
	{
		viewInBrowser(mUrl);
	}

	private void viewInBrowser(String url)
	{
		Intent next = new Intent("android.intent.action.VIEW", Uri.parse( url));
		IntentFactory.setIntentFlagsNewDocument(next);
		startActivity(next);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (mWebview!=null && (keyCode == KeyEvent.KEYCODE_BACK) && mWebview.canGoBack())
		{
			mWebview.goBack();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	//Task to allow share session between webview and httpclient.
	@SuppressWarnings("deprecation")
	public class WebViewTask extends TaskRunner.BaseTask<Boolean>
	{
		private CookieManager cookieManager;
		private List<HttpCookie> mCookies;

		@Override
		public void onPreExecute()
		{
			CookieSyncManager.createInstance(WebViewActivity.this);
			cookieManager = CookieManager.getInstance();

			// Copied from
			// http://www.walletapp.net/android-passing-cookie-to-webview

			// get cookies
			mCookies = Services.HttpService.getCookieManager().getCookieStore().getCookies();

			//delete old cookies , seems not necessary in our case
			//cookieManager.removeSessionCookie();
			// do it to remove any previous session with facebook twitter etc.
			cookieManager.removeAllCookie();
		}

		@Override
		public Boolean doInBackground()
		{
			// this is very important - THIS IS THE HACK
			SystemClock.sleep(1000);
			return false;
		}

		@SuppressLint("SetJavaScriptEnabled")
		@Override
		public void onPostExecute(Boolean result)
		{
			if (mCookies != null)
			{
				for (HttpCookie cookie : mCookies)
				{
					String cookieString = cookie.getName() + Strings.EQUAL + cookie.getValue() + "; domain=" + cookie.getDomain();
					cookieManager.setCookie(cookie.getDomain(), cookieString);
				}

				CookieSyncManager.getInstance().sync();
			}

			WebSettings webSettings = mWebview.getSettings();
			if (Services.Application.get().getAllowWebViewExecuteJavascripts())
				webSettings.setJavaScriptEnabled(true);
			webSettings.setBuiltInZoomControls(true);
			mWebview.setWebViewClient(new MyWebViewClient());
			mWebview.loadUrl(mUrl);
		}
	}

	private class MyWebViewClient extends WebViewClient
	{
		@SuppressWarnings("deprecation")
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
			if (url != null)
			{
				//Services.Log.debug("Url " + url);
				if (url.endsWith(".pdf") || url.endsWith(".apk") /*|| url.contains("youtube.com")*/)
				{
					viewInBrowser(url);
					return true;
				}
				if (url.startsWith("gxgam://"))
				{
					AuthBrowserHelper.finishExternalLogin(WebViewActivity.this, Uri.parse(url));
					return true;
				}

				if (PhoneHelper.launchFromWebView(WebViewActivity.this, url))
					return true;

				view.loadUrl(url);
			}
			return true;
		}

		@SuppressWarnings("deprecation")
		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
		{
			super.onReceivedError(view, errorCode, description, failingUrl);
			Services.Log.error("errorCode " + errorCode, " description " + description + " failingUrl " + failingUrl);
			if (mLoginExternalCall)
			{
				String errorMessage = Services.Strings.getResource(R.string.GXM_NetworkError, description);
				ResultDetail<?> result = ResultDetail.error(errorMessage);

				// Return the login result to the caller.
				Intent resultIntent = new Intent();
				resultIntent.putExtra(AuthBrowserHelper.EXTRA_EXTERNAL_LOGIN_RESULT, result);
				setResult(RESULT_OK, resultIntent);
				finish();
			}
		}
	}

	private class MyWebChromeClient extends WebChromeClient
	{
		@Override
		public void onProgressChanged(WebView view, int progress)
		{
			// Activities and WebViews measure progress with different scales.
			// The progress meter will automatically disappear when we reach 100%
			SherlockHelper.setProgress(WebViewActivity.this, progress * 100);
		}
	}
}
