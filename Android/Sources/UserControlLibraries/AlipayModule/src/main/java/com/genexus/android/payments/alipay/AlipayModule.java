package com.genexus.android.payments.alipay;


import android.content.Context;

import com.genexus.android.core.externalapi.ExternalApiDefinition;
import com.genexus.android.core.externalapi.ExternalApiFactory;
import com.genexus.android.core.framework.GenexusModule;

public class AlipayModule implements GenexusModule {

    @Override
    public void initialize(Context context) {
        ExternalApiDefinition basicExternalObject = new ExternalApiDefinition(
                AlipayApi.NAME,
                AlipayApi.class
        );
        ExternalApiFactory.addApi(basicExternalObject);
    }

}
