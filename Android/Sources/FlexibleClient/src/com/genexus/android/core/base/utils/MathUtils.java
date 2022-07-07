package com.genexus.android.core.base.utils;

public class MathUtils
{
	/**
     * Equivalent to Math.max(low, Math.min(high, amount));
     */
    public static int constrain(int amount, int low, int high)
    {
        return amount < low ? low : (amount > high ? high : amount);
    }

    /**
     * Equivalent to Math.max(low, Math.min(high, amount));
     */
    public static float constrain(float amount, float low, float high)
    {
        return amount < low ? low : (amount > high ? high : amount);
    }

    /**
     * Equivalent to Math.max(low, Math.min(high, amount));
     */
    public static double constrain(double amount, double low, double high)
    {
        return amount < low ? low : (amount > high ? high : amount);
    }

    /**
     * Returns true if amount is between low and high (both inclusive), false otherwise.
     */
    public static boolean isConstrained(int amount, int low, int high)
    {
    	return (amount >= low && amount <= high);
    }

    /**
     * Calculates a specified percentage of a quantity.
     */
	public static float getPercent(float quantity, float percent)
	{
		return quantity * (percent / 100f);
	}

	public static int round(float number)
	{
		return (int)(number + 0.5f);
	}
}
