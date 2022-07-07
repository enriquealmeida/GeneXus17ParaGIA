package com.genexus.android.analytics;

import android.app.Activity;

/**
 * Interface for Commerce Analytics Provider
 */
public interface CommerceAnalyticsProvider extends AnalyticsProvider {
    void trackPurchase(String transactionId, String affiliation, double revenue, double tax, double shipping, String currencyCode);
    void trackPurchaseItem(String transactionId, String itemId, String name, String category, double price, long quantity, String currencyCode);
    void setUserId(String userId);
    void setCommerceTrackerId(String trackerId);
    void trackView(Activity activity, String viewName);
    void trackEvent(String category, String action, String label, long value);
}
