package com.genexus.android.websockets;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;

import com.genexus.android.core.base.services.Services;

/**
 * Usage:
 * 
 * 1. Get the Foreground Singleton, passing a Context or Application object unless you
 * are sure that the Singleton has definitely already been initialised elsewhere.
 * 
 * 2.a) Perform a direct, synchronous check: Foreground.isForeground() / .isBackground()
 * 
 * or
 * 
 * 2.b) Register to be notified (useful in Service or other non-UI components):
 * 
 *   Foreground.Listener myListener = new Foreground.Listener(){
 *       public void onBecameForeground(){
 *           // ... whatever you want to do
 *       }
 *       public void onBecameBackground(){
 *           // ... whatever you want to do
 *       }
 *   }
 * 
 *   public void onCreate(){
 *      super.onCreate();
 *      Foreground.get(this).addListener(listener);
 *   }
 * 
 *   public void onDestroy(){
 *      super.onCreate();
 *      Foreground.get(this).removeListener(listener);
 *   }
 */
class Foreground implements Application.ActivityLifecycleCallbacks {
    public static final long CHECK_DELAY = 500;
    public static final String TAG = Foreground.class.getName();

    public interface Listener {
        void onBecameForeground(Activity foregroundActivity);
        void onBecameBackground();
    }

    private static Foreground instance;

    private boolean foreground = false;
    private boolean paused = true;
    private Handler handler;
    private List<Listener> listeners = new CopyOnWriteArrayList<>();
    private Runnable check;

    public Foreground() {
        // Use a background thread for the Handler.
        HandlerThread handlerThread = new HandlerThread("HandlerThread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    /**
     * Its not strictly necessary to use this method - _usually_ invoking
     * get with a Context gives us a path to retrieve the Application and
     * initialise, but sometimes (e.g. in test harness) the ApplicationContext
     * is != the Application, and the docs make no guarantees.
     *
     * @param application
     * @return an initialised Foreground instance
     */
    public static synchronized Foreground init(Application application){
        if (instance == null) {
            instance = new Foreground();
            application.registerActivityLifecycleCallbacks(instance);
        }
        return instance;
    }

    public static synchronized Foreground get(Application application){
        if (instance == null) {
            init(application);
        }
        return instance;
    }

    public static synchronized Foreground get(Context ctx){
        if (instance == null) {
            Context appCtx = ctx.getApplicationContext();
            if (appCtx instanceof Application) {
                init((Application)appCtx);
            }
            throw new IllegalStateException(
                "Foreground is not initialised and " +
                "cannot obtain the Application object");
        }
        return instance;
    }

    public static synchronized Foreground get(){
        if (instance == null) {
            throw new IllegalStateException(
                "Foreground is not initialised - invoke " +
                "at least once with parameterised init/get");
        }
        return instance;
    }

    public boolean isForeground(){
        return foreground;
    }

    public boolean isBackground(){
        return !foreground;
    }

    public void addListener(Listener listener){
        listeners.add(listener);
    }

    public void removeListener(Listener listener){
        listeners.remove(listener);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        paused = false;
        boolean wasBackground = !foreground;
        foreground = true;

        if (check != null)
            handler.removeCallbacks(check);

        if (wasBackground){
			Services.Log.info(TAG, "went foreground");
            for (Listener l : listeners) {
                l.onBecameForeground(activity);
            }
        } else {
			Services.Log.info(TAG, "still foreground");
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        paused = true;

        if (check != null)
            handler.removeCallbacks(check);

        handler.postDelayed(check = new Runnable(){
            @Override
            public void run() {
                if (foreground && paused) {
                    foreground = false;
					Services.Log.info(TAG, "went background");
                    for (Listener l : listeners) {
                        l.onBecameBackground();
                    }
                } else {
					Services.Log.info(TAG, "still foreground");
                }
            }
        }, CHECK_DELAY);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}

    @Override
    public void onActivityStarted(Activity activity) {}

    @Override
    public void onActivityStopped(Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

    @Override
    public void onActivityDestroyed(Activity activity) {}
}
