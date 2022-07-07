package com.genexus.android.core.base.utils;

public class PrimitiveUtils
{
	public static boolean isNonZero(Integer v)
	{
		return (v != null && v != 0);
	}
	
	public static boolean areEquals(Integer v1, Integer v2)
	{
		if (v1 != null)
			return v1.equals(v2);
		else
			return v2 == null;
	}
}
