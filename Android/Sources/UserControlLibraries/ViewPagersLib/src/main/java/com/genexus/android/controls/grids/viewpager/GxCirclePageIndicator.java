package com.genexus.android.controls.grids.viewpager;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.genexus.android.controls.grids.magazineviewer.FlipperOptions;
import com.genexus.android.controls.viewpagerindicator.CirclePageIndicator;

public class GxCirclePageIndicator extends CirclePageIndicator
{
	private ViewPager mViewPager;

    public GxCirclePageIndicator(Context context)
    {
        super(context);
    }

    public GxCirclePageIndicator(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

	public GxCirclePageIndicator(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public void setViewPager(ViewPager view)
	{
		super.setViewPager(view);
		mViewPager = view;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		// Workaround for bug in CirclePageIndicator: crashes on a ViewPager with no adapter.
		if (mViewPager != null && mViewPager.getAdapter() != null)
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		else
			setMeasuredDimension(0, 0);
	}

	public void setOptions(FlipperOptions options)
	{
		// Default colors.
		setFillColor(0xFFCCCCCC);   // Light gray
		setStrokeColor(0xFF888888); // Gray

		// Custom colors.
		if (options.getFooterBackgroundColor() != null)
			setBackgroundColor(options.getFooterBackgroundColor());

		if (options.getFooterSelectedColor() != null)
			setFillColor(options.getFooterSelectedColor());

		if (options.getFooterUnselectedColor() != null)
			setStrokeColor(options.getFooterUnselectedColor());

		// Footer visible?
		if (options.isShowFooter())
			setVisibility(View.VISIBLE);
		else
			setVisibility(View.GONE);
	}
}
