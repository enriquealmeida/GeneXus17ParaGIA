package com.genexus.android.core.ui.navigation.tabbed;

import android.content.Context;
import android.content.Intent;
import android.graphics.Insets;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.genexus.android.R;
import com.genexus.android.core.actions.ActionExecution;
import com.genexus.android.core.actions.ActionFactory;
import com.genexus.android.core.actions.ActionParameters;
import com.genexus.android.core.actions.CompositeAction;
import com.genexus.android.core.actions.CompositeAction.IEventListener;
import com.genexus.android.core.actions.UIContext;
import com.genexus.android.core.activities.ActivityHelper;
import com.genexus.android.core.activities.GenexusActivity;
import com.genexus.android.core.adapters.AdaptersHelper;
import com.genexus.android.layout.LayoutTag;
import com.genexus.android.core.app.ComponentId;
import com.genexus.android.core.app.ComponentParameters;
import com.genexus.android.core.app.ComponentType;
import com.genexus.android.core.app.ComponentUISettings;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.DashboardItem;
import com.genexus.android.core.base.metadata.DashboardMetadata;
import com.genexus.android.core.base.metadata.DataViewDefinition;
import com.genexus.android.core.base.metadata.Events;
import com.genexus.android.core.base.metadata.ILayoutDefinition;
import com.genexus.android.core.base.metadata.IViewDefinition;
import com.genexus.android.core.base.metadata.enums.Alignment;
import com.genexus.android.core.base.metadata.layout.LayoutDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.layout.Size;
import com.genexus.android.core.base.metadata.layout.TabControlDefinition;
import com.genexus.android.core.base.metadata.loader.DashboardMetadataLoader;
import com.genexus.android.core.base.metadata.theme.TabControlClassExtensionKt;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.compatibility.SherlockHelper;
import com.genexus.android.core.controllers.RefreshParameters;
import com.genexus.android.core.controls.tabs.SlidingTabLayout;
import com.genexus.android.core.controls.tabs.TabUtils;
import com.genexus.android.core.fragments.BaseFragment;
import com.genexus.android.core.fragments.IDataView;
import com.genexus.android.core.fragments.IFragmentHandleKeyEvents;
import com.genexus.android.core.fragments.IInspectableComponent;
import com.genexus.android.core.fragments.LayoutFragmentActivityState;
import com.genexus.android.core.fragments.WebViewFragment;
import com.genexus.android.core.ui.navigation.NavigationHandled;
import com.genexus.android.core.ui.navigation.UIObjectCall;
import com.genexus.android.core.ui.navigation.controllers.AbstractNavigationController;
import com.genexus.android.core.ui.navigation.controllers.StandardNavigationController;
import com.genexus.android.core.utils.ThemeUtils;

import java.util.ArrayList;
import java.util.List;

import static com.genexus.android.core.base.metadata.theme.TabControlClassExtensionKt.TAB_STRIP_POSITION_BOTTOM;
import static com.genexus.android.core.base.metadata.theme.TabControlClassExtensionKt.TAB_STRIP_POSITION_TOP;

public class TabbedNavigationController extends AbstractNavigationController {
	private final GenexusActivity mActivity;
	private final DashboardMetadata mDashboard;
	private final Handler mHandler;

	private final ArrayList<TabItemInfo> mTabItems;
	private final ThemeClassDefinition mThemeClassForTabs;

	private LinearLayout mMainView;
	private SlidingTabLayout mSlidingTabs;
	private ViewPager mViewPager;
	private Space mBottomSpace;

	private Entity mData;
	private Fragment mCurrentTabFragment;
	private boolean mClientStartExecuted;

	private ArrayList<TabItemInfo> mVisibleTabItems;
	private int mCurrentVisibleIndex;
	private int mCurrentAbsoluteIndex;

	private static final String STATE_CURRENT_TAB = "com.artech.ui.navigation.tabbed.TabbedNavigationController::CURRENT_TAB";
	private static final String STATE_DASHBOARD_DATA = "com.artech.ui.navigation.tabbed.TabbedNavigationController::DATA";
	private static final String CONTEXT_TAG_DASHBOARD_ITEM_INDEX = STATE_CURRENT_TAB;

