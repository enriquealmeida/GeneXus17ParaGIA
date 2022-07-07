package com.genexus.android.live_editing.executor;

import com.genexus.android.live_editing.inspector.IInspector;
import com.genexus.android.live_editing.inspector.InspectorModule;
import com.genexus.android.live_editing.ApplicationModule;
import com.genexus.android.live_editing.support.ILifecycleTracker;
import com.genexus.android.live_editing.support.ILiveEditingImageManager;
import com.genexus.android.live_editing.support.SupportServicesModule;

import java.util.concurrent.Executor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {SupportServicesModule.class, InspectorModule.class, ApplicationModule.class})
public class CommandExecutorModule {
	@Provides
	@Singleton
	public ICommandExecutor providesCommandExecutor(ILiveEditingImageManager liveEditingImageManager,
													IInspector inspector,
													ILifecycleTracker lifecycleTracker,
													Executor executor) {
		return new CommandExecutor(liveEditingImageManager, inspector, lifecycleTracker, executor);
	}
}
