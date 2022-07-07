package org.sqldroid;

import android.content.Context;

public class SQLDroidHelper
{
	private static ISQLDroidData mData;

	public static void initialize(Context context, ISQLDroidData data)
    {
		//if secure initialize db
		//net.sqlcipher.database.SQLiteDatabase.loadLibs(context);

		//store data for use later
		mData = data;
    }

	// this create the helper and initialize the data
	public static String getSQLDroidData()
	{
		return mData.getSQLDroidData();
	}

}
