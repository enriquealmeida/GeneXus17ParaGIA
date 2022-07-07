package com.genexus.android.core.controls;

import java.net.HttpCookie;
import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.webkit.*;

import com.genexus.android.R;
import com.genexus.android.core.actions.ExternalObjectEvent;
import com.genexus.android.core.activities.ActivityLauncher;
import com.genexus.android.core.activities.IntentFactory;
import com.genexus.android.core.base.controls.IGxEditThemeable;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.PhoneHelper;
import com.genexus.android.core.compatibility.SherlockHelper;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.utils.TaskRunner;
import com.genexus.android.core.utils.ThemeUtils;

public class GxWebView extends WebView implements IGxEdit, IGxEditThemeable
{
	private String mUrl;
	private ThemeClassDefinition mThemeClass;
	private Coordinator mCoordinator;
	private boolean mLoadAsHtmlDone;
	private boolean mOpenLinksInNewWindow;

	private boolean mNeedsMeasure = false;
	private int mWebViewContentHeight = 0;
	private boolean mRequestLayoutOnAttach = false;

	public GxWebView(Context context, Coordinator coordinator, LayoutItemDefinition item)
	{
		super(context);
		mCoordinator = coordinator;
		initialize();
		mNeedsMeasure = item != null && item.hasAutoGrow();
	}

	public GxWebView(Context context)
	{
		super(context);
		initialize();
	}

	public GxWebView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initialize();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initialize()
	{
		mOpenLinksInNewWindow = true;

		if (Services.Application.get().getAllowWebViewExecuteJavascripts())
		{
			getSettings().setJavaScriptEnabled(true);
			getSettings().setDomStorageEnabled(true);
		}

		if (!Services.Application.get().getAllowWebViewExecuteLocalFiles())
		{
			getSettings().setAllowFileAccess(false);
			getSettings().setAllowContentAccess(false);

		}

		String userAgentString = getSettings().getUserAgentString();
		userAgentString += " GeneXusSD/15 (Android)";
		getSettings().setUserAgentString(userAgentString);
		disableControls();

		//set web view transparent
		setBackgroundColor(Color.TRANSPARENT);
	}

	private void disableControls()
	{
		getSettings().setBuiltInZoomControls(true);
		getSettings().setDisplayZoomControls(false);
    }

