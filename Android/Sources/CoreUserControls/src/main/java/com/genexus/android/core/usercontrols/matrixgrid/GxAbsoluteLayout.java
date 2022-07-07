package com.genexus.android.core.usercontrols.matrixgrid;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.*;


@SuppressWarnings("deprecation")
/***
 * This class is just an abstraction over AbsouluteLayout in case you need to customize some behavior 
 * of AbsouluteLayout. At this time is not used.
 * If you need to use AbsoluteLayout use this instead so that you can customize it and maintain the interface 
 * in future releases.
 */
public class GxAbsoluteLayout extends AbsoluteLayout {
	
	public GxAbsoluteLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		
	}
	
	public void addViewInLayout(View v, AbsoluteLayout.LayoutParams params) {
		super.addView(v, params);
	}
}
