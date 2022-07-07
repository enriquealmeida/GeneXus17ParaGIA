package com.genexus.android.core.actions;

public enum ActionResult
{
	/**
	 * Indicates that the action was successful and execution may continue.
	 * This is the "normal" result.
	 */
	SUCCESS_CONTINUE,

	/**
	 * Indicates that the action was successful and execution may continue, but that the calling
	 * component should not be refreshed, probably because a refresh would override the effect of the action.
	 */
	SUCCESS_CONTINUE_NO_REFRESH,

	/**
	 * Indicates that the action has succeeded "so far" but it has not concluded. For example,
	 * an Activity was started and we need to wait for onActivityResult() to continue.
	 */
	SUCCESS_WAIT,

	/**
	 * Indicates that the action has failed. Normally this should abort the Composite block.
	 */
	FAILURE;

	public boolean isSuccess()
	{
		return this != FAILURE;
	}
}
