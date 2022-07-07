package com.genexus.android.live_editing.inspector;

import com.genexus.android.live_editing.ApplicationModule;
import com.genexus.android.live_editing.network.INetworkClient;
import com.genexus.android.live_editing.network.NetworkModule;
import com.genexus.android.live_editing.support.ILifecycleTracker;
import com.genexus.android.live_editing.support.SupportServicesModule;

import java.util.concurrent.Executor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {NetworkModule.class, SupportServicesModule.class, ApplicationModule.class})
public class InspectorModule {
	@Provides
	@Singleton
	public IInspector providesLiveInspector(INetworkClient networkClient,
											ILifecycleTracker lifecycleTracker,
											Executor executor) {
		return new InspectorImpl(networkClient, lifecycleTracker, executor);
	}
}
