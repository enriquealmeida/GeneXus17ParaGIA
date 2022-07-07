package com.genexus.android.controls.cardscanner;

import android.content.Context;

import com.genexus.android.core.externalapi.ExternalApiDefinition;
import com.genexus.android.core.externalapi.ExternalApiFactory;
import com.genexus.android.core.framework.GenexusModule;

/**
 * Card Scanner Module.
 * Supports the scan of credit cards with the camera.
 */
public class CardScannerModule implements GenexusModule
{
	@Override
	public void initialize(Context context)
	{
		ExternalApiFactory.addApi(new ExternalApiDefinition(CardScannerApi.OBJECT_NAME, CardScannerApi.class));
	}
}
