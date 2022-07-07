package com.genexus.android.uitesting.rules;

import android.app.Dialog;

import com.genexus.android.core.activities.StartupActivity;
import com.genexus.android.core.application.LifecycleListeners;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.fragments.LayoutFragmentDialog;
import com.genexus.android.uitesting.idlingresources.DialogIdlingResource;
import com.genexus.android.uitesting.idlingresources.LoadingIndicatorIdlingResource;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;

@SuppressWarnings("deprecation")
public class GenexusActivityTestRule<T extends StartupActivity> extends androidx.test.rule.ActivityTestRule<T> implements LifecycleListeners.Dialog {

    private final List<IdlingResource> mLoadingIdlingResources;
    private final List<IdlingResource> mDialogIdlingResources;

    public GenexusActivityTestRule(Class<T> activityClass) {
        super(activityClass);
        mLoadingIdlingResources = new ArrayList<>(1);
        mDialogIdlingResources = new ArrayList<>(1);
        Services.Application.getLifecycle().registerDialogsLifecycleListener(this);
    }

    @Override
    public T getActivity() {
        return super.getActivity();
    }

    @Override
    protected void afterActivityLaunched() {
        mLoadingIdlingResources.add(new LoadingIndicatorIdlingResource());
        for (IdlingResource res : mLoadingIdlingResources)
            IdlingRegistry.getInstance().register(res);
    }

    @Override
    protected void afterActivityFinished() {
        for (IdlingResource res : mLoadingIdlingResources)
            IdlingRegistry.getInstance().unregister(res);
    }

    @Override
    public void onDialogStarted(@NonNull Dialog dialog) {
        if (dialog instanceof LayoutFragmentDialog)
            return;

        mDialogIdlingResources.add(new DialogIdlingResource(dialog));
        for (IdlingResource res : mDialogIdlingResources)
            IdlingRegistry.getInstance().register(res);
    }

    @Override
    public void onDialogStopped(@NonNull Dialog dialog) {
        if (dialog instanceof LayoutFragmentDialog)
            return;

        for (IdlingResource res : mDialogIdlingResources)
            IdlingRegistry.getInstance().unregister(res);
    }
}
