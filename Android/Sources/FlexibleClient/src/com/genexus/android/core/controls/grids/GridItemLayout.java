package com.genexus.android.core.controls.grids;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;

import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.controls.GxListView;

public class GridItemLayout extends LinearLayout implements Checkable
{
	private View mParentGrid;
	private GridItemViewInfo mItemInfo;

	private int mPosition;
	private Entity mItem;

	public GridItemLayout(Context context)
	{
		super(context);
	}

	public GridItemLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public GridItemLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	GridItemViewInfo getItemInfo() { return mItemInfo; }

	void setData(GridItemViewInfo itemInfo, View parentGrid, int position, Entity item)
	{
		mItemInfo = itemInfo;
		mParentGrid = parentGrid;
		mPosition = position;
		mItem = item;

		if (mParentGrid instanceof ISupportsMultipleSelection && mItemInfo != null && mItemInfo.getCheckbox() != null)
			mItemInfo.getCheckbox().setOnClickListener(mOnCheckItem);
	}

	@Override
	public boolean hasFocusable()
	{
		// BUGFIX: A ListView item that contains focusable items does not fire its OnItemClickListener.
		// See https://code.google.com/p/android/issues/detail?id=3414
		// That's because AbsListView queries hasFocusable() for its direct descendants on each touch event.
		// Returning false here will "fix" this behavior, and APPARENTLY doesn't cause any side effects
		// (at least as tested in Android 2.3 - 5.1).
		if (getParent() instanceof GxListView)
		{
			if (((GxListView)getParent()).handlesClicksOn(this))
				return false;
		}

		return super.hasFocusable();
	}

	private OnClickListener mOnCheckItem = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			((ISupportsMultipleSelection)mParentGrid).setItemSelected(mPosition, !isChecked());
		}
	};

	public Entity getEntity()
	{
		return mItem;
	}

	@Override
	public void setChecked(boolean checked)
	{
		if (mItem != null && checked != mItem.isSelected())
			mItem.setIsSelected(checked);

		if (mItemInfo != null && mItemInfo.getCheckbox() != null)
			mItemInfo.getCheckbox().setChecked(checked);

		setSelected(checked);
	}

	@Override
	public boolean isChecked()
	{
		if (mItem != null)
			return mItem.isSelected();

		return false;
	}

	@Override
	public void toggle()
	{
		setChecked(!isChecked());
	}

	public int getPosition() {
		return mPosition;
	}

	public View getParentGrid() {
		return mParentGrid;
	}
}
