package com.genexus.android.controls.grids.smart;

import android.content.Context;

import com.genexus.android.core.framework.GenexusModule;
import com.genexus.android.core.usercontrols.UcFactory;
import com.genexus.android.core.usercontrols.UserControlDefinition;

public class SmartGridModule implements GenexusModule {

	@Override
	public void initialize(Context context) {
		UserControlDefinition uc = new UserControlDefinition(GxSmartGrid.NAME, GxSmartGrid.class);
		uc.SupportAutoGrow = true;
		UcFactory.addControl(uc);
	}
}
