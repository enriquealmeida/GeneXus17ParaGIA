package com.genexus.android.websockets;

import android.app.Application;
import android.content.Context;

import com.genexus.android.core.application.LifecycleListeners;
import com.genexus.android.core.base.services.IApplication;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.externalapi.ExternalApiDefinition;
import com.genexus.android.core.externalapi.ExternalApiFactory;
import com.genexus.android.core.framework.GenexusModule;

public class WebSocketClientModule implements GenexusModule, LifecycleListeners.MetadataLoading {

	@Override
	public void initialize(Context context) {
		ExternalApiFactory.addApi(new ExternalApiDefinition(WebSocketClient.OBJECT_NAME, WebSocketClient.class));
		Services.Application.getLifecycle().registerOnMetadataLoadFinished(this);
	}

	@Override
	public void onMetadataLoadFinished(IApplication application) {
		Foreground.init((Application)application);
		Services.Application.getLifecycle().registerComponentEventsListener(WebSocketsServiceFactory.getInstance());
	}
}
