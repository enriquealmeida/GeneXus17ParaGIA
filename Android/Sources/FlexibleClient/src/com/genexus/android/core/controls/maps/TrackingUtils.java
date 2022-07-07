package com.genexus.android.core.controls.maps;

import android.content.Context;
import android.location.Location;

import com.genexus.android.R;
import com.genexus.android.core.base.services.Services;

import java.text.DateFormat;
import java.util.Date;

class TrackingUtils {
    private static final String KEY_REQUESTING_LOCATION_UPDATES = "requesting_locaction_updates";

    /**
     * Returns true if requesting location updates, otherwise returns false.
     */
    static boolean requestingLocationUpdates() {
        return Services.Preferences.getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false);
    }

    /**
     * Stores the location updates state in SharedPreferences.
     * @param requestingLocationUpdates The location updates state.
     */
    static void setRequestingLocationUpdates(boolean requestingLocationUpdates) {
        Services.Preferences.setBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates);
    }

    /**
     * Returns the {@code location} object as a human readable string.
     * @param location  The {@link Location}.
     */
    static String getLocationText(Context context, Location location) {
        return context.getString(R.string.GXM_LocationUpdate,
                DateFormat.getDateTimeInstance().format(new Date()));
    }

    static String getLocationTitle(Context context) {
        return context.getString(R.string.GXM_LocationUpdating);
    }
}
