package com.genexus.android.payments.hms;

import android.content.Context;

import com.genexus.android.core.externalapi.ExternalApiDefinition;
import com.genexus.android.core.externalapi.ExternalApiFactory;
import com.genexus.android.core.framework.GenexusModule;
import com.genexus.android.hms.HMSHelper;


public class HmsInAppBillingModule implements GenexusModule {

	@Override
	public void initialize(Context context) {
		// replace definition, only if HMS is enable and GPS not available
		if (HMSHelper.isHmsAvailable(context) && !HMSHelper.isGmsAvailable(context))
			ExternalApiFactory.addApi(new ExternalApiDefinition(StoreManager.OBJECT_NAME, StoreManager.class));
	}
}
