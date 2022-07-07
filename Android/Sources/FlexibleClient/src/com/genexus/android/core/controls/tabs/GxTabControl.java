package com.genexus.android.core.controls.tabs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.genexus.android.ViewHierarchyVisitor;
import com.genexus.android.layout.CustomScrollView;
import com.genexus.android.layout.GridsLayoutVisitor;
import com.genexus.android.layout.GxLayoutInTab;
import com.genexus.android.core.base.controls.IGxControlPreserveState;
import com.genexus.android.core.base.controls.IGxControlRuntime;
import com.genexus.android.core.base.metadata.enums.Alignment;
import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.layout.TabControlDefinition;
import com.genexus.android.core.base.metadata.layout.TabItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.compatibility.SherlockHelper;
import com.genexus.android.core.controls.IGxThemeable;
import com.genexus.android.core.fragments.ComponentContainer;
import com.genexus.android.core.fragments.LayoutFragment;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.utils.Cast;

public class GxTabControl extends LinearLayout implements IGxThemeable, IGxControlRuntime, IGxControlPreserveState, ViewHierarchyVisitor.ICustomViewChildrenProvider
{
	private final Coordinator mCoordinator;
	private final TabControlDefinition mDefinition;
	private List<TabItemInfo> mTabItems;
	private ThemeClassDefinition mThemeClass;
	private ThemeClassDefinition mAppliedThemeClass;

	private CustomSlidingTabLayout mSlidingTabs;
	private ViewPager mViewPager;
	private TabsAdapter mTabsAdapter;

	private boolean mInitializing;
	private int mInitialTabIndex;

	private List<TabItemInfo> mVisibleTabItems;
	private int mCurrentVisibleIndex;
	private int mCurrentAbsoluteIndex;

	private static final String PROPERTY_ACTIVE_PAGE = "ActivePage";

	public GxTabControl(Context context, Coordinator coordinator, TabControlDefinition definition)
	{
		super(context);
		mCoordinator = coordinator;
		mDefinition = definition;
		initView();
	}

	private void initView()
	{
		mInitializing = true;
		mSlidingTabs = new CustomSlidingTabLayout(getContext());
		mViewPager = new CustomViewPager(getContext());

		setOrientation(VERTICAL);
		addView(mSlidingTabs, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		addView(mViewPager, new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f));

		mSlidingTabs.setDistributeEvenly(mDefinition.getTabStripKind() == TabControlDefinition.TabStripKind.Fixed);

		// Initialize the containers for each tab view.
		mTabItems = new ArrayList<>();
		for (TabItemDefinition tabItem : mDefinition.getTabItems())
		{
			TabItemInfo tabInfo = initTabItem(tabItem);
			tabInfo.index = mTabItems.size();
			mTabItems.add(tabInfo);
		}

		mCurrentVisibleIndex = -1;
		mVisibleTabItems = new ArrayList<>();
		updateVisibleTabItemsList();

		// Connect views and the adapter to construct the actual tabs.
		mTabsAdapter = new TabsAdapter();
		mViewPager.setAdapter(mTabsAdapter);
		mSlidingTabs.setViewPager(mViewPager);
		mSlidingTabs.setOnPageChangeListener(mPageChangeListener);

		mInitialTabIndex = 0;
		mInitializing = false;

		// its necessary in tabs if has dynamic components inside.
		mViewPager.setOffscreenPageLimit(mTabItems.size()); // HACK: To avoid fragment view recycling (our fragments do not support it).

	}

	private TabItemInfo initTabItem(TabItemDefinition tabItem)
	{
		TabItemInfo tabItemInfo = new TabItemInfo(tabItem);

		// Create tab title view (necessary even for invisible tabs to access their runtime properties).
		tabItemInfo.titleView = mSlidingTabs.createTabTitleView(tabItemInfo);
		tabItemInfo.titleView.applyThemeClasses();

		// Create tab content view (necessary even for invisible tabs to access runtime properties on its controls).
		GxLayoutInTab contentView = new GxLayoutInTab(getContext(),tabItem.getTable(), mCoordinator);
		contentView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		tabItemInfo.contentView = contentView;

		// See if we can/should add a ScrollView for the tab.
		if (!GridsLayoutVisitor.hasScrollableViews(tabItem.getTable()) && !mDefinition.hasAutoGrow())
		{
			//Add scroll to tabs if possible
			CustomScrollView scrollView = new CustomScrollView(getContext());
			scrollView.addView(contentView);
			tabItemInfo.rootView = scrollView;
			contentView.setHasScroll(true);
		}
		else
		{
			tabItemInfo.rootView = contentView;
		}

		return tabItemInfo;
	}

