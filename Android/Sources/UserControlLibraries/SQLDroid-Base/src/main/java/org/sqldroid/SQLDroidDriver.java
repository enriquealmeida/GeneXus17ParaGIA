package org.sqldroid;

import java.sql.Connection;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

@SuppressWarnings("checkstyle:IllegalCatch")
public class SQLDroidDriver implements java.sql.Driver {

	// TODO(uwe):  Allow jdbc:sqlite: url as well
	public static final String SQLDROID_PREFIX = "jdbc:sqldroid:";
	/** Provide compatibility with the SQLite JDBC driver from Xerial: <p> 
	 * http://www.xerial.org/trac/Xerial/wiki/SQLiteJDBC <p>
	 * by allowing the URLs to be jdbc:sqlite:
	 */
	// this used to be "sqlitePrefix" but it looks too similar to SQLDROID_PREFIX
	// making the code hard to read and easy to mistype.
	public static final String XERIAL_PREFIX = "jdbc:sqlite:";
	
	static {
		try {
	      java.sql.DriverManager.registerDriver(new SQLDroidDriver());
		} catch (Exception e) { }

	}
	
	/** Will accept any string that starts with SQLDROID_PREFIX ("jdbc:sqldroid:") or
	 * sqllitePrefix ("jdbc:sqlite"). 
	 */
	@Override
	public boolean acceptsURL(String url) throws SQLException {

		if(url.startsWith(SQLDROID_PREFIX) || url.startsWith(XERIAL_PREFIX)) {
			return true;
		}

		return false;
	}

    
	//keep only one connection for driver/database combination.
	// as explain here http://touchlabblog.tumblr.com/post/24474750219/single-sqlite-connection
	// http://stackoverflow.com/questions/2493331/what-are-the-best-practices-for-sqlite-on-android/3689883#3689883
	
    private static SQLDroidConnection connection = null;

	@Override
    public synchronized Connection connect(String url, Properties info) throws SQLException {
        /*
        if (connection!=null && connection.mUrl.equalsIgnoreCase(url)
                && connection.getDb()!=null)
        {
            return connection;
        }
        else
        {*/
        connection = new SQLDroidConnection(url, info);
        connection.setAutoCommit(false);
        return connection;
        /*}*/
    }

    public static SQLDroidConnection getCurrentConnection()
    {
        return connection;
    }

	public static void setCurrentConnection(SQLDroidConnection myConnection)
	{
		connection = myConnection;
	}

	@Override
    public int getMajorVersion() {
        return 0;
    }

	@Override
    public int getMinorVersion() {
        return 2;
    }

	@Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line "
                + DebugPrinter.getLineNumber());
        return null;
    }

	@Override
    public boolean jdbcCompliant() {
        return false;
    }
}
