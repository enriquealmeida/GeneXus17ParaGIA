package com.genexus.android.location.geolocation.tracking;

import java.util.Date;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

public class TrackingData {
	private static final String PREFERENCES_KEY = "TrackingData";
	private static final String IS_TRACKING = "isTracking";
	private static final String LAST_LOCATION_ACTION_TIME = "lastLocationActionTime";
	private static final String ACTION = "action";
	private static final String ACTION_INTERVAL = "actionInterval";

	private static TrackingData sInstance;

	private TrackingData() {}

	public static synchronized TrackingData getInstance() {
		if (sInstance == null)
			sInstance = new TrackingData();

		return sInstance;
	}

	public void setTrackingData(boolean isTracking, Date lastLocationActionTime, String action, Integer actionInterval) {
		SharedPreferences session = Services.Preferences.getAppSharedPreferences(PREFERENCES_KEY);
		Editor sessionEditor = session.edit();
		sessionEditor.putBoolean(IS_TRACKING, isTracking);
		sessionEditor.putLong(LAST_LOCATION_ACTION_TIME, lastLocationActionTime.getTime());
		sessionEditor.putString(ACTION, action);
		sessionEditor.putInt(ACTION_INTERVAL, actionInterval);
		sessionEditor.apply();
	}

	public void setTrackingDataDate(Date lastLocationActionTime) {
		SharedPreferences session = Services.Preferences.getAppSharedPreferences(PREFERENCES_KEY);
		Editor sessionEditor = session.edit();
		sessionEditor.putLong(LAST_LOCATION_ACTION_TIME, lastLocationActionTime.getTime());
		sessionEditor.apply();
	}

	public void restoreTrackingData() {
		SharedPreferences session = Services.Preferences.getAppSharedPreferences(PREFERENCES_KEY);
		boolean isTracking = session.getBoolean(IS_TRACKING, false);
		long dateTime = session.getLong(LAST_LOCATION_ACTION_TIME, 0);
		Date lastLocationActionTime = new Date(dateTime);
		String action = session.getString(ACTION, Strings.EMPTY);
		int actionInterval = session.getInt(ACTION_INTERVAL, 0);
		Services.Location.restoreTracking(isTracking, lastLocationActionTime, action, actionInterval);
	}

	public void clear() {
		SharedPreferences session = Services.Preferences.getAppSharedPreferences(PREFERENCES_KEY);
		Editor sessionEditor = session.edit();
		sessionEditor.clear();
		sessionEditor.apply();
	}
}