	public void setOpenLinksInNewWindow(boolean value)
	{
		mOpenLinksInNewWindow = value;
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		//Services.Log.info("onAttachedToWindow", " " + mUrl);
		if (mNeedsMeasure && !mRequestLayoutOnAttach)
		{
			// 	For tabs change in 5.x, need to post requestLayout
			final WebView myView = this;
			Services.Log.info("onAttachedToWindow", "requestLayout "+ mUrl);
			postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
					myView.requestLayout();
				}
			}, 300);
			mRequestLayoutOnAttach = true;
		}
	}

	@Override
	public void invalidate()
	{
		super.invalidate();
		//Services.Log.info("invalidate", " " + mUrl);
		// right place to requesLayout, the render is done here, follow this tip:
		// http://stackoverflow.com/questions/4065134/is-there-a-listener-for-when-the-webview-displays-its-content
		if (mNeedsMeasure && getContentHeight() > 0 && getContentHeight()!=mWebViewContentHeight)
		{
			mWebViewContentHeight = getContentHeight();
			// WebView has displayed some content and is scrollable.
			Services.Log.info("invalidate", "has new size " + String.valueOf(mWebViewContentHeight) + " " + mUrl);
			Services.Log.info("invalidate", "requestLayout "+ mUrl);
			this.requestLayout();
		}
	}


	public class MyWebViewClient extends WebViewClient
	{
		private boolean loadingFinished = false;
		private boolean redirect = false;
		private String lastVisitedUrl = null;

		//private static final String beforeNavigate = "GlobalEvents.BeforeNavigate";
		private static final String NAME = "GeneXus.SD.WebBrowser";
		private static final String EVENT_HANDLE = "BeforeNavigate";

		// TODO : override a different event for Android N
		@SuppressWarnings("deprecation")
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
			lastVisitedUrl = url;
			if (url != null)
			{
				ExternalObjectEvent event = new ExternalObjectEvent(NAME, EVENT_HANDLE);
				final WebView myView = view;
				final String myUrl = url;

				if (!event.fire(Arrays.asList(url, false), mCoordinator, (successful, parameters) -> {
					if (parameters.size() > 1 && parameters.get(1) instanceof String && Services.Strings.tryParseBoolean((String) parameters.get(1), false))
					{
						// Handled by GeneXus user code, ignore success because event may be cancel if the panel was exited with back, &Handled should be set before the call
					}
					else
					{
						// not handle in gx , call base class
						shouldOverrideLocal(myView, myUrl);
					}
				}))
				{
					return shouldOverrideLocal(view, url);
				}
				// if event fire handle the event return true
				return true;
			}
			return false;
		}

		@Override
		public void doUpdateVisitedHistory(WebView view, String url, boolean isReload)
		{
			if (lastVisitedUrl == null) {
				lastVisitedUrl = url; // ignore first page
			} else if (!lastVisitedUrl.equals(url)) {
				lastVisitedUrl = url;
				// Fire onBeforeNavigate here because is the only way when it has WebUX=Smooth.
				// WebUX=Smooth uses AJAX and the History API to navigate, this type of navigation
				// is used in React, Angular and other frameworks.
				// shouldOverrideUrlLoading is not fired in this case so we can't do nothing with the result
				ExternalObjectEvent event = new ExternalObjectEvent(NAME, EVENT_HANDLE);
				event.fire(Arrays.asList(url, false), mCoordinator, null);
			}
		}

		private boolean shouldOverrideLocal(WebView view, String url)
		{
			if (PhoneHelper.launchFromWebView(getContext(), url))
				return true;

			if (url.endsWith(".pdf") || url.endsWith(".apk"))
			{
				viewInBrowser(url);
				return true;
			}

			//check state
			if (loadingFinished)
			{
				// only open in new window a diferent url
				if (mOpenLinksInNewWindow && Strings.hasValue(url) && !url.equalsIgnoreCase(mUrl))
				{
					// now link works as double tab, open in new windows
					ActivityLauncher.startWebBrowser(getContext(), url);
					return true;
				}
			}
			else
			{
				redirect = true;
			}
			loadingFinished = false;
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			loadingFinished = false;
		}

		@Override
		public void onPageFinished(final WebView view, String url)
		{
			super.onPageFinished(view, url);
			//Services.Log.info("onPageFinished", " " + url);
			if (!redirect)
		       loadingFinished = true;

			if (loadingFinished && !redirect)
		    {
		    	// try to re measure parent.
				// finish load url in webview.
				Services.Log.info("onPageFinished", "Url: " + url);
	       	}
		    else
	       		redirect = false;
		}
	}

	private void viewInBrowserYoutube(String url) {

		String vid= Uri.parse(url).getQueryParameter("v");
		if (!Services.Strings.hasValue(vid))
		{
			vid= Uri.parse(url).getLastPathSegment();
		}
		//Call directly to youtube app
		Uri uri = Uri.parse("vnd.youtube:" + vid);
		//Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + url));
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		//Intent next = new Intent("android.intent.action.VIEW", Uri.parse( url));
		List<ResolveInfo> list = getContext().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		if (list.size() == 0) {
			viewInBrowser( url);
		}
		else
		{
			//start youtube
			getContext().startActivity(intent);
		}
	}

	private void viewInBrowser(String url)
	{
		Intent next = new Intent("android.intent.action.VIEW", Uri.parse(url));
		IntentFactory.setIntentFlagsNewDocument(next);
		getContext().startActivity(next);
	}


	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		if (mCoordinator != null && mCoordinator.hasAnyEventHandler(this, GxTouchEvents.getAllEvents()))
			return false; // Let custom events execute. This will disable scrolling, zooming, &c though.

		// Handle special gestures (e.g. tap to open YouTube app).
		if (mGestureDetector.onTouchEvent(e))
			return true;

		return super.onTouchEvent(e);
	}

	private final GestureDetector mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener()
	{
		@Override
		public boolean onDoubleTap(MotionEvent e)
		{
			// double tab only show video youtube, not open component in new window anymore.
			if (mUrl!=null && mUrl.length()>0)
			{
				Services.Log.info("onDoubleTap", "Url: " + mUrl);
				if (mUrl.contains("www.youtube.com"))
					viewInBrowserYoutube(mUrl);
				else
					return super.onDoubleTap(e);
				//else
				//	ActivityLauncher.CallComponent(getContext(), mUrl);
			}
			else
				return super.onDoubleTap(e);
			//else if (mHtml!=null && mHtml.length()>0)
			//	ActivityLauncher.CallComponentHtml(getContext(), mHtml);
			return true;
		}
	});

	public void navigate(String url)
	{
		//Services.Log.debug("navigate", url);
		if (!Strings.starsWithIgnoreCase(url, "http:") && !Strings.starsWithIgnoreCase(url, "https:") && !Strings.starsWithIgnoreCase(url, "file:"))
			url = Services.Application.get().linkObjectUrl(url);

		// Don't call loadUrl() if the URL is the same as the current one. This is a common occurrence when
		// loading with cache data, then server data afterwards. Note that we check against getUrl() instead of mUrl
		// because the user might have navigated away if the WebView allows it.
		String currentUrl = getUrl();
		if (currentUrl != null && currentUrl.equalsIgnoreCase(url))
			return;

		mUrl = url;

		setWebViewClient(new MyWebViewClient());
		setWebChromeClient(new MyWebChromeClient());

		// adjust page width, https://stackoverflow.com/a/7480473
		getSettings().setLoadWithOverviewMode(true);
		getSettings().setUseWideViewPort(true);

		// do not load url if local and is not allowed
		if (!Services.Application.get().getAllowWebViewExecuteLocalFiles() && Strings.starsWithIgnoreCase(url, "file:"))
			return;

		// load url
		if (Services.Application.get().getShareSessionToWebView())
			TaskRunner.execute(new WebViewTaskGxWebView());
		else
			super.loadUrl(url);


		if (Services.Strings.hasValue(mUrl) && mUrl.contains("www.youtube.com"))
		{
			// double tap to play in youtube. in place player should work
			Services.Messages.showMessage(getContext(), R.string.GXM_TapToPlay);
		}
	}

	//Task to allow share session between webview and httpclient.
	@SuppressWarnings("deprecation")
	private class WebViewTaskGxWebView extends TaskRunner.BaseTask<Boolean>
	{
		private CookieManager cookieManager;
		private List<HttpCookie> mCookies;

		@Override
		public void onPreExecute()
		{
			CookieSyncManager.createInstance(GxWebView.this.getContext());
			cookieManager = CookieManager.getInstance();

			// Copied from
			// http://www.walletapp.net/android-passing-cookie-to-webview

			// get cookies from Okhttp client
			mCookies = Services.HttpService.getCookieManager().getCookieStore().getCookies();

			//delete old cookies , seems not necessary in our case
			//cookieManager.removeSessionCookie();
			// do it to remove any previous session with facebook twitter etc.
			//cookieManager.removeAllCookie();
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

			//WebSettings webSettings = GxWebView.this.getSettings();
			//if (Services.Application.get().getAllowWebViewExecuteJavascripts())
			//	webSettings.setJavaScriptEnabled(true);
			//webSettings.setBuiltInZoomControls(true);
			//mWebview.setWebViewClient(new WebViewActivity.MyWebViewClient());
			GxWebView.super.loadUrl(mUrl);
		}
	}

	public void loadHtml(String html)
	{
		loadHtml(html, mThemeClass);
	}

	public void loadHtml(String html, ThemeClassDefinition themeClass)
	{
		//mHtml = html;
		//int size = LayoutHelper.convertDipToPixels(getContext(), 64);
		//this.setMinimumHeight(size);

		//Services.Log.debug("loadHtml "+ html + (themeClass!=null?themeClass.getName():"null") + " test");
		String backColor = null;
		String foreColor = null;
		String fontSize = null;
		String fontFamily = null;
		if (themeClass != null) {
			backColor = getBackColor(themeClass);
			foreColor = getForeColor(themeClass);
			fontSize = getFontSize(themeClass);
			fontFamily = getFontFamily(themeClass);
		}

		if ((foreColor == null) || (foreColor.length() == 0)) {
			foreColor = ThemeUtils.getAndroidThemeColor(getContext(), android.R.attr.textColorPrimary);
		}

		// Services.Log.info("GxWebView", "B " + backColor);
		// Services.Log.info("GxWebView", "F " + foreColor);

		String backgroundHtml = "";
		if (Services.Strings.hasValue(backColor)) {
			backgroundHtml = "background-color: " + backColor + "; ";
		}
		String forecolorHtml = "";
		if (Services.Strings.hasValue(foreColor)) {
			forecolorHtml = " color: " + foreColor + ";";
		}
		String fontSizeHtml = "";
		if (Services.Strings.hasValue(fontSize)) {
			fontSizeHtml = " font-size: " + fontSize + "px;";
		}
		String fontFamilyHtml = "";
		String fontFamilyExtraHtml = "";
		if (Services.Strings.hasValue(fontFamily)) {
			fontFamilyHtml = " @font-face { font-family: " + fontFamily + "; src: url('file:///android_asset/fonts/" + fontFamily + ".ttf'); }";
			fontFamilyExtraHtml = " font-family: " + fontFamily + ";";
		}

		String htmlToLoad = html;
		if (htmlToLoad != null && !Strings.toLowerCase(htmlToLoad).startsWith("<html>") && !Strings.toLowerCase(htmlToLoad).startsWith("<!doctype"))
		{
			// Append header for background / foreground color.
			htmlToLoad = "<html> <head><style type=\"text/css\">" + fontFamilyHtml + " body {" + fontFamilyExtraHtml + backgroundHtml + forecolorHtml + fontSizeHtml + " } a { color: #ddf; }</style></head><body>" +
					htmlToLoad +
					"</body></html>";
		}

		setWebViewClient(new MyWebViewClient());
		setWebChromeClient(new MyWebChromeClient());

		//Services.Log.info("loadHtml", " " + htmlToLoad);

		// Use a baseURL so it has a default for when other urls don't have a procotol or host, use Services URL as baseURL, the better thing would be to have a BaseUrl property in GX
		loadDataWithBaseURL(Services.Application.get().getAPIUri(), htmlToLoad, "text/html", "utf-8", "about:blank");

		if ((backColor == null) || (backColor.length() == 0)) {
			// Transparent background AFTER loading HTML, as per
			// http://stackoverflow.com/questions/5003156/android-webview-style-background-colortransparent-ignored-on-android-2-2/5899705#5899705
			// Set alpha to 1, so it 'looks' transparent. It might have a slight performance penalty.
			// See: http://stackoverflow.com/a/18256729/1922917
			// This solves a problem that on devices >= 3.0, transparent background on a WebView causes flickering when scrolling with hardwareAcceleration on.
			setBackgroundColor(0x01000000);
		}
		mLoadAsHtmlDone = true;
	}

	private String getBackColor(ThemeClassDefinition themeClass)
	{
		String color = themeClass.getBackgroundColor();
		if (color != null && color.contains("#") && color.length() == 9)
			color = color.substring(0, 7);
		return color;
	}

	private String getForeColor(ThemeClassDefinition themeClass) {
		String color = themeClass.getColor();
		if (color != null && color.contains("#") && color.length() == 9)
			color = color.substring(0, 7);
		return color;
	}

	private String getFontSize(ThemeClassDefinition themeClass) {
		// Important detail: HTML "pixel" font sizes are equivalent to Android's DPs.
		// Since getFontSize() returns a value in pixels, we need to "convert back" here.
		Integer fontSize = themeClass.getFont().getFontSize();
		if (fontSize != null && fontSize != 0)
			return String.valueOf(Services.Device.pixelsToDips(fontSize));
		else
			return null;
	}

	private String getFontFamily(ThemeClassDefinition themeClass) {
		return themeClass.getFont().getFontFamily();
	}

	private boolean mLoadAsHtml = true;

	public void setMode(boolean loadAsHtml)
	{
		mLoadAsHtml = loadAsHtml;
	}

	private String mValue = Strings.EMPTY;
	@Override
	public String getGxValue() {
		return mValue;

	}

	@Override
	public void setGxValue(String value) {
		mValue = value;
		if (mValue!=null && mValue.length()>0)
		{
			if (mLoadAsHtml)
			{
				loadHtml(mValue);
			}
			else
			{
				navigate(mValue);
			}
		}
		else
		{
			if (mLoadAsHtml)
			{
				loadHtml(Strings.EMPTY);
			}

		}
	}

	@Override
	public String getGxTag() {
		return (String)this.getTag();

	}

	@Override
	public void setGxTag(String data) {
		this.setTag(data);
	}

	@Override
	public void setValueFromIntent(Intent data) {
	}

	@Override
	public IGxEdit getViewControl() {
		return this;
	}

	@Override
	public IGxEdit getEditControl() {
		return this;
	}

	@Override
	public void applyEditClass(@NonNull ThemeClassDefinition themeClass)
	{
		mThemeClass = themeClass; // Prepare for next loadHtml().

		// If already loaded, reload it.
		if (mLoadAsHtml && mLoadAsHtmlDone && Strings.hasValue(mValue))
			loadHtml(mValue, themeClass);
	}

	@Override
	public void destroy() {
		synchronized(LOCK)
		{
			sIsWorking = false;
		}
		setWebChromeClient(null);
		super.destroy();
	}

	//Added to allow show loading in action bar from webview status.
	//TODO: see if need in webviewfragment. WebviewActivity should show action bar?
	private static final Object LOCK = new Object();
	private static boolean sIsWorking = false;

	public class MyWebChromeClient extends WebChromeClient
	{
		@Override
		public void onProgressChanged(WebView view, int progress)
		{
			// Activities and WebViews measure progress with different scales.
			// The progress meter will automatically disappear when we reach 100%
			if (getContext() instanceof Activity)
			{
				Activity myActivity = (Activity)getContext();
				SherlockHelper.setProgress(myActivity, progress * 100);
			}
			synchronized(LOCK)
			{
				sIsWorking = progress < 100;
			}
		}
	}

	public static boolean isWorking()
	{
		synchronized(LOCK)
		{
			return sIsWorking;
		}
	}

	@Override
	public boolean isEditable()
	{
		return false; // Never editable.
	}
}
