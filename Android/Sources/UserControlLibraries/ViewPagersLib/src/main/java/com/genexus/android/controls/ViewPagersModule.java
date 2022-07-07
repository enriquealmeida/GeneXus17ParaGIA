package com.genexus.android.controls;

import android.content.Context;

import com.genexus.android.core.framework.GenexusModule;
import com.genexus.android.core.usercontrols.UcFactory;
import com.genexus.android.core.usercontrols.UserControlDefinition;
import com.genexus.android.controls.grids.magazineviewer.GxHorizontalGrid;
import com.genexus.android.controls.grids.magazineviewer.GxMagazineViewer;
import com.genexus.android.controls.grids.viewpager.GxViewPager;


public class ViewPagersModule implements GenexusModule {

	@Override
	public void initialize(Context context) {
		UcFactory.addControl(new UserControlDefinition(GxMagazineViewer.NAME, GxMagazineViewer.class));
		UcFactory.addControl(new UserControlDefinition(GxViewPager.NAME, GxViewPager.class));
		UserControlDefinition uc = new UserControlDefinition(GxHorizontalGrid.NAME, GxHorizontalGrid.class);
		uc.SupportAutoGrow = true;
		UcFactory.addControl(uc);
	}
}
