package com.genexus.android.core.ui.navigation.slide;

import java.util.HashMap;

import com.genexus.android.core.app.ComponentParameters;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.fragments.LayoutFragmentActivityState;
import com.genexus.android.core.utils.Cast;

@SuppressWarnings("checkstyle:MemberName")
class SlideComponents {
	public boolean IsHub;
	private final HashMap<SlideNavigation.Target, ComponentParameters> mComponents = new HashMap<>();
	public boolean IsLeftMainComponent;
	public boolean IsRightMainComponent;
	public ActionDefinition PendingAction;

	private static final String STATE_KEY = "Gx::SlideNavigation::Components";

	public ComponentParameters get(SlideNavigation.Target target) {
		return mComponents.get(target);
	}

	public void set(SlideNavigation.Target target, ComponentParameters params) {
		mComponents.put(target, params);
	}

	public void saveTo(LayoutFragmentActivityState state) {
		state.setProperty(STATE_KEY, this);
	}

	public static SlideComponents readFrom(LayoutFragmentActivityState state) {
		if (state == null) {
			return null;
		}
		return Cast.as(SlideComponents.class, state.getProperty(STATE_KEY));
	}
}
