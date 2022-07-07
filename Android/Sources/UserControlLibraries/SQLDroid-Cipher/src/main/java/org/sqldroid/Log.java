package org.sqldroid;

public class Log {

	private static final String TAG = "SQLDroid";
	public static int sLevel = android.util.Log.ERROR;


	static void d(String message)
    {
        if (sLevel <=android.util.Log.DEBUG) android.util.Log.d(TAG, message);
    }

    static void w(String message) 
    {
        if (sLevel <=android.util.Log.WARN) android.util.Log.w(TAG, message);
    }
    
    static void e(String message) 
    {
        if (sLevel <=android.util.Log.ERROR) android.util.Log.e(TAG, message);
    }

    static void e(String message, Throwable t) 
    {
        if (sLevel <=android.util.Log.ERROR) android.util.Log.e(TAG, message, t);
    }

    static void i(String message) 
    {
        if (sLevel <=android.util.Log.INFO) android.util.Log.i(TAG, message);
    }

    static void v(String message) 
    {
        if (sLevel <=android.util.Log.VERBOSE) android.util.Log.v(TAG, message);
    }

}
