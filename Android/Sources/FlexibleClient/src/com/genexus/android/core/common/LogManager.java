package com.genexus.android.core.common;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.util.SparseIntArray;

import com.genexus.android.core.base.metadata.InstanceProperties;
import com.genexus.android.core.base.services.ILog;
import com.genexus.android.core.base.services.LogCategory;
import com.genexus.android.core.base.services.LogLevel;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.AndroidLog;

class LogManager implements ILog
{
	private static final String APPLICATION_TAG = "GeneXusApplication";

	private boolean mEnabled;
	private final SparseIntArray mLogLevels; // like HashMap<Integer, Integer>

	LogManager()
	{
		mLogLevels = new SparseIntArray();
		mLogLevels.put(LogCategory.UNDEFINED, LogLevel.OFF);
		/*
		mLogLevels.put(LogCategory.DATA_HTTP, LogLevel.OFF);
		mLogLevels.put(LogCategory.DATA_DATABASE, LogLevel.OFF);
		mLogLevels.put(LogCategory.SYNCHRONIZATION, LogLevel.OFF);
		*/
	}

	@Override
	public void initialize(InstanceProperties mainProperties)
	{
		if (mainProperties == null)
			return;

		mLogLevels.clear();
		mEnabled = mainProperties.getBooleanProperty("EnableLogging", true);
		int defaultLevel = setLevelFrom(mainProperties, LogCategory.UNDEFINED, "LogLevelDefault", LogLevel.OFF);
		setLevelFrom(mainProperties, LogCategory.DATA_HTTP, "LogLevelHttpConnections", defaultLevel);
		setLevelFrom(mainProperties, LogCategory.DATA_DATABASE, "LogLevelOfflineDataBaseAccess", defaultLevel);
		setLevelFrom(mainProperties, LogCategory.SYNCHRONIZATION, "LogLevelOfflineSync", defaultLevel);
	}

	private int setLevelFrom(InstanceProperties properties, int category, String key, int defaultValue)
	{
		int level = defaultValue;
		String strLevel = properties.optStringProperty(key);

		if (Strings.hasValue(strLevel))
		{
			if (strLevel.equalsIgnoreCase("Error"))
				level = LogLevel.ERROR;
			else if (strLevel.equalsIgnoreCase("Warning"))
				level = LogLevel.WARNING;
			else if (strLevel.equalsIgnoreCase("Info"))
				level = LogLevel.INFO;
			else if (strLevel.equalsIgnoreCase("Debug"))
				level = LogLevel.DEBUG;
		}

		setLevel(category, level);
		return level;
	}

	@Override
	public void setLevel(int category, int level)
	{
		mLogLevels.put(category, level);
		if (LogCategory.DATA_DATABASE==category)
		{
			AndroidLog.LEVEL = level;
			switch (level)
			{
				case LogLevel.DEBUG :
					org.sqldroid.Log.sLevel = Log.DEBUG;
					break;
				case LogLevel.INFO :
					org.sqldroid.Log.sLevel = Log.INFO;
					break;
				case LogLevel.WARNING :
					org.sqldroid.Log.sLevel = Log.WARN;
					break;
				case LogLevel.ERROR :
					org.sqldroid.Log.sLevel = Log.ERROR;
					break;
				case LogLevel.OFF :
					org.sqldroid.Log.sLevel = Log.ASSERT;
					break;
			}
		}
	}

	@Override
	public int getLevel(int category)
	{
		// If log is disabled then log levels are ignored and we never log anything.
		if (!mEnabled)
			return LogLevel.OFF;

		int level = mLogLevels.get(category, -1);
		if (level == -1)
			level = mLogLevels.get(LogCategory.UNDEFINED, LogLevel.OFF); // Unspecified? Get level for "generic".

		return level;
	}

	private boolean isLogged(int category, int level)
	{
		if (level < LogLevel.ERROR || level > LogLevel.DEBUG)
			throw new IllegalArgumentException("Invalid log level: " + level);

		return (level <= getLevel(category));
	}

	@Override
	public void error(String tag, String message)
	{
		error(LogCategory.UNDEFINED, tag, message);
	}

	@Override
	public void error(String tag, String message, Throwable ex)
	{
		error(LogCategory.UNDEFINED, tag, message, ex);
	}

