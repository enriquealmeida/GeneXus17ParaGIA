package com.genexus.android.live_editing;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.genexus.android.core.application.LifecycleListeners;
import com.genexus.android.core.base.services.IApplication;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.framework.GenexusModule;
import com.genexus.android.live_editing.commands.IServerCommand;
import com.genexus.android.live_editing.executor.ICommandExecutor;
import com.genexus.android.live_editing.inspector.IInspector;
import com.genexus.android.live_editing.network.INetworkEventsListener;
import com.genexus.android.live_editing.network.NetworkModule;
import com.genexus.android.live_editing.storage.IDataStorage;
import com.genexus.android.live_editing.support.Endpoint;
import com.genexus.android.live_editing.support.ILifecycleTracker;
import com.genexus.android.live_editing.support.ILiveEditingImageManager;
import com.genexus.android.live_editing.ui.IUserInterface;
import com.genexus.android.live_editing.ui.UIModule;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class LiveEditingGenexusModule implements GenexusModule, LifecycleListeners.MetadataLoading, INetworkEventsListener {

	public static final String TAG = "LiveEditing";
	private IDataStorage mDataStorage;
	private ILiveEditingImageManager mLiveEditingImageManager;
	private IInspector mInspector;
	private ICommandExecutor mCommandExecutor;
	private IUserInterface mUserInterface;
	private ILifecycleTracker mLifecycleTracker;
	private IApplication mApplication;
	private AtomicBoolean mEnabled;

	@Override
	public void initialize(Context context) {
		Services.Application.getLifecycle().registerOnMetadataLoadFinished(this);
	}

	/**
	 * At this point we are certain that the Live Editing module has finished its initialization
	 * (its last step was registering this callback), and the app's metadata has finished loading.
	 */
	@Override
	public void onMetadataLoadFinished(@NonNull IApplication application) {
		initiate(application);
	}

	public void initiate(IApplication application) {
		mEnabled = new AtomicBoolean(false);

		LiveEditingComponent liveEditingComponent = DaggerLiveEditingComponent.builder()
				.applicationModule(new ApplicationModule(application))
				.networkModule(new NetworkModule(this))
				.uIModule(new UIModule(this))
				.build();

		// Bind
		mDataStorage = liveEditingComponent.getDataStorage();
		mLiveEditingImageManager = liveEditingComponent.getLiveEditingImageManager();
		mInspector = liveEditingComponent.getInspector();
		mCommandExecutor = liveEditingComponent.getCommandExecutor();
		mUserInterface = liveEditingComponent.getUserInterface();

		// Begin tracking activities
		Set<Activity> activities = null;
		if (mLifecycleTracker != null && mApplication != null)
		{
			mLifecycleTracker.endTracking(mApplication.getAndroidApplication());
			if (mApplication == application)
				activities = mLifecycleTracker.getActivities();  // if it is the same application dont lose the activities
		}
		mApplication = application;
		mLifecycleTracker = liveEditingComponent.getLifecycleTracker();
		mLifecycleTracker.beginTracking(application.getAndroidApplication());
		if (activities != null)
			mLifecycleTracker.setActivities(activities);

		Services.overrideResourcesService(mLiveEditingImageManager);
		Services.Application.enableLiveEditingMode();
		liveEditingComponent.getNetworkClient().connect();
		Services.Log.info(TAG,"Starting connection");
	}

	@Override
	public void onConnectionAttempt(@NonNull Endpoint targetEndpoint) {
		mUserInterface.displayConnectionAttempt(targetEndpoint);
		Services.Log.debug(TAG,"New connection attempt at " + targetEndpoint.toString());
	}

	@Override
	public void onMaxAttemptsReached() {
		mUserInterface.displayConnectionFailed();
		Services.Log.warning(TAG,"Max connection attempts reached");
	}

	@Override
	public void onConnectionEstablished(@NonNull Endpoint endpoint) {
		mDataStorage.storeEndpoint(endpoint);
		mLiveEditingImageManager.setCurrentEndpoint(endpoint);
		mInspector.enable();
		mEnabled.compareAndSet(false, true);
		mUserInterface.displayConnectionEstablished(endpoint);
		Services.Log.info(TAG,"Connection established");
	}

	@Override
	public void onConnectionDropped() {
		mInspector.disable();
		mEnabled.compareAndSet(true, false);
		mUserInterface.displayConnectionDropped();
		Services.Log.info(TAG,"Connection dropped");
	}

	@Override
	public void onCommandReceived(IServerCommand command) {
		Services.Log.info(TAG,"New command received from IDE");
		if (mEnabled.get())
			mCommandExecutor.enqueue(command);
		else
			Services.Log.warning(TAG, String.format("Received '%s' command won't be executed", command.getClass().toString()));
	}
}
