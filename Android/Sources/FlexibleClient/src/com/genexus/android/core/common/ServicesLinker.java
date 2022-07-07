package com.genexus.android.core.common;

import android.content.Context;
import android.os.Build;

import com.genexus.android.core.application.LifecycleListeners;
import com.genexus.android.core.base.metadata.GenexusApplication;
import com.genexus.android.core.base.services.IApplication;
import com.genexus.android.core.base.services.ILocationServices;
import com.genexus.android.core.base.services.IMaps;
import com.genexus.android.core.base.services.ISuperApps;
import com.genexus.android.core.base.services.OnMapsConnectedCallback;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.synchronization.SynchronizationHelper;

import java.util.ArrayList;
import java.util.List;

public class ServicesLinker {

	private final IApplication mApplication;
	private static ServicesLinker mInstance;

	private static final List<OnMapsConnectedCallback> CALLBACKS_MAPS = new ArrayList<>();

	private ServicesLinker(IApplication application) {
		mApplication = application;
		mApplication.getLifecycle().registerApplicationLifecycleListener(mApplicationCallbacks);
		mApplication.getLifecycle().registerMiniApplicationLifecycleListener(mMiniApplicationCallbacks);
	}

	public static synchronized ServicesLinker get(IApplication application) {
		if (mInstance == null)
			mInstance = new ServicesLinker(application);

		return mInstance;
	}

	private final LifecycleListeners.Application mApplicationCallbacks = application -> connect();
	private final LifecycleListeners.MiniApp mMiniApplicationCallbacks = new LifecycleListeners.MiniApp() {
		@Override public void onMiniAppStarted() { connectReusableServices(mApplication, mApplication.get()); }
		@Override public void onMiniAppStopped() { connectReusableServices(mApplication, mApplication.get());}
	};

	private void connect() {
		mApplication.getLifecycle().unregisterApplicationLifecycleListener(mApplicationCallbacks);
		GenexusApplication gxApplication = mApplication.get();
		connectGlobalServices(mApplication);
		connectReusableServices(mApplication, gxApplication);
	}

	private void connectGlobalServices(IApplication application) {
		Context context = application.getAndroidApplication();
		Services.Application = application;
		Services.Device = new DeviceHelper(context);
		Services.Exceptions = new ExceptionManager();
		Services.Language = new LanguageManager(application);
		Services.Log = new LogManager();
		Services.Messages = new MessagesHelper(context);
		Services.Serializer = new SerializationHelper(context);
		Services.Strings = new StringUtil(context);
		Services.Extensions = new ExtensionsManager();

		// Avoids @SuppressLint("NewApi") - It doesn't matter if Services.Shortcuts is null because
		// it will only be called if API level >= 25
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1)
			Services.Shortcuts = new ShortcutsHelper(context);
	}

	private void connectReusableServices(IApplication application, GenexusApplication genexusApplication) {
		Context context = application.getAndroidApplication();
		Services.Assets = new AssetsManager(context);
		Services.Fonts = new FontsManager();
		Services.HttpService = new ServiceHelper(context, genexusApplication);
		Services.Images = new ImageHelper();
		Services.Notifications = new NotificationsManager(context, genexusApplication);
		Services.Preferences = new PreferencesHelper(context);
		Services.Resources = new ResourcesManager(application);
		Services.Sync = new SynchronizationHelper(context, genexusApplication);
		Services.Themes = new ThemesManager();
	}

	public void connectLocation(ILocationServices locationService) {
		Services.Location = locationService;
	}

	public void connectMaps(IMaps mapsServices, IMaps.Offline mapsOfflineServices) {
		Services.Maps = mapsServices;
		Services.MapsOffline = mapsOfflineServices;
		for (OnMapsConnectedCallback callback : CALLBACKS_MAPS)
			callback.onMapsConnected();

		CALLBACKS_MAPS.clear();
	}

	public void connectSuperApps(ISuperApps superAppsService) {
		if (Services.Application.hasActiveMiniApp())
			throw new IllegalStateException("SuperApps service connection can only be triggered from the SuperApp");

		Services.SuperApps = superAppsService;
	}

	public void registerMapsConnectedCallback(OnMapsConnectedCallback callback) {
		if (callback == null)
			return;

		//Maybe Maps has already been initialized, so the callback is invoked immediately
		if (Services.Maps != null)
			callback.onMapsConnected();
		else
			CALLBACKS_MAPS.add(callback);
	}
}
