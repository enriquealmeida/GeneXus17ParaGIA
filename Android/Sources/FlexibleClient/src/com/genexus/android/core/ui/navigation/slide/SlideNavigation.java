package com.genexus.android.core.ui.navigation.slide;

import android.content.Intent;

import com.genexus.android.core.activities.GenexusActivity;
import com.genexus.android.core.app.ComponentParameters;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.DashboardMetadata;
import com.genexus.android.core.base.metadata.IDataViewDefinition;
import com.genexus.android.core.base.metadata.IViewDefinition;
import com.genexus.android.core.base.metadata.settings.PlatformDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.PlatformHelper;
import com.genexus.android.core.ui.navigation.CallOptionsHelper;
import com.genexus.android.core.ui.navigation.CallTarget;
import com.genexus.android.core.ui.navigation.NavigationController;
import com.genexus.android.core.ui.navigation.NavigationType;

public class SlideNavigation implements NavigationType
{
	static final String INTENT_EXTRA_IS_HUB_CALL = "com.artech.ui.navigation.slide.isHub";

	static final CallTarget TARGET_LEFT = new CallTarget("Left", "Target[1]", "Slide[1]");
	static final CallTarget TARGET_CONTENT = new CallTarget("Center", "Content", "Detail", "Target[2]", "Slide[2]");
	static final CallTarget TARGET_RIGHT = new CallTarget("Right", "Target[3]", "Slide[3]");

	public enum Target
	{
		Left,
		Content,
		Right
	}

	@Override
	public boolean isNavigationFor(GenexusActivity activity, IViewDefinition mainView)
	{
		return (PlatformHelper.getNavigationStyle() == PlatformDefinition.NAVIGATION_SLIDE);
	}

	@Override
	public NavigationController createController(GenexusActivity activity, IViewDefinition mainView)
	{
		return new SlideNavigationController(activity);
	}

	static SlideComponents getComponents(Intent intent, ComponentParameters intentParams)
	{
		SlideComponents slide = new SlideComponents();

		// TODO: @fpanizza: It's unclear why this comparison is being done and why if they are not
		// equal, then a new ComponentParameters with the main's DataView data is created.
		if (intentParams.Object == Services.Application.get().getMain())
		{
			// Main is always a hub.
			slide.IsHub = true;

			if (Services.Application.get().getSlidePositionRight())
			{
				slide.IsRightMainComponent = true;

				// Call to main component. Show as right drawer.
				slide.set(Target.Right, intentParams);
				slide.PendingAction = getPendingAction(intentParams.Object);
			}
			else
			{
				slide.IsLeftMainComponent = true;

				// Call to main component. Show as left drawer.
				slide.set(Target.Left, intentParams);
				slide.PendingAction = getPendingAction(intentParams.Object);
			}

		}
		else
		{
			if (Services.Application.get().getSlidePositionRight())
			{
				// Call to another object. Show main on right.
				slide.set(Target.Right, new ComponentParameters(Services.Application.get().getMain()));

				slide.set(Target.Content, intentParams);
				slide.IsRightMainComponent = false;

				if (CallTarget.BLANK.isTarget(CallOptionsHelper.getCurrentCallOptions(intent)))
					slide.set(Target.Right, null); // Don't show main on left if a "new window" target was specified.
			}
			else
			{
				// Call to another object. Show main on left.
				slide.set(Target.Left, new ComponentParameters(Services.Application.get().getMain()));

				slide.set(Target.Content, intentParams);
				slide.IsLeftMainComponent = false;

				if (CallTarget.BLANK.isTarget(CallOptionsHelper.getCurrentCallOptions(intent)))
					slide.set(Target.Left, null); // Don't show main on left if a "new window" target was specified.

			}
			// Is this a call to hub view?
			if (intent != null && intent.getExtras() != null && intent.getBooleanExtra(INTENT_EXTRA_IS_HUB_CALL, false))
				slide.IsHub = true;
		}

		return slide;
	}

	private static ActionDefinition getPendingAction(IViewDefinition intentView)
	{
		ActionDefinition slideStart = intentView.getEvent("Slide.Start");
		if (slideStart != null)
			return slideStart;

		if (intentView instanceof DashboardMetadata)
		{
			// Dashboard -> Try to execute first option as content.
			DashboardMetadata dashboard = (DashboardMetadata)intentView;
			if (dashboard.getItems().size() != 0)
				return dashboard.getItems().get(0).getActionDefinition();
		}
		else if (intentView instanceof IDataViewDefinition)
		{
			// Panel -> Try to execute 'GxStart' as content (for compatibility).
			return intentView.getEvent("GxStart");
		}

		return null;
	}
}
