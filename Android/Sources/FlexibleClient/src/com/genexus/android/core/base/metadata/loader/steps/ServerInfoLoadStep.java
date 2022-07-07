package com.genexus.android.core.base.metadata.loader.steps;

import android.content.Context;

import com.genexus.android.core.base.metadata.GenexusApplication;
import com.genexus.android.core.base.metadata.loader.MetadataLoadStep;
import com.genexus.android.core.base.metadata.loader.MetadataLoader;
import com.genexus.android.core.base.serialization.INodeObject;
import com.genexus.android.core.base.services.Services;

public class ServerInfoLoadStep implements MetadataLoadStep {
	private final Context mContext;
	private final GenexusApplication mApplication;

	public ServerInfoLoadStep(Context context, GenexusApplication application) {
		mContext = context;
		mApplication = application;
	}

	@Override
	public void load() {
		INodeObject appId = MetadataLoader.getDefinition(mContext, "appid");
		if (appId != null) {
			String id = appId.getString("id");
			if (id != null) {
				mApplication.setAppId(id);
			} else {
				Services.Log.error("Invalid 'id' field in appid metadata");
			}

			Integer serverType = (Integer) appId.get("servertype");
			if (serverType != null) {
				mApplication.setServerType(serverType);
			} else {
				Services.Log.error("Invalid 'ServerType' field in appid metadata");
			}
		} else {
			Services.Log.error("Could not parse 'appid' metadata");
		}
	}
}
