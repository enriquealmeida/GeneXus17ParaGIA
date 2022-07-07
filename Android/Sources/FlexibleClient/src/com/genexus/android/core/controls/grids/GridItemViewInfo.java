package com.genexus.android.core.controls.grids;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.CheckBox;


import com.genexus.android.R;
import com.genexus.android.core.controls.GxTextView;
import com.genexus.android.layout.GxLayout;
import com.genexus.android.layout.LayoutTag;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.DataBoundControl;
import com.genexus.android.core.ui.GridItemCoordinator;
import com.genexus.android.core.utils.Cast;

import org.jetbrains.annotations.Nullable;

public class GridItemViewInfo
{
	private GridItemLayout mView;
	private ArrayList<View> mBoundViews = new ArrayList<>();
	private GridItemLayoutVersion mLayoutVersion;

	private Entity mData;
	private int mPosition;
	private boolean mSelected;
	private GridItemCoordinator mCoordinator;
	private GxLayout mHolder;
	private GxTextView mHeaderText;
	private CheckBox mCheckbox;

	public GridItemViewInfo(GridItemLayout view, GridItemLayoutVersion version, List<View> boundViews,
	                        GxLayout holder, String selectionExpression)
	{
		mView = view;
		mLayoutVersion = version;
		mBoundViews.addAll(boundViews);
		mHolder = holder;

		mHeaderText = view.findViewById(R.id.grid_item_header_text);
		mCheckbox = view.findViewById(R.id.grid_item_checkbox);
		String selectionFlag = getSelectionFlag(boundViews, selectionExpression);
		if (mCheckbox != null && selectionFlag != null) {
			mCheckbox.setTag(LayoutTag.GRID_SELECTION_CHECKBOX, true);
			mCheckbox.setTag(selectionFlag);
		}
	}

	GridItemCoordinator getCoordinator()
	{
		return mCoordinator;
	}

	void setCoordinator(GridItemCoordinator coordinator)
	{
		mCoordinator = coordinator;
	}

	public GridItemLayout getView() { return mView; }
	public List<View> getBoundViews() { return mBoundViews; }
	public GridItemLayoutVersion getVersion() { return mLayoutVersion; }

	public int getPosition() { return mPosition; }
	public boolean getSelected() { return mSelected; }
	public Entity getData() { return mData; }

	public GxLayout getHolder() { return mHolder; }
	public GxTextView getHeaderText() { return mHeaderText; }
	public CheckBox getCheckbox() { return mCheckbox; }

	public static GridItemViewInfo fromView(View view)
	{
		if (view == null)
			return null;

		return Cast.as(GridItemViewInfo.class, view.getTag(R.id.tag_grid_item_info));
	}

	void assignTo(View view)
	{
		view.setTag(R.id.tag_grid_item_info, this);
	}

	static void discard(View view)
	{
		view.setTag(R.id.tag_grid_item_info, null);
	}

	void setData(View grid, int position, Entity data, boolean selected)
	{
		setData(position, data, selected);
		mView.setData(this, grid, position, data);
	}

	void setData(int position, Entity data, boolean selected)
	{
		mData = data;
		mPosition = position;
		mSelected = selected;

		if (mCoordinator != null)
			mCoordinator.setData(data);

		// mView?
	}

	private @Nullable String getSelectionFlag(List<View> boundViews, String selectionExpression) {
		if (!Strings.hasValue(selectionExpression))
			return null;

		for (View v : boundViews) {
			if (v instanceof DataBoundControl) {
				String controlName = ((DataBoundControl) v).getDefinition().getName();
				if (controlName != null && controlName.toLowerCase().contains(selectionExpression.toLowerCase()))
					return controlName;
			}
		}

		return null;
	}
}
