package com.genexus.android.device;

import java.util.UUID;

import android.content.Context;
import android.content.SharedPreferences;

class DeviceInstallUuidFactory
{
    private static final String PREFS_FILE = "device_install.xml";
    private static final String PREFS_DEVICE_ID = "device_install_id";

    private static UUID uuid;

    public DeviceInstallUuidFactory(Context context) {
        synchronized (DeviceInstallUuidFactory.class) {
            if( uuid == null) {
                final SharedPreferences prefs = context.getSharedPreferences( PREFS_FILE, 0);
                final String id = prefs.getString(PREFS_DEVICE_ID, null );

                if (id != null) {
                    // Use the ids previously computed and stored in the prefs file
                    uuid = UUID.fromString(id);

                } else {

					// if dont have uuid generate a new one.
					// one new install or reinstall
					// like explain here: https://developer.android.com/training/articles/user-data-ids.html#working_with_instance_ids_&_guids

                    uuid =  UUID.randomUUID();

                    // Write the value out to the prefs file
                    prefs.edit().putString(PREFS_DEVICE_ID, uuid.toString() ).commit();

                }

            }
        }
    }


    /**
     * Returns a unique UUID for the current android device and app instalation.  As with all UUIDs, this unique ID is "very highly likely"
     * to be unique across all Android devices.
     *
     * see https://developer.android.com/training/articles/user-data-ids.html#working_with_instance_ids_&_guids
     *
     * @return a UUID that may be used to uniquely identify your device and app instalation for most purposes.
     */
    public UUID getDeviceInstalationUuid() {
        return uuid;
    }
}
