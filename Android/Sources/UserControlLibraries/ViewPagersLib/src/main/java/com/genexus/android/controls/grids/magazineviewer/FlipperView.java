package com.genexus.android.controls.grids.magazineviewer;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;

public class FlipperView extends ViewPager {

	public FlipperView(Context context) {
		super(context);
	}
	
	public FlipperView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}


	public FlipperAdapter getFlipperAdapter() {
		return (FlipperAdapter) getAdapter();
	}
}
