package com.genexus.android.analytics.google;

import android.app.Activity;
import android.content.Context;

import com.genexus.android.core.actions.UIContext;
import com.genexus.android.analytics.CommerceAnalyticsProvider;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.IViewDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.google.android.gms.analytics.ExceptionReporter;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;

/**
 * Google Analytics Provider.
 */
class GoogleAnalyticsProvider implements CommerceAnalyticsProvider
{
	private GoogleAnalytics mGoogleAnalytics;
	private Tracker mTracker;
	private Tracker mCommerceTracker;
	private StandardExceptionParser mExceptionParser;

	public GoogleAnalyticsProvider(Context context)
	{
		context = context.getApplicationContext();

		// Initialize Google Analytics (if able to).
		String trackingId = context.getString(R.string.ga_trackingId);
		if (Strings.hasValue(trackingId))
		{
			// Set up single application-wide Tracker.
			mGoogleAnalytics = GoogleAnalytics.getInstance(context);
			mTracker = mGoogleAnalytics.newTracker(trackingId);

			// For debugging.
			// mGoogleAnalytics.getLogger().setLogLevel(com.google.android.gms.analytics.Logger.LogLevel.VERBOSE);

			// Set up exception reporting.
			Thread.UncaughtExceptionHandler exceptionHandler = new ExceptionReporter(mTracker, Thread.getDefaultUncaughtExceptionHandler(), context);
			Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);
			mExceptionParser = new StandardExceptionParser(context, null);
		}
		else
			Services.Log.error("Failed to set up Google Anlytics, because 'ga_trackingId' is missing in the configuration file!");
	}

	private Tracker getCommerceTracker()
	{
		return (mCommerceTracker == null) ? mTracker : mCommerceTracker;
	}

	@Override
	public void onActivityStart(Activity activity)
	{
		if (mGoogleAnalytics == null)
			return;

		mGoogleAnalytics.reportActivityStart(activity);
	}

	@Override
	public void onActivityStop(Activity activity)
	{
		if (mGoogleAnalytics == null)
			return;

		mGoogleAnalytics.reportActivityStop(activity);
	}

	@Override
	public void onComponentStart(Activity activity, IViewDefinition definition)
	{
		if (mTracker == null)
			return;

		mTracker.setScreenName(definition.getName());
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
	}

	@Override
	public void onAction(UIContext context, ActionDefinition action)
	{
		if (mTracker == null)
			return;

		String screenName = "<Unknown>";
		if (action.getViewDefinition() != null)
			screenName = action.getViewDefinition().getName();

		mTracker.setScreenName(screenName);

		// Report as "Event".
		mTracker.send(new HitBuilders.EventBuilder()
				.setCategory(screenName)
				.setAction(action.getName())
				.setLabel(Strings.EMPTY)
				.build());
	}

	@Override
	public void onException(Exception e)
	{
		if (mTracker == null)
			return;

		mTracker.send(new HitBuilders.ExceptionBuilder()
				.setDescription(mExceptionParser.getDescription(Thread.currentThread().getName(), e))
				.setFatal(false)
				.build());
	}

	@SuppressWarnings("deprecation")
	@Override
	public void trackPurchase(String transactionId, String affiliation, double revenue, double tax, double shipping, String currencyCode) {
		if (getCommerceTracker() == null)
			return;

		getCommerceTracker().send(new HitBuilders.TransactionBuilder()
				.setTransactionId(transactionId)
				.setAffiliation(affiliation)
				.setRevenue(revenue)
				.setTax(tax)
				.setShipping(shipping)
				.setCurrencyCode(currencyCode)
				.build());
	}

	@SuppressWarnings("deprecation")
	@Override
	public void trackPurchaseItem(String transactionId, String itemId, String name, String category, double price, long quantity, String currencyCode) {
		if (getCommerceTracker() == null)
			return;

		getCommerceTracker().send(new HitBuilders.ItemBuilder()
				.setTransactionId(transactionId)
				.setSku(itemId)
				.setName(name)
				.setCategory(category)
				.setPrice(price)
				.setQuantity(quantity)
				.setCurrencyCode(currencyCode)
				.build());
	}


	@Override
	public void setUserId(String userId)
	{
		if (getCommerceTracker() == null)
			return;

		getCommerceTracker().set("&uid", userId);
	}

	@Override
	public void setCommerceTrackerId(String trackerId) {
		if (mGoogleAnalytics != null)
			mCommerceTracker = mGoogleAnalytics.newTracker(trackerId);
	}

	@Override
	public void trackView(Activity activity, String viewName) {
		if (getCommerceTracker() == null)
			return;

		getCommerceTracker().setScreenName(viewName);
		getCommerceTracker().send(new HitBuilders.ScreenViewBuilder().build());
	}

	@Override
	public void trackEvent(String category, String action, String label, long value) {
		if (getCommerceTracker() == null)
			return;

		getCommerceTracker().send(new HitBuilders.EventBuilder()
				.setCategory(category)
				.setAction(action)
				.setLabel(label)
				.setValue(value)
				.build());
	}
}
