package com.genexus.android.core.ui.navigation;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;

import com.genexus.android.core.activities.GenexusActivity;
import com.genexus.android.core.base.metadata.IViewDefinition;
import com.genexus.android.core.ui.navigation.controllers.StandardNavigationController;
import com.genexus.android.core.ui.navigation.slide.SlideNavigation;
import com.genexus.android.core.ui.navigation.split.SplitNavigation;
import com.genexus.android.core.ui.navigation.tabbed.TabbedNavigation;

public class Navigation
{
	private static final ArrayList<NavigationType> NAVIGATION_TYPES;

	static
	{
		// Initialize known navigation controllers.
		NAVIGATION_TYPES = new ArrayList<>();
		NAVIGATION_TYPES.add(new SlideNavigation());
		NAVIGATION_TYPES.add(new TabbedNavigation());
		NAVIGATION_TYPES.add(new SplitNavigation());
	}

	public static void addNavigationType(NavigationType type)
	{
		NAVIGATION_TYPES.add(0, type);
	}

	public static NavigationController createController(GenexusActivity activity, IViewDefinition view)
	{
		for (NavigationType navigation : NAVIGATION_TYPES)
		{
			if (navigation.isNavigationFor(activity, view))
				return navigation.createController(activity, view);
		}

		return new StandardNavigationController(activity);
	}

	public static NavigationHandled handle(UIObjectCall call, Intent callIntent)
	{
		Activity hostActivity = call.getContext().getActivity();
		if (hostActivity instanceof INavigationActivity)
		{
			NavigationController controller = ((INavigationActivity)hostActivity).getNavigationController();
			if (controller != null)
				return controller.handle(call, callIntent);
		}

		return NavigationHandled.NOT_HANDLED;
	}
}
