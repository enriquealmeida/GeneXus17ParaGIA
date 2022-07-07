package com.genexus.android.remotenotification;

import android.content.Context;
import androidx.annotation.NonNull;

import com.genexus.android.core.base.metadata.GenexusApplication;

/**
 * Notifications Provider interface.
 */
public interface IRemoteNotificationProvider
{
	/***
	 * Get the id for this provider. ie: "gcm", "onesignal", etc
	 */
	@NonNull String getId();

	/***
	 * Register the device in the Notification provider.
	 */
	void registerDevice(Context context, GenexusApplication genexusApplication);
}
