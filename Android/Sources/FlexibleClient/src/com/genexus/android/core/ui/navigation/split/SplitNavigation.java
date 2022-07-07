package com.genexus.android.core.ui.navigation.split;

import android.content.res.Configuration;

import com.genexus.android.core.activities.GenexusActivity;
import com.genexus.android.core.base.metadata.IViewDefinition;
import com.genexus.android.core.base.metadata.WWListDefinition;
import com.genexus.android.core.base.metadata.settings.PlatformDefinition;
import com.genexus.android.core.base.utils.PlatformHelper;
import com.genexus.android.core.ui.navigation.NavigationController;
import com.genexus.android.core.ui.navigation.NavigationType;

/**
 * Split Navigation (only enabled for WWSD list/detail for now).
 */
public class SplitNavigation implements NavigationType
{
	@Override
	public boolean isNavigationFor(GenexusActivity activity, IViewDefinition mainView)
	{
		return (PlatformHelper.getNavigationStyle() == PlatformDefinition.NAVIGATION_SPLIT &&
				isValidConfigurationForSplit(activity.getResources().getConfiguration()) &&
				mainView instanceof WWListDefinition);
	}

	private static boolean isValidConfigurationForSplit(Configuration configuration)
	{
		return (configuration != null &&
				configuration.smallestScreenWidthDp >= PlatformDefinition.SMALLEST_WIDTH_DP_TABLET);
		// 		&& configuration.orientation == Configuration.ORIENTATION_LANDSCAPE); ?
	}

	@Override
	public NavigationController createController(GenexusActivity activity, IViewDefinition mainView)
	{
		return new SplitNavigationController(activity, mainView);
	}
}
