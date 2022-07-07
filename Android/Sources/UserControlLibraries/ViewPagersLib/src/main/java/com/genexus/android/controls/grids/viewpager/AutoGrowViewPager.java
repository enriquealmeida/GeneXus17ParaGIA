package com.genexus.android.controls.grids.viewpager;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class AutoGrowViewPager extends ViewPager
{
	private boolean autoGrow = false;

	public AutoGrowViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public void setAutoGrow(boolean autoGrow)
	{
		this.autoGrow = autoGrow;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		if (autoGrow)
		{
			// from: https://mobikul.com/viewpager/

			super.onMeasure(widthMeasureSpec, heightMeasureSpec);  // super has to be called in the beginning so the child views can be initialized.

			int height = 0;
			for (int i = 0; i < getChildCount(); i++) {
				View child = getChildAt(i);
				child.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
				int h = child.getMeasuredHeight();
				if (h > height) height = h;
			}

			heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
		}
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
