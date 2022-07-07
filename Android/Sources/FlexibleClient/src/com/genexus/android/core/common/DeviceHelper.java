package com.genexus.android.core.common;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import com.genexus.android.core.base.metadata.enums.Orientation;
import com.genexus.android.core.base.metadata.settings.PlatformDefinition;
import com.genexus.android.core.base.services.IDeviceService;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.ResultRunnable;
import com.genexus.android.core.base.utils.RunnableUtils;
import com.genexus.android.core.base.utils.Version;

public class DeviceHelper implements IDeviceService {
    private final Context mContext;
    private final Thread mUiThread;
    private final Handler mUiThreadHandler;
    private final Version mOSVersion;

    public DeviceHelper(Context context) {
        mContext = context;
        mUiThread = Looper.getMainLooper().getThread();
        mUiThreadHandler = new Handler(Looper.getMainLooper());
        mOSVersion = new Version(Build.VERSION.RELEASE);
    }

    @Override
    public int getOS() {
        return PlatformDefinition.OS_ANDROID;
    }

    @Override
    public Version getOSVersion() {
        return mOSVersion;
    }

    @Override
    public int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    @Override
    public boolean isDeviceNotificationEnabled() {
        return true;
    }

    @Override
    public int getScreenSmallestWidth() {
        return mContext.getResources().getConfiguration().smallestScreenWidthDp;
    }

    @Override
    public int getScreenWidth() {
        return mContext.getResources().getConfiguration().screenWidthDp;
    }

    @Override
    public int getScreenHeight() {
        return mContext.getResources().getConfiguration().screenHeightDp;
    }

    @Override
    public Orientation getScreenOrientation() {
        // Map unknown Android values to Orientation.UNKNOWN.
        int orientation = mContext.getResources().getConfiguration().orientation;

        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                return Orientation.PORTRAIT;
            case Configuration.ORIENTATION_LANDSCAPE:
                return Orientation.LANDSCAPE;
            default:
                return Orientation.UNDEFINED;
        }
    }

    @Override
    public int dipsToPixels(int dips) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        // fix problem of round int for negative values.
        if (dips < 0)
            return (int) (dips * scale - 0.5f);
        else
            return (int) (dips * scale + 0.5f);
    }

    @Override
    public int pixelsToDips(int pixels) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        // fix problem of round int for negative values.
        if (pixels < 0)
            return (int) (pixels / scale - 0.5f);
        else
            return (int) (pixels / scale + 0.5f);
    }


    @Override
    public boolean isMainThread() {
        return (mUiThread == null || Thread.currentThread() == mUiThread);
    }

    @Override
    public void runOnUiThread(Runnable r) {
        if (isMainThread())
            r.run();
        else if (mUiThreadHandler != null)
            mUiThreadHandler.post(r);
    }

    @Override
    public void postOnUiThread(Runnable r) {
        if (mUiThreadHandler != null)
            mUiThreadHandler.post(r);
    }

    @Override
    public void postOnUiThreadDelayed(Runnable r, long delayMillis) {
        if (mUiThreadHandler != null)
            mUiThreadHandler.postDelayed(r, delayMillis);
    }

    private static class Reference<V> {
        V value;
    }

    @Override
    public <V> V invokeOnUiThread(final ResultRunnable<V> r) {
        // Simple scenario.
        if (isMainThread())
            return r.run();

        // Complicated scenario. First wrap the ResultRunnable into a standard Runnable
        // that will store the result in a local variable notify() itself when finished.
        final Reference<V> result = new Reference<>();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                result.value = r.run();
                synchronized (this) {
                    notify();
                }
            }
        };

        // Then post said runnable to the queue, and wait for it to complete.
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (runnable) {
            mUiThreadHandler.post(runnable);
            try {
                runnable.wait();
            } catch (InterruptedException e) {
                Services.Log.error(e);
                return null;
            }
        }

        // Finally return the stored result, if any.
        return result.value;
    }

    @Override
    public void invokeOnUiThread(final Runnable r) {
        invokeOnUiThread(RunnableUtils.buildResultRunnableFromRunnable(r));
    }
}
