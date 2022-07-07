package com.genexus.android.core.base.utils;

import androidx.annotation.NonNull;

/**
 * Utils for Runnables.
 */
public class RunnableUtils
{
	public static <T> ResultRunnable<T> buildResultRunnableFromRunnable(@NonNull final Runnable r)
	{
		return new ResultRunnable<T>()
		{
			@Override
			public T run()
			{
				r.run();
				return null;
			}
		};
	}

	public static Runnable chainRunnables(@NonNull final Iterable<Runnable> runnables)
	{
		return new Runnable()
		{
			@Override
			public void run()
			{
				for (Runnable r : runnables)
					r.run();
			}
		};
	}
}
