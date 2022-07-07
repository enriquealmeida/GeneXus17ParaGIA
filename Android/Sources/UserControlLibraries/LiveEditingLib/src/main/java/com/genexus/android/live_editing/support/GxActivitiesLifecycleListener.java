package com.genexus.android.live_editing.support;

import android.app.Activity;

public interface GxActivitiesLifecycleListener {
	void onActivityCreated(Activity activity);
	void onActivityResumed(Activity activity);
	void onActivityPaused(Activity activity);
	void onActivityDestroyed(Activity activity);
}
