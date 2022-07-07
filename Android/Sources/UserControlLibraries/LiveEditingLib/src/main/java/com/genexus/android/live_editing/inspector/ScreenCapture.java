package com.genexus.android.live_editing.inspector;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import androidx.annotation.UiThread;
import androidx.core.util.Pair;
import android.view.View;

import com.genexus.android.core.activities.ActivityHelper;
import com.genexus.android.layout.LayoutTag;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.fragments.IInspectableComponent;
import com.genexus.android.core.ui.navigation.INavigationActivity;
import com.genexus.android.core.ui.navigation.NavigationController;

class ScreenCapture {
	/**
	 * This method MUST be called in the UI thread since it accesses views.
	 * Otherwise a ViewRootImpl$CalledFromWrongThreadException will be encountered.
	 */
	@UiThread
	public static MasterLayoutData createMasterLayoutData(Activity activity) {
		List<IInspectableComponent> activeComponents = new ArrayList<>();

		if (activity instanceof INavigationActivity) {
			NavigationController navigationController = ((INavigationActivity) activity).getNavigationController();
			activeComponents = navigationController.getActiveComponents();
		} else if (activity instanceof IInspectableComponent) { // FiltersActivity, etc
			activeComponents.add((IInspectableComponent) activity);
			ActivityHelper.addApplicationBarComponent(activity, activeComponents);
		}

		List<GxControl> controlsList = GxControlsVisitor.fromActiveComponents(activeComponents);
		List<ControlData> controls = createControlsDataList(controlsList);
		Pair<Integer, Integer> windowDimens = ActivityHelper.getAppUsableScreenSize(activity);

		ThemeClassDefinition appClass = Services.Themes.getApplicationClass();
		String appClassName = appClass != null ? appClass.getName() : "";
		String appBackgroundColor = appClass != null ? appClass.getBackgroundColor() : "";

		return new MasterLayoutData(controls, appClassName, appBackgroundColor, windowDimens);
	}

	private static List<ControlData> createControlsDataList(List<GxControl> controls) {
		List<ControlData> controlsData = new ArrayList<>();

		for (GxControl control : controls) {
			control.getView().setAlpha(0f);
		}

		for (GxControl control : controls) {
			View controlView = control.getView();
			LayoutItemDefinition controlDefinition = (LayoutItemDefinition) controlView.getTag(LayoutTag.CONTROL_DEFINITION);
			ThemeClassDefinition controlThemeClassDef = (ThemeClassDefinition) controlView.getTag(LayoutTag.CONTROL_THEME_CLASS);
			int level = control.getLevel();
			int z = control.getZ();

			ControlData controlData = new ControlData(controlView, controlDefinition, controlThemeClassDef, level, z);
			controlsData.add(controlData);
		}

		for (GxControl control : controls) {
			control.getView().setAlpha(1f);
		}

		return controlsData;
	}
}
