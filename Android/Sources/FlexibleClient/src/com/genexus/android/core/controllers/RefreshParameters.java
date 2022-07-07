package com.genexus.android.core.controllers;

/**
 * Refresh Options.
 */
public class RefreshParameters
{
	/**
	 * Reason for the refresh. The UI may behave differently according to this value.
	 */
	public final Reason reason;

	/**
	 * If true, the data request will ask for the same records that are currently loaded
	 * (instead of the first "n").
	 */
	public final boolean keepPosition;

	public RefreshParameters(Reason reason, boolean keepPosition)
	{
		this.reason = reason;
		this.keepPosition = keepPosition;
	}

	public static final RefreshParameters IMPLICIT = new RefreshParameters(Reason.IMPLICIT, true);
	public static final RefreshParameters AUTOMATIC_TIMED = new RefreshParameters(Reason.AUTOMATIC_TIMED, true);

	public boolean isSearchOrFilter()
	{
		return (reason == Reason.SEARCH ||
				reason == Reason.FILTER ||
				reason == Reason.RESET_SEARCH_OR_FILTER);
	}

	public enum Reason
	{
		IMPLICIT,
		AUTOMATIC_TIMED,
		MANUAL,
		SEARCH,
		FILTER,
		RESET_SEARCH_OR_FILTER
	}
}
