package com.genexus.android.twitter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.genexus.android.core.compatibility.SherlockHelper;

public class TwitterAuthWebView extends AppCompatActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		String url = intent.getStringExtra(TwitterApiConstants.AUTH_URL);
		if (TextUtils.isEmpty(url)) {
			finish();
			return;
		}
		
		WebView webView = createCustomWebView(url);
		setContentView(webView);
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	private WebView createCustomWebView(String url) {
		WebView webView = new WebView(this);
		
		// Enable JavaScript and Built-in zoom
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setBuiltInZoomControls(true);
		
		webView.setWebViewClient(new CustomWebViewClient());
		webView.setWebChromeClient(new CustomWebChromeClient());
		
		webView.loadUrl(url);
		
		SherlockHelper.requestWindowFeature(this, Window.FEATURE_PROGRESS);
		SherlockHelper.requestWindowFeature(this, Window.FEATURE_INDETERMINATE_PROGRESS);
		
		return webView;
	}

	@SuppressWarnings("deprecation")
	private class CustomWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// Only load pages from the Twitter API domain.
			if (Uri.parse(url).getHost().equals(TwitterApiConstants.ENDPOINT_DOMAIN)) {
				SherlockHelper.setProgressBarIndeterminateVisibility(TwitterAuthWebView.this, true);
				return false;
			}
			
			// If the callback URI is called, we get the authorization result.
			if (url != null && url.startsWith(TwitterApiConstants.CALLBACK_URI)) {
				Uri uri = Uri.parse(url);
				sendAuthorizationResult(uri);
				return true;
			}
			
			// Otherwise, launch another activity to handle the link
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(intent);
			
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			SherlockHelper.setProgressBarIndeterminateVisibility(TwitterAuthWebView.this, false);
			super.onPageFinished(view, url);
		}
	}
	
	private class CustomWebChromeClient extends WebChromeClient {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			SherlockHelper.setProgress(TwitterAuthWebView.this, newProgress * 100);
		}
	}
	
	private void sendAuthorizationResult(Uri uri) {
		String oauthVerifier = uri.getQueryParameter(TwitterApiConstants.OAUTH_VERIFIER);
		
		Intent resultIntent = new Intent();
		resultIntent.putExtra(TwitterApiConstants.AUTHORIZATION_RESULT, oauthVerifier);
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}

}
