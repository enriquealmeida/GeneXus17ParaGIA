package com.genexus.android.core.activities;

import androidx.annotation.UiThread;

public interface IGxThemeableActivity {
    @UiThread
    void applyCurrentTheme(boolean force);
}
