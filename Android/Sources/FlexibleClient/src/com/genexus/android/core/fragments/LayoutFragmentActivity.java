package com.genexus.android.core.fragments;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import com.genexus.android.core.actions.UIContext;
import com.genexus.android.core.activities.ActivityController;
import com.genexus.android.core.activities.ActivityHelper;
import com.genexus.android.core.activities.ActivityLauncher;
import com.genexus.android.core.activities.ActivityModel;
import com.genexus.android.core.activities.DataViewHelper;
import com.genexus.android.core.activities.GenexusActivity;
import com.genexus.android.core.activities.GxBaseActivity;
import com.genexus.android.core.activities.IGxActivity;
import com.genexus.android.core.adapters.AdaptersHelper;
import com.genexus.android.analytics.Analytics;
import com.genexus.android.core.app.ComponentId;
import com.genexus.android.core.app.ComponentParameters;
import com.genexus.android.core.app.ComponentUISettings;
import com.genexus.android.core.base.metadata.ILayoutDefinition;
import com.genexus.android.core.base.metadata.IViewDefinition;
import com.genexus.android.core.base.metadata.enums.Connectivity;
import com.genexus.android.core.base.metadata.enums.Orientation;
import com.genexus.android.core.base.metadata.layout.LayoutDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.PlatformHelper;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.BiometricsHelper;
import com.genexus.android.core.common.DataRequest;
import com.genexus.android.core.common.SecurityHelper;
import com.genexus.android.core.controllers.DataViewController;
import com.genexus.android.core.controllers.DataViewModel;
import com.genexus.android.core.controllers.IDataViewController;
import com.genexus.android.core.controllers.RefreshParameters;
import com.genexus.android.core.controllers.ViewData;
import com.genexus.android.core.controls.IGxLocalizable;
import com.genexus.android.core.ui.navigation.tabbed.TabbedNavigationController;
import com.genexus.android.core.utils.Cast;

import androidx.annotation.NonNull;

import static com.genexus.android.core.activities.IntentParameters.IS_STARTUP_ACTIVITY;

/**
 * Base class for all activities that support fragments.
 */
public abstract class LayoutFragmentActivity extends GxBaseActivity implements IGxActivity, IDataViewHost, IGxLocalizable
{
	private ActivityController mController;
	private Set<BaseFragment> mFragments;
	private Set<IDataView> mDataViews;
	private LayoutFragment mMainFragment;

	// State
	private boolean mShouldNotSaveState;
	private boolean mLoginCalled;
	private LayoutFragmentActivityState mPreviousState;
	private boolean mPreviousUIStateRestored;
	private long mActivityTimestamp;
	private boolean mActivityDestroyedToApplyOrientation;
	private Orientation mCurrentDesiredOrientation;
	private boolean mAllowUnrestrictedOrientationChange;

	public abstract UIContext getUIContext();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		mShouldNotSaveState = false;
		mCurrentDesiredOrientation = Orientation.UNDEFINED;
		mAllowUnrestrictedOrientationChange = false;

		super.onCreate(savedInstanceState);
		ActivityHelper.initialize(this, savedInstanceState);

		mFragments = new LinkedHashSet<>();
		mDataViews = new LinkedHashSet<>();
		mController = new ActivityController(this);

		if (!initializeController(mController))
		{
			setContentView(ActivityHelper.getInvalidMetadataMessage(this));
			return;
		}

		if (BiometricsHelper.isBlurOnBackgroundEnabled())
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

		// Restore state if changing orientation. Store it in local variable to pass to fragments later.
		getLastCustomNonConfig();
		if (mPreviousState != null) {
			mController.restoreState(mPreviousState);
			restoreActivityState(mPreviousState);
		}

