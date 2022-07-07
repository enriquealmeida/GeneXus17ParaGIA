package com.genexus.android.core.externalobjects;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.annotation.NonNull;

import com.genexus.android.core.actions.ActionExecution;
import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.actions.ExternalObjectEvent;
import com.genexus.android.core.activities.ActivityHelper;
import com.genexus.android.core.application.LifecycleListeners;
import com.genexus.android.core.base.services.IApplication;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.externalapi.ExternalApiResult;
import com.genexus.android.core.fragments.IDataView;

import java.util.Arrays;

public class AppLifecycleAPI extends ExternalApi {
	public static final String OBJECT_NAME = "GeneXus.SD.AppLifecycle";

	private static final String PROPERTY_APPLICATION_STATE = "ApplicationState";
	private static final String EVENT_APP_STATE_CHANGED = "AppStateChanged";

	private static final int APPLICATION_STATE_ACTIVE = 0;
	private static final int APPLICATION_STATE_INACTIVE = 1;
	private static final int APPLICATION_STATE_BACKGROUND = 2;
	private static final int APPLICATION_STATE_NOT_RUNNING = 3;
	private static final MyLifecycleObserver APPLICATION_LIFECYCLE_OBSERVER = new MyLifecycleObserver();
	private final IMethodInvoker mGetApplicationState = parameters ->
			ExternalApiResult.success(APPLICATION_LIFECYCLE_OBSERVER.getCurrentState());

	public AppLifecycleAPI(ApiAction action) {
		super(action);
		addReadonlyPropertyHandler(PROPERTY_APPLICATION_STATE, mGetApplicationState);
	}

	public static void initialize() {
		ProcessLifecycleOwner.get().getLifecycle().addObserver(APPLICATION_LIFECYCLE_OBSERVER);
	}

	private static class MyLifecycleObserver implements LifecycleObserver {
		private final ExternalObjectEvent mAppStateChanged = new ExternalObjectEvent(OBJECT_NAME, EVENT_APP_STATE_CHANGED);
		private int mCurrentState = APPLICATION_STATE_NOT_RUNNING;

		public int getCurrentState() {
			return mCurrentState;
		}

		private void fireEvent(int newState) {
			if (mCurrentState != newState) {
				int oldState = mCurrentState;
				mCurrentState = newState;
				mAppStateChanged.fire(Arrays.asList(oldState, newState));
			}
		}

		@OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
		public void onCreate() {
			class Listener implements LifecycleListeners.MetadataLoading, ActivityHelper.MainActivitySetListener, LifecycleListeners.Component {
				private boolean mApplicationLoaded;
				private boolean mMainActivitySet;
				private boolean mComponentInitialized;

				private void fireEventIfReady() {
					if (mApplicationLoaded && mMainActivitySet && mComponentInitialized)
						fireEvent(APPLICATION_STATE_ACTIVE);
				}

				@Override
				public void onMetadataLoadFinished(IApplication application) {
					mApplicationLoaded = true;
					fireEventIfReady();
					Services.Application.getLifecycle().unregisterOnMetadataLoadFinished(this);
				}

				@Override
				public void onMainActivitySet() {
					mMainActivitySet = true;
					fireEventIfReady();
					ActivityHelper.removeMainActivitySetListener(this);
				}

				@Override
				public void onComponentInitialized(IDataView component) {
					mComponentInitialized = true;
					fireEventIfReady();
					Services.Application.getLifecycle().unregisterComponentEventsListener(this);
				}
			}

			Listener listener = new Listener();
			Services.Application.getLifecycle().registerOnMetadataLoadFinished(listener);
			ActivityHelper.addMainActivitySetListener(listener);
			Services.Application.getLifecycle().registerComponentEventsListener(listener);
		}

		@OnLifecycleEvent(Lifecycle.Event.ON_START)
		public void onStart(@NonNull LifecycleOwner owner) {
			if (mCurrentState != APPLICATION_STATE_NOT_RUNNING)
				fireEvent(APPLICATION_STATE_ACTIVE);
		}

		@OnLifecycleEvent(Lifecycle.Event.ON_STOP)
		public void onStop(@NonNull LifecycleOwner owner) {
			if (!ActionExecution.currentCatchOnActivityResult())
				fireEvent(APPLICATION_STATE_BACKGROUND);
		}

		@OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
		public void onDestroy(@NonNull LifecycleOwner owner) {
			// Never called, https://developer.android.com/reference/android/arch/lifecycle/ProcessLifecycleOwner
			fireEvent(APPLICATION_STATE_NOT_RUNNING);
		}
	}
}
