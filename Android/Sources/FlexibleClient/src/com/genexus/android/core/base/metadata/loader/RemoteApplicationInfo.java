package com.genexus.android.core.base.metadata.loader;

public class RemoteApplicationInfo {
	private final int mMinorVersion;
	private final int mMajorVersion;
	private final String mAppStoreUrl;

	public RemoteApplicationInfo(int majorVersion, int minorVersion, String appstoreUrl) {
		mMajorVersion = majorVersion;
		mMinorVersion = minorVersion;
		mAppStoreUrl = appstoreUrl;
	}

	public int getMajorVersion() {
		return mMajorVersion;
	}

	public int getMinorVersion() {
		return mMinorVersion;
	}

	public String getAppStoreUrl() {
		return mAppStoreUrl;
	}
}
