package com.genexus.android.core.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.collection.SparseArrayCompat;

import com.genexus.android.core.activities.IntentParameters;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.common.DataRequest;
import com.genexus.android.core.common.IntentHelper;
import com.genexus.android.core.providers.EntityDataProvider;

public abstract class EntityService extends Service
{
	public static final String ACTION_LOAD_DATA = "com.artech.services.EntityService.LOAD_DATA";
	public static final String ACTION_DESTROY_SESSION = "com.artech.services.EntityService.DESTROY_SESSION";

	private final SparseArrayCompat<List<LoadDataTask>> mTasks = new SparseArrayCompat<>();
	private final EntityServiceTaskRunner mTaskRunner = new EntityServiceTaskRunner();
	private final Object sSyncObject = new Object();

	private static final boolean LOG_ENABLED = false;
	private static final String LOG_TAG = "EntityService";

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		if (intent == null)
			return Service.START_NOT_STICKY;

		String action = intent.getAction();
		int sessionId = intent.getIntExtra(IntentParameters.Service.DATA_VIEW_SESSION, 0);

		if (ACTION_LOAD_DATA.equals(action))
		{
			String intentFilter = intent.getStringExtra(IntentParameters.Service.INTENT_FILTER);
			EntityDataProvider provider = IntentHelper.getObject(intent, IntentParameters.Service.DATA_PROVIDER, EntityDataProvider.class);
			int requestType = intent.getIntExtra(IntentParameters.Service.REQUEST_TYPE, 0); // This parameter is mandatory; 0 is an error.
			int requestCount = intent.getIntExtra(IntentParameters.Service.REQUEST_COUNT, DataRequest.COUNT_DEFAULT);

			if (sessionId == 0 || intentFilter == null || requestType == 0 || provider == null)
			{
				Services.Log.error("Incorrect intent passed to EntityService. Ignoring.");
				return Service.START_NOT_STICKY;
			}

			startTask(sessionId, provider, intentFilter, requestType, requestCount);
		}
		else if (ACTION_DESTROY_SESSION.equals(action))
		{
			if (sessionId != 0)
				destroyTasks(sessionId);
		}

		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	private void startTask(int sessionId, EntityDataProvider provider, String intentFilter, int requestType, int requestCount)
	{
		// Cases of requests:
		// 1) FIRST: first return data from cache and then query the server.
		// 2) REFRESH: only query the server (checking for changes).
		// 3) MORE: only query the server (previous data is assumed to have been loaded).
		// 4) CACHE: return data from cache and stop.
		if (requestType == DataRequest.REQUEST_FIRST ||	requestType == DataRequest.REQUEST_CACHED)
		{
			// Ask for local data first.
			LoadDataTask task = new LoadDataTask(this, sessionId, provider, intentFilter, DataRequest.REQUEST_CACHED, DataRequest.COUNT_ALL);
			startOrEnqueueTask(task);
		}

		if (requestType != DataRequest.REQUEST_CACHED)
		{
			// Ask for server data. May return "up-to-date" if:
			// a) Asking for data which was already returned by the previous request from cache.
			// b) Asking for REQUEST_MORE and the data is already fully cached.
			LoadDataTask task = new LoadDataTask(this, sessionId, provider, intentFilter, requestType, requestCount);
			startOrEnqueueTask(task);
		}
	}

