package com.genexus.android.core.base.metadata.loader.steps;

import android.content.Context;

import com.genexus.android.core.base.metadata.GenexusApplication;
import com.genexus.android.core.base.metadata.loader.MetadataLoadStep;
import com.genexus.android.core.base.metadata.loader.MetadataLoader;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;

public class DeepLinksLoadStep implements MetadataLoadStep {
	private final Context mContext;
	private final GenexusApplication mApplication;

	public DeepLinksLoadStep(Context context, GenexusApplication application) {
		mContext = context;
		mApplication = application;
	}

	@Override
	public void load() {
		String file = mApplication.getAppEntry().toLowerCase() + ".deeplink";
		INodeObject deepLink = MetadataLoader.getDefinition(mContext, file, true);
		if (deepLink != null) {
			for (INodeObject obj : deepLink.getCollection("DeepLinks")) {
				String link = obj.getString("link");
				String panel = obj.getString("panel");
				Services.Application.getDefinition().putDeepLink(link, panel);
			}
		}
	}
}
