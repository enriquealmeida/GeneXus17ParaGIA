package com.genexus.android.core.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.genexus.android.core.base.metadata.GenexusApplication;
import com.genexus.android.core.base.services.IApplication;
import com.genexus.android.core.base.services.IEntityProvider;
import com.genexus.android.core.framework.GenexusModule;

public abstract class MyApplication extends Application implements IEntityProvider {

	private final IApplication mApplicationHelper = new ApplicationHelper(this, this);
	private GenexusApplication mGenexusApplication = null;

	@Override
	public void onCreate() {
		super.onCreate();
		mApplicationHelper.onCreate(mGenexusApplication);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mApplicationHelper.onConfigurationChanged(newConfig);
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		mApplicationHelper.attachBaseContext(base);
	}

	protected void setApp(GenexusApplication app) {
		mGenexusApplication = app;
	}

	protected void registerModule(GenexusModule module) {
		mApplicationHelper.registerModule(module);
	}

	protected void setAppsLinksProtocol(String value) {
		mApplicationHelper.setAppsLinksProtocol(value);
	}
}
