package com.genexus.android.core.base.synchronization;

import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.base.services.Services;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SynchronizationSendExecutor
{
	static SynchronizationSendExecutor  mSynchronizationSendExecutor = null;

	ThreadPoolExecutor threadPoolExecutor = null;

	public static SynchronizationSendExecutor getInstance() {
		if (mSynchronizationSendExecutor==null)
			mSynchronizationSendExecutor = new SynchronizationSendExecutor( );
		// return the current instance
		return mSynchronizationSendExecutor;
	}

	public void startProcess()
	{
		if (threadPoolExecutor==null)
		{
			// like  newSingleThreadExecutor(), but with access to it queue
			threadPoolExecutor = new ThreadPoolExecutor(1, 1,
					0L, TimeUnit.MILLISECONDS,
					new LinkedBlockingQueue<Runnable>());

			threadPoolExecutor.execute(getSynchronizationSendRunnable());
		}
		else
		{
			Services.Log.debug("SynchronizationSendExecutor startProcess queue size " + threadPoolExecutor.getQueue().size() + " active" + threadPoolExecutor.getActiveCount());
			// add executor if there is none pending.
			if (threadPoolExecutor.getQueue().size()==0) {
				Services.Log.debug("SynchronizationSendExecutor startProcess add runnable to execute");
				threadPoolExecutor.execute(getSynchronizationSendRunnable());
			}

		}
	}
	private Runnable getSynchronizationSendRunnable()
	{
		Runnable runnable = new Runnable()
		{
			@Override
			public void run()
			{
				if (SynchronizationSendHelper.isRunningSendBackground) {
					Services.Log.debug("callOfflineReplicator (Sync.Send) from background NOT RUN, already Running ");
					return;
				}
				SynchronizationSendHelper.isRunningSendBackground = true;

				EntityList pendings = Services.Sync.getPendingEventsList(SynchronizationHelper.PENDING_STATUS_STRING); // Pending
				if (pendings.size()>0)
				{
					Services.Log.debug("callOfflineReplicator (Sync.Send) from background ");
					SynchronizationSendHelper.callOfflineReplicator();
				}
				else
				{
					Services.Log.debug("callOfflineReplicator (Sync.Send) from background. No pending events ");
				}

				SynchronizationSendHelper.isRunningSendBackground = false;

			}
		};
		return runnable;
	}


			
}
