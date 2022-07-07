package com.genexus.android.live_editing.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.genexus.android.live_editing.support.Endpoint;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import androidx.test.core.app.ApplicationProvider;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
public class SharedPreferencesStorageTest {
	private IDataStorage mDataStorage;

	@Before
	public void setUp() {
		SharedPreferences sp = ApplicationProvider.getApplicationContext().getSharedPreferences(
				SharedPreferencesStorage.PREFERENCES_FILENAME,
				Context.MODE_PRIVATE
		);
		mDataStorage = new SharedPreferencesStorage(sp);
	}

	@Test
	public void shouldNotStoreDuplicatedValues() {
		mDataStorage.storeEndpoint(new Endpoint("192.168.1.1", 30100));
		mDataStorage.storeEndpoint(new Endpoint("192.168.1.1", 30100));

		assertThat(mDataStorage.getStoredEndpoints())
				.containsExactly(new Endpoint("192.168.1.1", 30100));
	}

	@Test
	public void shouldStoreEndpointsWithDifferentValues() {
		mDataStorage.storeEndpoint(new Endpoint("10.0.0.1", 30100));
		mDataStorage.storeEndpoint(new Endpoint("10.0.0.2", 30100));
		mDataStorage.storeEndpoint(new Endpoint("192.168.1.1", 30100));
		mDataStorage.storeEndpoint(new Endpoint("192.168.1.1", 30101));

		assertThat(mDataStorage.getStoredEndpoints())
				.containsExactly(
						new Endpoint("10.0.0.1", 30100),
						new Endpoint("10.0.0.2", 30100),
						new Endpoint("192.168.1.1", 30100),
						new Endpoint("192.168.1.1", 30101)
				);
	}
}
