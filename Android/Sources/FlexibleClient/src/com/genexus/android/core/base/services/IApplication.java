package com.genexus.android.core.base.services;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

import com.genexus.android.core.actions.UIContext;
import com.genexus.android.core.application.ApplicationLifecycle;
import com.genexus.android.core.base.metadata.ApplicationDefinition;
import com.genexus.android.core.base.metadata.GenexusApplication;
import com.genexus.android.core.common.ServicesLinker;
import com.genexus.android.core.framework.GenexusModule;
import com.genexus.android.core.superapps.MiniApp;
import com.genexus.android.core.base.metadata.enums.Connectivity;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.providers.IApplicationServer;
import com.genexus.android.core.externalapi.ExternalApiFactory;
import com.genexus.android.core.usercontrols.TableLayoutFactory;

public interface IApplication
{

	void onCreate(GenexusApplication application);
	void onConfigurationChanged(Configuration newConfig);
	void attachBaseContext(Context context);

	Context getAppContext();

	ServicesLinker getServicesLinker();
	ApplicationLifecycle getLifecycle();
	ApplicationDefinition getDefinition();
	IEntityProvider getEntityProvider();
	ExternalApiFactory getExternalApiFactory();
	TableLayoutFactory getTableLayoutFactory();

	/**
	 * @return the {@link MiniApp} instance if an active one exists,
	 * or the main {@link GenexusApplication} instance otherwise.
	 */
	GenexusApplication get();

	void registerModule(GenexusModule module);

	/**
	 * @return the application's {@link Application} instance.
	 */
	Application getAndroidApplication();

	boolean isLoaded();
	boolean isLoadingMetadata();

	/*
	 * Try to handle an application Intent, traversing all registered handlers. (ie: App Indexing, App Links, etc)
	 */
	boolean handleIntent(UIContext ctx, Intent intent, Entity entity);

	/*
	 * The protocol handler for this application
	 */
	String getAppsLinksProtocol();
	void setAppsLinksProtocol(String value);

	/**
	 * Returns a {@link ClientStorage} object that can be used to store property values securely.
	 * @param name Storage file name.
	 */
	@NonNull ClientStorage getClientStorage(@NonNull String name);

	boolean isLiveEditingEnabled();
	void enableLiveEditingMode();

	void setMiniApp(MiniApp miniApplication);
	MiniApp getMiniApp();
	boolean hasActiveMiniApp();
	void unloadMiniApp();

	boolean isRTLLanguage();
	void clearData();

	/**
	 * Persists the new API Uri in the application's preferences.
	 * @param newServicesUrl The new Services Url to load on the next execution
	 */
	void saveNewDynamicServicesUrl(String newServicesUrl);

	IApplicationServer getApplicationServer(Connectivity connectivity);
}
