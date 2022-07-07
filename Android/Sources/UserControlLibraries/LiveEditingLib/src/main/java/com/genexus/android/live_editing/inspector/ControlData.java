package com.genexus.android.live_editing.inspector;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.View;

import com.genexus.android.core.base.metadata.layout.LayoutDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.maps.common.IGxMapView;

public class ControlData {
    //TODO: Should refactor this class to make these fields final.
    private String mName;
    private String mThemeClassName;
    private String mLayoutId;
    private String mParentObjectName;
    private boolean mIsVisible;
    private int mLevel;
    private int mActualHeight;
    private int mActualWidth;
    private int mPosX;
    private int mPosY;
    private int mPosZ;
    private int mOriginalHeight;
    private int mOriginalPosY;
    private @Nullable Bitmap mBitmap;

    public ControlData(@NonNull View view, LayoutItemDefinition controlDefinition,
                       ThemeClassDefinition controlThemeClassDef, int level, int posZ) {
        setControlInfo(controlDefinition, controlThemeClassDef);
        setDimensions(view);
        setPosition(view, level, posZ);
        setBitmap(view);
    }

    public String getName() {
        return mName;
    }

    public String getThemeClassName() {
        return mThemeClassName;
    }

    public String getLayoutId() {
        return mLayoutId;
    }

    public String getParentObjectName() {
        return mParentObjectName;
    }

    public boolean isVisible() {
        return mIsVisible;
    }

    public int getLevel() {
        return mLevel;
    }

    public int getActualHeight() {
        return mActualHeight;
    }

    public int getActualWidth() {
        return mActualWidth;
    }

    public int getPosX() {
        return mPosX;
    }

    public int getPosY() {
        return mPosY;
    }

    public int getPosZ() {
        return mPosZ;
    }

    public int getOriginalHeight() {
        return mOriginalHeight;
    }

    public int getOriginalPosY() {
        return mOriginalPosY;
    }

    public @Nullable Bitmap getBitmap() {
        return mBitmap;
    }

    private void setControlInfo(LayoutItemDefinition controlDef,
                                ThemeClassDefinition controlThemeClassDef) {
        String controlName = Strings.EMPTY;
        String themeClassName = Strings.EMPTY;
        String layoutId = Strings.EMPTY;
        String objName = Strings.EMPTY;
        boolean isVisible = true;

        if (controlDef != null) {
            controlName = controlDef.getName();
            isVisible = controlDef.isVisible();
            LayoutDefinition layoutDef = controlDef.getLayout();
            if (layoutDef != null) {
                layoutId = layoutDef.getId();
                objName = layoutDef.getParent().getPattern().getName();
            } else {
                // If it doesn't have a layout, assume it's an application object
                // (e.g. Window, Application Bar, etc).
                objName = "<Application>";
            }
        } else  {
			// If it doesn't have a definition, assume it's an application object
			// (e.g. Menu Tab Control, etc).
			objName = "<Application>";
		}

        if (controlThemeClassDef != null) {
            themeClassName = controlThemeClassDef.getName();
        }

        mName = controlName;
        mThemeClassName = themeClassName;
        mIsVisible = isVisible;
        mLayoutId = layoutId;
        mParentObjectName = objName;
    }

    public void setDimensions(View view) {
        int actualHeight = Services.Device.pixelsToDips(view.getHeight());
        int actualWidth = Services.Device.pixelsToDips(view.getWidth());

        mActualHeight = actualHeight;
        mActualWidth = actualWidth;
        mOriginalHeight = actualHeight; // TODO: Intended to be used to display the auto-grow.
    }

    public void setPosition(View view, int level, int posZ) {
        int[] position = new int[2];

        view.getRootView().getLocationOnScreen(position);
        int rootPosXDips = Services.Device.pixelsToDips(position[0]);
        int rootPosYDips = Services.Device.pixelsToDips(position[1]);

        view.getLocationInWindow(position);
        int posXDips = Services.Device.pixelsToDips(position[0]) + rootPosXDips;
        int posYDips = Services.Device.pixelsToDips(position[1]) + rootPosYDips;

        mPosX = posXDips;
        mPosY = posYDips;
        mPosZ = posZ;
        mOriginalPosY = posYDips; // TODO: Intended to be used to display the auto-grow.
        mLevel = level;
    }

    public void setBitmap(View view) {
        int width = view.getWidth();
        int height = view.getHeight();

        if (width <= 0 || height <= 0) {
            return;
        }

        if (view instanceof IGxMapView) {
            return;
        }

        float alpha = view.getAlpha();
        view.setAlpha(1f);

        int widthInDips = Services.Device.pixelsToDips(width);
        int heightInDips = Services.Device.pixelsToDips(height);
        float scalingFactor = 1 / view.getResources().getDisplayMetrics().density;

        Bitmap bitmap = Bitmap.createBitmap(widthInDips, heightInDips, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.scale(scalingFactor, scalingFactor);
        view.draw(canvas);
        canvas.setBitmap(null);

        view.setAlpha(alpha);

        mBitmap = bitmap;
    }
}
