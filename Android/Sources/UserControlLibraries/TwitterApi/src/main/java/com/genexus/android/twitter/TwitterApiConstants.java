package com.genexus.android.twitter;

import com.genexus.android.core.base.services.Services;

public class TwitterApiConstants {
	public static final String CONSUMER_KEY = Services.Strings.getResource(R.string.TwitterConsumerKey);
	public static final String CONSUMER_SECRET = Services.Strings.getResource(R.string.TwitterConsumerSecret);
	
	public static final String PREFERENCES_KEY = "twitter_session";
	public static final String PREF_NAME = "twitter_oauth";
	public static final String PREF_KEY_TOKEN = "twitter_oauth_token";
	public static final String PREF_KEY_TOKEN_SECRET = "twitter_oauth_token_secret";
	
	public static final String ENDPOINT_DOMAIN = "api.twitter.com";
	public static final String CALLBACK_URI = "https://twitter4j";
	
	public static final String AUTH_URL = "auth_url";
	public static final String OAUTH_VERIFIER = "oauth_verifier";
	public static final String PENDING_OPERATION = "pending_operation";
	public static final String AUTHORIZATION_RESULT = "authorization_result";
	
	public static final String SHOW_STATUS_URI = "twitter://status?status_id=";
	public static final String SHOW_USER_PROFILE_URI = "twitter://user?screen_name=";
	public static final String WEB_TWEET_URL = "https://twitter.com/intent/tweet?source=webclient&text=";
	public static final String MOBILE_URL = "https://mobile.twitter.com/";
	
	static final String[] TWITTER_PACKAGES = {"com.twitter.android", "com.twidroid", "com.handmark.tweetcaster", "com.thedeck.android"};
}
