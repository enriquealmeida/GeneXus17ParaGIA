package com.genexus.android.controls.grids.magazineviewer;

import android.view.View;

import com.genexus.android.core.base.metadata.layout.Size;

public interface IFlipDataSource
{
	int getNumberOfPages();
	void resetNumberOfPages();

	View getViewForPage(int page, Size size);
	void destroyPageView(int page, View pageView);
}
