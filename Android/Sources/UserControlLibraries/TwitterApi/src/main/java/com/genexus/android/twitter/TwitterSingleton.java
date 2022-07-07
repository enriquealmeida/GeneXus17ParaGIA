package com.genexus.android.twitter;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterSingleton {
	private static Twitter sTwitter = null;
	
	public static Twitter getInstance() {
		if (sTwitter == null) {
			sTwitter = createInstance();
		}

		return sTwitter;
	}

	private static Twitter createInstance() {
		Twitter twitter;

		if (!Strings.hasValue(TwitterApiConstants.CONSUMER_KEY) || !Strings.hasValue(TwitterApiConstants.CONSUMER_SECRET)) {
			Services.Log.error("TwitterApi", "Consumer key/secret is empty.");
		}

		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey(TwitterApiConstants.CONSUMER_KEY);
		builder.setOAuthConsumerSecret(TwitterApiConstants.CONSUMER_SECRET);
		Configuration config = builder.build();

		TwitterFactory factory = new TwitterFactory(config);

		boolean isAuthorized = Services.Preferences.getAppSharedPreferences(TwitterApiConstants.PREFERENCES_KEY).getString(TwitterApiConstants.PREF_KEY_TOKEN, null) != null;
		if (isAuthorized) {
			String accessTokenStr = Services.Preferences.getAppSharedPreferences(TwitterApiConstants.PREFERENCES_KEY).getString(TwitterApiConstants.PREF_KEY_TOKEN, "");
			String accessTokenSecretStr = Services.Preferences.getAppSharedPreferences(TwitterApiConstants.PREFERENCES_KEY).getString(TwitterApiConstants.PREF_KEY_TOKEN_SECRET, "");
			AccessToken accessToken = new AccessToken(accessTokenStr, accessTokenSecretStr);

			twitter = factory.getInstance(accessToken);
		} else {
			twitter = factory.getInstance();
		}

		return twitter;
	}
	
	public static void destroyInstance() {
		sTwitter = null;
	}
}