		if (getMainDefinition() != null)
		{
			LayoutDefinition mainLayout = Cast.as(LayoutDefinition.class, getMainLayout());
			if (mainLayout != null)
			{

				Orientation desiredOrientation = mainLayout.getActualOrientation();
				mCurrentDesiredOrientation = desiredOrientation;
				if (desiredOrientation != Orientation.UNDEFINED && desiredOrientation != Services.Device.getScreenOrientation()
						&& !mAllowUnrestrictedOrientationChange)
				{
					mActivityDestroyedToApplyOrientation = true;
					ActivityHelper.setOrientation(this, desiredOrientation);
					return;
				}
			}

			Services.Log.debug(String.format("Starting '%s'...", getMainDefinition().getName()));

			DataViewHelper.setTitle(this, null, getMainDefinition().getCaption());
		}
		else
		{
			// if Main and tabbed navigation. No definition, set orientation like dashboardactivity.
			// there is no mainLayout for dashboard.
			if (isTabbedNavigationController())
			{
				// get main orientation
				Orientation desiredOrientation = PlatformHelper.getDefaultOrientation();
				mCurrentDesiredOrientation = desiredOrientation;
				if (desiredOrientation != Orientation.UNDEFINED && desiredOrientation != Services.Device.getScreenOrientation()
						&& !mAllowUnrestrictedOrientationChange)
				{
					mActivityDestroyedToApplyOrientation = true;
					ActivityHelper.setOrientation(this, desiredOrientation);
					return;
				}
			}
		}

		final Pair<View, Boolean> initViewResult = preInitializeView(mController, savedInstanceState, mPreviousState);

		// if use a tabbed navigation. finish create view here.
		// Tabbed do not need cache size, it calculate it for each tabs later. Also if change draw methods (after onCreate)tabs replace fail on rotate.
		if (isTabbedNavigationController())
		{
			onCreateInitView(savedInstanceState);
			return;
		}

		Orientation deviceOrientation = Services.Device.getScreenOrientation();
		boolean hasCacheWindowSizes = AdaptersHelper.hasCacheWindowSizes(deviceOrientation);

