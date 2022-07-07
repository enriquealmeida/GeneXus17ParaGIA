package com.genexus.android.controls.grids.magazineviewer;

import android.os.Parcelable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.genexus.android.core.base.metadata.layout.Size;

public class FlipperAdapter extends PagerAdapter
{
	private final IFlipDataSource mDataSource;
	private Size mSize;

	public FlipperAdapter(IFlipDataSource ds, Size size)
	{
		mDataSource = ds;
		mSize = size;
	}

	@Override
	public void notifyDataSetChanged()
	{
		mDataSource.resetNumberOfPages();
		super.notifyDataSetChanged();
	}

	public void setNewSize(Size newSize)
	{
		mSize = newSize;
	}

	@Override
	public int getCount()
	{
		return mDataSource.getNumberOfPages();
	}

	@Override
	public int getItemPosition(Object object)
	{
		// Return POSITION_NONE so that the view is recreated by ViewPager (after updating data and calling notifyDataSetChanged()).
		return POSITION_NONE;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void startUpdate(View arg0) { }

	@SuppressWarnings("deprecation")
	@Override
	public Object instantiateItem(View container, int position)
	{
		View itemView = mDataSource.getViewForPage(position, mSize);
		((ViewPager)container).addView(itemView, 0);
		return itemView;
	}

	@Override
	public boolean isViewFromObject(View view, Object object)
	{
	 	return view == object;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void destroyItem(View container, int idx, Object view)
	{
		((ViewPager)container).removeView((View) view);
		mDataSource.destroyPageView(idx, (View)view);
	}

	@Override
	public void finishUpdate(ViewGroup container) {
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) { }

	@Override
	public Parcelable saveState() { return null; }
}
