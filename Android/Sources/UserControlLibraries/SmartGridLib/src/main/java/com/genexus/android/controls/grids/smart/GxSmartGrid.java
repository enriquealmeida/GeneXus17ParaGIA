package com.genexus.android.controls.grids.smart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.SnapHelper;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.genexus.android.core.base.metadata.layout.ControlInfo;
import com.genexus.android.core.base.metadata.layout.GridDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.utils.Cast;

@SuppressLint("ViewConstructor")
public class GxSmartGrid extends GxRecyclerView {
    public static final String NAME = "SDSmartGrid";
    private int mOrientation;
    private int mSpanCount;
    private boolean mAutoGrow;

    public GxSmartGrid(Context context, Coordinator coordinator, GridDefinition gridDefinition) {
        super(context, coordinator, gridDefinition);
        mAutoGrow = gridDefinition.hasAutoGrow();
    }

    @Override
    protected void setControlInfo(ControlInfo controlInfo, boolean reverseLayout) {
        LayoutManager layoutManager;
        mOrientation = controlInfo.optStringProperty("@SDSmartGridscrollDirection").equals("horizontal") ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL;
        boolean snapToGrid = controlInfo.optStringProperty("@SDSmartGridsnapToGrid").equals("True");
        String multipleItemsValue = controlInfo.optStringProperty("@SDSmartGridmultipleItems");
        boolean multipleQuantity = multipleItemsValue.equals("multiple_quantity");
        boolean staggeredQuantity = multipleItemsValue.equals("staggered_quantity");
		boolean multipleSize = multipleItemsValue.equals("multiple_size");
        if (multipleQuantity || staggeredQuantity) {
            mSpanCount = controlInfo.optIntProperty(mOrientation == LinearLayoutManager.VERTICAL ? "@SDSmartGriditemsPerRow" : "@SDSmartGriditemsPerColumn");
            if (multipleQuantity) {
                layoutManager = new GridLayoutManager(getContext(), mSpanCount, mOrientation, reverseLayout);
            }
            else {
                StaggeredGridLayoutManager lm = new StaggeredGridLayoutManager(mSpanCount, mOrientation);
                lm.setReverseLayout(reverseLayout);
                layoutManager = lm;
            }
        }
        else if (multipleSize) {
            mSpanCount = 1;
            int minimum = controlInfo.optIntProperty(mOrientation == LinearLayoutManager.VERTICAL ? "@SDSmartGridminimumWidth" : "@SDSmartGridminimumHeight");
            int maximum = controlInfo.optIntProperty(mOrientation == LinearLayoutManager.VERTICAL ? "@SDSmartGridmaximumWidth" : "@SDSmartGridmaxnimumHeight");
            minimum = Services.Device.dipsToPixels(minimum);
            maximum = Services.Device.dipsToPixels(maximum);
            layoutManager = new BySizeLayoutManager(this, getContext(), minimum, maximum, mOrientation, reverseLayout);
        }
        else {
            mSpanCount = 1;
            layoutManager = new LinearLayoutManager(getContext(), mOrientation, reverseLayout);
        }
        if (snapToGrid) {
            SnapHelper helper = new StartSnapHelper();
            helper.attachToRecyclerView(this);
        }
        setClipToPadding(false); // Web & iOS also behave in this way
        setLayoutManager(layoutManager);
    }

    private boolean mFirstGainFocus = true;
    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect)
    {
        if (mAutoGrow && mFirstGainFocus) {
            mFirstGainFocus = false;
            return true; // hack so that controls above the grid don't start hidden because the scroll starts on the grid when it is too large
        }

        return super.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mAutoGrow && mOrientation == LinearLayoutManager.HORIZONTAL) {
			int spanCount = Math.max(1, Math.min(mSpanCount, getChildCount()));
			GridLayoutManager gridLayoutManager = Cast.as(GridLayoutManager.class, getLayoutManager());
			if (gridLayoutManager != null) {
				gridLayoutManager.setSpanCount(spanCount);
			} else {
				StaggeredGridLayoutManager staggeredGridLayoutManager = Cast.as(StaggeredGridLayoutManager.class, getLayoutManager());
				if (staggeredGridLayoutManager != null)
					staggeredGridLayoutManager.setSpanCount(spanCount);
			}
		}
	}
}
