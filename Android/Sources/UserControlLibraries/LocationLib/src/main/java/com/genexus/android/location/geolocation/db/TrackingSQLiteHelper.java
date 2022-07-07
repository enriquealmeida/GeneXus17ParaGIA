package com.genexus.android.location.geolocation.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import androidx.annotation.NonNull;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controls.maps.LocationApi;
import com.genexus.android.location.geolocation.LocationEntityFactory;

public class TrackingSQLiteHelper extends SQLiteOpenHelper {
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "trackingLocationsManager";

	// proximityAlerts table name
	private static final String TABLE_TRACKINGLOCATION = "trackingLocation";

	// proximityAlerts Table Columns names
	private static final String KEY_ID = "id";

	private static final String KEY_DATE_TIME = "datetime";
	private static final String KEY_GEOLOCATION = "geolocation";

	// Singleton Db connection from  https://stackoverflow.com/a/24201387
	private static TrackingSQLiteHelper instance;

	public static synchronized TrackingSQLiteHelper getInstance(Context context) {
		if (instance == null)
			instance = new TrackingSQLiteHelper(context);

		return instance;
	}

	private TrackingSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String createStmt = "CREATE TABLE " + TABLE_TRACKINGLOCATION + "("
				+ KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_DATE_TIME + " INTEGER,"  // date as long with getTime()
				+ KEY_GEOLOCATION + " TEXT" + ")";  // geolocation as String in json format
		db.execSQL(createStmt);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACKINGLOCATION);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new Location
	public long addLocation(Location location, Context context) {
		long row = 0;
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		//values.put(KEY_ID, notification.getId()); // Location Id

		// from: http://stackoverflow.com/questions/18192469/how-to-store-and-retrieve-date-from-sqlite-db-in-android
		values.put(KEY_DATE_TIME, Long.valueOf(location.getTime() / 1000)); // Location
		values.put(KEY_GEOLOCATION, LocationEntityFactory.createLocationEntity(location, context).toString()); // Location

		// Inserting Row
		row = db.insert(TABLE_TRACKINGLOCATION, null, values);
		return row;
	}

	// Getting All Location
	public @NonNull List<TrackingLocation> getAllLocations(LocationApi apiType) {
		List<TrackingLocation> locationList = new ArrayList<>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_TRACKINGLOCATION;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		try {
			while (cursor.moveToNext()) {
				TrackingLocation location = new TrackingLocation();

				location.setId(cursor.getColumnIndexOrThrow(KEY_ID));
				location.setGeolocationJson(cursor.getString(cursor.getColumnIndexOrThrow(KEY_GEOLOCATION)), apiType);
				location.setDateTimetime(cursor.getLong(cursor.getColumnIndexOrThrow(KEY_DATE_TIME)) * 1000);

				// Adding contact to list
				locationList.add(location);
			}
		} catch (IllegalArgumentException ex) {
			Services.Log.error(ex);
		}

		// return Notification list
		return locationList;
	}

	/*
    // Deleting single Location
    public void deleteLocation(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRACKINGLOCATION, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }
 	*/

	public void deleteAllLocation() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_TRACKINGLOCATION, null, null);
	}
}
