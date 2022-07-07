package com.genexus.android.core.activities;

import java.util.Collections;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.genexus.android.core.actions.UIContext;
import com.genexus.android.core.app.ComponentId;
import com.genexus.android.core.app.ComponentParameters;
import com.genexus.android.core.app.ComponentUISettings;
import com.genexus.android.core.base.metadata.DashboardMetadata;
import com.genexus.android.core.base.metadata.IDataViewDefinition;
import com.genexus.android.core.base.metadata.enums.Connectivity;
import com.genexus.android.core.base.metadata.enums.DisplayModes;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.fragments.BaseFragment;
import com.genexus.android.core.fragments.DashboardFragment;
import com.genexus.android.core.fragments.FragmentFactory;
import com.genexus.android.core.fragments.IDataView;
import com.genexus.android.core.fragments.LayoutFragment;
import com.genexus.android.core.fragments.LayoutFragmentActivity;
import com.genexus.android.core.fragments.LayoutFragmentActivityState;
import com.genexus.android.core.ui.navigation.INavigationActivity;
import com.genexus.android.core.ui.navigation.Navigation;
import com.genexus.android.core.ui.navigation.NavigationController;
import com.genexus.android.core.ui.navigation.controllers.StandardNavigationController;

import androidx.annotation.NonNull;

public class GenexusActivity extends LayoutFragmentActivity implements INavigationActivity
{
	private boolean mIsActive;
	private NavigationController mNavigationController = new StandardNavigationController(this);
	private ComponentParameters mMainParams;

	@Override
	protected boolean initializeController(ActivityController controller)
	{
		Intent intent = getIntent();
		if (controller.initializeFrom(getIntent()))
		{
			mMainParams = controller.getModel().getMain().getParams();
		}
		else if (Strings.hasValue(intent.getStringExtra(IntentParameters.DASHBOARD_METADATA)))
		{
			DashboardMetadata mainViewDefinition = (DashboardMetadata) Services.Application.getDefinition().getPattern(intent.getStringExtra(IntentParameters.DASHBOARD_METADATA));
			// Only create main Params if has a valid definition.
			if (mainViewDefinition!=null)
				mMainParams = new ComponentParameters(mainViewDefinition, DisplayModes.VIEW, Collections.emptyList());
		}

		if (mMainParams != null)
			mNavigationController = Navigation.createController(this, mMainParams.Object);

		return (mMainParams != null);
	}

	@Override
	protected Pair<View, Boolean> preInitializeView(ActivityController controller, Bundle savedInstanceState, LayoutFragmentActivityState previousState)
	{
		return mNavigationController.onPreCreate(savedInstanceState, mMainParams);
	}

	@Override
	protected boolean initializeView(ActivityController controller, Bundle savedInstanceState, LayoutFragmentActivityState previousState)
	{
		mNavigationController.onCreate(savedInstanceState);
		return mNavigationController.start(mMainParams, previousState);
	}

	@Override
	public NavigationController getNavigationController()
	{
		return mNavigationController;
	}

	@Override
	public UIContext getUIContext()
	{
		return getController().getModel().getUIContext();
	}

	@Override
	public @NonNull BaseFragment createComponent(@NonNull ComponentId id, @NonNull ComponentParameters parameters, @NonNull ComponentUISettings uiSettings, Connectivity parentConnectivity)
	{
		BaseFragment fragment = FragmentFactory.newFragment(parameters);
		initializeComponent(fragment, id, parameters, uiSettings, parentConnectivity);
		registerFragment(fragment);

		// in slide navigation, only execute notification action once, for slide component
		if (uiSettings.isMain && !uiSettings.isContent) {
			Intent intent = getIntent();
			String action = intent.getStringExtra(StartupActivity.EXTRA_NOTIFICATION_ACTION);
			String params = intent.getStringExtra(StartupActivity.EXTRA_NOTIFICATION_PARAMS);
			String object = intent.getStringExtra(StartupActivity.EXTRA_NOTIFICATION_OBJECT);
			if (!TextUtils.isEmpty(action)) {
				if (action.equals(ActivityLauncher.INTENT_EXTRA_LINK_ACTION) && !params.isEmpty())
					ActivityLauncher.startWebBrowser(this, params);
				else
					Services.Notifications.executeNotificationAction(this, action, params, object);
			}
		}

		return fragment;
	}

