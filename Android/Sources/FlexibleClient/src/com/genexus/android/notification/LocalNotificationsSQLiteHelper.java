package com.genexus.android.notification;
 
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
public class LocalNotificationsSQLiteHelper extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;
 
    // Database Name
    private static final String DATABASE_NAME = "notificationsManager";
 
    // Notifications table name
    private static final String TABLE_NOTIFICATIONS = "notifications";
	private static final String TABLE_NOTIFICATIONS_TEMP = "notifications_temp";
 
    // Notifications Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DATE_TIME = "datetime";
    private static final String KEY_TEXT = "text";

    private static final String DB_CREATE = "CREATE TABLE %s ("
		    + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
		    + KEY_DATE_TIME + " TEXT,"
		    + KEY_TEXT + " TEXT" + ")";

	// Singleton Db connection from  https://stackoverflow.com/a/24201387
	private static LocalNotificationsSQLiteHelper instance;

	public static synchronized LocalNotificationsSQLiteHelper getInstance(Context context)
	{
		if (instance == null)
			instance = new LocalNotificationsSQLiteHelper(context);

		return instance;
	}

    public LocalNotificationsSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
	    // Creating Notifications Table
        String createStmt = String.format(DB_CREATE, TABLE_NOTIFICATIONS);
        db.execSQL(createStmt);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Create temporal Notifications table
		String createTempStmt = String.format(DB_CREATE, TABLE_NOTIFICATIONS_TEMP);
		db.execSQL(createTempStmt);

	    // Backup current data in temporal Notifications table
	    String insertTempStmt = "INSERT INTO " + TABLE_NOTIFICATIONS_TEMP + " (" + KEY_ID + ", " + KEY_DATE_TIME + ", " + KEY_TEXT + ") "
			    + "SELECT " + KEY_ID + ", " + KEY_DATE_TIME + ", " + KEY_TEXT + " "
	            + "FROM " + TABLE_NOTIFICATIONS + ";";
	    db.execSQL(insertTempStmt);

        // Drop old table if exists
	    String deleteStmt = "DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS + ";";
        db.execSQL(deleteStmt);

        // Rename temporal to actual table name
	    String renameStmt = "ALTER TABLE " + TABLE_NOTIFICATIONS_TEMP + " RENAME TO " + TABLE_NOTIFICATIONS + ";";
        db.execSQL(renameStmt);
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    // Adding new Notification
    public long addNotification(Notification notification) {
    	SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        //values.put(KEY_ID, notification.getId()); // Notification Id
        values.put(KEY_DATE_TIME, notification.getDateTime()); // Notification DateTime
        values.put(KEY_TEXT, notification.getText()); // Notification Text
 
        // Inserting Row
	    return db.insert(TABLE_NOTIFICATIONS, null, values);
    }
 
    // Getting All Notifications
    public List<Notification> getAllNotifications() {
        List<Notification> notificationList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTIFICATIONS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	Notification notification = new Notification();
                notification.setDateTime(cursor.getString(1));
                notification.setText(cursor.getString(2));
                // Adding contact to list
                notificationList.add(notification);
            } while (cursor.moveToNext());
        }

        cursor.close();
        // return Notification list
        return notificationList;
    }
 
    // Deleting single Notification
    public void deleteNotification(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTIFICATIONS, KEY_ID + " = ?", new String[] { String.valueOf(id) });
    }
 
    
    public void deleteAllNotifications() {
    	SQLiteDatabase db = this.getWritableDatabase();
    	db.delete(TABLE_NOTIFICATIONS,null, null);
	}
    
    
    public int getId(Notification notification){
    	int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
    
        String[] select = new String[] {KEY_ID};
        String[] args = new String[] {notification.getDateTime(),notification.getText()};
         
        Cursor cursor = db.query(TABLE_NOTIFICATIONS, select ,KEY_DATE_TIME +" =? AND " + KEY_TEXT + " =?", args, null, null, null);
           // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	id = cursor.getInt(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
    	return id;
    }
 
}