	public TabControlDefinition getDefinition()
	{
		return mDefinition;
	}

	public List<TabItemInfo> getTabItems()
	{
		return mTabItems;
	}

	private TabItemInfo getCurrentTabItem()
	{
		if (mCurrentVisibleIndex >= 0 && mCurrentVisibleIndex < mVisibleTabItems.size())
			return mVisibleTabItems.get(mCurrentVisibleIndex);
		else
			return null;
	}

	private void setCurrentTabItem(TabItemInfo tab)
	{
		if (tab.visibleIndex != -1)
			mInitialTabIndex = tab.visibleIndex;

		// SHOULD BE: mViewPager.setCurrentItem(tab.visibleIndex);
		// but this does not work when initializing because we have no place for the inner fragments.
	}

	@Override
	public Collection<View> getCustomViewChildren()
	{
		List<View> children = new ArrayList<>();

		// We want to return all controls relevant for us: the tab widget, and the layout of all the tabs.
		// Invisible tabs are not in the hierarchy, so we return the individual views themselves.
		for (TabItemInfo tabItem : mTabItems)
		{
			children.add(tabItem.titleView);
			children.add(tabItem.contentView);
		}

		return Collections.unmodifiableCollection(children);
	}

	public static class TabItemInfo
	{
		public final TabItemDefinition definition;
		public final String id;

		View rootView;
		public GxLayoutInTab contentView;
		GxTabPageTextView titleView;

		public int index;
		public CharSequence title;
		public boolean visible;
		int visibleIndex;

		TabItemInfo(TabItemDefinition tabItem)
		{
			definition = tabItem;
			id = tabItem.getName();
			title = tabItem.getCaption();
			visible = tabItem.isVisible();
		}

		public void setActive(boolean value)
		{
			if (titleView != null)
				titleView.setSelected(value);

			if (contentView != null)
				contentView.setIsActiveTab(value);
		}
	}

	private void updateVisibleTabItemsList()
	{
		mVisibleTabItems.clear();
		for (TabItemInfo itemInfo : mTabItems)
		{
			if (itemInfo.visible)
			{
				itemInfo.visibleIndex = mVisibleTabItems.size();
				mVisibleTabItems.add(itemInfo);
			}
			else
				itemInfo.visibleIndex = -1;
		}
	}

	public void notifyTabsChanged()
	{
		updateVisibleTabItemsList();
		if (mTabsAdapter != null)
		{
			mTabsAdapter.notifyDataSetChanged();
			mSlidingTabs.setViewPager(mViewPager);
		}
	}

	private class CustomSlidingTabLayout extends SlidingTabLayout
	{
		public CustomSlidingTabLayout(Context context)
		{
			super(context);
		}

		@Override
		protected View createTabView(PagerAdapter adapter, int position)
		{
			TabItemInfo itemInfo = mVisibleTabItems.get(position);
			return itemInfo.titleView;
		}

		private GxTabPageTextView createTabTitleView(TabItemInfo itemInfo)
		{
			TabItemDefinition itemDefinition = itemInfo.definition;

			// Create a textview with a compound drawable for the image and the specified background color.
			GxTabPageTextView tabView = new GxTabPageTextView(getContext(), GxTabControl.this, itemInfo);
			applyDefaultTabViewStyle(tabView);

			tabView.getInternalTextView().setText(itemInfo.title);
	        TabUtils.setTabImage(tabView.getInternalTextView(), itemDefinition.getImageUnselected(), itemDefinition.getImage(), itemDefinition.getImageAlignment());

			boolean hasImageAndText =  ( (tabView.getInternalTextView().getText().length() != 0) &&
					(itemDefinition.getImageAlignment() == Alignment.TOP || itemDefinition.getImageAlignment() == Alignment.BOTTOM) );

			TabUtils.fixTabHeightAndPadding(tabView, itemDefinition.getParent().getTabStripHeight(), hasImageAndText);

			return tabView;
		}
	}

	@SuppressWarnings("SimplifiableIfStatement")
	private class CustomViewPager extends ViewPager
	{
		public CustomViewPager(Context context)
		{
			super(context);
		}

		@Override
		public boolean onTouchEvent(MotionEvent ev)
		{
			if (!mDefinition.getAllowSwipe())
				return false;

			return super.onTouchEvent(ev);
		}

