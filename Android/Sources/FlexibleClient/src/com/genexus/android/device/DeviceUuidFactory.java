package com.genexus.android.device;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings.Secure;

import com.genexus.android.core.base.metadata.loader.MetadataLoader;
import com.genexus.android.core.base.services.Services;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

class DeviceUuidFactory {

	private static final String PREFS_DEVICE_ID = "device_id";
	private static final String FIXED_UUID = "9774d56d682e549c";
	private UUID uuid;

	@SuppressLint("HardwareIds")
	public DeviceUuidFactory(Context context) {
		synchronized (DeviceUuidFactory.class) {
			if (uuid == null) {
				boolean hasMiniApp = Services.Application.hasActiveMiniApp();
				final SharedPreferences prefs;
				if (hasMiniApp)
					prefs = Services.Preferences.getAppSharedPreferences();
				else
					prefs = Services.Preferences.getAppSharedPreferences(PREFS_DEVICE_ID);

				final String id = prefs.getString(PREFS_DEVICE_ID, null);
				if (id != null) {
					// Use the ids previously computed and stored in the prefs file
					uuid = UUID.fromString(id);
				} else {
					String androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
					if (hasMiniApp)
						androidId += MetadataLoader.getPrefsName(Services.Application.getMiniApp());

					// Use the Android ID unless it's broken,
					// then fallback on a random number which we store
					// to a prefs file
					// fix, in the emulator androidId is null
					if (androidId != null && !FIXED_UUID.equals(androidId))
						uuid = UUID.nameUUIDFromBytes(androidId.getBytes(StandardCharsets.UTF_8));
					else
						uuid = UUID.randomUUID();

					// Write the value out to the prefs file
					prefs.edit().putString(PREFS_DEVICE_ID, uuid.toString()).apply();
				}
			}
		}
	}


	/**
	 * Returns a unique UUID for the current android device.  As with all UUIDs, this unique ID is "very highly likely"
	 * to be unique across all Android devices.  Much more so than ANDROID_ID is.
	 * <p>
	 * The UUID is generated by using ANDROID_ID as the base key if appropriate, falling back on
	 * falling back
	 * on a random UUID that's persisted to SharedPreferences
	 * <p>
	 * In some rare circumstances, this ID may change.  In particular, if the device is factory reset a new device ID
	 * may be generated.  In addition, if a user upgrades their phone from certain buggy implementations of Android 2.2
	 * to a newer, non-buggy version of Android, the device ID may change.  Or, if a user uninstalls your app on
	 * a device that has neither a proper Android ID nor a Device ID, this ID may change on reinstallation.
	 * <p>
	 * Note that if the code falls back on using TelephonyManager.getDeviceId(), the resulting ID will NOT
	 * change after a factory reset.  Something to be aware of.
	 * <p>
	 * Works around a bug in Android 2.2 for many devices when using ANDROID_ID directly.
	 * <p>
	 * see http://code.google.com/p/android/issues/detail?id=10603
	 *
	 * @return a UUID that may be used to uniquely identify your device for most purposes.
	 */
	public UUID getDeviceUuid() {
		return uuid;
	}
}
