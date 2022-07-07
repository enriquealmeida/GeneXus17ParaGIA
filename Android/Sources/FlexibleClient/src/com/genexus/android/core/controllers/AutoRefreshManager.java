package com.genexus.android.core.controllers;

import java.util.LinkedHashMap;

import android.os.Handler;
import android.os.Looper;

import com.genexus.android.core.activities.ActivityController;
import com.genexus.android.core.base.metadata.IDataSourceDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.utils.Cast;

public class AutoRefreshManager
{
	private final ActivityController mParent;
	private final LinkedHashMap<IDataSourceController, AutoRefreshRunnable> mControllers;
	private final Handler mHandler;

	private static final String LOG_TAG = "AutoRefresh";

	public AutoRefreshManager(ActivityController parent)
	{
		mParent = parent;
		mControllers = new LinkedHashMap<>();
		mHandler = new Handler(Looper.myLooper());
	}

	public void onResume()
	{
		// Enable all current auto-refresh Runnables.
		for (IDataSourceController controller : mControllers.keySet())
			addDataSource(controller);
	}

	public void onPause()
	{
		// Stop all current auto-refresh Runnables.
		for (AutoRefreshRunnable r : mControllers.values())
			mHandler.removeCallbacks(r);

		for (IDataSourceController controller : mControllers.keySet())
			mControllers.put(controller, null);
	}

	public void onDestroy()
	{
		onPause();
		mControllers.clear();
	}

	public void addDataSource(IDataSourceController dataSource)
	{
		if (mControllers.get(dataSource) != null)
			return; // This controller is already tracked.

		DataSourceController real = Cast.as(DataSourceController.class, dataSource);
		if (real != null)
		{
			IDataSourceDefinition definition = real.getDefinition();
			if (definition.getAutoRefreshTime() != 0)
			{
				// Track for pause/resume.
				AutoRefreshRunnable runnable = new AutoRefreshRunnable(real);
				mControllers.put(dataSource, runnable);

				// Post refresh for when <time> has passed.
				int delayMillis = definition.getAutoRefreshTime() * 1000;
				mHandler.postDelayed(runnable, delayMillis);
			}
		}
	}

	public void removeAll(Iterable<IDataSourceController> dataSources)
	{
		for (IDataSourceController dataSource : dataSources)
			remove(dataSource);
	}

	private void remove(IDataSourceController dataSource)
	{
		AutoRefreshRunnable runnable = mControllers.get(dataSource);
		if (runnable != null)
			mHandler.removeCallbacks(runnable);

		mControllers.remove(dataSource);
	}

	private class AutoRefreshRunnable implements Runnable
	{
		private final DataSourceController mDataSource;

		private AutoRefreshRunnable(DataSourceController dataSource)
		{
			mDataSource = dataSource;
		}

		@Override
		public void run()
		{
			mControllers.put(mDataSource, null);
			if (mParent.isRunning())
			{
				// Run and reschedule for next delay.
				Services.Log.debug(LOG_TAG, "Automatic Refresh for " + mDataSource.getName());
				mDataSource.onRefresh(RefreshParameters.AUTOMATIC_TIMED);
				addDataSource(mDataSource);
			}
		}
	}
}