		@Override
		public boolean onInterceptTouchEvent(MotionEvent ev)
		{
			if (!mDefinition.getAllowSwipe())
				return false;

			return super.onInterceptTouchEvent(ev);
		}

		/* not working in all cases, see how to fix it.
		somehow braak tab strips size.
		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
		{
			//if (autoGrow)
			//{
				// from: http://stackoverflow.com/questions/8394681/android-i-am-unable-to-have-viewpager-wrap-content
				//  get the height of the biggest child it currently has.
				int height = 0;
				for (int i = 0; i < getChildCount(); i++)
				{
					View child = getChildAt(i);
					child.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
					int h = child.getMeasuredHeight();
					if (h > height) height = h;
				}

				heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
			//}
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}

		 */

	}

	private class TabsAdapter extends PagerAdapter
	{
		@Override
		public int getCount()
		{
			return mVisibleTabItems.size();
		}

		@Override
		public CharSequence getPageTitle(int position)
		{
			return mVisibleTabItems.get(position).title;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position)
		{
			// Return TabItemInfo as object (instead of the view itself) so that we can match them correctly
			// in getItemPosition() and isViewFromObject() (to support dynamic changes to tab visibility).
			TabItemInfo tabItem = mVisibleTabItems.get(position);
			container.addView(tabItem.rootView, 0);
			return tabItem;
		}

		@Override
		public void setPrimaryItem(ViewGroup container, int position, Object object)
		{
			// BUGFIX: onPageSelected() is not executed for the first page.
			if (mCurrentVisibleIndex == -1 && position == 0)
			{
				if (mInitialTabIndex != 0)
					mViewPager.setCurrentItem(mInitialTabIndex);
				else
					mPageChangeListener.onPageSelected(mInitialTabIndex); // Force event.
			}
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			TabItemInfo tabItem = (TabItemInfo)object;
			container.removeView(tabItem.rootView);
		}

		@Override
		public boolean isViewFromObject(View view, Object object)
		{
			TabItemInfo tabItem = (TabItemInfo)object;
			return tabItem.rootView == view;
		}

	    @Override
		public int getItemPosition(Object object)
	    {
	    	TabItemInfo tabItem = (TabItemInfo)object;
	    	int position = mVisibleTabItems.indexOf(tabItem);
	    	if (position != -1)
	    		return position;
	    	else
	    		return POSITION_NONE;
	    }
	}

