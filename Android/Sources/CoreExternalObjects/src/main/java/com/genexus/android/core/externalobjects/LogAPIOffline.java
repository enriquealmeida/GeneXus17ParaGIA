package com.genexus.android.core.externalobjects;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

/**
 * This class allow call Log API methods from offline code.
 */
public class LogAPIOffline {
	public static void write(String message) {
		write(message, Strings.EMPTY);
	}

	public static void write(String message, String topic) {
		Services.Log.write(topic, message);
	}

	public static void write(String message, String topic, int logLevel) {
		Services.Log.write(topic, message, logLevel);
	}

	public static void error(String message) {
		error(message, Strings.EMPTY);
	}

	public static void error(String message, String topic) {
		Services.Log.error(topic, message);
	}

	public static void warning(String message) {
		warning(message, Strings.EMPTY);
	}

	public static void warning(String message, String topic) {
		Services.Log.warning(topic, message);
	}

	public static void info(String message) {
		info(message, Strings.EMPTY);
	}

	public static void info(String message, String topic) {
		Services.Log.info(topic, message);
	}

	public static void debug(String message) {
		debug(message, Strings.EMPTY);
	}

	public static void debug(String message, String topic) {
		Services.Log.debug(topic, message);
	}

	public static void fatal(String message) {
		error(message, Strings.EMPTY);
	}

	public static void fatal(String message, String topic) {
		Services.Log.error(topic, message);
	}
}
