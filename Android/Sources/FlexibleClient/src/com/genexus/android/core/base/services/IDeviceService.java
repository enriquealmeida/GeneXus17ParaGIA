package com.genexus.android.core.base.services;

import com.genexus.android.core.base.metadata.enums.Orientation;
import com.genexus.android.core.base.utils.ResultRunnable;
import com.genexus.android.core.base.utils.Version;

public interface IDeviceService
{
	/**
	 * Returns the OS of the current device.
	 * @return OS value, among the ones defined in PlatformDefinition.OS_x constants.
	 */
	int getOS();

	/**
	 * Returns the OS version of the current device (e.g. 2.3.6, 4.1).
	 */
	Version getOSVersion();

	/**
	 * Returns the SDK level of the current device (e.g. 8, 11, 18).
	 */
	int getSDKVersion();

	/**
	 * Returns the smallest width (in dips) of the display of the current device.
	 */
	int getScreenSmallestWidth();

	/**
	 * Returns the width (in dips) of the display of the current device.
	 */
	int getScreenWidth();

	/**
	 * Returns the height (in dips) of the display of the current device.
	 */
	int getScreenHeight();

	/**
	 * Returns the current screen orientation of the device (portrait / landscape).
	 * @return Orientation, among the ones defined in enums.Orientation.
	 */
	Orientation getScreenOrientation();

	/**
	 * Converts from a DIP (Device Independent Pixel) value to a pixels value, based on the screen density.
	 */
	int dipsToPixels(int dips);

	/**
	 * Converts from a pixels value to a DIP (Device Independent Pixel) value, based on the screen density.
	 */
	int pixelsToDips(int pixels);

	/**
	 * Returns true if the thread in which the method is called is the main (UI) thread,
	 */
	boolean isMainThread();

	/**
	 * Runs the specified runnable in the UI thread. If the UI thread is the current thread, runs
	 * immediately, otherwise it is posted to the UI thread's message queue.
	 */
	void runOnUiThread(Runnable r);

	/**
	 * Posts the specified runnable in the UI thread message queue.
	 * This will not run immediately, even if the current thread is the UI thread.
	 */
	void postOnUiThread(Runnable r);

	/**
	 * Posts the specified runnable in the UI thread message queue.
	 * It will run when the specified delay has elapsed.
	 */
	void postOnUiThreadDelayed(Runnable r, long delayMillis);

	/**
	 * Runs the specified ResultRunnable in the UI thread, always waiting for completion and returning its result.
	 */
	<V> V invokeOnUiThread(ResultRunnable<V> r);

	/**
	 * Runs the specified ResultRunnable in the UI thread, waiting for completion.
	 */
	void invokeOnUiThread(Runnable r);

	/**
	 * Returns if Device OS version support Notification.
	 */
	boolean isDeviceNotificationEnabled();
}
