package com.genexus.android.live_editing;

import javax.inject.Singleton;

import com.genexus.android.live_editing.executor.CommandExecutorModule;
import com.genexus.android.live_editing.executor.ICommandExecutor;
import com.genexus.android.live_editing.inspector.IInspector;
import com.genexus.android.live_editing.inspector.InspectorModule;
import com.genexus.android.live_editing.network.INetworkClient;
import com.genexus.android.live_editing.storage.IDataStorage;
import com.genexus.android.live_editing.storage.StorageModule;
import com.genexus.android.live_editing.support.ILifecycleTracker;
import com.genexus.android.live_editing.support.ILiveEditingImageManager;
import com.genexus.android.live_editing.support.SupportServicesModule;
import com.genexus.android.live_editing.ui.IUserInterface;
import com.genexus.android.live_editing.ui.UIModule;

import dagger.Component;

@Singleton
@Component(modules = {CommandExecutorModule.class, InspectorModule.class, StorageModule.class,
		UIModule.class, SupportServicesModule.class})
public interface LiveEditingComponent {
	ICommandExecutor getCommandExecutor();
	IInspector getInspector();
	IDataStorage getDataStorage();
	INetworkClient getNetworkClient();
	IUserInterface getUserInterface();
	ILiveEditingImageManager getLiveEditingImageManager();
	ILifecycleTracker getLifecycleTracker();
}
