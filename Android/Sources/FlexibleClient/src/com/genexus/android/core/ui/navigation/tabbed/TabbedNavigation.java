package com.genexus.android.core.ui.navigation.tabbed;

import java.util.Arrays;
import java.util.Locale;

import com.genexus.android.core.activities.GenexusActivity;
import com.genexus.android.core.base.metadata.DashboardMetadata;
import com.genexus.android.core.base.metadata.IViewDefinition;
import com.genexus.android.core.base.metadata.settings.PlatformDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.PlatformHelper;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.ui.navigation.NavigationController;
import com.genexus.android.core.ui.navigation.NavigationType;

public class TabbedNavigation implements NavigationType
{
	static final int TAB_NONE = -1;

	private static final Integer[] COMPATIBLE_NAVIGATION_STYLES = new Integer[] { 
		PlatformDefinition.NAVIGATION_DEFAULT, PlatformDefinition.NAVIGATION_FLIP, PlatformDefinition.NAVIGATION_UNKNOWN };
	
	@Override
	public boolean isNavigationFor(GenexusActivity activity, IViewDefinition mainView)
	{
		return (Arrays.asList(COMPATIBLE_NAVIGATION_STYLES).contains(PlatformHelper.getNavigationStyle()) &&
				mainView instanceof DashboardMetadata &&
				((DashboardMetadata)mainView).getControl().equalsIgnoreCase(DashboardMetadata.CONTROL_TABS));
	}

	@Override
	public NavigationController createController(GenexusActivity activity, IViewDefinition mainView)
	{
		return new TabbedNavigationController(activity, (DashboardMetadata)mainView);
	}

	static int getTabPositionFromTargetName(String targetName)
	{
		if (Strings.hasValue(targetName))
		{
			targetName = targetName.toLowerCase(Locale.US);
			if (targetName.startsWith("tab[") && targetName.endsWith("]"))
				return Services.Strings.tryParseInt(targetName.substring(4, targetName.length() - 1), TAB_NONE);

			if (targetName.startsWith("target[") && targetName.endsWith("]"))
				return Services.Strings.tryParseInt(targetName.substring(7, targetName.length() - 1), TAB_NONE);
		}

		return TAB_NONE;
	}
}