	/**
	 * Either start the task immediately (if none others are running for this sessionId)
	 * or enqueue it to be started later otherwise.
	 */
	private void startOrEnqueueTask(LoadDataTask task)
	{
		debug("EntityService: Readying task -- " + task);
		synchronized (sSyncObject)
		{
			// Get the list of running and/or waiting tasks for this session.
			List<LoadDataTask> taskList = mTasks.get(task.getSessionId());
			if (taskList == null)
			{
				taskList = new ArrayList<>();
				mTasks.put(task.getSessionId(), taskList);
			}
			else
			{
				// Optimization: ignore e.g. repeated refreshes of the same data.
				for (LoadDataTask pendingTask : taskList)
				{
					if (pendingTask.getRequestType() == task.getRequestType() &&
						pendingTask.getDataUri().equals(task.getDataUri()))
					{
						debug("EntityService: Task ignored -- " + task);
						return;
					}
				}
			}

			// Always add it to the map. It contains both running and waiting tasks.
			taskList.add(task);

			// Reorder only PENDING tasks (i.e. excluding the first one, which is presumably running)
			// so that local tasks execute first.
			if (taskList.size() > 2)
				Collections.sort(taskList.subList(1, taskList.size()), LoadDataTask.PRIORITY_COMPARATOR);

			// See if it can start immediately or must wait.
			if (taskList.size() == 1)
			{
				debug("EntityService: Task starting immediately -- " + task);
				mTaskRunner.execute(task);
			}
			else
				debug("EntityService: Task will wait -- " + task);
		}
	}

	/**
	 * Called by LoadDataTask when it finishes.
	 * @param task Task that finished.
	 * @param intentFilter Intent action filter (supplied by the call to service).
	 * @param response Data returned by EntityDataProvider. Null if the request was canceled.
	 */
	void onTaskFinished(LoadDataTask task, String intentFilter, EntityServiceResponse response)
	{
		// Send the response back via the broadcast manager.
		if (response != null)
		{
			debug("EntityService: Task FINISHED_OK -- " + task);

			Intent intent = new Intent(intentFilter);
			EntityServiceResponse.put(intent, response);
			LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
		}
		else
			debug("EntityService: Task FINISHED_NO_DATA -- " + task);

		// Update the tasks queue, running any that might've been waiting for this one.
		synchronized (sSyncObject)
		{
			// Get the list of running and/or waiting tasks for this session. This one
			// should be among them (unless it was canceled and removed).
			List<LoadDataTask> taskList = mTasks.get(task.getSessionId());

			if (!task.isCancelled())
			{
				// Sanity checks.
				if (taskList == null || !taskList.contains(task))
					throw new IllegalStateException("Task reports being finished, but it was not in the list of running tasks! " + task);

				if (taskList.indexOf(task) != 0)
					throw new IllegalStateException("Finished task should be the first one in its queue, but it's not. Task: " + task);

				taskList.remove(task);

				// Either move to the next one, or remove the list if its emptied.
				if (taskList.size() != 0)
				{
					LoadDataTask nextTask = taskList.get(0);
					debug("Service: Waking up next task for session -- " + nextTask);
					mTaskRunner.execute(nextTask);
				}
				else
				{
					debug("Service: No more tasks for session -- " + task.getSessionId());
					mTasks.remove(task.getSessionId());
				}
			}
			else
			{
				// Sanity checks
				if (taskList != null && taskList.contains(task)) {
					// change exception for log error to avoid app crash, this should not happens in a common case.
					// put error message to identify this error if we reproduce it somehow
					Services.Log.error("Task reports being cancelled, but it was in the list of running tasks! " + task);
				}
			}
		}
	}

	private void destroyTasks(int sessionId)
	{
		synchronized (sSyncObject)
		{
			List<LoadDataTask> taskList = mTasks.get(sessionId);
			if (taskList != null)
			{
				debug("EntityService: Discarding " + taskList.size() + " pending tasks for session " + sessionId);

				for (LoadDataTask task : taskList)
					task.cancel();

				taskList.clear();
				mTasks.remove(sessionId);
			}
		}
	}

	/**
	 * Queries if there are any tasks currently executing.
	 */
	public boolean isWorking()
	{
		synchronized(sSyncObject)
		{
			return mTasks.size() != 0;
		}
	}

	static void debug(String message)
	{
		if (LOG_ENABLED)
			Services.Log.debug(LOG_TAG, message);
	}
}
