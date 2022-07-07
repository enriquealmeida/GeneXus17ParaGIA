package com.genexus.android.controls.grids.magazineviewer;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.*;

import com.genexus.android.core.base.metadata.layout.Size;
import com.genexus.android.core.controls.GxGradientDrawable;
import com.genexus.android.core.controls.grids.GridAdapter;
import com.genexus.android.core.controls.grids.GridHelper;

@SuppressWarnings("deprecation")
@SuppressLint("ViewConstructor")
public class PageLayoutView extends AbsoluteLayout {

	private IViewProvider mViews;
	private ArrayList<Integer> mLayout;
	private GridAdapter mGridAdapter;
	private GridHelper mHelper;
	private int mRowsPerColumn;

	public PageLayoutView(Context context, int rowsPerColumn, ArrayList<Integer> layout,
						  IViewProvider viewProvider, Size size, GridAdapter adapter,
						  int initialView, GridHelper helper)
	{
		super(context);
		mViews = viewProvider;
		mLayout = layout;
		mGridAdapter = adapter;
		mHelper = helper;
		int headerHeight = 0;
		int columns = mLayout.size();
		int availableHeight = size.getHeight();
		int wScreen = size.getWidth();
		int nextView = initialView;
		int currentX = 0;

		int cellWidth = wScreen / columns;
		mRowsPerColumn = rowsPerColumn;

		if (mRowsPerColumn > 0) {
			int currentY = headerHeight;
			int rows = mRowsPerColumn;
			for (int r = 0; r < rows; r++) {
				currentX = 0;
				for (int c = 0; c < columns; c++) {
					if (nextView >= mViews.size())
						break;
					addItemView(nextView, cellWidth, availableHeight / rows, currentX, currentY);
					currentX += cellWidth;
					nextView++;
				}
				currentY += availableHeight / rows;
			}
		} else {
			for (int c = 0; c < columns; c++) {
				int currentY = headerHeight;
				int rows = mLayout.get(c);
				for (int r = 0; r < rows ; r++)
				{
					if (nextView >= mViews.size())
						break;

					addItemView(nextView, cellWidth, availableHeight / rows, currentX, currentY);
					currentY += availableHeight / rows;
					nextView++;
				}
				currentX += cellWidth;
			}
		}
	}

	private void addItemView(int viewId, int width, int height, int x, int y) {
		View view = mViews.getView(viewId, width, height);
		GxGradientDrawable back = new GxGradientDrawable();
		back.setColor(Color.TRANSPARENT);
		//remove stroke from magazine viewer
		//back.setStroke(1, Color.LTGRAY);
		view.setBackgroundDrawable(back);
		view.setTag(viewId);
		view.setOnClickListener(onClickView);
		if (mHelper.getDefinition().gridUserControlSupportAutoGrow()
				&& mHelper.getDefinition().hasAutoGrow())
			addView(view, new AbsoluteLayout.LayoutParams(width, LayoutParams.WRAP_CONTENT, x, y));
		else
			addView(view, new AbsoluteLayout.LayoutParams(width, height, x, y));


	}

	OnClickListener onClickView = new OnClickListener(){
		@Override
		public void onClick(View v) {
			mGridAdapter.selectIndex((Integer) v.getTag(), true);
			mGridAdapter.runDefaultAction( (Integer) v.getTag());
		}
	};

	public void setRowsPerColumn(int rowsPerColumn)
	{
		mRowsPerColumn = rowsPerColumn;
	}

	public Iterable<View> getPageItems()
	{
		// Should be kept consistent with addItemView().
		// For example, if an intermediate container view is added there, it should be ignored here.
		List<View> pageItems = new ArrayList<>();
		for (int i = 0; i < getChildCount(); i++)
			pageItems.add(getChildAt(i));

		return pageItems;
	}
}
