package com.genexus.android.core.base.services;

import com.genexus.android.core.base.metadata.InstanceProperties;

public interface ILog
{
	void initialize(InstanceProperties mainProperties);
	void setLevel(int category, int level);
	int getLevel(int category);

	void error(String tag, String message);
	void error(String tag, String message, Throwable ex);
	void error(String message);
	void error(String message, Throwable ex);
	void error(Throwable ex);

	void warning(String tag, String message);
	void warning(String tag, String message, Throwable ex);
	void warning(String message);
	void warning(String message, Throwable ex);

	void info(String tag, String message);
	void info(String message);

	void debug(String tag, String message);
	void debug(String tag, String message, Throwable ex);
	void debug(String message);
	void debug(String message, Throwable ex);

	// basic write methods
	void write(String tag, String message);
	void write(String tag, String message, int logLevel);

	// Overloads that specify a logging category.
	void debug(int category, String tag, String message);
	void info(int category, String tag, String message);
	void warning(int category, String tag, String message);
	void error(int category, String tag, String message);
	void error(int category, String tag, String message, Throwable ex);
}
