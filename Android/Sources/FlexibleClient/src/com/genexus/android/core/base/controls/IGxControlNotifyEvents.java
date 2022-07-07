package com.genexus.android.core.base.controls;

public interface IGxControlNotifyEvents
{
	enum EventType { ACTION_CALLED, REFRESH, ACTIVITY_PAUSED, ACTIVITY_RESUMED, ACTIVITY_DESTROYED,
					ACTIVITY_STOPPED, ACTIVITY_STARTED, GRID_PAGE_CHANGED}

	void notifyEvent(EventType type);
}
