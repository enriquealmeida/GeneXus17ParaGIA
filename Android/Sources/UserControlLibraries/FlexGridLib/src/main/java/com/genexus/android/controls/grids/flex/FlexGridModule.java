package com.genexus.android.controls.grids.flex;

import android.content.Context;

import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.framework.GenexusModule;
import com.genexus.android.core.usercontrols.TableLayoutFactory;
import com.genexus.android.core.usercontrols.UcFactory;
import com.genexus.android.core.usercontrols.UserControlDefinition;

public class FlexGridModule implements GenexusModule {

	@Override
	public void initialize(Context context) {
		UcFactory.addControl(new UserControlDefinition(FlexGridView.NAME, FlexGridView.class));
		Services.Application.getTableLayoutFactory().addControl(TableLayoutFactory.FLEX, GxFlexLayout.class);
		Services.Application.getTableLayoutFactory().addControl(TableLayoutFactory.ROOTFLEX, GxRootFlexLayout.class);
	}
}