	private TabsPagerAdapter mTabsPagerAdapter = null;

	private int mTabHeight = 0;

	public TabbedNavigationController(GenexusActivity activity, DashboardMetadata dashboard) {
		mActivity = activity;
		mDashboard = dashboard;
		mHandler = new Handler(Looper.myLooper());

		mTabItems = new ArrayList<>();
		mThemeClassForTabs = mDashboard.getThemeClassForTabs();
	}

	@Override
	public Pair<View, Boolean> onPreCreate(Bundle savedInstanceState, ComponentParameters mainParams)
	{
		// if tabs bottom , prepare for possible header row
		if (mThemeClassForTabs != null && TabControlClassExtensionKt.getTabStripPosition(mThemeClassForTabs) == TAB_STRIP_POSITION_BOTTOM) {
			// ActionBar EnableHeaderRowPattern
			ActivityHelper.setActionBarOverlay(mActivity);
			// StatusBar EnableHeaderRowPattern
			ActivityHelper.setStatusBarOverlay(mActivity);

			// disable until show a tab with header row.
			// use first tab as the default value.

			// TODO: find an equivalent to onPreCreateDeprecated for Build.VERSION_CODES.R
			//if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			//	mActivity.getWindow().setDecorFitsSystemWindows(true);
			//} else {
			onPreCreateDeprecated();
			//}
		}

		// use new layout for header row
		mActivity.setContentView(R.layout.tabbed_navigation);
		mMainView = mActivity.findViewById(R.id.content_frame);

		return new Pair<>(mMainView, false);
	}

	@SuppressWarnings("deprecation")
	private void onPreCreateDeprecated() {
		mActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		ActivityHelper.setSupportActionBar(mActivity);

		// set title,  necessary in navigation tabs
		mActivity.setTitle(mDashboard.getCaption());

		// avoid orientation change if default orientation set (get from main orientation)
		// there is no mainLayout for dashboard.
		boolean isSwitch = ActivityHelper.setDefaultOrientation(mActivity);
		if (isSwitch)
			Services.Log.warning("orientation should be change before in LayoutFragment Activity onCreate");

		// Set title color and background.
		ActivityHelper.applyStyle(mActivity, mDashboard);

		// HACK: Discard all fragments on configuration change, recreate them later.
		if (savedInstanceState != null && mActivity.getSupportFragmentManager().getFragments().size() != 0) {
			FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
			for (Fragment fragment : mActivity.getSupportFragmentManager().getFragments()) {
				if (fragment instanceof BaseFragment || fragment instanceof WebViewFragment || fragment instanceof TabPlaceholderFragment)
					ft.remove(fragment);
			}

			ft.commitAllowingStateLoss();
		}
	}

