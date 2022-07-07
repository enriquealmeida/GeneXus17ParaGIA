package com.genexus.android.payments.mercadopago;

import android.content.Context;

import com.genexus.android.core.externalapi.ExternalApiDefinition;
import com.genexus.android.core.externalapi.ExternalApiFactory;
import com.genexus.android.core.framework.GenexusModule;

public class MercadoPagoModule implements GenexusModule {
	@Override
	public void initialize(Context context) {
		ExternalApiFactory.addApi(new ExternalApiDefinition(MercadoPagoAPI.OBJECT_NAME, MercadoPagoAPI.class, false));
	}
}
