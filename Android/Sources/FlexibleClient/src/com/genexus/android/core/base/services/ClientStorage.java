package com.genexus.android.core.base.services;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Storage interface.
 * Basically, an abstraction for SharedPreferences that supports secure (i.e. encrypted) storage.
 */
public interface ClientStorage
{
	void putString(@NonNull String key, @Nullable String value);
	void putStringSecure(@NonNull String key, @Nullable String value);
	String getString(@NonNull String key, @Nullable String defValue);

	void putBoolean(@NonNull String key, boolean value);
	boolean getBoolean(@NonNull String key, boolean defValue);

	void remove(@NonNull String key);
	void clear();
}
