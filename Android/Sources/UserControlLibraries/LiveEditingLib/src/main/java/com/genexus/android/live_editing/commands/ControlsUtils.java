package com.genexus.android.live_editing.commands;

import java.util.List;

import android.app.Activity;
import androidx.annotation.NonNull;

import com.genexus.android.ViewHierarchyVisitor;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.metadata.theme.TransformationDefinition;
import com.genexus.android.core.controls.IGxLocalizable;
import com.genexus.android.core.controls.IGxThemeable;

class ControlsUtils {
    public static List<IGxThemeable> getThemeableControls(Activity activity) {
        return ViewHierarchyVisitor.getViews(IGxThemeable.class, activity.getWindow().getDecorView());
    }

    public static List<IGxLocalizable> getLocalizableControls(Activity activity) {
        return ViewHierarchyVisitor.getViews(IGxLocalizable.class, activity.getWindow().getDecorView());
    }

    public static List<IGxThemeable> getControlsWithThemeClass(Activity activity) {
        return CollectionUtils.filter(getThemeableControls(activity), new ControlsUtils.HasAThemeClass());
    }

    public static List<IGxThemeable> getControlsWithThemeClassName(Activity activity, String themeClassName) {
        return CollectionUtils.filter(getThemeableControls(activity), new ControlsUtils.IsThemeClass(themeClassName));
    }

    public static List<IGxThemeable> getControlsWithTransformationName(Activity activity, String transformationName) {
        return CollectionUtils.filter(ControlsUtils.getThemeableControls(activity), new ControlsUtils.IsThemeTransformation(transformationName));
    }

    private static class HasAThemeClass implements CollectionUtils.Predicate<IGxThemeable> {
        @Override
        public boolean apply(@NonNull IGxThemeable control) {
            return control.getThemeClass() != null;
        }
    }

    private static class IsThemeClass implements CollectionUtils.Predicate<IGxThemeable> {
        private final String mThemeClassName;

        public IsThemeClass(@NonNull String themeClassName) {
            mThemeClassName = themeClassName;
        }

        @Override
        public boolean apply(@NonNull IGxThemeable control) {
            ThemeClassDefinition oldDefinition = control.getThemeClass();
            return oldDefinition != null && mThemeClassName.equals(oldDefinition.getName());
        }
    }

    private static class IsThemeTransformation implements CollectionUtils.Predicate<IGxThemeable> {
        private final String mThemeTransformationName;

        public IsThemeTransformation(@NonNull String themeTransformationName) {
            mThemeTransformationName = themeTransformationName;
        }

        @Override
        public boolean apply(@NonNull IGxThemeable control) {
            ThemeClassDefinition themeClassDefinition = control.getThemeClass();
            if (themeClassDefinition == null) {
                return false;
            }
            TransformationDefinition transformationDefinition = themeClassDefinition.getTransformation();
            return transformationDefinition != null && mThemeTransformationName.equals(transformationDefinition.getName());
        }
    }
}
