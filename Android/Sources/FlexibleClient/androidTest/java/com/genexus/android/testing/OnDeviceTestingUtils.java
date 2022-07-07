package com.genexus.android.testing;

import java.util.Locale;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.genexus.android.core.base.metadata.layout.ControlInfo;
import com.genexus.android.core.base.metadata.layout.LayoutDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;

public class OnDeviceTestingUtils {
    @SuppressWarnings("deprecation")
    public static void setLocale(Context context, Locale locale) {
        // Update the locale for date formatters
        Locale.setDefault(locale);
        // Update the locale for app resources
        Resources res = context.getResources();
        Configuration config = res.getConfiguration();
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
    }
}