	public boolean setupActionBarInitLayoutTabbed(ILayoutDefinition layout, boolean addMargins, boolean hideShowActionBar) {
		StandardNavigationController.setupActionBarInitLayout(mActivity, layout, addMargins, hideShowActionBar);
		// TODO: find an equivalent to setupActionBarInitLayoutTabbedDeprecated for Build.VERSION_CODES.R
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			if (layout.getEnableHeaderRowPattern()) {
				mActivity.getWindow().setDecorFitsSystemWindows(false);
				Insets insets = mActivity.getWindowManager().getCurrentWindowMetrics().getWindowInsets().getInsets(WindowInsets.Type.navigationBars());
				mBottomSpace.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, insets.bottom + insets.top));
				return true;
			} else {
				mActivity.getWindow().setDecorFitsSystemWindows(true);
				mBottomSpace.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
				return false;
			}
		} else {
			return setupActionBarInitLayoutTabbedDeprecated(layout);
		}
	}

	@SuppressWarnings("deprecation")
	private boolean setupActionBarInitLayoutTabbedDeprecated(ILayoutDefinition layout) {
		boolean result = false;
		if (layout.getEnableHeaderRowPattern()) {
			// UI Visibility not modified, default value.
			if (mActivity.getWindow().getDecorView().getSystemUiVisibility()==View.SYSTEM_UI_FLAG_VISIBLE)
				result = true;
			mActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
		} else {
			mActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
		}
		return result;
	}

	@Override
	public boolean start(ComponentParameters mainParams, LayoutFragmentActivityState previousState) {
		if (mDashboard == null)
			throw new IllegalArgumentException("TabbedNavigationController requires a Dashboard view definition.");

		mData = EntityFactory.newEntity();
		mData.setExtraMembers(mDashboard.getVariables());

		// Hide title bar if data view instructs it. This must be done here to work.
		if (!mDashboard.getShowApplicationBar())
			ActivityHelper.setActionBarVisibility(mActivity, false);

		initTabItems();

		Integer previousCurrentTab = null;
		if (previousState != null) {
			previousCurrentTab = (Integer) previousState.getProperty(STATE_CURRENT_TAB);
			Entity previousData = (Entity) previousState.getProperty(STATE_DASHBOARD_DATA);
			if (previousData != null) {
				mClientStartExecuted = true;
				mData = previousData;
			}
		}

		if (!mClientStartExecuted) {
			// Execute the ClientStart event, add tabs afterwards.
			UIContext context = UIContext.base(mActivity, mDashboard.getConnectivitySupport());
			if (!runEvent(mDashboard, Events.CLIENT_START, context, mData, mAfterClientStart)) {
				// No ClientStart, execute "After ClientStart" immediately.
				mAfterClientStart.onEndEvent(null, true);
			}
		} else
			initTabLayout(previousCurrentTab);

		return true;
	}

	private final IEventListener mAfterClientStart = new IEventListener() {
		@Override
		public void onEndEvent(CompositeAction event, boolean successful) {
			Services.Device.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mClientStartExecuted = true;
					initTabLayout(null); // There can be no previous tab if ClientStart hadn't executed.
				}
			});
		}
	};

	private static class TabItemInfo {
		private int index;
		private DashboardItem definition;
		private boolean started;
		private TabPlaceholderFragment holderFragment;
		private Fragment contentFragment;
		private TextView titleView;

		// tab status.
		private String barTitle;

		public boolean visible;
		int visibleIndex;

		ComponentId getComponentId() {
			return new ComponentId(null, "[TabbedNavigation]::TAB." + index);
		}
	}

	private void initTabItems() {
		for (DashboardItem item : mDashboard.getItems()) {
			TabItemInfo tabItem = new TabItemInfo();
			tabItem.index = mTabItems.size();
			tabItem.definition = item;
			tabItem.started = false;
			tabItem.holderFragment = TabPlaceholderFragment.newInstance(tabItem.index);
			tabItem.visible = true;

			mTabItems.add(tabItem);
		}

		mCurrentVisibleIndex = -1;
		mVisibleTabItems = new ArrayList<>();
		updateVisibleTabItemsList();
	}

	private void initTabLayout(final Integer setAsCurrentTab) {
		Services.Log.debug("initTabLayout Called ");

		ViewPager previousViewPager = mMainView.findViewById(R.id.tab_navigation_viewpager);
		if (previousViewPager!=null)
		{
			Services.Log.info("duplicate initTabLayout Called , ignore call");
			return;
		}

		mViewPager = new ViewPager(mActivity);
		mViewPager.setId(R.id.tab_navigation_viewpager); // Necessary for FragmentPagerAdapter
		mViewPager.setOffscreenPageLimit(mTabItems.size()); // HACK: To avoid fragment view recycling (our fragments do not support it).

		mMainView.addView(mViewPager, new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f));

		int tabsPosition = 0; // By default, tabs go up.
		if (mThemeClassForTabs != null && TabControlClassExtensionKt.getTabStripPosition(mThemeClassForTabs) == TAB_STRIP_POSITION_BOTTOM)
			tabsPosition = 1;

		boolean isImageTop = (mDashboard.getTabsImageAlignment() == Alignment.TOP || mDashboard.getTabsImageAlignment() == Alignment.BOTTOM);
		int tabHeight = TabControlDefinition.getDefaultTabStripHeight(mThemeClassForTabs, isImageTop);
		mTabHeight = tabHeight;

		mSlidingTabs = new CustomSlidingTabLayout(SherlockHelper.getActionBarThemedContext(mActivity));
		mMainView.addView(mSlidingTabs, tabsPosition, new LayoutParams(LayoutParams.MATCH_PARENT, tabHeight));

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			mBottomSpace = new Space(mActivity);
			mMainView.addView(mBottomSpace, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
		}

		mSlidingTabs.setDistributeEvenly(mDashboard.getTabStripKind() == TabControlDefinition.TabStripKind.Fixed);

		applyThemeClass();

		// Connect views and the adapter to construct the actual tabs.
		mTabsPagerAdapter = new TabsPagerAdapter();
		mViewPager.setAdapter(mTabsPagerAdapter);
		mSlidingTabs.setViewPager(mViewPager);
		mSlidingTabs.setOnPageChangeListener(mPageChangeListener);

		// BUGFIX: When recreating the Activity via the task switcher after leaving with the back key,
		// sometimes the fragments are not properly initialized (getActivity() returns null).
		// "Posting" this instead of running it immediately seems to fix the problem.
		Services.Device.postOnUiThread(new Runnable() {
			@Override
			public void run() {
				// Restore the current tab.
				// BUGFIX: ViewPager does not fire onPageSelected() for the first page. Force it in that case.
				if (setAsCurrentTab != null && setAsCurrentTab != 0)
					mViewPager.setCurrentItem(setAsCurrentTab);
				else
					mPageChangeListener.onPageSelected(0);
			}
		});
	}

	private void applyThemeClass() {
		// Before actually applying the class, set the defaults from the Action Bar theme
		// (since tabs should be shown as if they were part of the action bar itself).
		Integer actionBarBackgroundColor = ThemeUtils.getAndroidThemeColorId(mActivity, R.attr.colorPrimary);

		// If the Action Bar class has a background color, use it instead.
		if (mDashboard.getApplicationBarClass() != null && Strings.hasValue(mDashboard.getApplicationBarClass().getBackgroundColor()))
			actionBarBackgroundColor = ThemeUtils.getColorId(mDashboard.getApplicationBarClass().getBackgroundColor());

		if (actionBarBackgroundColor != null)
			mSlidingTabs.setBackgroundColor(actionBarBackgroundColor);

		// Use same elevation as action bar.
		if (mActivity.getSupportActionBar() != null) {
			mSlidingTabs.setElevation(mActivity.getSupportActionBar().getElevation());

			// Remove Action Bar elevation if Action Bar and tabs are together.
			if (mThemeClassForTabs == null || TabControlClassExtensionKt.getTabStripPosition(mThemeClassForTabs) == TAB_STRIP_POSITION_TOP)
				mActivity.getSupportActionBar().setElevation(0f);
		}

		// Then apply the custom class. The above properties will be considered "default".
		TabUtils.applyTabControlClass(mMainView, null, mSlidingTabs, mThemeClassForTabs);
	}

	private class CustomSlidingTabLayout extends SlidingTabLayout {
		public CustomSlidingTabLayout(Context context) {
			super(context);
			setTag(LayoutTag.CONTROL_GENEXUS, true); // Mark it so it shown in live editing, needed for background
		}

		@Override
		protected View createTabView(PagerAdapter adapter, int position) {
			TabItemInfo itemInfo = mVisibleTabItems.get(position);

			LinearLayout tabView = createDefaultTabView(getContext());

			LayoutItemDefinition definition = new LayoutItemDefinition(null);
			definition.setName(itemInfo.definition.getObjectName());
			tabView.setTag(LayoutTag.CONTROL_GENEXUS, true);
			tabView.setTag(LayoutTag.CONTROL_DEFINITION, definition);
			tabView.setTag(LayoutTag.CONTROL_THEME_CLASS, mThemeClassForTabs);
			boolean isImageTop = (mDashboard.getTabsImageAlignment() == Alignment.TOP || mDashboard.getTabsImageAlignment() == Alignment.BOTTOM);

			boolean hasImageAndText = (itemInfo.definition.getTitle() != null && itemInfo.definition.getTitle().length() != 0)
					&& isImageTop;

			TabUtils.fixTabHeightAndPadding(tabView, TabControlDefinition.getDefaultTabStripHeight(mThemeClassForTabs, isImageTop), hasImageAndText);

			TextView childTextView = tabView.findViewById(R.id.tabcontrol_textview);

			childTextView.setText(itemInfo.definition.getTitle());
			// support image align
			TabUtils.setTabImage(childTextView, itemInfo.definition.getImageName(), Strings.EMPTY, mDashboard.getTabsImageAlignment());
			applyTabItemClass(childTextView);

			itemInfo.titleView = childTextView;

			return tabView;
		}

	}

	@SuppressWarnings("deprecation")
	private class TabsPagerAdapter extends FragmentPagerAdapter {
		private TabsPagerAdapter() {
			super(mActivity.getSupportFragmentManager());
		}

		@Override
		public Fragment getItem(int position) {
			TabItemInfo tabItem = mVisibleTabItems.get(position);
			return tabItem.holderFragment;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			TabItemInfo tabItem = mVisibleTabItems.get(position);
			return tabItem.definition.getTitle();
		}

		@Override
		public int getCount() {
			return mVisibleTabItems.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// Return TabItemInfo as object (instead of the view itself) so that we can match them correctly
			// in getItemPosition() and isViewFromObject() (to support dynamic changes to tab visibility).
			TabItemInfo tabItem = mVisibleTabItems.get(position);
			super.instantiateItem(container, position);
			return tabItem;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			TabItemInfo tabItem = (TabItemInfo) object;
			return tabItem.holderFragment.getView() == view;
		}

		@Override
		public int getItemPosition(Object object) {
			TabItemInfo tabItem = (TabItemInfo) object;
			int position = mVisibleTabItems.indexOf(tabItem);
			if (position != -1)
				return position;
			else
				return POSITION_NONE;
		}

		@Override
		public void setPrimaryItem(ViewGroup container, int position, Object object) {
			TabItemInfo tabItem = (TabItemInfo) object;
			if (tabItem.contentFragment != null)
				super.setPrimaryItem(container, position, tabItem.contentFragment);
			else
				super.setPrimaryItem(container, position, tabItem.holderFragment);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			TabItemInfo tabItem = (TabItemInfo) object;
			super.destroyItem(container, position, tabItem.holderFragment);
		}

		@Override
		public long getItemId(int position) {
			// return the position of visible item in all items array, same position for same item.
			TabItemInfo tabItem = mVisibleTabItems.get(position);
			return tabItem.index;
		}
	}

	private final ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
		@Override
		public void onPageSelected(int position) {
			mCurrentVisibleIndex = position;
			TabItemInfo selectedTab = getCurrentTabItem();
			setTabItemAsSelected(selectedTab);

			if (!selectedTab.started) {
				// This is the first time switching to this tab.
				// Launch the tab action, eventually the tab content will be filled.
				// Set a tag on the UIContext so that we can intercept the first object call and treat it as the tab content.
				UIContext context = UIContext.base(mActivity, mDashboard.getConnectivitySupport());
				context.setTag(CONTEXT_TAG_DASHBOARD_ITEM_INDEX, position);
				selectedTab.started = true;

				startTab(context, selectedTab);
			} else {
				// Refresh data when switching back to a tab (content may not be available yet, that case will be ignored).
				updateCurrentContent(true);
			}

			// Fire the ActivePageChanged event if the page effectively changed (when making tabs visible/invisible
			// the visible index will change although the user stays in the "same" tab).
			if (selectedTab != null && selectedTab.index != mCurrentAbsoluteIndex) {
				mCurrentAbsoluteIndex = selectedTab.index;
				UIContext context = UIContext.base(mActivity, mDashboard.getConnectivitySupport());
				runEvent(mDashboard, "ActivePageChanged", context, mData, null);

				// change tab. Update layout.
			}
		}

		private void setTabItemAsSelected(TabItemInfo tabItem) {
			for (TabItemInfo otherTabItem : mTabItems) {
				if (otherTabItem != tabItem) {
					// view otherTabItem.titleView could be null for not visible tabs on start.
					if (otherTabItem.titleView != null && otherTabItem.titleView.isSelected()) {
						otherTabItem.titleView.setSelected(false);
						applyTabItemClass(otherTabItem.titleView);
					}
				}
			}

			tabItem.titleView.setSelected(true);
			applyTabItemClass(tabItem.titleView);
		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		}
	};

	private void startTab(final UIContext context, final TabItemInfo tabItem) {
		if (tabItem.definition.getKind() == DashboardMetadataLoader.COMPONENT_KIND) {
			// Special case, show WebView.
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					ComponentParameters webParams = new ComponentParameters(tabItem.definition.getObjectName());
					handle(new UIObjectCall(context, webParams), null);
				}
			});
		} else {
			// Standard case, normal action. Will eventually display a Fragment.
			ActionDefinition definition = tabItem.definition.getActionDefinition();
			CompositeAction action = ActionFactory.getAction(context, definition, new ActionParameters(mData), definition.getIsComposite());
			ActionExecution exec = new ActionExecution(action);
			exec.executeAction();
		}
	}

	private void updateCurrentContent(boolean refresh) {
		TabItemInfo tabInfo = mVisibleTabItems.get(mCurrentVisibleIndex);
		Fragment fragment = tabInfo.contentFragment;

		if (fragment != null && !fragment.equals(mCurrentTabFragment)) {
			// Mark the previous tab as inactive.
			if (mCurrentTabFragment != null && mCurrentTabFragment instanceof IDataView)
				((IDataView) mCurrentTabFragment).setActive(false);

			mCurrentTabFragment = fragment;

			if (fragment instanceof IDataView) {
				IDataView dataView = (IDataView) fragment;
				dataView.setActive(true);
				//adjust action bar. with the current layout
				setupActionBarInitLayoutTabbed(dataView.getLayout(), true, false);
				// should be after init layout tabbed. Update action bar style on tab changed
				dataView.updateActionBar();

				// set bar title from this tab tabText
				if (Services.Strings.hasValue(tabInfo.barTitle))
					mActivity.setTitle(tabInfo.barTitle);

				if (refresh)
					dataView.refreshData(RefreshParameters.IMPLICIT);
			}
		}
	}

	@Override
	public boolean setTitle(IDataView fromDataView, CharSequence title) {
		// find tab with this IDataView
		for (TabItemInfo item : mTabItems) {
			IDataView dataView = (IDataView) item.contentFragment;
			if (dataView != null && dataView.equals(fromDataView)) {
				// store title in this tabInfo
				// keep the current app bar title for this dataView.
				item.barTitle = title.toString();
			}
		}

		// Return false to continue update title.
		return false;
	}

	@Override
	public @NonNull
	NavigationHandled handle(final UIObjectCall call, Intent callIntent) {
		// If instantiating a Tab, intercept the call. Otherwise let it fall through.
		Integer tabIndex = (Integer) call.getContext().getTag(CONTEXT_TAG_DASHBOARD_ITEM_INDEX);
		if (tabIndex != null) {
			final TabItemInfo tabItem = mVisibleTabItems.get(tabIndex);
			if (!tabItem.started || tabItem.contentFragment != null)
				throw new IllegalStateException("Invalid state when about to set tab item content!");

			Services.Device.invokeOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (mActivity.isFinishing() || mActivity.isDestroyed())
						return; // Discard operation for activity that is being torn down.

					// start a new tab in ui thread.

					// The ViewPager is already measured so we can check getWidth()/getHeight() to figure content size.
					int contentWidth = mViewPager.getWidth() - mViewPager.getPaddingLeft() - mViewPager.getPaddingRight();
					int contentHeight = mViewPager.getHeight() - mViewPager.getPaddingTop() - mViewPager.getPaddingBottom();

					ComponentParameters params = call.toComponentParams();

					if (params.Object instanceof DataViewDefinition) {
						LayoutDefinition layout = ((DataViewDefinition) params.Object).getLayoutForMode(params.Mode);
						//adjust height
						if (layout != null) {
							contentHeight = AdaptersHelper.getWindowHeightForDahsboardTabs(mActivity, layout) - mTabHeight;
							boolean showStatusBar = setupActionBarInitLayoutTabbed(layout, true, false);
							if (showStatusBar)
								contentHeight += AdaptersHelper.getStatusBarHeight(mActivity);
						}
					}

					Size fragmentSize = new Size(contentWidth, contentHeight);

					Fragment calledFragment;
					ComponentUISettings uiSettings = new ComponentUISettings(true, null, fragmentSize);

					if (params.Type == ComponentType.Form)
						calledFragment = mActivity.createComponent(tabItem.getComponentId(), params, uiSettings, call.getContext().getConnectivitySupport());
					else
						calledFragment = WebViewFragment.newInstance(params.Url);

					// Add the content fragment inside the placeholder.

					// only replace the content if the fragment is currently added
					if (!tabItem.holderFragment.isAdded()) {
						Services.Log.error("Replace in tab fragment discarded.");
						return; // Discard operation for activity that is being turn down.
					}

					tabItem.contentFragment = calledFragment;
					tabItem.holderFragment.setContentFragment(calledFragment);

					// Allow header row for each tab fragment.
					if (calledFragment instanceof BaseFragment) {
						((BaseFragment) calledFragment).setAllowHeaderRow(true);
					}

					if (tabItem.index == mCurrentAbsoluteIndex)
						updateCurrentContent(false);
				}
			});

			return NavigationHandled.HANDLED_CONTINUE;
		}

		if (StandardNavigationController.handlePopup(mActivity, call))
			return NavigationHandled.HANDLED_WAIT_FOR_RESULT;

		return NavigationHandled.NOT_HANDLED;
	}

	@Override
	public boolean showTarget(String targetName) {
		int tabPosition = TabbedNavigation.getTabPositionFromTargetName(targetName);
		if (tabPosition != TabbedNavigation.TAB_NONE && tabPosition >= 1 && tabPosition <= mTabItems.size()) {
			int index = tabPosition - 1;
			// get index in tabs  items.
			TabItemInfo info = mTabItems.get(index);
			if (!info.visible) {
				info.visible = true;
				// recreate this tabItem, it was destroyed before
				info.started = false;
				info.contentFragment = null;

				updateVisibleTabItemsList();
				// update viewpager
				notifyTabsChanged();
			}

			// set visible item in all items visibles
			if (info.visibleIndex >= 0) {
				mViewPager.setCurrentItem(info.visibleIndex);
			}
			return true;
		}

		return false;
	}

	// hide Target
	@Override
	public boolean hideTarget(String targetName) {
		int tabPosition = TabbedNavigation.getTabPositionFromTargetName(targetName);
		if (tabPosition != TabbedNavigation.TAB_NONE && tabPosition >= 1 && tabPosition <= mTabItems.size()) {
			int index = tabPosition - 1;

			TabItemInfo info = mTabItems.get(index);
			info.visible = false;
			updateVisibleTabItemsList();

			// update the view pager
			notifyTabsChanged();
			return true;
		}

		return false;
	}

	@Override
	public boolean isTargetVisible(String targetName)
	{
		int tabPosition = TabbedNavigation.getTabPositionFromTargetName(targetName);
		if (tabPosition != TabbedNavigation.TAB_NONE && tabPosition >= 1 && tabPosition <= mTabItems.size())
		{
			int index = tabPosition - 1;
			// get index in tabs  items.
			TabItemInfo info = mTabItems.get(index);
			return info.visible;
		}
		//by default is visible
		return true;
	}

	private void applyTabItemClass(TextView tabTitleView) {
		if (mThemeClassForTabs != null)
			TabUtils.applyTabItemClass(tabTitleView,
				TabControlClassExtensionKt.getUnselectedPageClass(mThemeClassForTabs),
				TabControlClassExtensionKt.getSelectedPageClass(mThemeClassForTabs));
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (mCurrentTabFragment != null && mCurrentTabFragment instanceof IFragmentHandleKeyEvents) {
			IFragmentHandleKeyEvents f = (IFragmentHandleKeyEvents) mCurrentTabFragment;
			if (f.onKeyUp(keyCode, event))
				return true;
		}

		return super.onKeyUp(keyCode, event);
	}

	@Override
	public void saveActivityState(LayoutFragmentActivityState outState) {
		super.saveActivityState(outState);
		outState.setProperty(STATE_CURRENT_TAB, mCurrentVisibleIndex);

		if (mClientStartExecuted)
			outState.setProperty(STATE_DASHBOARD_DATA, mData);
	}

	@Override
	public List<IInspectableComponent> getActiveComponents() {
		List<IInspectableComponent> activeComponents = new ArrayList<>();

		activeComponents.add(() -> mSlidingTabs);

		for (TabItemInfo item : mTabItems) {
			IDataView dataView = (IDataView) item.contentFragment;
			if (dataView != null && dataView.isActive()) {
				activeComponents.add(dataView);

				for (BaseFragment fragment : mActivity.getFragments())
					if (fragment.equals(item.contentFragment)) //Main fragment has already been added; should add popup if present
						activeComponents.add(fragment);
			}
		}

		ActivityHelper.addApplicationBarComponent(mActivity, activeComponents);
		return activeComponents;
	}

	private void updateVisibleTabItemsList() {
		mVisibleTabItems.clear();
		for (TabItemInfo itemInfo : mTabItems) {
			if (itemInfo.visible) {
				itemInfo.visibleIndex = mVisibleTabItems.size();
				mVisibleTabItems.add(itemInfo);
			} else
				itemInfo.visibleIndex = -1;
		}
	}

	private TabItemInfo getCurrentTabItem() {
		if (mCurrentVisibleIndex >= 0 && mCurrentVisibleIndex < mVisibleTabItems.size())
			return mVisibleTabItems.get(mCurrentVisibleIndex);
		else
			return null;
	}

	public void notifyTabsChanged() {
		updateVisibleTabItemsList();
		if (mTabsPagerAdapter != null) {
			mTabsPagerAdapter.notifyDataSetChanged();
			mSlidingTabs.setViewPager(mViewPager);
		}
	}

	@Override
	public DashboardItem getMenuItemDefinition(String itemName) {
		for (DashboardItem item : mDashboard.getItems()) {
			if (itemName.equalsIgnoreCase(item.getName()))
				return item;
		}

		return null;
	}

	public int getActivePage() {
		return mViewPager.getCurrentItem();
	}

	public void setActivePage(int index) {
		if (index >= 0 && index < mTabsPagerAdapter.getCount())
			mViewPager.setCurrentItem(index);
		else
			Services.Log.warning("TabbedNavigationController setActivePage index out of range");
	}

	private boolean runEvent(IViewDefinition definition, String eventName, UIContext uiContext, Entity data, CompositeAction.IEventListener eventListener) {
		// Run the event if present.
		ActionDefinition event = definition.getEvent(eventName);
		if (event == null) {
			return false;
		}

		CompositeAction action = ActionFactory.getAction(uiContext, event, new ActionParameters(data), event.getIsComposite());
		if (eventListener != null)
			action.setEventListener(eventListener);

		ActionExecution exec = new ActionExecution(action);
		exec.executeAction();

		return true;
	}

	@Override
	public boolean onBackPressed()
	{
		ActionDefinition backEvent = mDashboard.getEvent(Events.BACK);
		if (backEvent != null)
		{
			UIContext context = UIContext.base(mActivity, mDashboard.getConnectivitySupport());
			runEvent(mDashboard, Events.BACK, context, mData, null);
			return true; // Runs only the menu tabs Back event in this case. tabs back events will not run.
		}
		return false;
	}

}
