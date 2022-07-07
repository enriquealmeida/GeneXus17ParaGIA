package com.genexus.android.core.base.utils;

import java.io.Serializable;

/**
 * Class representing a numeric range (both minimum and maximum may be null,
 * specifying that the range is unbounded on that side).
 */
public class Range implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public final Integer minimum;
	public final Integer maximum;

	/**
	 * Constructs a new Range with the specified minimum and maximum
	 * @param min Minimum value. If null, the range will have no minimum.
	 * @param max Maximum value. If null, the range will have no maximum.
	 */
	public Range(Integer min, Integer max)
	{
		minimum = min;
		maximum = max;
	}

	public boolean contains(int value)
	{
		return (minimum == null || minimum <= value) && (maximum == null || maximum >= value);
	}

	public boolean contains(Range other)
	{
		if (minimum != null)
		{
			if (other.minimum == null)
				return false;

			if (other.minimum < minimum)
				return false;
		}

		if (maximum != null)
		{
			if (other.maximum == null)
				return false;

			if (other.maximum > maximum)
				return false;
		}

		return true;
	}

	public boolean isAll()
	{
		return (minimum == null && maximum == null);
	}
}
