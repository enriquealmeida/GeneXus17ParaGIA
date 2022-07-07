package com.genexus.android.live_editing;

import android.content.Context;

import com.genexus.android.core.base.services.IApplication;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
	private IApplication mApplication;

	public ApplicationModule(IApplication application) {
		mApplication = application;
	}

	@Provides
	@Singleton
	IApplication providesMyApplication() {
		return mApplication;
	}

	@Provides
	@Singleton
	Context providesContext() {
		return mApplication.getAppContext();
	}

	@Provides
	@Singleton
	Executor providesExecutor() {
		return Executors.newSingleThreadExecutor();
	}
}