	@Override
	public void error(String message)
	{
		error(APPLICATION_TAG, message);
	}

	@Override
	public void error(String message, Throwable ex)
	{
		error(APPLICATION_TAG, message, ex);
	}

	@Override
	public void error(Throwable ex)
	{
		error("Logged exception", ex);
	}

	@Override
	public void warning(String tag, String message)
	{
		warning(LogCategory.UNDEFINED, tag, message);
	}

	@Override
	public void warning(String tag, String message, Throwable ex)
	{
		log(LogCategory.UNDEFINED, LogLevel.WARNING, tag, message, ex);
	}

	@Override
	public void warning(String message)
	{
		warning(APPLICATION_TAG, message);
	}

	@Override
	public void warning(String message, Throwable ex)
	{
		warning(APPLICATION_TAG, message, ex);
	}

	@Override
	public void info(String tag, String message)
	{
		info(LogCategory.UNDEFINED, tag, message);
	}

	@Override
	public void info(String message)
	{
		info(APPLICATION_TAG, message);
	}

	@Override
	public void debug(String tag, String message)
	{
		debug(LogCategory.UNDEFINED, tag, message);
	}

	@Override
	public void debug(String tag, String message, Throwable ex)
	{
		log(LogCategory.UNDEFINED, LogLevel.DEBUG, tag, message, ex);
	}

	@Override
	public void debug(String message)
	{
		debug(APPLICATION_TAG, message);
	}

	@Override
	public void debug(String message, Throwable ex)
	{
		debug(APPLICATION_TAG, message, ex);
	}

	@Override
	public void debug(int category, String tag, @NonNull String message)
	{
		log(category, LogLevel.DEBUG, tag, message, null);
	}

	@Override
	public void info(int category, String tag, @NonNull String message)
	{
		log(category, LogLevel.INFO, tag, message, null);
	}

	@Override
	public void warning(int category, String tag, @NonNull String message)
	{
		log(category, LogLevel.WARNING, tag, message, null);
	}

	@Override
	public void error(int category, String tag, @NonNull String message)
	{
		log(category, LogLevel.ERROR, tag, message, null);
	}

	@Override
	public void error(int category, String tag, @NonNull String message, @Nullable Throwable ex)
	{
		log(category, LogLevel.ERROR, tag, message, ex);
	}

	@Override
	public void write(String tag, String message)
	{
		debug(tag, message);
	}

	@Override
	public void write(String tag, String message, int logLevel)
	{
		int level = getAndroidLevel(logLevel);
		if (level!=LogLevel.OFF)
			log(LogCategory.UNDEFINED, level , tag, message, null);
	}

	private void log(int category, int level, String tag, @NonNull String message, @Nullable Throwable exception)
	{
		if (isLogged(category, level))
		{
			if (!Strings.hasValue(tag))
				tag = APPLICATION_TAG;

			if (exception != null)
			{
				String exceptionMessage = Log.getStackTraceString(exception);
				if (Strings.hasValue(message))
					message += '\n' + exceptionMessage;
				else
					message = exceptionMessage;
			}

			switch (level)
			{
				case LogLevel.DEBUG :
					android.util.Log.d(tag, message);
					break;

				case LogLevel.INFO :
					android.util.Log.i(tag, message);
					break;

				case LogLevel.WARNING :
					android.util.Log.w(tag, message);
					break;

				case LogLevel.ERROR :
					android.util.Log.e(tag, message);
					break;

				default :
					throw new IllegalArgumentException("Invalid log level: " + level);
			}
		}
	}

	private int getAndroidLevel(int logLevel)
	{
		switch (logLevel)
		{
			case 0: //LogLevel off
				return LogLevel.OFF;
			case 1:
				// trace
				return LogLevel.DEBUG;

			case 5:
				// debug
				return LogLevel.DEBUG;

			case 10:
				// info
				return LogLevel.INFO;

			case 15:
				// warning
				return LogLevel.WARNING;

			case 20:
				// error
				return LogLevel.ERROR;

			case 30:
				// fatal
				return LogLevel.ERROR;
			default:
				return LogLevel.DEBUG;
		}
	}
}