	private final ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener()
	{
		private List<ComponentContainer> mLastTabContainer = null;

		@Override
		public void onPageSelected(int position)
		{
			mCurrentVisibleIndex = position;
			TabItemInfo selectedTab = getCurrentTabItem();

			// Why mark all tabs unselected instead of just the previous visible one?
			// Because the visible tabs list may have changed, so the previous value of mCurrentVisibleIndex would be off.
			for (TabItemInfo otherTab : mTabItems)
			{
				if (otherTab != selectedTab)
					otherTab.setActive(false);
			}

			if (selectedTab != null)
				selectedTab.setActive(true);

			// If the new or old tabs contain Fragments, set their activated/inactivated state.
			adjustFragmentsAfterTabChanged(selectedTab);

			// Fire the ActivePageChanged event if the page effectively changed (when making tabs visible/invisible
			// the visible index will change although the user stays in the "same" tab).
			if (selectedTab != null && selectedTab.index != mCurrentAbsoluteIndex)
			{
				mCurrentAbsoluteIndex = selectedTab.index;
				mCoordinator.runControlEvent(GxTabControl.this, "ActivePageChanged");
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) { }

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

		private void adjustFragmentsAfterTabChanged(TabItemInfo currentTab)
		{
			LayoutFragment parentFragment = null;

			// Inactivate old container
			if (mLastTabContainer != null)
			{
				for (ComponentContainer container : mLastTabContainer)
				{
					if (container.getStatus() == ComponentContainer.ACTIVE)
					{
						container.setStatus(ComponentContainer.TOINACTIVATED);
						if (container.getParentFragment() != null)
							parentFragment = container.getParentFragment();
					}

					// Tab not activate yet, never get show to the user. set to inactive
					if (container.getStatus() == ComponentContainer.TOACTIVATED && container.getParentFragment() == null)
						container.setStatus(ComponentContainer.INACTIVE);
				}
			}

			// Activated new container
			if (currentTab != null)
			{
				mLastTabContainer = ViewHierarchyVisitor.getViews(ComponentContainer.class, currentTab.contentView);
				for (ComponentContainer container : mLastTabContainer)
				{
					//Try to activate tabs that cannot be activated before.
					if (container.getStatus() == ComponentContainer.TOACTIVATED)
					{
						if (container.getParentFragment() != null)
							parentFragment = container.getParentFragment();
					}

					//Activate content of current selected tab
					if (container.getStatus() == ComponentContainer.INACTIVE)
					{
						container.setStatus(ComponentContainer.TOACTIVATED);
						if (container.getParentFragment() != null)
						{
							parentFragment = container.getParentFragment();
							// ActivePage in start event, before attach
							boolean isConnected = !container.hasTabParentDisconected();
							boolean tabParentVisible = container.getHasTabParentVisible();
							if (!isConnected && container.getFragment()==null
									&& tabParentVisible)
							{
								Services.Log.debug("container without fragment " + container.getDefinition().getName());
							}
						}
						else
						{
							//container not attached
							container.setPendingAttach(true);
						}
					}
				}
			}

			if (parentFragment != null)
			{
				parentFragment.attachContentContainers();
			}

			// Menu could be updated after tabs change (because grids become visible/invisible).
			if (!mInitializing && getContext() instanceof Activity)
				SherlockHelper.invalidateOptionsMenu((Activity)getContext());
		}
	};

	@Override
	public ThemeClassDefinition getThemeClass()
	{
		return mThemeClass;
	}

	@Override
	public void setThemeClass(ThemeClassDefinition themeClass)
	{
		mThemeClass = themeClass;
		applyClass(themeClass);
	}

	@Override
	public void applyClass(ThemeClassDefinition themeClass)
	{
		if (themeClass == null || themeClass.equals(mAppliedThemeClass))
			return;

		// Apply classes to the whole view and to the sliding tabs container.
		TabUtils.applyTabControlClass(this, mDefinition, mSlidingTabs, themeClass);

		// Apply related classes to tab item title views.
		for (TabItemInfo tabItem : mTabItems)
			tabItem.titleView.applyParentThemeClass(themeClass);

		mAppliedThemeClass = themeClass;
	}

	@Override
	public void setPropertyValue(String name, Value value)
	{
		if (PROPERTY_ACTIVE_PAGE.equalsIgnoreCase(name) && value != null)
		{
			Integer pageNumber = value.coerceToInt();
			if (pageNumber != null && pageNumber >= 1 && pageNumber <= mTabItems.size())
			{
				// Check that we don't switch to an invisible tab.
				int index = pageNumber - 1;
				TabItemInfo tabItem = mTabItems.get(index);
				if (tabItem.visibleIndex != -1)
				{
					// ActivePage on Start Event
					//set ActivePage, if tab not initialize . mark to do it.
					List<ComponentContainer> nextTabContainer = ViewHierarchyVisitor.getViews(ComponentContainer.class, tabItem.contentView);
					for (ComponentContainer container : nextTabContainer)
					{
						// mark inactive container without fragment Activate to init
						if (container.getStatus() == ComponentContainer.INACTIVE)
						{
							boolean isConnected = !container.hasTabParentDisconected();
							boolean tabParentVisible = container.getHasTabParentVisible();
							if (!isConnected && container.getFragment()==null
									&& tabParentVisible
									&& container.getParentFragment() != null)
							{
								Services.Log.debug("container pending attach from ActivePage " + container.getDefinition().getName());
								container.setPendingAttach(true);
							}
						}
					}

					// set Active Page
					mViewPager.setCurrentItem(tabItem.visibleIndex);

				}
			}
		}
	}

	@Override
	public Value getPropertyValue(String name)
	{
		if (PROPERTY_ACTIVE_PAGE.equalsIgnoreCase(name))
		{
			if (mCurrentVisibleIndex == -1)
				return Value.newInteger(0);

			int index = mVisibleTabItems.get(mCurrentVisibleIndex).index;
			return Value.newInteger(index + 1);
		}
		else
			return null;
	}

	@Override
	public String getControlId()
	{
		return mDefinition.getName();
	}

	@Override
	public void saveState(Map<String, Object> state)
	{
		TabItemInfo currentTab = getCurrentTabItem();
		if (currentTab != null)
			state.put(PROPERTY_ACTIVE_PAGE, currentTab.id);
	}

	@Override
	public void restoreState(Map<String, Object> state)
	{
		String activePageId = Cast.as(String.class, state.get(PROPERTY_ACTIVE_PAGE));
		if (Strings.hasValue(activePageId))
		{
			for (TabItemInfo tab : mVisibleTabItems)
			{
				if (tab.id.equals(activePageId))
					setCurrentTabItem(tab);
			}
		}
	}
}
