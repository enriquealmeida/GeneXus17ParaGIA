package com.genexus.android.core.common;

public class DataRequest
{
	/**
	 * Request first page of data (on Activity start).
	 */
	public static final int REQUEST_FIRST = 1;

	/**
	 * Request more data from server (on Activity next page when reaching the end of displayed records).
	 */
	public static final int REQUEST_MORE = 2;

	/**
	 * Request fresh data from server (on manual or automatic Refresh).
	 */
	public static final int REQUEST_REFRESH = 3;

	/**
	 * Request ONLY data that is already cached in the device.
	 */
	public static final int REQUEST_CACHED = 4;

	/**
	 * Indicates that the result of a request is empty.
	 */
	public static final int RESULT_SOURCE_NONE = 1;

	/**
	 * Indicates that the result of a request comes from the local cache.
	 */
	public static final int RESULT_SOURCE_LOCAL = 2;

	/**
	 * Indicates that the result of a request comes from the Web server.
	 */
	public static final int RESULT_SOURCE_SERVER = 3;

	/**
	 * Indicates that we want to request all records from a collection data source.
	 */
	public static final int COUNT_ALL = -1;

	/**
	 * Indicates that we want to request a normal amount of records from a collection data source.
	 * If different from this value and from ALL, exactly that number of records will be asked for.
	 */
	public static final int COUNT_DEFAULT = 0;

	/**
	 * Indicates that the request was successful.
	 */
	public static final int ERROR_NONE = 0;

	/**
	 * Indicates that the request failed due to a network error.
	 * This could mean that the device does not have a data connection, that a network
	 * timeout occurred, or that the server was not reachable.
	 */
	public static final int ERROR_NETWORK = 1;

	/**
	 * Indicates that a server response arrived, but it had an HTTP error code.
	 * For example, 404 (not found) or 500 (internal server error).
	 * Security errors (401, 403) are excluded from this category.
	 */
	public static final int ERROR_SERVER = 2;

	/**
	 * Indicates that the server responded with an "authentication failed" message.
	 * (Normally because we didn't supply an OAuth token or the token was invalid).
	 */
	public static final int ERROR_SECURITY_AUTHENTICATION = 3;

	/**
	 * Indicates that the server responded with a 403 (Forbidden) message.
	 * (Normally because we don't have permissions on that service, or the specified
	 * REST operation (for BCs) is not enabled.
	 */
	public static final int ERROR_SECURITY_AUTHORIZATION = 4;

	/**
	 * Indicates that the server responded with a 403 (Forbidden) message,
	 * specifying that the password must be changed (or is time-limited and has expired).
	 */
	public static final int ERROR_SECURITY_CHANGE_PASSWORD = 5;

	/**
	 * Indicates that the server sent an (apparently) valid response, but it could
	 * not be parsed (e.g. badly formed JSON content).
	 */
	public static final int ERROR_DATA = 6;

	/**
	 * Indicates that the server responded with a 400 message,
	 * specifying that the user must enter the OTP in order to complete the authentication process.
	 */
	public static final int ERROR_SECURITY_OTP_REQUIRED = 7;

	/**
	 * Indicates that the server responded with a 401 message,
	 * specifying that the user must click on the email link sent to complete the authentication process
	 * (possibly through deep linking if properly configured).
	 */
	public static final int ERROR_SECURITY_OTP_LINK = 8;

	/**
	 * Indicates that the server responded with a 410 message,
	 * specifying that the user must enter the 2FA code in order to complete the authentication process.
	 */
	public static final int ERROR_SECURITY_2FA_REQUIRED = 9;
}
