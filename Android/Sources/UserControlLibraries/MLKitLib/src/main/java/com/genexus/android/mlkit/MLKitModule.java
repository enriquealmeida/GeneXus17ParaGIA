/*
               File: MLKitModule.java
        Description: External object registration module for ML Kit
             Author: Damián Salvia
       Generated on: April 17, 2019

       Copyright © 2019 GeneXus. All rights reserved.
*/

package com.genexus.android.mlkit;

import android.content.Context;

import com.genexus.android.core.externalapi.ExternalApiDefinition;
import com.genexus.android.core.externalapi.ExternalApiFactory;

import com.genexus.android.core.framework.GenexusModule;

public class MLKitModule implements GenexusModule {

	@Override
	public void initialize(Context context) {

		ExternalApiDefinition mlKitExternalObject = new ExternalApiDefinition(
				MLKit.OBJECT_NAME,
				MLKit.class
		);
		ExternalApiFactory.addApi(mlKitExternalObject);

	}
}
