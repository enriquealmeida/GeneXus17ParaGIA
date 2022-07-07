package com.genexus.android.analytics.firebase;

import android.content.Context;

import com.genexus.android.analytics.Analytics;
import com.genexus.android.core.framework.GenexusModule;

public class FirebaseAnalyticsModule implements GenexusModule {
    @Override
    public void initialize(Context context)
    {
        Analytics.setProvider(new FirebaseAnalyticsProvider(context));
    }
}
