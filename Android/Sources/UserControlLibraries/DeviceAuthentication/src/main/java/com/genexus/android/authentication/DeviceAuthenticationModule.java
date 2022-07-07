package com.genexus.android.authentication;

import android.content.Context;

import com.genexus.android.core.externalapi.ExternalApiDefinition;
import com.genexus.android.core.externalapi.ExternalApiFactory;
import com.genexus.android.core.framework.GenexusModule;

public class DeviceAuthenticationModule implements GenexusModule {
    @Override
    public void initialize(Context context)
    {
        ExternalApiFactory.addApi(new ExternalApiDefinition(DeviceAuthenticationAPI.OBJECT_NAME, DeviceAuthenticationAPI.class));
    }
}
