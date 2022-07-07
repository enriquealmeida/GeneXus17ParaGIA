package com.genexus.android.core.ui.navigation.slide;

import java.util.HashMap;

import android.app.Activity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.genexus.android.R;
import com.genexus.android.core.adapters.AdaptersHelper;
import com.genexus.android.core.app.ComponentId;
import com.genexus.android.core.app.ComponentParameters;
import com.genexus.android.core.base.metadata.ILayoutDefinition;
import com.genexus.android.core.base.metadata.layout.Size;
import com.genexus.android.core.fragments.IDataView;
import com.genexus.android.core.fragments.LayoutFragmentActivityState;
import com.genexus.android.core.ui.navigation.UIObjectCall;

class SlideNavigationTargets
{
	private final DrawerLayout mDrawerLayout;
	private final HashMap<SlideNavigation.Target, IDataView> mFragments;

	private Size mDrawerSize;
	private Size mContentSize;

	private static final String STATE_DRAWER_OPEN_FORMAT = "Gx::SlideNavigation::IsDrawerOpen::%s";

	public SlideNavigationTargets(DrawerLayout drawerLayout)
	{
		mDrawerLayout = drawerLayout;
		mFragments = new HashMap<>();
	}

	public void start(Activity activity, ComponentParameters mainParams)
	{
		// Calculate drawer and content sizes. These are fixed here because later on the R.id.content view
		// may change size if the keyboard is opened and we need the "original" values to measure.
		// Use the "main" component for this (it decides whether the action bar is visible or not).
		ILayoutDefinition mainLayout = UIObjectCall.getLayoutFromViewDefinition(mainParams.Object, mainParams.Mode);
		Size windowSize = AdaptersHelper.getWindowSize(activity, mainLayout);

		int drawerWidth = (int)activity.getResources().getDimension(R.dimen.drawer_width);
		mDrawerSize = new Size(drawerWidth, windowSize.getHeight());
		mContentSize = windowSize;
	}

	public static int getTargetViewId(SlideNavigation.Target target)
	{
		switch (target)
		{
			case Left :	return R.id.left_drawer;
			case Content : return R.id.content_frame;
			case Right : return R.id.right_drawer;
			default : throw new IllegalArgumentException(String.format("Invalid target: '%s'.", target));
		}
	}

	public static ComponentId getComponentId(SlideNavigation.Target target)
	{
		switch (target)
		{
			case Left :
			case Content :
			case Right :
				return new ComponentId(null, "[SlideNavigation]::" + target.toString());

			default :
				throw new IllegalArgumentException(String.format("Invalid target: '%s'.", target));
		}
	}

	public static int getGravity(SlideNavigation.Target target)
	{
		switch (target)
		{
			case Left :
				return GravityCompat.START;

			case Right :
				return GravityCompat.END;

			default :
				throw new IllegalArgumentException(String.format("Target '%s' does not have an associated gravity.", target.name()));
		}
	}

	public Size getSize(Activity activity, SlideNavigation.Target target, ComponentParameters params)
	{
		ILayoutDefinition layout = UIObjectCall.getLayoutFromViewDefinition(params.Object, params.Mode);
		if (target == SlideNavigation.Target.Left || target == SlideNavigation.Target.Right)
		{
			int drawerWidth = (int)activity.getResources().getDimension(R.dimen.drawer_width);
			int drawerHeight = AdaptersHelper.getWindowHeight(activity, layout);
			if (layout == null || !layout.getEnableHeaderRowPattern()) {
				// if not HR add status and action bar size for drawer
				drawerHeight += AdaptersHelper.getStatusAndActionBarHeight(activity, layout);
				// subtract status bar if not overlayint it.
				if (layout!=null && !layout.getEnableHeaderRowPattern())
					drawerHeight -= AdaptersHelper.getStatusBarHeight(activity);
			}
			mDrawerSize = new Size(drawerWidth, drawerHeight);
			return mDrawerSize;
		}
		else
		{
			int sizeWidth = AdaptersHelper.getWindowWidth(activity);
			int sizeHeight = AdaptersHelper.getWindowHeight(activity, layout);
			mContentSize =  new Size(sizeWidth, sizeHeight);
			return mContentSize;
		}
	}

	public void putFragment(SlideNavigation.Target target, IDataView fragment)
	{
		mFragments.put(target, fragment);
	}

	public IDataView getFragment(SlideNavigation.Target target)
	{
		return mFragments.get(target);
	}

	public void saveStateTo(LayoutFragmentActivityState outState)
	{
		saveDrawerStateTo(SlideNavigation.Target.Left, outState);
		saveDrawerStateTo(SlideNavigation.Target.Right, outState);
	}

	private void saveDrawerStateTo(SlideNavigation.Target target, LayoutFragmentActivityState outState)
	{
		String key = String.format(STATE_DRAWER_OPEN_FORMAT, target);
		//noinspection ResourceType
		boolean value = mDrawerLayout.isDrawerOpen(getGravity(target));
		outState.setProperty(key, value);
	}

	public void restoreStateFrom(LayoutFragmentActivityState inState)
	{
		boolean wasDrawerOpenLeft = restoreDrawerStateFrom(SlideNavigation.Target.Left, inState);
		boolean wasDrawerOpenRight = restoreDrawerStateFrom(SlideNavigation.Target.Right, inState);
		// set content active to true if drawer is closed
		if (!wasDrawerOpenLeft && !wasDrawerOpenRight && inState!=null)
		{
			IDataView mContentFragment = mFragments.get(SlideNavigation.Target.Content);
			if (mContentFragment != null)
				mContentFragment.setActive(true);
		}
	}

	private boolean restoreDrawerStateFrom(SlideNavigation.Target target, LayoutFragmentActivityState inState)
	{
		IDataView targetDataView = mFragments.get(target);
		if (targetDataView != null)
		{
			if (inState != null)
			{
				String key = String.format(STATE_DRAWER_OPEN_FORMAT, target);
				boolean wasDrawerOpen = inState.getBooleanProperty(key, false);
				targetDataView.setActive(wasDrawerOpen);
				return wasDrawerOpen;
			}
			else
			{
				// Drawer is closed by default.
				targetDataView.setActive(false);
			}
		}
		return false;
	}
}
