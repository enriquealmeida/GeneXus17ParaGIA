package com.genexus.android.live_editing.storage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.SharedPreferences;
import androidx.annotation.NonNull;

import com.genexus.android.live_editing.support.Endpoint;

class SharedPreferencesStorage implements IDataStorage {
	public static final String PREFERENCES_FILENAME = "com.genexus.live_editing.settings";
	private static final String KEY_SAVED_ENDPOINTS = "key_saved_endpoints";

	private final SharedPreferences mSharedPreferences;

	public SharedPreferencesStorage(@NonNull SharedPreferences sharedPreferences) {
		mSharedPreferences = sharedPreferences;
	}

	@Override
	public void storeEndpoint(Endpoint endpoint) {
		List<Endpoint> savedEndpoints = getStoredEndpoints();

		// Don't attempt to add the endpoint if it's already in the set.
		if (savedEndpoints.contains(endpoint)) {
			return;
		}

		Set<String> endpoints = new HashSet<>(savedEndpoints.size() + 1);
		for (Endpoint savedEndpoint : savedEndpoints) {
			endpoints.add(savedEndpoint.toString());
		}
		endpoints.add(endpoint.toString());

		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putStringSet(KEY_SAVED_ENDPOINTS, endpoints);
		editor.apply();
	}

	@Override
	public List<Endpoint> getStoredEndpoints() {
		Set<String> savedEndpoints = mSharedPreferences.getStringSet(KEY_SAVED_ENDPOINTS, null);

		if (savedEndpoints == null) {
			return Collections.emptyList();
		}

		List<Endpoint> endpoints = new ArrayList<>(savedEndpoints.size());
		for (String savedEndpoint : savedEndpoints) {
			endpoints.add(new Endpoint(savedEndpoint));
		}

		return endpoints;
	}
}
