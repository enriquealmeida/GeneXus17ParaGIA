package com.genexus.android.uitesting.utils;

import android.view.View;

import com.genexus.android.layout.LayoutTag;

public class GenexusModel {
    public static boolean isGenexusControl(View view) {
        Object isControlTag = view.getTag(LayoutTag.CONTROL_GENEXUS);
        if (!(isControlTag instanceof Boolean))
            return false;

        return (Boolean) isControlTag;
    }

    public static boolean isGridCheckbox(View view) {
        Object isControlTag = view.getTag(LayoutTag.GRID_SELECTION_CHECKBOX);
        if (!(isControlTag instanceof Boolean))
            return false;

        return (Boolean) isControlTag;
    }
}
