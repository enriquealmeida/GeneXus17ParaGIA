package com.genexus.android.analytics.firebase;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.genexus.android.core.actions.UIContext;
import com.genexus.android.analytics.CommerceAnalyticsProvider;
import com.genexus.android.analytics.IGxAnalyticsControl;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.IViewDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.utils.Cast;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Locale;

class FirebaseAnalyticsProvider implements CommerceAnalyticsProvider {
    private FirebaseAnalytics mFirebaseAnalytics;

    public FirebaseAnalyticsProvider(Context context) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Override
    public void trackPurchase(String transactionId, String affiliation, double revenue, double tax, double shipping, String currencyCode) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.TRANSACTION_ID, transactionId);
        bundle.putString(FirebaseAnalytics.Param.AFFILIATION, affiliation);
        bundle.putDouble(FirebaseAnalytics.Param.VALUE, revenue);
        bundle.putDouble(FirebaseAnalytics.Param.TAX, tax);
        bundle.putDouble(FirebaseAnalytics.Param.SHIPPING, shipping);
        bundle.putString(FirebaseAnalytics.Param.CURRENCY, currencyCode);
		Services.Device.runOnUiThread(() -> mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.PURCHASE, bundle));
    }

    @Override
    public void trackPurchaseItem(String transactionId, String itemId, String name, String category, double price, long quantity, String currencyCode) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.TRANSACTION_ID, transactionId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, itemId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, category);
        bundle.putDouble(FirebaseAnalytics.Param.PRICE, price);
        bundle.putLong(FirebaseAnalytics.Param.QUANTITY, quantity);
        bundle.putString(FirebaseAnalytics.Param.CURRENCY, currencyCode);
		Services.Device.runOnUiThread(() -> mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART, bundle));
    }

    @Override
    public void setUserId(String userId) {
        mFirebaseAnalytics.setUserId(userId);
    }

    @Override
    public void setCommerceTrackerId(String trackerId) {
        // TODO
    }

    @Override
    public void trackView(Activity activity, String viewName) {
		Services.Device.runOnUiThread(() -> mFirebaseAnalytics.setCurrentScreen(activity, viewName, null));
    }

	@Override
	public void onComponentStart(Activity activity, IViewDefinition definition) {
		Services.Device.runOnUiThread(() -> mFirebaseAnalytics.setCurrentScreen(activity, definition.getName(), null));
	}

	@Override
    public void trackEvent(String category, String action, String label, long value) {
        Bundle bundle = new Bundle();
        bundle.putString("gx_category", category);
        bundle.putString("gx_label", label);
        bundle.putLong("gx_value", value);
        logEvent(action, bundle);
    }

	@Override
	public void onAction(UIContext context, ActionDefinition action) {
    	if (action.getName().equals("ClientStart"))
    		return; // don't log it because of firebase events limitation

		String screenName = "<Unknown>";
		String eventName;
		if (action.getViewDefinition() != null) {
			screenName = action.getViewDefinition().getName();
			String objectName = action.getViewDefinition().getObjectName();
			int index = objectName.lastIndexOf('.');
			if (index != -1) {
				objectName = objectName.substring(index + 1); // remove module
			}
			eventName = objectName + "_" + action.getName();
		} else {
			eventName = action.getName();
		}

		IGxAnalyticsControl c = context.getAnchor() == null ? null : Cast.as(IGxAnalyticsControl.class, context.getAnchor().getView());
		String label = c != null ? c.getLabel() : "";
		long value = c != null ? c.getValue() : 0;
		trackEvent(screenName, eventName, label, value);
	}

    private void logEvent(String name, Bundle bundle) {
		StringBuilder eventNameBuilder = new StringBuilder();
		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if (c == '.')
				eventNameBuilder.append("_");
			else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <='9') || c == '_')
				eventNameBuilder.append(c);
		}
		String eventName = eventNameBuilder.toString();
		if (eventName.length() > 0) {
			eventName = eventName.length() > 40 ? eventName.substring(eventName.length() - 40) : eventName;
			if (eventName.charAt(0) == '_')
				eventName = eventName.substring(1);
			if (eventName.toUpperCase(Locale.US).charAt(0) < 'A' || eventName.toUpperCase(Locale.US).charAt(0) > 'Z')
				eventName = "A" + eventName.substring(1);

			final String finalEventName = eventName;
			Services.Device.runOnUiThread(() -> mFirebaseAnalytics.logEvent(finalEventName, bundle));
		}
	}

    @Override
    public void onActivityStart(Activity activity) { }

    @Override
    public void onActivityStop(Activity activity) { }

    @Override
    public void onException(Exception e) {
        Bundle bundle = new Bundle();
        bundle.putString("gx_message", e.getMessage());
		Services.Device.runOnUiThread(() -> mFirebaseAnalytics.logEvent("gx_exception", bundle));
    }
}
