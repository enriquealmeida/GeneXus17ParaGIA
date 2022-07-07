package com.genexus.android.core.base.metadata.enums;

public class RequestCodes
{
	public static final short ACTION = 20;
	public static final short LOGIN = 40;
	public static final short FILTERS = 50;
	public static final short PREFERENCE = 60;

	public static final int PICKER = 150;

	public static final short ACTIONNOREFRESH = 30;

	/**
	 * Similar to ACTION, but resultCode should be ignored in onActivityResult().
	 * Always consider this action as successful when finished.
	 */
	public static final short ACTION_ALWAYS_SUCCESSFUL = 31;
}
