package com.genexus.android.facebook.api;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.facebook.bolts.AppLinks;
import com.genexus.android.core.actions.DynamicCallAction;
import com.genexus.android.core.actions.UIContext;
import com.genexus.android.core.activities.IIntentHandler;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.facebook.FacebookSdk;
import com.facebook.applinks.AppLinkData;

class AppLinksGx implements IIntentHandler {

	private static final String KEY_NAME_TARGET = "target_url";

	void initialize(Context context) {
		if (Strings.hasValue(FacebookSdk.getApplicationId())) {
			AppLinkData.fetchDeferredAppLinkData(context, new AppLinkData.CompletionHandler() {
				@Override
				public void onDeferredAppLinkDataFetched(AppLinkData appLinkData) {
					// TODO: We should open the deferred URI by passing it to the current activity.
					/*
					if (appLinkData != null)
					{
						Uri deferredTargetUri = appLinkData.getTargetUri();
						// ...
					}
					*/
				}
			});
		}
	}

	@Override
	public boolean tryHandleIntent(UIContext context, Intent intent, Entity entity) {
		Uri targetUrl = getTargetUrlFromInboundIntent(intent);
		if (targetUrl != null) {
			int iStartTarget = intent.getDataString().lastIndexOf("?");
			String myLink = intent.getDataString().substring(0, iStartTarget);
			// At this moment we are just supporting linking but no actions so we just create a DynamicCallAction and execute it
			String dynamicAll = createDynamicCallFromTargetUrl(myLink);
			DynamicCallAction.redirect(context, null, dynamicAll, true);
			return true;
		}

		return false;
	}

	private static Uri getTargetUrlFromInboundIntent(Intent intent) {
		Bundle appLinkData = AppLinks.getAppLinkData(intent);
		if (appLinkData != null) {
			String targetString = appLinkData.getString(KEY_NAME_TARGET);
			if (targetString != null)
				return Uri.parse(targetString);
		}

		return null;
	}

	private String createDynamicCallFromTargetUrl(String targetUrl) {
		String protocolHandler = String.format("%s://", Services.Application.getAppsLinksProtocol());
		targetUrl = targetUrl.replace(protocolHandler, "sd:");
		// Now is not coming encoded so here we support for  "Obj.Detal?123,124"  = "Obj-Detail---123--124"
		targetUrl = targetUrl.replace("---", "?");
		targetUrl = targetUrl.replace("--", ",");
		targetUrl = targetUrl.replace("-", ".");
		return targetUrl;
	}
}
