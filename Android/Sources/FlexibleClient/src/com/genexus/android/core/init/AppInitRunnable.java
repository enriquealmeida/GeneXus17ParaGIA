package com.genexus.android.core.init;

import android.content.Context;

import com.genexus.android.ContextImpl;
import com.genexus.android.core.base.metadata.loader.ApplicationLoader;
import com.genexus.android.core.base.metadata.loader.LoadResult;
import com.genexus.android.core.base.metadata.loader.SyncManager;
import com.genexus.android.core.base.services.Services;

import org.jetbrains.annotations.NotNull;

public class AppInitRunnable implements Runnable {
    private final Context mContext;
    private final ApplicationLoader mApplicationLoader;
    private final Listener mListener;
    private final SyncManager.Listener mSyncListener;
	private final ApplicationLoader.Listener mAppLoaderListener;

    public AppInitRunnable(Context context,
                           ApplicationLoader applicationLoader,
                           Listener listener,
                           SyncManager.Listener syncListener,
						   ApplicationLoader.Listener appLoaderListener) {
        mContext = context;
        mApplicationLoader = applicationLoader;
        mListener = listener;
        mSyncListener = syncListener;
        mAppLoaderListener = appLoaderListener;
    }

    @Override
    public void run() {
		LoadResult loadResult = loadApplicationResult(mContext, mApplicationLoader, mSyncListener, mAppLoaderListener);
        Services.Device.runOnUiThread(() -> mListener.onAppInitFinished(loadResult));
    }

	@NotNull
	private static LoadResult loadApplicationResult(Context context,
	                                                ApplicationLoader applicationLoader,
											        SyncManager.Listener syncListener,
											        ApplicationLoader.Listener metadataListener) {
		LoadResult loadResult;

		try {
			// Load the Application.
			loadResult = applicationLoader.loadApplication(new ContextImpl(context), context, syncListener, metadataListener);
		} catch (OutOfMemoryError ex) {
			// Notify the user that the app could not load correctly due to reduced memory.
			loadResult = LoadResult.error(ex);
		}

		if (loadResult.getCode() == LoadResult.RESULT_OK) {
			Services.Application.clearData();
			Services.Application.getLifecycle().notifyMetadataLoadFinished();
		}
		return loadResult;
	}

	public static boolean checkAndLoadApplicationResult() {
		if (!Services.Application.isLoaded() && !Services.Application.isLoadingMetadata()) {
			Services.Log.warning("Reload app metadata from checkAndLoadApplicationResult");
			ApplicationLoader applicationLoader = new ApplicationLoader(Services.Application.get());
			LoadResult loadResult = loadApplicationResult(Services.Application.getAppContext(), applicationLoader, null, null);
			return loadResult.getCode() == LoadResult.RESULT_OK;
		}
		return true;
	}

	public interface Listener {
        void onAppInitFinished(LoadResult loadResult);
    }
}
