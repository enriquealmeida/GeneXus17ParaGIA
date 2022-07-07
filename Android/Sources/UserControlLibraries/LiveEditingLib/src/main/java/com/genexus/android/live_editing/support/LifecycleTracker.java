package com.genexus.android.live_editing.support;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

class LifecycleTracker implements ILifecycleTracker, ActivityLifecycleCallbacks {
    private final List<GxActivitiesLifecycleListener> mGxActivitiesLifecycleListeners;
    private Set<Activity> mActivities;

    public LifecycleTracker() {
        mGxActivitiesLifecycleListeners = new CopyOnWriteArrayList<>();
        mActivities = new CopyOnWriteArraySet<>();
    }

    @Override
    public void registerActivitiesLifecycleListener(GxActivitiesLifecycleListener listener) {
        mGxActivitiesLifecycleListeners.add(listener);
    }

    @Override
    public void unregisterActivitiesLifecycleListener(GxActivitiesLifecycleListener listener) {
        mGxActivitiesLifecycleListeners.remove(listener);
    }

    @Override
    public void beginTracking(Application application) {
        application.registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void endTracking(Application application) {
        application.unregisterActivityLifecycleCallbacks(this);
    }

    @Override
    public Set<Activity> getActivities() {
        return mActivities;
    }

	@Override
	public void setActivities(Set<Activity> activities) {
		mActivities = activities;
	}

	@Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mActivities.add(activity);
        for (GxActivitiesLifecycleListener listener : mGxActivitiesLifecycleListeners) {
            listener.onActivityCreated(activity);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        for (GxActivitiesLifecycleListener listener : mGxActivitiesLifecycleListeners) {
            listener.onActivityResumed(activity);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        for (GxActivitiesLifecycleListener listener : mGxActivitiesLifecycleListeners) {
            listener.onActivityPaused(activity);
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mActivities.remove(activity);
        for (GxActivitiesLifecycleListener listener : mGxActivitiesLifecycleListeners) {
            listener.onActivityDestroyed(activity);
        }
    }
}
