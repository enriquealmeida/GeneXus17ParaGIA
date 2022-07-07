package com.genexus.android.core.externalobjects;

import android.content.Context;

import com.genexus.android.core.application.LifecycleListeners;
import com.genexus.android.core.base.services.IApplication;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.externalapi.ExternalApiDefinition;
import com.genexus.android.core.externalapi.ExternalApiFactory;
import com.genexus.android.core.framework.GenexusModule;

public class CoreExternalObjectsModule implements GenexusModule, LifecycleListeners.MetadataLoading {

	@Override
	public void initialize(Context context) {
		ExternalApiFactory.addApi(new ExternalApiDefinition(ActionsAPI.OBJECT_NAME, ActionsAPI.class));
		ExternalApiFactory.addApi(new ExternalApiDefinition(AnalyticsAPI.OBJECT_NAME, AnalyticsAPI.class));
		ExternalApiFactory.addApi(new ExternalApiDefinition(AppLifecycleAPI.OBJECT_NAME, AppLifecycleAPI.class));
		ExternalApiFactory.addApi(new ExternalApiDefinition(CalendarAPI.OBJECT_NAME, CalendarAPI.class));
		ExternalApiFactory.addApi(new ExternalApiDefinition(CameraAPI.OBJECT_NAME, CameraAPI.class));
		ExternalApiFactory.addApi(new ExternalApiDefinition(ClientInformationAPI.OBJECT_NAME, ClientInformationAPI.class));
		ExternalApiFactory.addApi(new ExternalApiDefinition(ClientStorageAPI.OBJECT_NAME, ClientStorageAPI.class));
		ExternalApiFactory.addApi(new ExternalApiDefinition(ClipboardAPI.OBJECT_NAME, ClipboardAPI.class));
		ExternalApiFactory.addApi(new ExternalApiDefinition(ContactsAPI.OBJECT_NAME, ContactsAPI.class));
		ExternalApiFactory.addApi(new ExternalApiDefinition(GAMUserAPI.OBJECT_NAME, GAMUserAPI.class));
		ExternalApiFactory.addApi(new ExternalApiDefinition(GAMApplicationAPI.OBJECT_NAME, GAMApplicationAPI.class));
		ExternalApiFactory.addApi(new ExternalApiDefinition(HttpClientAPI.OBJECT_NAME, HttpClientAPI.class));
		ExternalApiFactory.addApi(new ExternalApiDefinition(InteropAPI.OBJECT_NAME, InteropAPI.class));
		ExternalApiFactory.addApi(new ExternalApiDefinition(LogAPI.OBJECT_NAME, LogAPI.class));
		ExternalApiFactory.addApi(new ExternalApiDefinition(NavigationAPI.OBJECT_NAME, NavigationAPI.class));
		ExternalApiFactory.addApi(new ExternalApiDefinition(NetworkAPI.OBJECT_NAME, NetworkAPI.class));
		ExternalApiFactory.addApi(new ExternalApiDefinition(PhotoLibraryAPI.OBJECT_NAME, PhotoLibraryAPI.class));
		ExternalApiFactory.addApi(new ExternalApiDefinition(RuntimeAPI.OBJECT_NAME, RuntimeAPI.class));
		ExternalApiFactory.addApi(new ExternalApiDefinition(ShareAPI.OBJECT_NAME, ShareAPI.class));
		ExternalApiFactory.addApi(new ExternalApiDefinition(SynchronizationEventsAPI.OBJECT_NAME, SynchronizationEventsAPI.class));
		ExternalApiFactory.addApi(new ExternalApiDefinition(DataBaseAPI.OBJECT_NAME, DataBaseAPI.class));
		ExternalApiFactory.addApi(new ExternalApiDefinition(FileAPI.OBJECT_NAME, FileAPI.class));
		ExternalApiFactory.addApi(new ExternalApiDefinition(DirectoryAPI.OBJECT_NAME, DirectoryAPI.class));
		ExternalApiFactory.addApi(new ExternalApiDefinition(WebBrowserAPI.OBJECT_NAME, WebBrowserAPI.class, false));
		ExternalApiFactory.addApi(new ExternalApiDefinition(LocalNotificationsAPI.OBJECT_NAME, LocalNotificationsAPI.class));
		ExternalApiFactory.addApi(new ExternalApiDefinition(ProgressIndicatorAPI.OBJECT_NAME, ProgressIndicatorAPI.class));
		ExternalApiFactory.addApi(new ExternalApiDefinition(DynamicCallAPI.OBJECT_NAME, DynamicCallAPI.class));
		ExternalApiFactory.addApi(new ExternalApiDefinition(PropertiesTypeAPI.TYPE_NAME, PropertiesTypeAPI.class));
		ExternalApiFactory.addApi(new ExternalApiDefinition(PropertyTypeAPI.TYPE_NAME, PropertyTypeAPI.class));
		ExternalApiFactory.addApi(new ExternalApiDefinition(RemoteConfigAPI.OBJECT_NAME, RemoteConfigAPI.class));
		ExternalApiFactory.addApi(new ExternalApiDefinition(DesignSystemAPI.OBJECT_NAME, DesignSystemAPI.class));
		ExternalApiFactory.addApi(new ExternalApiDefinition(PermissionsAPI.NAME, PermissionsAPI.class, false));

		Services.Application.getLifecycle().registerOnMetadataLoadFinished(this);
	}

	@Override
	public void onMetadataLoadFinished(IApplication application) {
		NetworkAPI.registerNetworkChangeListener(application.getAppContext());
	}
}