		// calculate screen size only if necessary (cache size if not available) from main layout view
		if (!hasCacheWindowSizes)
		{
			View mainView = initViewResult.first;

			// get the correct size
			mainView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
			{
				@Override
				public void onGlobalLayout()
				{
					mainView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

					View content = getWindow().findViewById(Window.ID_ANDROID_CONTENT);

					int height = 0;
					int witdh = 0;
					if (content != null)
					{
						height = content.getHeight();
						witdh = content.getWidth();
					}
					//View rootView = mContentView.getFirstChild();
					Services.Log.debug("LayoutFragmentActivity view size height : " + height);
					Services.Log.debug("LayoutFragmentActivity view size width : " + witdh);

					Services.Log.debug("orientation : " + deviceOrientation.toString());

					// if use status bar., use boolean to know if rest status bar or not.
					AdaptersHelper.cacheWindowSizes(LayoutFragmentActivity.this, deviceOrientation, height, witdh, initViewResult.second);

					//actually set content view.
					onCreateInitView(savedInstanceState);
					onResume();
				}
			});
		}
		else
		{
			//actually set content view.
			onCreateInitView(savedInstanceState);
		}
	}

	@SuppressWarnings("deprecation")
	private void getLastCustomNonConfig() {
		mPreviousState = Cast.as(LayoutFragmentActivityState.class, getLastCustomNonConfigurationInstance());
	}

	public void onCreateInitView(Bundle savedInstanceState)
	{
		//actually set content view.
		initializeView(mController, savedInstanceState, mPreviousState);

		// Hide title bar if MAIN data view instructs it. This must be done after calling setContentView().
		if (getMainLayout() != null && !getMainLayout().getShowApplicationBar())
			ActivityHelper.setActionBarVisibilityForNavigationController(this, false);

		ActivityHelper.applyStyle(this, getMainLayout());

		// Analytics tracking.
		Analytics.onActivityStart(this);

		mActivityTimestamp = System.nanoTime();
	}

	@Override
	public IViewDefinition getMainDefinition()
	{
		if (mController == null || mController.getModel() == null || mController.getModel().getMain() == null)
			return null;

		if (mController.getModel().getMain().getDefinition() != null)
			return mController.getModel().getMain().getDefinition();

		return mController.getModel().getMain().getDashboardDefinition();
	}

	@Override
	public ILayoutDefinition getMainLayout()
	{
		// TODO: This is a redundant calculation; should be done somewhere else.
		if (mController == null || mController.getModel() == null || mController.getModel().getMain() == null)
			return null;

		DataViewModel model = mController.getModel().getMain();
		if (model.getDefinition() != null)
			return model.getDefinition().getLayoutForMode(model.getParams().Mode);

		return model.getDashboardDefinition().getLayout();
	}

	public void setAllowUnrestrictedOrientationChange(boolean allow) {
		mAllowUnrestrictedOrientationChange = allow;

		if (!allow && !mCurrentDesiredOrientation.equals(Services.Device.getScreenOrientation())) {
			ActivityHelper.setOrientation(this, mCurrentDesiredOrientation);
		}
	}

	protected abstract boolean initializeController(ActivityController controller);
	protected abstract Pair<View, Boolean> preInitializeView(ActivityController controller, Bundle savedInstanceState, LayoutFragmentActivityState previousState);
	protected abstract boolean initializeView(ActivityController controller, Bundle savedInstanceState, LayoutFragmentActivityState previousState);

	protected void registerFragment(BaseFragment fragment)
	{
		mFragments.add(fragment);
	}

	protected void unregisterFragment(BaseFragment fragment)
	{
		mFragments.remove(fragment);
	}

	public Set<BaseFragment> getFragments() {
		return mFragments;
	}

	protected void initializeLayoutFragment(LayoutFragment component, ComponentId id, ComponentParameters params, ComponentUISettings uiSettings, Connectivity parentConnectivity)
	{
		mDataViews.add(component);

		UIContext parentContext = getUIContext();
		// change the connectivity when necessary, do not touch if inherit (parent could not be inherit)
		if (parentConnectivity!=null &&  (parentConnectivity!=Connectivity.Inherit)
				&& !parentConnectivity.equals(parentContext.getConnectivitySupport()) )
		{
			parentContext = new UIContext(parentContext, parentConnectivity);
		}

		IDataViewController controller = mController.getController(parentContext, component, id, params);
		component.initialize(parentContext.getConnectivitySupport(), this, (LayoutFragment)uiSettings.parent, controller);

		if (uiSettings.size != null)
			component.setDesiredSize(uiSettings.size);

		if (uiSettings.isMain)
		{
			mMainFragment = component;
			LayoutDefinition mainLayout = component.getLayout();

			if (mainLayout != null)
			{
				// Change orientation if MAIN data view asks to do so.
				Orientation desiredOrientation = mainLayout.getActualOrientation();
				if (desiredOrientation != Orientation.UNDEFINED)
				{
					// Make sure ALL activity stops before CHANGING orientation.
					// Otherwise controllers may return with data for the now-defunct activity.
					if (desiredOrientation != Services.Device.getScreenOrientation())
					{
						mController.onDestroy();
						mActivityDestroyedToApplyOrientation = true;
					}

					// Request a FIXED orientation if needed.
					// This could mean rotating now (if current orientation is different) or preventing a future rotation.
					mCurrentDesiredOrientation = desiredOrientation;
					if (!mainLayout.isOrientationSwitchable() && !mAllowUnrestrictedOrientationChange) {
						ActivityHelper.setOrientation(this, desiredOrientation);
					}
				}
			}

		}

		// Restore state associated to this DV.
		restoreFragmentState(component);

		// Main data view is always active.
		if (uiSettings.isMain)
			component.setActive(true);
	}

	public void controlsToData() {
		for (BaseFragment fragment : mFragments) {
			fragment.finishEdit(); // needed for some controls in edit mode (i.e. GxEditTextNumeric)
			fragment.getContextEntity(); // it has the side effect to load data from the controls
		}
	}

	protected void checkSecurity(UIContext context, ComponentParameters params) {
		// If entering a restricted data view, redirect to login.
		// Must be done at the end of this method (after expanding layout), because successful login will return here.
		// Also, only call login ONCE per activity (without flag, it may be called once by detail and once by inline section).
		if (!mLoginCalled && SecurityHelper.callLoginIfNecessary(context, params.Object))
		{
			mLoginCalled = true;
			Services.Log.debug(String.format("Redirecting from '%s' startup to login.", params.Object));
		}
	}

	protected void restoreFragmentState(BaseFragment fragment)
	{
		if (mPreviousState != null)
		{
			LayoutFragmentState fragmentState = mPreviousState.getState(fragment.getUri());
			if (fragmentState != null)
			{
				if (fragment instanceof LayoutFragment)
				{
					LayoutFragment layoutFragment = (LayoutFragment)fragment;
					if (fragmentState.getData() != null && !fragmentState.getData().isEmpty())
					{
						// Set entity from saved state.
						ViewData data = ViewData.customData(fragmentState.getData(), DataRequest.RESULT_SOURCE_LOCAL);
						((DataViewController)layoutFragment.getController()).restoreRootData(data);
						layoutFragment.update(data);
					}
				}

				// Opportunity to restore custom state.
				fragment.restoreFragmentState(fragmentState);
			}
		}
	}

	public boolean isLoginPending()
	{
		return mLoginCalled;
	}

	protected void finalizeLayoutFragment(LayoutFragment dataView)
	{
		mDataViews.remove(dataView);
		mController.remove(dataView);
		dataView.setActive(false);
	}

	protected void registerDashboardFragment(DashboardFragment dataView)
	{
		mDataViews.add(dataView);
		dataView.setActive(true);
	}

	@Override
	public Iterable<IDataView> getDataViews()
	{
		return mDataViews;
	}

	@Override
	public Iterable<IDataView> getActiveDataViews(boolean includeNoLayoutDataViews)
	{
		ArrayList<IDataView> activeViews = new ArrayList<>();
		for (IDataView dataView : mDataViews)
			if (dataView.isActive() && (includeNoLayoutDataViews || dataView.getLayout() != null) )
				activeViews.add(dataView);

		return activeViews;
	}

	@Override
	public ActivityModel getModel()
	{
		return mController.getModel();
	}

	@Override
	public ActivityController getController()
	{
		return mController;
	}

	@Override
	protected void onStart()
	{
		ActivityHelper.onStart(this);
		mController.onStart();
		super.onStart();
	}

	@Override
	protected void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
		ActivityHelper.onNewIntent(this, intent);
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		if (!ActivityHelper.onResume(this))
			return;

		// Restore global activity state.
		if (mPreviousState != null && !mPreviousUIStateRestored)
		{
			restoreActivityUIState(mPreviousState);
			mPreviousUIStateRestored = true;
		}

		mController.onResume();

	}

	@Override
	protected void onPause()
	{
		ActivityHelper.onPause(this);
		mController.onPause();
		super.onPause();
	}

	@Override
	public void refreshData(RefreshParameters params)
	{
		mController.onRefresh(params);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		if (mController != null)
			return mController.onCreateOptionsMenu(menu);

		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		return mController.onOptionsItemSelected(item);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		ActivityHelper.onSaveInstanceState(this, outState);
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// call super is mandatory
		super.onActivityResult(requestCode, resultCode, data);

		// Handle action continuation and refresh/reload on activity result.
		mController.onActivityResult(requestCode, resultCode, data);
		ActivityHelper.onActivityResult(this, requestCode, resultCode, data);
		mLoginCalled = false;

	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
		mController.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override
	public boolean onSearchRequested()
	{
		return mController.onSearchRequested();
	}

	@Override
	@SuppressWarnings("deprecation")
	public final Object onRetainCustomNonConfigurationInstance()
	{
		LayoutFragmentActivityState state = null;
		if (mActivityDestroyedToApplyOrientation)
			state = mPreviousState;
		else {
			// Fix for (at least) Samsung Galaxy bug: when the camera is invoked and returns, the activity
			// may be rotated twice very quickly. In that case, the 2nd, interim activity doesn't get the
			// data from the taken photo into the control, and when it's asked to copy control data into the
			// entity, overwrites the correct value.
			// As a temporary fix, assume that an activity with a very short lifetime doesn't have any changed data.
			long lifetimeMillis = (System.nanoTime() - mActivityTimestamp) / 1000000;
			if (mPreviousState != null && lifetimeMillis < 800) {
				state = mPreviousState;
			} else if (!mShouldNotSaveState) {
				state = new LayoutFragmentActivityState();
				if (mController != null)
					mController.saveState(state);

				saveActivityState(state);

				for (BaseFragment fragment : mFragments)
					state.saveState(fragment);
			}
		}

		if (state != null && needsToApplyCurrentTheme())
			state.removeProperty(STATE_ACTION_BAR_THEME_CLASS); // Do it here for recreate() to work when the theme is changed

		return state;
	}

	private static final String STATE_ACTION_BAR_THEME_CLASS = "ActionBar::ThemeClass";
	private static final String ALLOW_UNRESTRICTED_ORIENTATION_CHANGE = "::AllowUnrestrictedOrientationChange";
	private static final String CURRENT_DESIRED_ORIENTATION = "::CurrentDesiredOrientation";

	/**
	 * Override to save custom state (for orientation change).
	 */
	protected void saveActivityState(LayoutFragmentActivityState state)
	{
		state.setProperty(STATE_ACTION_BAR_THEME_CLASS, ActivityHelper.getActionBarThemeClass(this));
		state.setProperty(ALLOW_UNRESTRICTED_ORIENTATION_CHANGE, mAllowUnrestrictedOrientationChange);
		state.setProperty(CURRENT_DESIRED_ORIENTATION, mCurrentDesiredOrientation);
	}

	/**
	 * Override to restore custom state (from orientation change).
	 */
	protected void restoreActivityState(LayoutFragmentActivityState state)
	{
		mAllowUnrestrictedOrientationChange = state.getBooleanProperty(ALLOW_UNRESTRICTED_ORIENTATION_CHANGE, false);
		mCurrentDesiredOrientation = state.getProperty(Orientation.class, CURRENT_DESIRED_ORIENTATION);
	}

	protected void restoreActivityUIState(LayoutFragmentActivityState state) {
		ActivityHelper.setActionBarThemeClass(this, state.getProperty(ThemeClassDefinition.class, STATE_ACTION_BAR_THEME_CLASS));
	}

	@Override
	public void setReturnResult()
	{
		Intent data = new Intent();

		if (mMainFragment != null)
			mMainFragment.setReturnResult(data);

		setResult(Activity.RESULT_OK, data);
	}

	@Override
	public void onBackPressed()
	{
		if (mController != null && mController.handleOnBackPressed())
			return;

		boolean isMain = mController != null && mController.getActivity().getIntent().hasExtra(IS_STARTUP_ACTIVITY);
		if (Services.Application.hasActiveMiniApp() && isMain) {
			Services.SuperApps.exit(this);
			return;
		}

		super.onBackPressed();
	}

	@Override
	public void finish()
	{
		Services.Device.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				LayoutFragmentActivity.super.finish();
				ActivityLauncher.onReturn(LayoutFragmentActivity.this, getIntent());
			}
		});
	}

	@Override
	public void onStop()
	{
		Analytics.onActivityStop(this);
		ActivityHelper.onStop(this);
		mController.onStop();
		super.onStop();
	}

	@Override
	protected void onDestroy()
	{
		if (mController != null)
			mController.onDestroy();

		ActivityHelper.onDestroy(this);
		super.onDestroy();
	}

	@Override
	public void onTranslationChanged() {
		if (getMainDefinition() != null && Strings.hasValue(getMainDefinition().getCaption())) {
			DataViewHelper.setTitle(this, null, getMainDefinition().getCaption());
		}
	}

	public LayoutFragment getMainFragment() {
		return mMainFragment;
	}

	public void setShouldNotSaveState(boolean shouldNotSaveState) {
		mShouldNotSaveState = shouldNotSaveState;
	}

	private boolean isTabbedNavigationController()
	{
		if (this instanceof GenexusActivity)
		{
			GenexusActivity activity = (GenexusActivity) this;
			if (activity.getNavigationController() instanceof TabbedNavigationController)
			{
				return true;
			}
		}
		return false;
	}
}
