package com.genexus.android.live_editing.inspector;

import android.view.View;

class GxControl {
    private final View mView;
    private final int mLevel;
    private final int mZ;

    public GxControl(View view, int level, int z) {
        mView = view;
        mLevel = level;
        mZ = z;
    }

    public View getView() {
        return mView;
    }

    public int getLevel() {
        return mLevel;
    }

    public int getZ() {
        return mZ;
    }
}
