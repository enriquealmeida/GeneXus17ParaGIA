package com.genexus.android.core.application;

import java.util.UUID;

import android.content.Context;
import androidx.annotation.NonNull;

import com.genexus.android.core.base.services.ClientStorage;
import com.genexus.android.core.base.services.Services;

class ApplicationStorageImpl
{
    private static final String PREFS_FILE = "AppStorageData";
    private static final String PREFS_STORAGE_DATA = "app_storage_data_value";

    private static UUID uuid;

    public ApplicationStorageImpl(Context context) {
        synchronized (ApplicationStorageImpl.class) {
            if( uuid == null) {
                final String id = getStorage().getString(PREFS_STORAGE_DATA, null);

                if (id != null) {
                    // Use the ids previously computed and stored in the prefs file
                    uuid = UUID.fromString(id);

                } else {

					// if dont have uuid generate a new one.
					// one new install or reinstall
					// like explain here: https://developer.android.com/training/articles/user-data-ids.html#working_with_instance_ids_&_guids

                    uuid =  UUID.randomUUID();

                    // Write the value out to the prefs file
					getStorage().putStringSecure(PREFS_STORAGE_DATA, uuid.toString());
                }

            }
        }
    }

	/**
	 *
	 * @return String Storage Value
	 */
    public String getDeviceStorageValue() {
        return uuid.toString();
    }

	// custom storage
	private static ClientStorage sStorage;

	private static @NonNull synchronized ClientStorage getStorage()
	{
		if (sStorage == null)
			sStorage = Services.Application.getClientStorage(PREFS_FILE);

		return sStorage;
	}
}
