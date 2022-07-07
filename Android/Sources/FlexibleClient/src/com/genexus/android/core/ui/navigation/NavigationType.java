package com.genexus.android.core.ui.navigation;

import com.genexus.android.core.activities.GenexusActivity;
import com.genexus.android.core.base.metadata.IViewDefinition;

public interface NavigationType
{
	boolean isNavigationFor(GenexusActivity activity, IViewDefinition mainView);
	NavigationController createController(GenexusActivity activity, IViewDefinition mainView);
}
