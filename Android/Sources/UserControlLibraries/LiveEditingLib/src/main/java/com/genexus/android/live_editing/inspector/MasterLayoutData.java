package com.genexus.android.live_editing.inspector;

import java.util.List;

import androidx.core.util.Pair;

public class MasterLayoutData {
    private final List<ControlData> mControls;
    private final String mAppClassName;
    private final String mAppBackgroundColor;
    private final int mWindowHeight;
    private final int mWindowWidth;

    public MasterLayoutData(List<ControlData> controls, String appClassName,
                            String appBackgroundColor, Pair<Integer, Integer> windowDimensions) {
        mControls = controls;
        mAppClassName = appClassName;
        mAppBackgroundColor = appBackgroundColor;
        mWindowHeight = windowDimensions.first;
        mWindowWidth = windowDimensions.second;
    }

    public List<ControlData> getControls() {
        return mControls;
    }

    public String getAppClassName() {
        return mAppClassName;
    }

    public String getAppBackgroundColor() {
        return mAppBackgroundColor;
    }

    public int getWindowHeight() {
        return mWindowHeight;
    }

    public int getWindowWidth() {
        return mWindowWidth;
    }
}
