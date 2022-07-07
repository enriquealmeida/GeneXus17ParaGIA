package com.genexus.android.websockets;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Intent;

import com.genexus.android.core.actions.ActionExecution;
import com.genexus.android.core.base.services.Services;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class ProcessMessagesHelper
{

	static ProcessMessagesHelper  myProcessMessageHelper = null;

	private ConcurrentLinkedQueue<Intent> mSendQueue = new ConcurrentLinkedQueue<Intent>();
	private String mFullEventName;

	private ExecutorService threadPoolExecutor = null;

	// Private Constructor: can't be called from outside this class
	private ProcessMessagesHelper(String fullEventName) {
		mFullEventName = fullEventName;
	}

	public static ProcessMessagesHelper getInstance(String fullEventName) {
		if (myProcessMessageHelper==null)
			myProcessMessageHelper = new ProcessMessagesHelper( fullEventName);
		// return the current instance
		return myProcessMessageHelper;
	}

	// enqueue intent
	public void enQueue(Intent message)
	{
		mSendQueue.add(message);
	}

	public boolean hasPendings()
	{
		return !mSendQueue.isEmpty();
	}

	public void startProcess()
	{
		if (threadPoolExecutor==null)
		{
			threadPoolExecutor = Executors.newSingleThreadExecutor();
			threadPoolExecutor.execute(getProcessMessagesRunnable());
		}
		else
		{
			threadPoolExecutor.execute(getProcessMessagesRunnable());
		}
	}


	private Runnable getProcessMessagesRunnable()
	{
		Runnable runnable = new Runnable()
		{
			@Override
			public void run()
			{
				Services.Log.debug("ProcessMessagesRunnable", "Start runnable task, queue size" + mSendQueue.size());
				while (hasPendings()) {
					// wait previous messages to process.
					while (ActionExecution.isEventRunning(mFullEventName)) {
						Services.Log.debug("ProcessMessagesRunnable", "wait until last message was process ");
						try {
							Thread.sleep(100);
						} catch (InterruptedException ignored) { }
					}
					Services.Log.debug("ProcessMessagesRunnable", "process message now!");

					Intent lastIntent = mSendQueue.poll();
					LocalBroadcastManager.getInstance(Services.Application.getAppContext()).sendBroadcast(lastIntent);

					// wait message create new event, and start execute it
					try {
						Thread.sleep(200);
					} catch (InterruptedException ignored) { }
				}

				Services.Log.debug("ProcessMessagesRunnable", "End runnable Task, queue size" + mSendQueue.size());
			}
		};
		return runnable;
	}

}
