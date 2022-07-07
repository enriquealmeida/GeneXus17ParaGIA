package com.genexus.android.analytics.hms;

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

//import classes from Analytics Kit
import com.huawei.hms.analytics.HiAnalytics;
import com.huawei.hms.analytics.HiAnalyticsInstance;
import com.huawei.hms.analytics.type.HAEventType;
import com.huawei.hms.analytics.type.HAParamType;

import java.util.Locale;


class HMSAnalyticsProvider implements CommerceAnalyticsProvider {
    private HiAnalyticsInstance mHiAnalytics;
	private static String sClientStart = "ClientStart";

    public HMSAnalyticsProvider(Context context) {

		// Initiate Analytics Kit
		// Enable Analytics Kit Log
    	//HiAnalyticsTools.enableLog();

        mHiAnalytics = HiAnalytics.getInstance(context);

    }

   	@Override
    public void trackPurchase(String transactionId, String affiliation, double revenue, double tax, double shipping, String currencyCode) {
        Bundle bundle = new Bundle();
        bundle.putString(HAParamType.TRANSACTIONID, transactionId);
        // TODO: check for a equivalent to AFFILIATION
        bundle.putString(HAParamType.PLACE, affiliation);
        bundle.putDouble(HAParamType.REVENUE, revenue);
        bundle.putDouble(HAParamType.TAXFEE, tax);
        bundle.putDouble(HAParamType.SHIPPING, shipping);
        bundle.putString(HAParamType.CURRNAME, currencyCode);

        Services.Device.runOnUiThread(() -> mHiAnalytics.onEvent(HAEventType.COMPLETEPURCHASE, bundle));
    }

    @Override
    public void trackPurchaseItem(String transactionId, String itemId, String name, String category, double price, long quantity, String currencyCode) {
        Bundle bundle = new Bundle();
        bundle.putString(HAParamType.TRANSACTIONID, transactionId);
        bundle.putString(HAParamType.PRODUCTID, itemId);
        bundle.putString(HAParamType.PRODUCTNAME, name);
        bundle.putString(HAParamType.CATEGORY, category);
        bundle.putDouble(HAParamType.PRICE, price);
        bundle.putLong(HAParamType.QUANTITY, quantity);
        bundle.putString(HAParamType.CURRNAME, currencyCode);
		Services.Device.runOnUiThread(() -> mHiAnalytics.onEvent(HAEventType.ADDPRODUCT2CART, bundle));
    }

    @Override
    public void setUserId(String userId) {
		mHiAnalytics.setUserId(userId);
    }

    @Override
    public void setCommerceTrackerId(String trackerId) {
        // TODO
    }

    //By default, the HMS Core Analytics SDK automatically collects activity screen events.
	// Therefore, you do not need to manually set tracing points.
	// To collect non-activity screen events, you are advised to use
	// the pageStart(String pageName, String pageClassOverride) and pageEnd(String pageName) APIs.
	@Override
    public void trackView(Activity activity, String viewName) {
		Services.Device.runOnUiThread(() -> mHiAnalytics.pageStart(viewName, activity.toString()));
    }

	@Override
	public void onComponentStart(Activity activity, IViewDefinition definition) {
		Services.Device.runOnUiThread(() -> mHiAnalytics.pageStart(definition.getName(), activity.toString()));
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
    	if (action.getName().equals(sClientStart))
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
			Services.Device.runOnUiThread(() -> mHiAnalytics.onEvent(finalEventName, bundle));
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
		Services.Device.runOnUiThread(() -> mHiAnalytics.onEvent("gx_exception", bundle));
    }
}
