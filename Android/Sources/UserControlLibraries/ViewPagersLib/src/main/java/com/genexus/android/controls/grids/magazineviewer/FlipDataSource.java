package com.genexus.android.controls.grids.magazineviewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.genexus.android.core.base.metadata.layout.Size;
import com.genexus.android.core.controls.grids.GridAdapter;
import com.genexus.android.core.controls.grids.GridHelper;
import com.genexus.android.core.utils.Cast;

/* This implements a DataSource taking a common adapter and returning the pages depending on the options */
public class FlipDataSource implements IFlipDataSource
{
	private FlipperOptions mOptions;
	private final GridAdapter mAdapter;
	private int mNumberOfPages = -1;
	private Context mContext;
	private ViewProvider mViewProvider;
	private HashMap<Integer, ArrayList<Integer>> mLayouts = new LinkedHashMap<>();
	private final GridHelper mHelper;

	public FlipDataSource(Context context, FlipperOptions options, GridAdapter adapter, GridHelper helper)
	{
		mOptions = options;
		mAdapter = adapter;
		mContext = context;
		mViewProvider = new ViewProvider(adapter);
		mHelper = helper;
	}

	@Override
	public int getNumberOfPages() {
		if (mNumberOfPages <= 0) {
			mNumberOfPages = 0;
			int totalItems = mAdapter.getCount();
			if (totalItems > 0) {
				mNumberOfPages = totalItems / mOptions.getItemsPerPage();
				if (totalItems % mOptions.getItemsPerPage() != 0) {
					mNumberOfPages++;
				}
			}
		}
		return mNumberOfPages;
	}

	@Override
	public void resetNumberOfPages()
	{
		mNumberOfPages = -1;
	}

	@Override
	public View getViewForPage(int page, Size size) {
		if (page >= 0) {
			ArrayList<Integer> layout;
			if (mLayouts.containsKey(page))
				layout = mLayouts.get(page);
			else {
				layout = mOptions.getLayout();
				mLayouts.put(page, layout);
			}
			int nextView = page * mOptions.getItemsPerPage();
			PageLayoutView view = new PageLayoutView(mContext, mOptions.getRowsPerColumn(), layout, mViewProvider, size, mAdapter, nextView, mHelper);
			view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			return view;
		}
		return null;
	}

	@Override
	public void destroyPageView(int page, View pageView)
	{
		PageLayoutView pageLayoutView = Cast.as(PageLayoutView.class, pageView);
		if (pageLayoutView != null)
		{
			for (View pageItemView : pageLayoutView.getPageItems())
				mHelper.discardItemView(pageItemView);
		}
	}
}
