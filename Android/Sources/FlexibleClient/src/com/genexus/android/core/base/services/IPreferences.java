package com.genexus.android.core.base.services;

import android.content.SharedPreferences;

import com.genexus.android.core.base.metadata.GenexusApplication;

public interface IPreferences {
    void setString(String key, String value);
    String getString(String key);
    void setLong(String key, long value);
    long getLong(String key, long defaultValue);
    void setBoolean(String key, boolean value);
    boolean getBoolean(String key, boolean defaultValue);
    void removePreferences(String key);
    SharedPreferences getAppSharedPreferences();
    SharedPreferences getAppSharedPreferences(String qualifier);
    SharedPreferences getAppSharedPreferences(GenexusApplication application);
}
