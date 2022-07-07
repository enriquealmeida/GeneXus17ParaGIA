package com.genexus.android.controls.grids.magazineviewer;

import android.view.View;

public interface IViewProvider {

	View getView(int index, int width, int height);
	int size();
}
