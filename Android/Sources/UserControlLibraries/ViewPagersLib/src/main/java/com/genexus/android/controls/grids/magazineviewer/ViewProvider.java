package com.genexus.android.controls.grids.magazineviewer;

import android.view.View;

import com.genexus.android.core.controls.grids.GridAdapter;

public class ViewProvider implements IViewProvider {

	private GridAdapter mAdapter;
	
	public ViewProvider(GridAdapter adapter) {
		mAdapter = adapter;
	}
	
	@Override
	public View getView(int index, int width, int height) {
		mAdapter.setBounds(width, height);
		return mAdapter.getView(index, null, null);
	}

	@Override
	public int size() {
		return mAdapter.getCount();
	}

}
