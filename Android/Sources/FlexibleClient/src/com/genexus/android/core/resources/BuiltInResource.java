package com.genexus.android.core.resources;

public class BuiltInResource {
    private final String mName;
    private final int mDarkResource;
    private final int mLightResource;
    private final boolean mShouldAutomirror;

    public BuiltInResource(String name, int darkResource, int lightResource, boolean shouldAutomirror) {
        mName = name;
        mDarkResource = darkResource;
        mLightResource = lightResource;
        mShouldAutomirror = shouldAutomirror;
    }

    public String getName() {
        return mName;
    }

    public int getDarkResource() {
        return mDarkResource;
    }

    public int getLightResource() {
        return mLightResource;
    }

    public boolean shouldAutomirror() {
        return mShouldAutomirror;
    }
}
