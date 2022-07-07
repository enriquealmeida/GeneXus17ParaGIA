package com.genexus.android.live_editing.storage;

import javax.inject.Singleton;

import android.content.Context;
import android.content.SharedPreferences;

import com.genexus.android.live_editing.ApplicationModule;

import dagger.Module;
import dagger.Provides;

@Module(includes = ApplicationModule.class)
public class StorageModule {
	@Provides
	@Singleton
	public IDataStorage providesDataStorage(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SharedPreferencesStorage.PREFERENCES_FILENAME,
				Context.MODE_PRIVATE
		);
		return new SharedPreferencesStorage(sharedPreferences);
	}
}
