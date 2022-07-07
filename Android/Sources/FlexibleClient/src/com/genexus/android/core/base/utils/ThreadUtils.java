package com.genexus.android.core.base.utils;

public class ThreadUtils
{
	/**
	 * Causes the thread which sent this message to sleep for the given interval of time (given in milliseconds).
	 * @param time The time to sleep in milliseconds
	 * @return True if the time was completed; false if an InterruptedException occurred.
	 */
	public static boolean sleep(long time)
	{
		try
		{
			Thread.sleep(time);
			return true;
		}
		catch (InterruptedException e)
		{
			return false;
		}
	}
}
