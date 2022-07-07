package com.genexus.android.core.base.metadata.theme;

public enum BackgroundImageMode
{
	SCALE_TO_FILL ("scaleToFill"),
	TILE ("tile");

	private final String mString;

	BackgroundImageMode(String str)
	{
		mString = str;
	}

	public static BackgroundImageMode parse(String str)
	{
		for (BackgroundImageMode mode : values())
			if (mode.mString.equalsIgnoreCase(str))
				return mode;

		return SCALE_TO_FILL;
	}
}
