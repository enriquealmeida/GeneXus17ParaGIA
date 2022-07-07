package com.genexus.android.location.geolocation.db;
 
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.genexus.android.core.base.services.Services;
 
public class ProximityAlertsSQLiteHelper extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "proximityAlertsManager";
 
    // proximityAlerts table name
    private static final String TABLE_PROXIMITYALERTS = "proximityAlerts";
 
    // proximityAlerts Table Columns names
    private static final String KEY_ID = "id";
    
    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_GEOLOCATION = "geolocation";
    private static final String KEY_RADIUS = "radius";
    private static final String KEY_DATE_TIME = "datetime";
    private static final String KEY_ACTION_NAME = "actionname";
 
    public ProximityAlertsSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createStmt = "CREATE TABLE " + TABLE_PROXIMITYALERTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_GEOLOCATION + " TEXT,"
                + KEY_RADIUS + " INTEGER,"
        		+ KEY_DATE_TIME + " TEXT,"
                + KEY_ACTION_NAME + " TEXT" + ")";
        db.execSQL(createStmt);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROXIMITYALERTS);
 
        // Create tables again
        onCreate(db);
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    // Adding new proximityAlerts
    public long addProximityAlert(ProximityAlert alert) {
        long row = 0;
    	SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        //values.put(KEY_ID, notification.getId()); // proximityAlerts Id
          
        values.put(KEY_NAME, alert.getName()); // proximityAlerts Name
        values.put(KEY_DESCRIPTION, alert.getDescription()); // proximityAlerts Description
        values.put(KEY_GEOLOCATION, alert.getGeolocation()); // proximityAlerts GeoLocation
        values.put(KEY_RADIUS, alert.getRadius()); // proximityAlerts Radius
        values.put(KEY_DATE_TIME, alert.getExpirationDateTime()); // proximityAlerts Time
        values.put(KEY_ACTION_NAME, alert.getActionName()); // proximityAlerts ActionName
        
        // Inserting Row
        row = db.insert(TABLE_PROXIMITYALERTS, null, values);
        db.close(); // Closing database connection
        return row;
    }
 
    // Getting All proximityAlerts
    public List<ProximityAlert> getAllProximityAlerts() {
        List<ProximityAlert> proximityList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PROXIMITYALERTS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        try
        {
        	while (cursor.moveToNext())
        	{
        		ProximityAlert proximityAlert = new ProximityAlert();

				proximityAlert.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
				proximityAlert.setName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)));
        		proximityAlert.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)));
        		proximityAlert.setGeolocation(cursor.getString(cursor.getColumnIndexOrThrow(KEY_GEOLOCATION)));
        		proximityAlert.setRadius(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_RADIUS)));
        		proximityAlert.setExpirationDateTime(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE_TIME)));
        		proximityAlert.setActionName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ACTION_NAME)));
                
        		// Adding contact to list
        		proximityList.add(proximityAlert);
        	}
        }
        catch (IllegalArgumentException ex)
        {
        	Services.Log.error(ex);
        }
    
        // return Notification list
        db.close(); // Closing database connection
        return proximityList;
    }

	/*
    // Deleting single Notification
    public void deleteProximityAlerts(int id) 
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PROXIMITYALERTS, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close(); // Closing database connection
    }
 	*/
    
    public void deleteAllProximityAlerts() 
    {
    	SQLiteDatabase db = this.getWritableDatabase();
    	db.delete(TABLE_PROXIMITYALERTS,null, null);
		db.close(); // Closing database connection
    }

	// Getting All proximityAlerts
	public ProximityAlert getProximityAlerts(int id) {
		ProximityAlert proximityAlert = null;
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_PROXIMITYALERTS +" WHERE " + KEY_ID + " = ?";

		Services.Log.debug(selectQuery);

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(id) });

		Services.Log.debug("cursor count :" + cursor.getCount());
		// looping through all rows and adding to list
		try
		{
			while (cursor.moveToNext())
			{
				proximityAlert = new ProximityAlert();

				proximityAlert.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
				proximityAlert.setName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)));
				proximityAlert.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)));
				proximityAlert.setGeolocation(cursor.getString(cursor.getColumnIndexOrThrow(KEY_GEOLOCATION)));
				proximityAlert.setRadius(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_RADIUS)));
				proximityAlert.setExpirationDateTime(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE_TIME)));
				proximityAlert.setActionName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ACTION_NAME)));
			}
		}
		catch (IllegalArgumentException ex)
		{
			Services.Log.error(ex);
		}

		// return Notification list
		db.close(); // Closing database connection
		Services.Log.debug("return" + proximityAlert);
		return proximityAlert;
	}

    /*
    public int getId(ProximityAlert notification){
    	int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
    
        //identify a ProximityAlert by name and description
        //KEY_NAME
        //KEY_DESCRIPTION
              
        String[] select = new String[] {KEY_ID};
        String[] args = new String[] {notification.getName(),notification.getDescription()};
         
        Cursor cursor = db.query(TABLE_PROXIMITYALERTS, select ,KEY_NAME +" =? AND " + KEY_DESCRIPTION + " =?", args, null, null, null);
        // looping through all rows and adding to list
        while (cursor.moveToNext())
    	{
        	id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID));
    	}
        db.close(); // Closing database connection
		return id;
    }
 	*/
}
