package com.genexus.android.live_editing.inspector;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;

import com.genexus.android.layout.LayoutTag;
import com.genexus.android.core.fragments.IInspectableComponent;

class GxControlsVisitor {
    /**
     * Returns the genexus controls in order from the root views of components.
     */
    public static List<GxControl> fromActiveComponents(List<IInspectableComponent> activeComponents) {
        List<View> rootViews = new ArrayList<>(activeComponents.size());
        for (IInspectableComponent component : activeComponents) {
            if (component != null) {
                rootViews.add(component.getRootView());
            }
        }

        List<GxControl> controlsList = new ArrayList<>();
        int baseLevel = 0;
        int baseZ = 0;
        for (View rootView : rootViews) {
            controlsList.addAll(getGxControls(rootView, baseLevel, baseZ));
            baseLevel = getMaxLevel(controlsList) + 1;
            baseZ = getMaxZ(controlsList) + 1;
        }
        return controlsList;
    }

    /**
     * @param level indicates the depth in the view hierarchy tree.
     * @param z indicates which view should be drawn first.
     */
    private static List<GxControl> getGxControls(View view, int level, int z) {
        List<GxControl> res = new ArrayList<>();

        // Process current node.
        boolean isGxControl = isGxControl(view);
        if (isGxControl) {
            res.add(new GxControl(view, level, z));
        }

        // Process child nodes, if it has any.
        if (view instanceof ViewGroup) {
            int nextLevel = isGxControl ? level + 1 : level;
            int nextZ = isGxControl ? z + 1 : z;
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                res.addAll(getGxControls(viewGroup.getChildAt(i), nextLevel, nextZ + i));
            }
        }

        return res;
    }

    private static boolean isGxControl(View view) {
        if (view == null) {
            return false;
        }
        Boolean isGxControl = (Boolean) view.getTag(LayoutTag.CONTROL_GENEXUS);
        return isGxControl != null;
    }

    private static int getMaxLevel(List<GxControl> controls) {
        int maxLevel = 0;
        for (GxControl control : controls) {
            int level = control.getLevel();
            if (level > maxLevel) {
                maxLevel = level;
            }
        }
        return maxLevel;
    }

    private static int getMaxZ(List<GxControl> controls) {
        int maxZ = 0;
        for (GxControl control : controls) {
            int z = control.getZ();
            if (z > maxZ) {
                maxZ = z;
            }
        }
        return maxZ;
    }
}
