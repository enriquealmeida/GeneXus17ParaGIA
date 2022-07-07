package com.genexus.android.beacons;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.genexus.android.core.base.utils.NameMap;

class ProximityAlertsDatabase {

	private static final int VERSION = 1;
	private static final String DB_NAME = "gxbeacons.db";
	private final SQLiteDatabase mDatabase;
	private NameMap<GxBeaconProximityAlert> mCache;

	private interface ProximityAlerts {
		String REGION_ID = "ProximityAlertId";
		String BEACON_UUID = "ProximityAlertRegionUuid";
		String BEACON_GROUP_ID = "ProximityAlertRegionGroupId";
		String BEACON_ID = "ProximityAlertRegionId";
		String NOTIFY_ENTER = "ProximityAlertNotifyOnEnter";
		String NOTIFY_EXIT = "ProximityAlertNotifyOnExit";

		String TABLE = "ProximityAlerts";
		String[] COLUMNS = new String[]{REGION_ID, BEACON_UUID, BEACON_GROUP_ID, BEACON_ID, NOTIFY_ENTER, NOTIFY_EXIT};
	}

	private static class DatabaseOpenHelper extends SQLiteOpenHelper {
		DatabaseOpenHelper(Context context) {
			super(context, DB_NAME, null, VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + ProximityAlerts.TABLE + " (" +
					ProximityAlerts.REGION_ID + " CHARACTER, " +
					ProximityAlerts.BEACON_UUID + " CHARACTER, " +
					ProximityAlerts.BEACON_GROUP_ID + " INTEGER, " +
					ProximityAlerts.BEACON_ID + " INTEGER, " +
					ProximityAlerts.NOTIFY_ENTER + " INTEGER, " + // BOOLEAN
					ProximityAlerts.NOTIFY_EXIT + " INTEGER, " +  // BOOLEAN
					"PRIMARY KEY (" + ProximityAlerts.REGION_ID + "))");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Change this if we update VERSION!
			db.execSQL("DROP TABLE IF EXISTS " + ProximityAlerts.TABLE);
			onCreate(db);
		}
	}

	ProximityAlertsDatabase(Context context) {
		DatabaseOpenHelper helper = new DatabaseOpenHelper(context);
		mDatabase = helper.getWritableDatabase();
	}

	void addProximityAlert(GxBeaconProximityAlert alert) {
		ContentValues values = new ContentValues();
		values.put(ProximityAlerts.REGION_ID, alert.getRegionId());
		values.put(ProximityAlerts.BEACON_UUID, alert.getRegion().getBeaconMatch().getUuid());
		values.put(ProximityAlerts.BEACON_GROUP_ID, alert.getRegion().getBeaconMatch().getGroupId());
		values.put(ProximityAlerts.BEACON_ID, alert.getRegion().getBeaconMatch().getId());
		values.put(ProximityAlerts.NOTIFY_ENTER, alert.shouldNotifyOnEntry() ? 1 : 0);
		values.put(ProximityAlerts.NOTIFY_EXIT, alert.shouldNotifyOnExit() ? 1 : 0);

		invalidateCache();
		mDatabase.insertWithOnConflict(ProximityAlerts.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}

	public void addProximityAlerts(List<GxBeaconProximityAlert> alerts) {
		mDatabase.beginTransaction();
		try {
			for (GxBeaconProximityAlert alert : alerts)
				addProximityAlert(alert);

			mDatabase.setTransactionSuccessful();
		} finally {
			mDatabase.endTransaction();
		}
	}

	List<GxBeaconProximityAlert> getProximityAlerts() {
		return new ArrayList<>(getCache().values());
	}

	GxBeaconProximityAlert getProximityAlert(String regionId) {
		return getCache().get(regionId);
	}

	void removeProximityAlert(String regionId) {
		invalidateCache();
		mDatabase.delete(ProximityAlerts.TABLE, String.format("%s = ?", ProximityAlerts.REGION_ID), new String[]{regionId});
	}

	void clearProximityAlerts() {
		invalidateCache();
		mDatabase.delete(ProximityAlerts.TABLE, null, null);
	}

	private synchronized NameMap<GxBeaconProximityAlert> getCache() {
		if (mCache == null) {
			NameMap<GxBeaconProximityAlert> cache = new NameMap<>();
			try (Cursor cursor = mDatabase.query(ProximityAlerts.TABLE, ProximityAlerts.COLUMNS,
					null, null, null, null, null)) {

				int colRegionId = cursor.getColumnIndexOrThrow(ProximityAlerts.REGION_ID);
				int colBeaconUuid = cursor.getColumnIndexOrThrow(ProximityAlerts.BEACON_UUID);
				int colBeaconGroupId = cursor.getColumnIndexOrThrow(ProximityAlerts.BEACON_GROUP_ID);
				int colBeaconId = cursor.getColumnIndexOrThrow(ProximityAlerts.BEACON_ID);
				int colNotifyEnter = cursor.getColumnIndexOrThrow(ProximityAlerts.NOTIFY_ENTER);
				int colNotifyExit = cursor.getColumnIndexOrThrow(ProximityAlerts.NOTIFY_EXIT);
				while (cursor.moveToNext()) {
					String regionId = cursor.getString(colRegionId);
					String beaconUuid = cursor.getString(colBeaconUuid);
					int beaconGroupId = cursor.getInt(colBeaconGroupId);
					int beaconId = cursor.getInt(colBeaconId);
					boolean notifyEnter = (cursor.getInt(colNotifyEnter) != 0);
					boolean notifyExit = (cursor.getInt(colNotifyExit) != 0);
					GxBeaconRegion region = new GxBeaconRegion(regionId, new GxBeaconInfo(beaconUuid, beaconGroupId, beaconId));
					GxBeaconProximityAlert alert = new GxBeaconProximityAlert(region, notifyEnter, notifyExit);
					cache.put(alert.getRegionId(), alert);
				}
				mCache = cache;
			}
		}
		return mCache;
	}

	private synchronized void invalidateCache() {
		mCache = null;
	}
}
