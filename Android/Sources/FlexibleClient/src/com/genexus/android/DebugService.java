package com.genexus.android;

import java.io.IOException;
import java.util.Random;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.MathUtils;

public class DebugService
{
	private static double sNetworkDelayProbability = 0;
	private static int sNetworkDelayTime = 0;
	private static double sNetworkErrorProbability = 0;
	private static boolean sNetworkOffline = false;

	private static Random sRandom = new Random();

	/**
	 * Forces a delay in random requests to server.
	 * @param probability Probability that the delay will occur (0 to 1.0).
	 * @param delay Delay in milliseconds.
	 */
	public static void setNetworkDelay(double probability, int delay)
	{
		sNetworkDelayProbability = MathUtils.constrain(probability, 0.0, 1.0);
		sNetworkDelayTime = Math.max(0, delay);
	}

	/**
	 * Forces random requests to the server to fail.
	 * @param probability Probability that the error will occur (0 to 1.0).
	 */
	public static void setNetworkError(double probability)
	{
		sNetworkErrorProbability = probability;
	}

	/**
	 * Forces the application to think there is no internet connection.
	 */
	public static void setNetworkOffline(boolean offline)
	{
		sNetworkOffline = offline;
	}

	public static void onHttpRequest() throws IOException
	{
		if (sNetworkDelayProbability > 0 && sNetworkDelayTime > 0)
		{
			try
			{
				// Chance to add a delay to the request.
				double roll = sRandom.nextDouble();
				if (roll < sNetworkDelayProbability)
					Thread.sleep(sNetworkDelayTime);
			}
			catch (InterruptedException e) { }
		}

		if (sNetworkErrorProbability > 0)
		{
			double roll = sRandom.nextDouble();
			if (roll < sNetworkErrorProbability)
				throw new IOException("DebugService: Random exception forced on network request.");
		}
	}

	public static boolean isNetworkOffline()
	{
		if (sNetworkOffline)
			Services.Log.debug("DebugService: Reporting network as offline");

		return sNetworkOffline;
	}
}
