package com.genexus.android.core.base.metadata.enums;

import androidx.annotation.NonNull;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

public enum ImageScaleType
{
	NO_SCALE,
	FILL,
	FILL_KEEPING_ASPECT,
	FIT,
	TILE;

	public static @NonNull ImageScaleType parse(String str)
	{
		return parse(str, FIT);
	}

	public static @NonNull ImageScaleType parse(String str, @NonNull ImageScaleType defaultValue)
	{
		if (!Strings.hasValue(str))
			return defaultValue;

		if (str.equalsIgnoreCase("originalSize"))
			return NO_SCALE;

		if (str.equalsIgnoreCase("scaleToFill"))
			return FILL;

		if (str.equalsIgnoreCase("scaleAspectFillKeepingAspect"))
			return FILL_KEEPING_ASPECT;

		if (str.equalsIgnoreCase("scaleToFitKeepingAspect"))
			return FIT;

		if (str.equalsIgnoreCase("tile"))
			return TILE;

		Services.Log.warning("Unknown ImageScaleType value: " + str);
		return defaultValue;
	}
}
