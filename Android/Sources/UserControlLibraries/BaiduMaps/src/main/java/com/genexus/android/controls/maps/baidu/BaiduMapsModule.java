package com.genexus.android.controls.maps.baidu;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.genexus.android.core.activities.IGxBaseActivity;
import com.genexus.android.WithPermission;
import com.genexus.android.core.base.services.OnMapsConnectedCallback;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.location.baidu.BaiduLocationProvider;
import com.genexus.android.core.framework.GenexusModule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BaiduMapsModule implements GenexusModule, OnMapsConnectedCallback, Application.ActivityLifecycleCallbacks {

    private Context mContext = null;

    @Override
    public void initialize(Context context) {
        mContext = context;
		Services.Application.getServicesLinker().registerMapsConnectedCallback(this);
    }

    @Override
    public void onMapsConnected() {
        Services.Maps.addProvider(new MapsProvider());
        Services.Application.getLifecycle().addActivityListener(this);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        if (activity instanceof IGxBaseActivity) {
            new WithPermission.Builder<Void>(activity)
                    .require(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE})
                    .attachToActivityController()
                    .onFailure(() -> {
                        Services.Log.error("Storage permission not granted for Baidu");
                        Services.Application.getLifecycle().removeActivityListener(this);
                    })
                    .onSuccess(() -> {
                        Services.Location.setProvider(new BaiduLocationProvider(mContext));
                        Services.Application.getLifecycle().removeActivityListener(this);
                    })
                    .build()
                    .run();
        }
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) { }

    @Override
    public void onActivityResumed(@NonNull Activity activity) { }

    @Override
    public void onActivityPaused(@NonNull Activity activity) { }

    @Override
    public void onActivityStopped(@NonNull Activity activity) { }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) { }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) { }
}
