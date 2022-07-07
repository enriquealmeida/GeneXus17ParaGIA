package com.genexus.android.core.application

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.genexus.android.core.base.services.IApplication
import com.genexus.android.core.fragments.IDataView
import java.util.concurrent.atomic.AtomicInteger

class ApplicationLifecycle(private val application: IApplication) {

    private val mMetadataLoadListeners = mutableListOf<LifecycleListeners.MetadataLoading>()
    private val mComponentEventsListeners = mutableListOf<LifecycleListeners.Component>()
    private val mDialogsLifecycleListeners = mutableListOf<LifecycleListeners.Dialog>()
    private val mApplicationLifecycleListeners = mutableListOf<LifecycleListeners.Application>()
    private val mMiniApplicationLifecycleListeners = mutableListOf<LifecycleListeners.MiniApp>()
    private val mForegroundListeners = hashSetOf<LifecycleListeners.Foreground>()
    private val mActivityListeners = hashSetOf<Application.ActivityLifecycleCallbacks>()
    private val mActivityCounter = AtomicInteger(0)

    fun registerOnMetadataLoadFinished(listener: LifecycleListeners.MetadataLoading) {
        mMetadataLoadListeners.add(listener)
    }

    fun unregisterOnMetadataLoadFinished(listener: LifecycleListeners.MetadataLoading) {
        mMetadataLoadListeners.remove(listener)
    }

    fun notifyMetadataLoadFinished() {
        for (listener in ArrayList<LifecycleListeners.MetadataLoading>(mMetadataLoadListeners))
            listener.onMetadataLoadFinished(application)
    }

    fun registerComponentEventsListener(listener: LifecycleListeners.Component) {
        mComponentEventsListeners.add(listener)
    }

    fun unregisterComponentEventsListener(listener: LifecycleListeners.Component) {
        mComponentEventsListeners.remove(listener)
    }

    fun notifyComponentInitialized(component: IDataView) {
        for (listener in ArrayList<LifecycleListeners.Component>(mComponentEventsListeners))
            listener.onComponentInitialized(component)
    }

    fun registerDialogsLifecycleListener(listener: LifecycleListeners.Dialog) {
        mDialogsLifecycleListeners.add(listener)
    }

    fun unregisterDialogsLifecycleListener(listener: LifecycleListeners.Dialog) {
        mDialogsLifecycleListeners.remove(listener)
    }

    fun notifyDialogStarted(dialog: Dialog) {
        for (listener in mDialogsLifecycleListeners)
            listener.onDialogStarted(dialog)
    }

    fun notifyDialogStopped(dialog: Dialog) {
        for (listener in mDialogsLifecycleListeners)
            listener.onDialogStopped(dialog)
    }

    fun registerApplicationLifecycleListener(listener: LifecycleListeners.Application) {
        mApplicationLifecycleListeners.add(listener)
    }

    fun unregisterApplicationLifecycleListener(listener: LifecycleListeners.Application) {
        mApplicationLifecycleListeners.remove(listener)
    }

    fun notifyApplicationCreated(application: IApplication) {
        for (listener in mApplicationLifecycleListeners)
            listener.onApplicationCreated(application)
    }

    fun registerMiniApplicationLifecycleListener(listener: LifecycleListeners.MiniApp) {
        mMiniApplicationLifecycleListeners.add(listener)
    }

    fun unregisterMiniApplicationLifecycleListener(listener: LifecycleListeners.MiniApp) {
        mMiniApplicationLifecycleListeners.remove(listener)
    }

    fun notifyMiniApplicationStarted() {
        for (listener in mMiniApplicationLifecycleListeners)
            listener.onMiniAppStarted()
    }

    fun notifyMiniApplicationStopped() {
        for (listener in mMiniApplicationLifecycleListeners)
            listener.onMiniAppStopped()
    }

    /**
     * Returns whether the application is currently in the foreground (i.e. showing an activity).
     */
    fun isForeground(): Boolean {
        return if (mActivityCounter.get() > 0) true else !isBackground()
    }

    private fun isBackground(): Boolean {
        val activityManager = application.androidApplication.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcesses = activityManager.runningAppProcesses
        return (
            runningAppProcesses != null && runningAppProcesses.isNotEmpty() &&
                runningAppProcesses[0].importance <= ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
            )
    }

    /**
     * Adds a listener to be notified when the application's foreground/background state changes.
     */
    fun addForegroundListener(listener: LifecycleListeners.Foreground) {
        mForegroundListeners.add(listener)
    }

    /**
     * Removes a listener from the set to be notified when the application's foreground/background state changes.
     */
    fun removeForegroundListener(listener: LifecycleListeners.Foreground) {
        mForegroundListeners.remove(listener)
    }

    fun addActivityListener(listener: Application.ActivityLifecycleCallbacks) {
        mActivityListeners.add(listener)
    }

    fun removeActivityListener(listener: Application.ActivityLifecycleCallbacks) {
        mActivityListeners.remove(listener)
    }

    private val activityLifecycleCallbacks: Application.ActivityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            for (listener in mActivityListeners)
                listener.onActivityCreated(activity, savedInstanceState)
        }

        override fun onActivityStarted(activity: Activity) {
            // Tracked in start/stop instead of resume/pause because start/stop do overlap
            // when an activity starts another one.
            val counter = mActivityCounter.incrementAndGet()
            if (counter == 1)
                for (listener in mForegroundListeners)
                    listener.onBecameForeground(activity)

            for (listener in mActivityListeners)
                listener.onActivityStarted(activity)
        }

        override fun onActivityResumed(activity: Activity) {
            for (listener in mActivityListeners)
                listener.onActivityResumed(activity)
        }

        override fun onActivityPaused(activity: Activity) {
            for (listener in mActivityListeners)
                listener.onActivityPaused(activity)
        }

        override fun onActivityStopped(activity: Activity) {
            val counter = mActivityCounter.decrementAndGet()
            check(counter >= 0) { "Internal error in activity count tracking. Counter is less than zero!" }
            if (counter == 0)
                for (listener in mForegroundListeners)
                    listener.onBecameBackground(activity)

            for (listener in mActivityListeners)
                listener.onActivityStopped(activity)
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            for (listener in mActivityListeners)
                listener.onActivitySaveInstanceState(activity, outState)
        }

        override fun onActivityDestroyed(activity: Activity) {
            for (listener in mActivityListeners)
                listener.onActivityDestroyed(activity)
        }
    }

    init {
        application.androidApplication.registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
    }
}
