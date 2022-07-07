package com.genexus.android.core.externalobjects;

import androidx.annotation.NonNull;

import com.genexus.android.core.base.services.ClientStorage;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.NameMap;
import com.genexus.android.core.base.utils.Strings;

public class ClientStorageAPIOffline {

	private static final String PREFERENCES_KEY = "ClientStorageApi";
	private static final NameMap<ClientStorage> STORAGES = new NameMap<>();

	private static synchronized @NonNull ClientStorage getStorage() {
		String storageKey = PREFERENCES_KEY;
		if (Services.Application.hasActiveMiniApp())
			storageKey = Services.Application.getMiniApp().getName() + storageKey;

		ClientStorage storage = STORAGES.get(storageKey);
		if (storage == null) {
			storage = Services.Application.getClientStorage(storageKey);
			STORAGES.put(storageKey, storage);
		}

		return storage;
	}

	public static void set(String key, String value) {
		getStorage().putString(key, value);
	}

	public static void secureSet(String key, String value) {
		getStorage().putStringSecure(key, value);
	}

	public static String get(String key) {
		return getStorage().getString(key, Strings.EMPTY);
	}

	public static void remove(String key) {
		getStorage().remove(key);
	}

	public static void clear() {
		getStorage().clear();
	}
}
