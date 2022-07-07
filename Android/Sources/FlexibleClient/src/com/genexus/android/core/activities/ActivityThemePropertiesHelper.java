package com.genexus.android.core.activities;

import android.app.Activity;

import com.genexus.android.core.base.metadata.ILayoutDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.fragments.IDataViewHost;
import com.genexus.android.core.fragments.LayoutFragmentActivity;

public class ActivityThemePropertiesHelper {
    public static void applyChanges(Activity activity) {
        ILayoutDefinition layoutDefinition = null;
        if (activity instanceof LayoutFragmentActivity) {
            layoutDefinition = ((IDataViewHost) activity).getMainLayout();
        } else if (activity instanceof IGxDashboardActivity) {
            layoutDefinition = (ILayoutDefinition) ((IGxDashboardActivity) activity).getDashboardDefinition();
        }
        ActivityHelper.applyStyle(activity, layoutDefinition);
    }

    public static void applyCurrentAppTheme(Activity activity) {
		int themeId = 0;
		String name = "";
		ThemeDefinition themeDefinition = Services.Themes.getCurrentTheme();
		if (themeDefinition != null) {
			name = themeDefinition.getResourceStyleName();
			themeId = activity.getResources().getIdentifier("ApplicationTheme" + name, "style", activity.getPackageName());
		}
        if (themeId == 0) {
        	if (themeDefinition != null && !name.equals(Services.Themes.calculateAppThemeName())) { // default theme doesn't have a specific resource
				Services.Log.error("ApplyAppTheme", "Failure to get " + name + " theme id. Try getting the default theme id");
			}
            themeId = activity.getResources().getIdentifier("ApplicationTheme", "style", activity.getPackageName());
        }
        if (themeId != 0) {
            activity.setTheme(themeId);
        } else {
            Services.Log.error("ApplyAppTheme", "Failure to get default theme id.");
        }
    }
}
