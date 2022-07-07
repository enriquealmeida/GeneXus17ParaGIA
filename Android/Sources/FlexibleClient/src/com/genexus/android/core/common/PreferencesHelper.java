package com.genexus.android.core.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.genexus.android.core.base.metadata.GenexusApplication;
import com.genexus.android.core.base.metadata.loader.MetadataLoader;
import com.genexus.android.core.base.services.IPreferences;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

public class PreferencesHelper implements IPreferences {

	public static final String PREFERENCES_NOTIFICATIONS_PANEL = "NotificationsPanelName";

    private final Context mContext;

	public static final String DYNAMIC_URL_PREFS_KEY = "DynamicUrlPreference";
	public static final String DYNAMIC_URL_VALUE_KEY = "dynamicUrl";

    public PreferencesHelper(Context context) {
        mContext = context;
    }

    @Override
    public void removePreferences(String key) {
        getAppSharedPreferences(key)
                .edit()
                .clear()
                .apply();
    }

    @Override
    public void setString(String key, String value) {
        getAppSharedPreferences()
                .edit()
                .putString(key, value)
                .apply();
    }

    @Override
    public String getString(String key) {
        return getAppSharedPreferences().getString(key, Strings.EMPTY);
    }

    @Override
    public void setLong(String key, long value) {
        getAppSharedPreferences()
                .edit()
                .putLong(key, value)
                .apply();
    }

    @Override
    public long getLong(String key, long defaultValue) {
        return getAppSharedPreferences().getLong(key, defaultValue);
    }

    @Override
    public void setBoolean(String key, boolean value){
        getAppSharedPreferences()
                .edit()
                .putBoolean(key, value)
                .apply();
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return getAppSharedPreferences().getBoolean(key, defaultValue);
    }

    @Override
    public SharedPreferences getAppSharedPreferences() {
        return getAppSharedPreferences(null, null);
    }

    @Override
    public SharedPreferences getAppSharedPreferences(String qualifier) {
        return getAppSharedPreferences(null, qualifier);
    }

    @Override
    public SharedPreferences getAppSharedPreferences(GenexusApplication application) {
        return getAppSharedPreferences(application, null);
    }

    private SharedPreferences getAppSharedPreferences(GenexusApplication application, String qualifier) {
        String fullName;
        if (application != null)
            fullName = MetadataLoader.getPrefsName(application);
        else if (Services.Application.hasActiveMiniApp())
            fullName = MetadataLoader.getPrefsName(Services.Application.getMiniApp());
        else
            fullName = Services.Application.get().getName() + Services.Application.get().getAppEntry();

        if (Services.Strings.hasValue(qualifier))
            fullName += "." + qualifier;

        return mContext.getSharedPreferences(fullName, Context.MODE_PRIVATE);
    }
}
