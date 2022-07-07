package com.genexus.android.core.ui.navigation;

/**
 * Enumeration for CallType property in CallOptions.
 */
public enum CallType
{
	DEFAULT,
	PUSH,
	REPLACE,
	POPUP,
	CALLOUT;

	private static final String STR_PUSH = "push";
	private static final String STR_REPLACE = "replace";
	private static final String STR_POPUP = "popup";
	private static final String STR_CALLOUT = "callout";

	public static CallType tryParse(String str)
	{
		if (STR_PUSH.equalsIgnoreCase(str))
			return PUSH;
		else if (STR_REPLACE.equalsIgnoreCase(str))
			return REPLACE;
		else if (STR_POPUP.equalsIgnoreCase(str))
			return POPUP;
		else if (STR_CALLOUT.equalsIgnoreCase(str))
			return CALLOUT;
		else
			return DEFAULT;
	}
}
