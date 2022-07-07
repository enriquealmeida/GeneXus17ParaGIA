package com.genexus.android.core.services;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.annotation.NonNull;

import com.genexus.android.core.utils.TaskRunner;

/**
 * Task executor for EntityService.
 *
 * Mimics TaskRunner thread pool but keeps a separate thread pool to avoid
 * starving out other Tasks (e.g. ActionExecution).
 */
class EntityServiceTaskRunner
{
	private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
	private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
	private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
	private static final int KEEP_ALIVE = 1;

	private static final ThreadFactory THREAD_FACTORY = new ThreadFactory()
	{
		private final AtomicInteger mCount = new AtomicInteger(1);

		@Override
		public Thread newThread(@NonNull Runnable r)
		{
			return new Thread(r, "EntityService Task #" + mCount.getAndIncrement());
		}
	};

	private static final BlockingQueue<Runnable> POOL_WORK_QUEUE = new LinkedBlockingQueue<>();

	private static final Executor EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE,
			MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, POOL_WORK_QUEUE, THREAD_FACTORY);

	public void execute(LoadDataTask task)
	{
		TaskRunner.executeOnExecutor(EXECUTOR, task);
	}
}
