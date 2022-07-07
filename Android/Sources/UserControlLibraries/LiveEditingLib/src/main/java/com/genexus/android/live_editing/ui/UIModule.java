package com.genexus.android.live_editing.ui;

import javax.inject.Singleton;

import com.genexus.android.core.base.services.IApplication;
import com.genexus.android.live_editing.ApplicationModule;
import com.genexus.android.live_editing.LiveEditingGenexusModule;

import dagger.Module;
import dagger.Provides;

@Module(includes = ApplicationModule.class)
public class UIModule {
	private final LiveEditingGenexusModule mLiveEditingGXModule;

	public UIModule(LiveEditingGenexusModule liveEditingGXModule) {
		mLiveEditingGXModule = liveEditingGXModule;
	}

	@Provides
	@Singleton
	public IUserInterface providesUserInterface(IApplication application) {
		return new NotificationsUI(application, mLiveEditingGXModule);
	}
}
