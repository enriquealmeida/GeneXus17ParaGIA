package com.genexus.android.core.externalobjects;

public class RuntimeAPIOffline {
	private static int mExitCode;

	public static short getEnvironment() {
		return 2; // RuntimeEnvironment.Device
	}
	public static int getExitCode() { return mExitCode; }
	public static void setExitCode(int exitCode) { mExitCode = exitCode; }
}
