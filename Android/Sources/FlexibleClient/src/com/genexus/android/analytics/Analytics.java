package com.genexus.android.analytics;

import android.app.Activity;
import androidx.annotation.NonNull;

import com.genexus.android.core.actions.UIContext;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.IViewDefinition;

/**
 * Analytics dispatcher.
 */
public class Analytics
{
	private static AnalyticsProvider sProvider;
	private static CommerceAnalyticsProvider sCommerceProvider;

	public static void setProvider(AnalyticsProvider provider)
	{
		sProvider = provider;
		if (provider instanceof CommerceAnalyticsProvider) {
			sCommerceProvider = (CommerceAnalyticsProvider)provider;
		}
	}

	public static void onActivityStart(@NonNull Activity activity)
	{
		if (sProvider != null)
			sProvider.onActivityStart(activity);
	}

	public static void onActivityStop(@NonNull Activity activity)
	{
		if (sProvider != null)
			sProvider.onActivityStop(activity);
	}

	public static void onComponentStart(Activity activity, IViewDefinition componentDefinition)
	{
		if (sProvider != null && activity != null && componentDefinition != null)
			sProvider.onComponentStart(activity, componentDefinition);
	}

	public static void onAction(@NonNull UIContext context, @NonNull ActionDefinition action)
	{
		if (sProvider != null)
			sProvider.onAction(context, action);
	}

	public static void onException(@NonNull Exception e)
	{
		if (sProvider != null)
			sProvider.onException(e);
	}

	public static void trackView(Activity activity, String viewName)
	{
		if (sCommerceProvider != null)
			sCommerceProvider.trackView(activity, viewName);
	}

	public static void setUserId(String userId)
	{
		if (sCommerceProvider != null)
			sCommerceProvider.setUserId(userId);
	}

	public static void trackEvent(String category, String action, String label, long value)
	{
		if (sCommerceProvider != null)
			sCommerceProvider.trackEvent(category, action, label, value);
	}

	public static void trackPurchase(String transactionId, String affiliation, double revenue, double tax, double shipping, String currencyCode)
	{
		if (sCommerceProvider != null)
			sCommerceProvider.trackPurchase(transactionId, affiliation, revenue, tax, shipping, currencyCode);
	}

	public static void trackPurchaseItem(String transactionId, String itemId, String name, String category, double price, long quantity, String currencyCode)
	{
		if (sCommerceProvider != null)
			sCommerceProvider.trackPurchaseItem(transactionId, itemId, name, category, price, quantity, currencyCode);
	}

	public static void setCommerceTrackerId(String trackerId)
	{
		if (sCommerceProvider != null)
			sCommerceProvider.setCommerceTrackerId(trackerId);
	}
}
