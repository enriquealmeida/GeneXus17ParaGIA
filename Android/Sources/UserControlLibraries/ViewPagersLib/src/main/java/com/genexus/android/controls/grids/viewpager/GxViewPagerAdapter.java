package com.genexus.android.controls.grids.viewpager;

import android.content.Context;
import android.os.Parcelable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

import com.genexus.android.core.base.metadata.layout.GridDefinition;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.controllers.ViewData;
import com.genexus.android.core.controls.grids.GridHelper;
import com.genexus.android.core.controls.grids.GridItemViewInfo;
import com.genexus.android.core.controls.grids.IGridAdapter;

class GxViewPagerAdapter extends PagerAdapter implements IGridAdapter
{
	public static final int SELECTED_INDEX_NONE = -1;

	private final GxViewPager mViewPager;
	private final GridHelper mHelper;
	private final GridDefinition mDefinition;

	private ViewData mViewData;
	private EntityList mData;
	private int mSelectedIndex;

	GxViewPagerAdapter(Context context, GxViewPager viewPager, GridHelper helper, GridDefinition definition)
	{
		super();
		mViewPager = viewPager;
		mHelper = helper;
		mDefinition = definition;
		mSelectedIndex = SELECTED_INDEX_NONE;
	}

	@Override
	public ViewData getData() { return mViewData; }

	@Override
	public int getCount()
	{
		return (mData != null ? mData.size() : 0);
	}

	@Override
	public Entity getEntity(int position)
	{
		return (mData != null ? mData.get(position) : null);
	}

	public int getSelectedIndex()
	{
		return mSelectedIndex;
	}

	void setData(ViewData data)
	{
		mViewData = data;
		mData = data.getEntities();
		notifyDataSetChanged();
	}

	void setCurrentItem(int position)
	{
		if (mData != null && position >= 0 && position < mData.size())
			mData.setCurrentItem(mData.get(position));
	}

	@SuppressWarnings("deprecation")
	@Override
	public Object instantiateItem(View collection, int position)
	{
		GridItemViewInfo itemView = mHelper.getItemView(this, position, null, false, false);
		View view = itemView.getView();
		//add the view to result
		((ViewPager)collection).addView(view, 0);
		view.setOnClickListener(mOnItemClickListener);
		return view;
	}

	private OnClickListener mOnItemClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			mViewPager.onItemClick(v);
		}
	};

	@SuppressWarnings("deprecation")
	@Override
	public void destroyItem(View collection, int position, Object view)
	{
		View itemView = (View)view;
		mHelper.discardItemView(itemView);
		((ViewPager)collection).removeView(itemView);
	}

	@Override
	public boolean isViewFromObject(View view, Object object)
	{
		return view == object;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) { }

	@Override
	public Parcelable saveState() { return null; }

	@Override
	public int getItemPosition(Object object)
	{
		// Return POSITION_NONE so that the view is recreated by ViewPager (after updating data and calling notifyDataSetChanged()).
		return POSITION_NONE;
	}

	public void selectIndex(int index, boolean fireSelectionChangedEvent)
	{
		if (mDefinition.getSelectionType() == GridDefinition.SelectionType.NoSelection)
		{
			// Can't select a grid item in this mode.
			return;
		}

		if (index < 0 || index >= getCount())
			return;

		Entity newSelection = getEntity(index);
		Entity previousSelection = getSelectedItem();

		boolean selectionChanged = false;

		if (newSelection != previousSelection)
		{
			if (!mDefinition.isMultipleSelectionEnabled()) {
				mSelectedIndex = index;
			}
			selectionChanged = true;

			if (fireSelectionChangedEvent)
				mHelper.getCoordinator().runControlEvent(mHelper.getGridView(), GridHelper.EVENT_SELECTION_CHANGED);
		}
		else
		{
			// Tapped on the selected item: Should it be deselected or remain selected?
			if (mDefinition.getSelectionType() == GridDefinition.SelectionType.KeepUntilNewSelection)
			{
				mSelectedIndex = SELECTED_INDEX_NONE;
				selectionChanged = true;
			}
		}

		// Force a re-layout, if necessary.
		if (selectionChanged && (mHelper.hasDifferentLayoutWhenSelected(newSelection) || mHelper.hasDifferentLayoutWhenSelected(previousSelection)))
			notifyDataSetChanged();
	}

	public void deselectIndex(int index, boolean fireSelectionChangedEvent)
	{
		if (mDefinition.getSelectionType() == GridDefinition.SelectionType.NoSelection)
		{
			// Can't deselect a grid item in this mode.
			return;
		}

		if (index < 0 || index >= getCount())
			return;

		boolean selectionChanged;
		Entity previousSelection = getSelectedItem();

		if (mSelectedIndex != SELECTED_INDEX_NONE) {
			mSelectedIndex = SELECTED_INDEX_NONE;
			selectionChanged = true;

			if (fireSelectionChangedEvent)
				mHelper.getCoordinator().runControlEvent(mHelper.getGridView(), GridHelper.EVENT_SELECTION_CHANGED);
		} else {
			selectionChanged = false;
		}

		// Force a re-layout, if necessary.
		if (selectionChanged && mHelper.hasDifferentLayoutWhenSelected(previousSelection))
			notifyDataSetChanged();
	}

	private Entity getSelectedItem()
	{
		if (mSelectedIndex >= 0 && mSelectedIndex < getCount())
			return getEntity(mSelectedIndex);
		else
			return null;
	}
}
