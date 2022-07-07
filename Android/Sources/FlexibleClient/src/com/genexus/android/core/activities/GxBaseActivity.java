package com.genexus.android.core.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;

import com.genexus.android.core.base.metadata.theme.ThemeDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.common.ImageLoader;

public abstract class GxBaseActivity extends AppCompatActivity implements IGxBaseActivity, IGxThemeableActivity {
	private ImageLoader mImageLoader;
	private ThemeDefinition mAppliedTheme;

	// TODO: Should this be static??
	public static String sPickingElementId = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageLoader = new ImageLoader(getApplicationContext());
		ActivityThemePropertiesHelper.applyCurrentAppTheme(this);
		mAppliedTheme = Services.Themes.getCurrentTheme();
		if (!Services.Application.isLoaded()) {
			Services.Log.warning("Reload app metadata from activity onCreate ");
			Intent intent = IntentFactory.getStartupActivity(this);
			this.startActivity(intent);
			finish(); // do not continue with this activity, it will fail later anyway
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		applyCurrentTheme(false);
	}

	@Override
	protected void onDestroy() {
		mImageLoader.stopThread();
		mImageLoader = null;

		// hack to release an internal reference see this thread for further informantion
		// http://stackoverflow.com/questions/5038158/main-activity-is-not-garbage-collected-after-destruction-because-it-is-referenced
		InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		manager.isActive();
		super.onDestroy();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		applyCurrentTheme(false);
	}

	@Override
	public ImageLoader getImageLoader() {
		return mImageLoader;
	}

	protected boolean needsToApplyCurrentTheme() {
		return mAppliedTheme != Services.Themes.getCurrentTheme();
	}

	@UiThread
	@Override
	public void applyCurrentTheme(boolean force) {
		if (force || needsToApplyCurrentTheme())
			recreate();
	}
}
