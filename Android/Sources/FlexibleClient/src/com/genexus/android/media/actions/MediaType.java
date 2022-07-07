package com.genexus.android.media.actions;

import android.os.Environment;

enum MediaType {
	Image("image/*", ".jpg", Environment.DIRECTORY_PICTURES),
	Video("video/*", ".mp4", Environment.DIRECTORY_MOVIES),
	Audio("audio/*", ".mp3", Environment.DIRECTORY_MUSIC);

	private final String mMimeType;
	private final String mDefaultExtension;
	private final String mStorageDirectoryName;

	MediaType(String mimeType, String defaultExtension, String storageDirectoryName) {
		mMimeType = mimeType;
		mDefaultExtension = defaultExtension;
		mStorageDirectoryName = storageDirectoryName;
	}

	public String getMimeType() {
		return mMimeType;
	}

	public String getDefaultExtension() {
		return mDefaultExtension;
	}

	public String getStorageDirectoryName() {
		return mStorageDirectoryName;
	}
}
