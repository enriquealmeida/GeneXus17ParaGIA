package com.genexus.android.printer;

import android.content.Context;

import com.genexus.android.core.externalapi.ExternalApiDefinition;
import com.genexus.android.core.externalapi.ExternalApiFactory;
import com.genexus.android.core.framework.GenexusModule;

public class PrinterModule implements GenexusModule {

    @Override
    public void initialize(Context context) {
        ExternalApiFactory.addApi(new ExternalApiDefinition(PrinterAPI.OBJECT_NAME, PrinterAPI.class, false));
    }
}