	private void initializeComponent(BaseFragment fragment, ComponentId id, ComponentParameters params, ComponentUISettings uiSettings, Connectivity parentConnectivity)
	{
		if (fragment != null)
		{
			UIContext context = fragment.getUIContext();
			if (context.getActivity() == null)
				context = getUIContext(); // Workaround: Fragment.getActivity() is null until the fragment has been attached.

			if (params.Object instanceof IDataViewDefinition)
			{
				LayoutFragment layoutFragment = (LayoutFragment)fragment;
				initializeLayoutFragment(layoutFragment, id, params, uiSettings, parentConnectivity);
				layoutFragment.setDesiredSize(uiSettings.size);
			}
			else if (params.Object instanceof DashboardMetadata)
			{
				DashboardFragment dashboardFragment = (DashboardFragment)fragment;
				dashboardFragment.initialize((DashboardMetadata)params.Object, getUIContext().getConnectivitySupport());
				// register dashboardFragment as other fragment do in initialize
				registerDashboardFragment(dashboardFragment);
				restoreFragmentState(dashboardFragment);
			}

			String objectName = params.Object.getObjectName();
			String loginObjectName = Services.Application.get().getLoginObject();

			// Don't launch another GAM Login panel if we're already in it
			if (objectName != null && !objectName.equals(loginObjectName)) {
				checkSecurity(context, params);
			}

			Services.Application.getLifecycle().notifyComponentInitialized((IDataView) fragment);
		}
	}

	@Override
	public void destroyComponent(BaseFragment fragment)
	{
		for (BaseFragment child : fragment.getChildFragments())
			destroyComponent(child);

		if (fragment instanceof LayoutFragment)
		{
			// Requires custom finalization.
			LayoutFragment layoutFragment = (LayoutFragment)fragment;
			finalizeLayoutFragment(layoutFragment);
		}

		unregisterFragment(fragment);
	}

	public boolean isActive()
	{
		return mIsActive;
	}

	// *************************************************************************
	// Forwarding of events to NavigationController.
	// *************************************************************************

	@Override
	public void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		mNavigationController.onPostCreate(savedInstanceState);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		mIsActive = true;
		mNavigationController.onResume();
	}

	@Override
	public void onPostResume()
	{
		super.onPostResume();
		mNavigationController.onPostResume();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		mNavigationController.onConfigurationChanged(newConfig);

		Services.Log.debug("Activity on onConfigurationChanged");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		mNavigationController.onSaveInstanceState(outState);
	}

	@Override
	protected void saveActivityState(LayoutFragmentActivityState outState)
	{
		super.saveActivityState(outState);
		mNavigationController.saveActivityState(outState);
	}

	@Override
	protected void onPause()
	{
		mNavigationController.onPause();
		mIsActive = false;
		super.onPause();
	}

	@Override
	public void onStop() {
		mNavigationController.onStop();
		super.onStop();
	}

	@Override
	protected void onStart() {
		mNavigationController.onStart();
		super.onStart();
	}

	public void setTitle(CharSequence title, IDataView fromDataView)
	{
		if (mNavigationController.setTitle(fromDataView, title))
			return;

		// Default implementation.
		setTitle(title);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (mNavigationController.onOptionsItemSelected(item))
			return true;

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyUp(int keyCode, @NonNull KeyEvent event)
	{
		if (mNavigationController.onKeyUp(keyCode, event))
			return true;

		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event)
	{
		// dispatch KEYCODE_NUMPAD_ENTER as KEYCODE_ENTER
		// more info https://issuetracker.google.com/issues/134205188
		if (event.getKeyCode() == KeyEvent.KEYCODE_NUMPAD_ENTER)
			event = new KeyEvent(event.getAction(), KeyEvent.KEYCODE_ENTER);

		return super.dispatchKeyEvent(event);
	}

	@Override
	public void onBackPressed()
	{
		if (mNavigationController.onBackPressed())
			return;

		super.onBackPressed();
	}	
}
